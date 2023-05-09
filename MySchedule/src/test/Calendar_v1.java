package test;


import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;
import java.nio.file.AtomicMoveNotSupportedException;
import java.util.Calendar;

import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calendar_v1 extends JFrame implements ActionListener, KeyListener {
	Container cn;
	JButton bt[][] = new JButton[7][7];
	JTextField tf;
	Timer timer;
	Calendar c = Calendar.getInstance();
	
	int YEAR = c.get(Calendar.YEAR);
	int MONTH = c.get(Calendar.MONTH);
	int DAY = c.get(Calendar.DAY_OF_MONTH);

	String w[] = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };
	String t[] = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
			"November", "December" };

	public Calendar_v1() {
		super("Calendar");
		cn = init();
		timer.start();
	}

	String DateTime[][] = new String[7][7];

	int preMonth = MONTH;
	int preYear = YEAR;

	public Container init() {
		Container cn = this.getContentPane();
		JPanel pn = new JPanel();
		pn.setForeground(new Color(255, 128, 0));
		pn.setLayout(new GridLayout(7, 7));
		JPanel pnButton = new JPanel();

		for (int i = 0; i < 7; i++)
			for (int j = 0; j < 7; j++) {
				bt[i][j] = new JButton();
				bt[i][j].addActionListener(this);
				bt[i][j].setActionCommand((i * 7 + j) + "");
				bt[i][j].addKeyListener(this);
				bt[i][j].setFont(new Font("Britannic Bold", 1, 25));
				bt[i][j].setBackground(Color.decode("#001c44"));
				bt[i][j].setForeground(Color.white);
				bt[i][j].setBorder(null);
				pn.add(bt[i][j]);
			}
		for (int i = 0; i < 7; i++)
			bt[0][i].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#005776")));
		cn.add(pn);

		JPanel pn2 = new JPanel();
		pn2.setLayout(new GridLayout(2, 1));
		pn2.setBackground(Color.LIGHT_GRAY);

		tf = new JTextField("Tháng " + (MONTH + 1) + ", " + YEAR);
		tf.setBackground(Color.LIGHT_GRAY);
		tf.setForeground(Color.black);
		tf.setBorder(null);
		tf.setFont(new Font("Britannic Bold", 1, 20));
		tf.setHorizontalAlignment(JTextField.CENTER);

		pn2.add(tf);
		pn2.add(pnButton);

		JButton btnNextMonth = new JButton("Tháng trước");
		btnNextMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Lấy tháng và năm hiện tại
				preMonth--;
				if (preMonth < 0) {
					preYear--;
					preMonth = 11;
				}

				// Cập nhật lại lịch
				int displayMonth = preMonth + 1;
				int displayYear = preYear;
				tf.setText("Tháng " + displayMonth + ", " + displayYear); // cập nhật lại JTextField

				// Cập nhật lại lịch hiển thị trên JButton
				update(displayMonth, (displayYear));
			}
		});

		pnButton.add(btnNextMonth);

		JButton btnToDay = new JButton("Hôm nay");
		btnToDay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				preMonth = month;
				preYear = year;
				tf.setText("Tháng " + (preMonth + 1) + ", " + preYear); // cập nhật lại JTextField

				update(month + 1, (year));
			}
		});

		pnButton.add(btnToDay);

		JButton btnAfterMonth = new JButton("Tháng sau");
		btnAfterMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Tháng sau")) {
					// Tăng tháng lên 1 đơn vị
					preMonth++;
					// Kiểm tra nếu tháng lớn hơn 11 thì tăng năm lên 1 và gán tháng bằng 0
					if (preMonth > 11) {
						preYear++;
						preMonth = 0;
					}
					int displayMonth = preMonth + 1;
					int displayYear = preYear;
					tf.setText("Tháng " + displayMonth + ", " + displayYear); // cập nhật lại JTextField

					// Cập nhật lại lịch hiển thị trên JButton
					update(displayMonth, (displayYear));
				}
			}
		});
		pnButton.add(btnAfterMonth);

		cn.add(pn2, "North");

		this.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(830, 646);
		this.setLocationRelativeTo(null);
		Point p = this.getLocation();
		this.setLocation((int) p.getX() - 245, (int) p.getY());

		update(preMonth + 1, preYear);
// 		timer = new Timer(200, new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Calendar c = Calendar.getInstance();
//				YEAR = c.get(Calendar.YEAR);
//			    MONTH = c.get(Calendar.MONTH);
//			    DAY = c.get(Calendar.DAY_OF_MONTH);
//				int m = ch.getSelectedIndex();
//				String str = tf.getText();
//				while (str.length() > 1 && str.charAt(0) == ' ')
//					str = str.substring(1, str.length() - 1);
//				while (str.length() > 1 && str.charAt(str.length() - 1) == ' ')
//					str = str.substring(0, str.length() - 2);
//				if (str.matches("[0-9]+")) {
//					if ((m != preMonth || preYear.compareTo(new BigInteger(str)) != 0)) {
//						ch.setSelectedIndex(m);
//						update(m + 1, new BigInteger(str));
//						preMonth = m;
//						preYear = new BigInteger(str);
//					}
//				} else
//					tf.setText("");
//			} 
//		});

		return cn;
	}

	public void reset() {
		for (int i = 1; i < 7; i++)
			for (int j = 0; j < 7; j++) {
				bt[i][j].setBackground(Color.decode("#005776"));
				bt[i][j].setForeground(Color.white);
				DateTime[i][j] = "";
				bt[i][j].setBorder(null);
			}
	}

	public void update(int month, int year) {
		reset();
		int thu = getThu(month, year);
		int day = Nday(month, year);
		int pday = 0;
		if (month > 1)
			pday = Nday(month - 1, year);
		else
			pday = Nday(12, year - 1);
		int start = thu - 1;
		if (start == 7)
			start = 0;

		for (int i = 0; i < 7; i++)
			bt[0][i].setText(w[i]);
		int I = 1, J = start;

		for (int i = 1; i <= day; i++) {
			bt[I][J].setText(String.valueOf(i));
			bt[I][J].setForeground(Color.white);
			DateTime[I][J] = leng2(i + "") + "-" + leng2(month + "") + "-" + year;

			if (year == YEAR && month == MONTH + 1 && i == DAY) {
				bt[I][J].setBackground(Color.cyan);
			}
			J++;
			if (J == 7) {
				J = 0;
				I++;
			}
		}
		for (int i = start - 1; i >= 0; i--) {
			bt[1][i].setText(pday-- + "");
			bt[1][i].setForeground(Color.gray);
		}
		int st = 1;
		while (!(I == 7 && J == 0)) {
			bt[I][J].setText(st++ + "");
			bt[I][J].setForeground(Color.gray);
			J++;
			if (J == 7) {
				J = 0;
				I++;
			}
		}
	}

	public String leng2(String s) {
		if (s.length() == 1)
			return "0" + s;
		return s;
	}

	public BigInteger toBig(int s) {
		return new BigInteger(String.valueOf(s));
	}

	public boolean isLeapYear(int N) {
		if (N % 4 == 0 && N % 100 != 0) {
			return true;
		}
		if (N % 400 == 0) {
			return true;
		}
		return false;
	}

	public int getThu(int month, int year) {
		year--;
		int d = year;
		d = year / 4;
		d = d - year / 100;
		d = d + year / 400;
		d = d + year * 365;
		for (int i = 1; i < month; i++)
			d = d + Nday(i, year + 1);
		d = (d % 7) + 2;
		return d;
	}

	public int Nday(int month, int year) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if (isLeapYear(year))
				return 29;
			return 28;
		}
		return 0;
	}

	public static void main(String[] args) {
		new Calendar_v1();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int k = Integer.parseInt(e.getActionCommand());
		int j = k % 7;
		int i = k / 7;
//		System.out.println(i + " " + j + " " + DateTime[i][j]);
		if (DateTime[i][j] != null && DateTime[i][j].length() > 1) {
			for (int I = 0; I < 7; I++)
				for (int J = 0; J < 7; J++)
					bt[I][J].setBorder(null);

			bt[i][j].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.red));
//			ev.updateEvent(DateTime[i][j]);

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == e.VK_DOWN) {
			System.out.println("Hello");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
