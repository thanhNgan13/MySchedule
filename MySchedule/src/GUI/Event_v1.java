package GUI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.text.TabableView;


import java.awt.GridLayout;
import java.awt.Label;
import java.awt.FlowLayout;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event_v1 extends JFrame implements ActionListener {

	private Panel pn;
	private Panel pn1;
	JScrollPane scrollPane;
	JButton btnNewEnvent;
	Label lb3;
	TextArea txtaContent;
	private JTable table;
	private JLabel lbDate;
	
	private String time;
	
	Container cn;
	
	


	private static String DB_URL = "jdbc:mysql://localhost:3306/BaiTapNhom?useSSL=false";
	private static String USER_NAME = "root";
	private static String PASSWORD = "212406Nngann";


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws SQLException{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Event_v1 frame = new Event_v1("11-05-2023");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public static Connection getConnection(String dbURL, String userName, String password) {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, userName, password);
			System.out.println("connect successfully!");
		} catch (Exception ex) {
			System.out.println("connect failure!");
			ex.printStackTrace();
		}
		return conn;
	}
	
	public Container init() {
		Container cn = this.getContentPane();

		
		table = new JTable();
		
		pn = new Panel();
		pn1 = new Panel(new FlowLayout());
		pn.setLayout(new BorderLayout(0, 0));

		pn.add(pn1, BorderLayout.NORTH);

		lbDate = new JLabel(time);
		lbDate.setFont(new Font("Tahoma", Font.BOLD, 20));
		pn1.add(lbDate);
		scrollPane = new JScrollPane(table);
		scrollPane.setBackground(Color.decode("#005776"));
		pn.add(scrollPane);

		updateEvent(time);
		this.setVisible(true);
		this.setResizable(false);
		this.setSize(500, 398);
		this.setLocationRelativeTo(null);
		Point p = this.getLocation();
		this.setLocation((int)p.getX() + 245, (int)p.getY());
		
		btnNewEnvent = new JButton("New Event");
		btnNewEnvent.addActionListener(this);
		btnNewEnvent.setFont(new Font("Algerian", 1, 25));
		btnNewEnvent.setBackground(Color.LIGHT_GRAY);
		btnNewEnvent.setForeground(Color.white);
		cn.add(btnNewEnvent, "South");
		
		TextArea txtaContent = new TextArea();
		txtaContent.setFont(new Font("Algerian", 1, 20));
		txtaContent.setBackground(Color.decode("#005776"));
		txtaContent.setForeground(Color.white);

		getContentPane().add(pn);
		return cn;

	}
	
	public Event_v1(String time) throws SQLException {
		this.time = time;
		cn = init();
		readEvent();
	}
	public Event_v1() throws SQLException {
		cn = init();
		readEvent();
	}
	public void readEvent() {
		try (Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD)) {
			// Khai báo định dạng ngày tháng đầu vào
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			// Chuỗi đầu vào cần chuyển đổi định dạng
			

			// Chuyển đổi định dạng
			LocalDate date = LocalDate.parse(time, inputFormatter);

			// Khai báo định dạng ngày tháng đầu ra
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			// Chuỗi đầu ra đã được chuyển đổi định dạng
			String output = date.format(outputFormatter);

			String sql = "SELECT NameApm,  ContentApm , TimeStart , TimeEnd"
					+ " FROM Apm WHERE DATE(TimeStart) = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,output);
			ResultSet rs = stmt.executeQuery();
			
			// Nếu không có bản ghi nào được tìm thấy
			if (!rs.isBeforeFirst()) {
				JOptionPane.showMessageDialog(this, "Ngày này chưa có cuộc hẹn nào cả!");
				return;
			}
			// Lấy thông tin về các cột trong ResultSet
			ResultSetMetaData meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();
			Vector<String> columnNames = new Vector<String>();

			// Loop through the columns and add the names to the Vector
			for (int i = 1; i <= columnCount; i++) {
			    String columnName = meta.getColumnName(i);
			    columnNames.add(columnName);
			}	
			// Tạo một table model để lưu trữ dữ liệu
			DefaultTableModel model = new DefaultTableModel(columnNames, 0);

			// Thêm các cột vào table model
			while (rs.next()) {
			    Vector<Object> row = new Vector<Object>();
			    for (int i = 1; i <= columnCount; i++) {
			        Object value = rs.getObject(i);
			        row.add(value);
			    }
			    model.addRow(row);
			}
			
			// Thiết lập table model cho table
			table.setModel(model);
			// Đóng ResultSet và Statement
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) {
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
		this.time = time;
		lbDate.setText(time);
		readEvent();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		CreateNewEvent add = new CreateNewEvent();
		add.setVisible(true);
	}

	
}
