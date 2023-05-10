package GUI;

import java.awt.EventQueue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import javax.swing.JLabel;

public class Test extends JFrame {

	JDatePickerImpl datePickerImpl;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test frame = new Test();
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
	public Test() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		SqlDateModel model = new SqlDateModel();
		Properties p = new Properties();
		p.put("text.day", "Day");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl pnale = new JDatePanelImpl(model, p);
		datePickerImpl = new JDatePickerImpl(pnale, new AbstractFormatter() {

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
		contentPane.add(datePickerImpl);

	}

}
