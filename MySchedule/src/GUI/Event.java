package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.BorderLayout;

public class Event extends JFrame implements ActionListener{
	Container cn;
	JTextField txtDate;
	JTextField txtTitle;

	TextArea txtaContent;
	JButton btnNewEnvent;
	String TIME;
	private JPanel contentPane;
	private static String DB_URL = "jdbc:mysql://localhost:3306/BaiTapNhom?useSSL=false";
	private static String USER_NAME = "root";
	private static String PASSWORD = "212406Nngann";

	
	public static Connection getConnection(String dbURL, String userName, String password) {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, userName, password);
			JOptionPane.showMessageDialog(null, "Connect successfully!");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Connect failure!");
			ex.printStackTrace();
		}
		return conn;
	}

	String evt[][] = new String[10000][2];
	int N = 0;
	
	public Event(String time) throws SQLException{
		this.TIME = time;
		cn = init();
		readEvent();
	}
	
	public Container init() {
		Container cn = this.getContentPane();
		cn.setForeground(Color.black);
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(2, 1));
		contentPane.setBackground(Color.decode("#001c44"));

		txtDate = new JTextField(TIME);
		txtDate.setFont(new Font("Palatino Linotype", Font.BOLD, 40));
		txtDate.setBackground(Color.decode("#001c44"));
		txtDate.setForeground(Color.white);
		txtDate.setHorizontalAlignment(JTextField.CENTER);
		txtDate.enable(false);
		contentPane.add(txtDate);
		
		txtTitle = new JTextField("Chủ đề");
		txtTitle.setBackground(Color.decode("#001c44"));
		txtTitle.setFont(new Font("Dialog", Font.ITALIC, 30));
		txtTitle.setForeground(Color.white);
		txtTitle.setHorizontalAlignment(JTextField.CENTER);
		contentPane.add(txtTitle);
		

		
		txtaContent = new TextArea();
		txtaContent.setFont(new Font("Algerian", 1, 20));
		txtaContent.setBackground(Color.decode("#005776"));
		txtaContent.setForeground(Color.white);
		
		btnNewEnvent = new JButton("New Event");
		btnNewEnvent.addActionListener(this);
		btnNewEnvent.setFont(new Font("Algerian", 1, 25));
		btnNewEnvent.setBackground(Color.LIGHT_GRAY);
		btnNewEnvent.setBackground(Color.black);
		btnNewEnvent.setForeground(Color.white);
		
		updateEvent(TIME);
		
		this.setVisible(true);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(500, 398);
		this.setLocationRelativeTo(null);
		Point p = this.getLocation();
		this.setLocation((int)p.getX() + 245, (int)p.getY());
		
		cn.add(contentPane, "North");
		
		
		
		cn.add(txtaContent, BorderLayout.CENTER);
		cn.add(btnNewEnvent, "South");
		
		return cn;
	}
	
	public void readEvent() {
		
		try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
			String sql = "SELECT NameApm, ContentApm FROM Apm WHERE TimeStart = '2023-05-28'";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String nameApm = rs.getString("NameApm");
				String contentApm = rs.getString("ContentApm");
				txtTitle.setText(nameApm);
				txtaContent.setText(contentApm);

			} else {
				JOptionPane.showMessageDialog(this, "No matching record found.");
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + ex.getMessage());
		}

	}
	
	public void writeEvent() throws IOException {

	}
	
	
	public String getEvent(String time) {

		return "No Event";
	}
	
	public void addEvent(String time, String ev) {

	}
	
	public void updateEvent(String time) {
		TIME = time;
		txtDate.setText(time);
		String Eve = getEvent(time);
		Eve = "     > " + Eve;
		Eve = Eve.replace("\n", "\n     > ");
		txtaContent.setText(Eve);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String str = JOptionPane.showInputDialog("More events for " + TIME);
		if (str.length() > 0) {
			addEvent(TIME, str);
			try {
				writeEvent();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateEvent(TIME);
//			setVisible(false);
		}
	}
	
	public static void main(String[] args) throws SQLException{
		new Event("09 - 05 - 2023") ;
	}
}
