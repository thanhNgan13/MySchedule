package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdatepicker.JDatePicker;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JTextField;
import javax.swing.JButton;
import org.jdatepicker.util.JDatePickerUtil;
import org.jdatepicker.graphics.JNextIcon;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.accessibility.AccessibleIcon;
import javax.swing.DefaultComboBoxModel;

public class CreateNewEvent extends JFrame {

	private JPanel contentPane;
	private JTextField tfNameApm;
	private JTextField tfContent;
	private JDatePickerImpl datePickerStart;
	private JDatePickerImpl datePickerEnd;
	private JComboBox<String> cmbStart;
	private JComboBox<String> cmbEnd;
	private JButton btnBack;
	private JButton btnSave;

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateNewEvent frame = new CreateNewEvent();
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

	public void setToday(SqlDateModel model) {
		Properties p = new Properties();
		p.put("text.day", "Day");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		LocalDate today = LocalDate.now();
		int year = today.getYear();
		int month = today.getMonthValue() - 1; // Month values range from 0 to 11
		int day = today.getDayOfMonth();
		model.setDate(year, month, day);
		model.setSelected(true);
	}

	public void setTimeComboBoxes(JComboBox<String> cbTimeStart, JComboBox<String> cbTimeEnd) {
		Vector<String> hours = new Vector<String>();
		for (int hour = 0; hour <= 23; hour++) {
			for (int minute = 0; minute <= 30; minute += 30) {
				String hourString = String.format("%02d", hour);
				String minuteString = String.format("%02d", minute);
				String time = hourString + ":" + minuteString;
				hours.add(time);
			}
		}
		cmbStart.setModel(new DefaultComboBoxModel<String>(hours));
		cmbEnd.setModel(new DefaultComboBoxModel<String>(hours));
	}

	// Hàm kiểm tra cả hai trường văn bản và kích hoạt nút bấm nếu cả hai đều được
	// nhập
	private void checkEnableBtnSave() {
		String text1 = tfContent.getText();
		String text2 = tfNameApm.getText();

		if (!text1.isEmpty() && !text2.isEmpty()) {
			btnSave.setEnabled(true);
		} else {
			btnSave.setEnabled(false);
		}
	}

	public void insertApm(String name, String timeStart, String timeEnd, String content) {
		try {
			// Kết nối đến cơ sở dữ liệu
			Connection conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			// Chuẩn bị câu lệnh SQL
			String sql = "INSERT INTO Apm (NameApm, TimeStart, TimeEnd, ContentApm) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(sql);

			// Thiết lập giá trị cho các tham số của câu lệnh SQL
			statement.setString(1, name);
			statement.setString(2, timeStart);
			statement.setString(3, timeEnd);
			statement.setString(4, content);
			// Thực thi câu lệnh SQL
			statement.executeUpdate();
			JOptionPane.showMessageDialog(null, "Thêm dữ liệu thành công");

			// Đóng kết nối
			conn.close();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Có lỗi trong quá trình thêm dữ liệu");
			ex.printStackTrace();
		}
	}

	public CreateNewEvent() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Tạo cuộc hẹn mới");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Palatino Linotype", Font.BOLD, 35));
		lblNewLabel.setBounds(151, 10, 283, 55);
		contentPane.add(lblNewLabel);

		JLabel lblTnCucHn = new JLabel("Tên cuộc hẹn");
		lblTnCucHn.setHorizontalAlignment(SwingConstants.LEFT);
		lblTnCucHn.setFont(new Font("Palatino Linotype", Font.BOLD, 20));
		lblTnCucHn.setBounds(10, 91, 131, 55);
		contentPane.add(lblTnCucHn);

		JLabel lblThiGian = new JLabel("Thời gian");
		lblThiGian.setHorizontalAlignment(SwingConstants.LEFT);
		lblThiGian.setFont(new Font("Palatino Linotype", Font.BOLD, 20));
		lblThiGian.setBounds(10, 160, 131, 55);
		contentPane.add(lblThiGian);

		JLabel lblNiDung = new JLabel("Nội dung");
		lblNiDung.setHorizontalAlignment(SwingConstants.LEFT);
		lblNiDung.setFont(new Font("Palatino Linotype", Font.BOLD, 20));
		lblNiDung.setBounds(10, 213, 131, 55);
		contentPane.add(lblNiDung);

		tfNameApm = new JTextField();
		tfNameApm.setFont(new Font("Palatino Linotype", Font.PLAIN, 20));
		tfNameApm.setColumns(10);
		tfNameApm.setBounds(151, 98, 600, 40);
		tfNameApm.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				checkEnableBtnSave(); // Kiểm tra cả hai trường văn bản
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				checkEnableBtnSave(); // Kiểm tra cả hai trường văn bản
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				checkEnableBtnSave(); // Kiểm tra cả hai trường văn bản
			}
		});
		contentPane.add(tfNameApm);

		tfContent = new JTextField();
		tfContent.setHorizontalAlignment(SwingConstants.LEFT);
		tfContent.setFont(new Font("Palatino Linotype", Font.PLAIN, 20));
		tfContent.setColumns(10);
		tfContent.setBounds(151, 224, 600, 229);
		tfContent.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				checkEnableBtnSave(); // Kiểm tra cả hai trường văn bản
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				checkEnableBtnSave(); // Kiểm tra cả hai trường văn bản
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				checkEnableBtnSave(); // Kiểm tra cả hai trường văn bản
			}
		});
		contentPane.add(tfContent);

		btnBack = new JButton("Quay lại");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnBack.setFont(new Font("Palatino Linotype", Font.BOLD, 15));
		btnBack.setBounds(533, 10, 102, 40);
		contentPane.add(btnBack);

		btnSave = new JButton("Lưu");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tfNameApm.getText() == "" || tfContent.getText() == "") {
					JOptionPane.showMessageDialog(null, "Vui lòng không để trống");
				} else {
					Date dateStart = (Date) datePickerStart.getModel().getValue();
					String selectedItemStart = (String) cmbStart.getSelectedItem();
					String resultStart = dateStart + " " + selectedItemStart;

					Date dateEnd = (Date) datePickerEnd.getModel().getValue();
					String selectedItemEnd = (String) cmbEnd.getSelectedItem();
					String resultEnd = dateEnd + " " + selectedItemEnd;

					JOptionPane.showMessageDialog(null, resultStart + "\n" + resultEnd);
					
					insertApm(tfNameApm.getText(), resultStart, resultEnd, tfContent.getText());
				}
			}
		});
		btnSave.setFont(new Font("Palatino Linotype", Font.BOLD, 15));
		btnSave.setBounds(645, 10, 102, 40);
		btnSave.setEnabled(false);
		contentPane.add(btnSave);

		// Tạo 1 đối tượng datetimePicker
		SqlDateModel modelStart = new SqlDateModel();
		setToday(modelStart);
		JDatePanelImpl panelStart = new JDatePanelImpl(modelStart, new Properties());
		SqlDateModel modelEnd = new SqlDateModel();
		setToday(modelEnd);
		JDatePanelImpl panelEnd = new JDatePanelImpl(modelEnd, new Properties());
		datePickerStart = new JDatePickerImpl(panelStart, new AbstractFormatter() {

			@Override
			public String valueToString(Object value) throws ParseException {
				// TODO Auto-generated method stub
				if (value != null) {
					Calendar cal = (Calendar) value;
					SimpleDateFormat fomart = new SimpleDateFormat("yyyy-MM-dd");
					String strDate = fomart.format(cal.getTime());
					return strDate;
				}
				return "";
			}

			@Override
			public Object stringToValue(String text) throws ParseException {
				// TODO Auto-generated method stub
				return null;
			}
		});
		datePickerStart.setBounds(150, 165, 150, 35);
		datePickerStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmbStart.setSelectedIndex(0);
				cmbEnd.setSelectedIndex(0);
				Date dateStart = (Date) datePickerStart.getModel().getValue();
				if (dateStart != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dateStart);
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH);
					int day = calendar.get(Calendar.DATE);
					modelEnd.setDate(year, month, day);
				}
			}
		});
		contentPane.add(datePickerStart);

		// Tạo 1 đối tượng datetimePicker

		datePickerEnd = new JDatePickerImpl(panelEnd, new AbstractFormatter() {

			@Override
			public String valueToString(Object value) throws ParseException {
				// TODO Auto-generated method stub
				if (value != null) {
					Calendar cal = (Calendar) value;
					SimpleDateFormat fomart = new SimpleDateFormat("yyyy-MM-dd");
					String strDate = fomart.format(cal.getTime());
					return strDate;
				}
				return "";
			}

			@Override
			public Object stringToValue(String text) throws ParseException {
				// TODO Auto-generated method stub
				return null;
			}
		});
		datePickerEnd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Date dateStart = (Date) datePickerStart.getModel().getValue();
				Date dateEnd = (Date) datePickerEnd.getModel().getValue();
				if (dateEnd.compareTo(dateStart) < 0) {
					JOptionPane.showMessageDialog(null, "Thời gian không hợp lệ");
					btnSave.setEnabled(false);
				}
				else {
					btnSave.setEnabled(true);
				}
			}
		});
		datePickerEnd.setBounds(505, 165, 150, 35);
		contentPane.add(datePickerEnd);

		cmbStart = new JComboBox<String>();
		cmbEnd = new JComboBox<String>();
		contentPane.add(cmbStart);
		cmbEnd.setBounds(676, 165, 75, 25);
		cmbStart.setBounds(329, 165, 80, 25);
		cmbStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) cmbStart.getSelectedItem();
				LocalTime endTime = LocalTime.parse(selectedItem).plusMinutes(30);
				// thực hiện hành động khác tùy vào giá trị được chọn trong combobox
				cmbEnd.setSelectedItem(endTime.format(DateTimeFormatter.ofPattern("HH:mm")));
			}
		});
		cmbEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedItemStart = (String) cmbStart.getSelectedItem();
				String selectedItemEnd = (String) cmbEnd.getSelectedItem();
				LocalTime endTime = LocalTime.parse(selectedItemEnd);
				LocalTime startTime = LocalTime.parse(selectedItemStart);
				if (endTime.compareTo(startTime) < 0) {
					JOptionPane.showMessageDialog(null, "Thời gian không hợp lệ");
					btnSave.setEnabled(false);

				}
				else {
					btnSave.setEnabled(true);
				}
			}
		});

		contentPane.add(cmbEnd);

		setTimeComboBoxes(cmbStart, cmbEnd);
	}
}
