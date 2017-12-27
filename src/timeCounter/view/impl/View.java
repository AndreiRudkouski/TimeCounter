package timeCounter.view.impl;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import timeCounter.counter.ITimeCounter;
import timeCounter.view.IView;
import timeCounter.init.annotation.Setter;
import timeCounter.listener.AbstractTimeListener;
import timeCounter.logger.MainLogger;

public class View implements IView
{
	private static final Font FONT_TOP_PANEL = new Font("sanserif", Font.BOLD, 12);
	private static final Font FONT_LEFT_PANEL = new Font("sanserif", Font.BOLD, 15);
	private static final Font FONT_CENTER_PANEL = new Font("sanserif", Font.BOLD, 16);

	private static final String LOCALE_NAME = "timeCounter.resources.locale";
	private static final Locale LOCALE_EN = Locale.ENGLISH;
	private static final Locale LOCALE_RU = new Locale("ru");
	private ResourceBundle bundle = ResourceBundle.getBundle(LOCALE_NAME, LOCALE_EN);

	private static final String ICON_NAME = "timeCounter/resources/image/icon.png";
	private static final String EXE_EXTENSION = "exe";
	private static final String DOT = ".";

	private JFrame frame;
	private JLabel labelApplication;
	private JLabel labelCurrentTime;
	private JLabel labelTodayTime;
	private JLabel labelTotalTime;
	private JTextField currentTimeField;
	private JTextField todayTimeField;
	private JTextField totalTimeField;
	private JMenu menuCounter;
	private JMenuItem menuCounterLoad;
	private JMenuItem menuCounterSave;
	private JMenu menuErase;
	private JMenuItem menuEraseApplication;
	private JMenuItem menuEraseCurrent;
	private JMenuItem menuEraseToday;
	private JMenuItem menuEraseTotal;
	private JMenu menuSetting;
	private JMenuItem menuSettingApplication;
	private JMenuItem menuSettingLocale;
	private JButton buttonStartStop;
	private JCheckBox checkBreak;
	private JCheckBox checkDate;

	@Setter(name = "startStopTimeListener")
	private AbstractTimeListener startStopTimeListener;
	@Setter(name = "loadTimeListener")
	private AbstractTimeListener loadTimeListener;
	@Setter(name = "saveTimeListener")
	private AbstractTimeListener saveTimeListener;
	@Setter(name = "eraseCurrentTimeListener")
	private AbstractTimeListener eraseCurrentTimeListener;
	@Setter(name = "eraseTodayTimeListener")
	private AbstractTimeListener eraseTodayTimeListener;
	@Setter(name = "eraseTotalTimeListener")
	private AbstractTimeListener eraseTotalTimeListener;
	@Setter(name = "eraseApplicationListener")
	private AbstractTimeListener eraseApplicationListener;
	@Setter(name = "applicationListener")
	private AbstractTimeListener applicationListener;
	@Setter(name = "localeListener")
	private AbstractTimeListener localeListener;
	@Setter
	private ITimeCounter timeCounter;

	@Override
	public void createView()
	{
		JPanel panelTop = new JPanel();
		labelApplication = new JLabel();
		labelApplication.setFont(FONT_TOP_PANEL);
		panelTop.add(labelApplication);
		panelTop.setPreferredSize(new Dimension(370, 30));
		panelTop.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

		JPanel panelLeft = new JPanel();
		panelLeft.setPreferredSize(new Dimension(165, 100));
		panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
		panelLeft.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
		labelCurrentTime = new JLabel();
		labelCurrentTime.setFont(FONT_LEFT_PANEL);
		labelTodayTime = new JLabel();
		labelTodayTime.setFont(FONT_LEFT_PANEL);
		labelTotalTime = new JLabel();
		labelTotalTime.setFont(FONT_LEFT_PANEL);
		panelLeft.add(labelCurrentTime);
		panelLeft.add(Box.createVerticalStrut(12));
		panelLeft.add(labelTodayTime);
		panelLeft.add(Box.createVerticalStrut(12));
		panelLeft.add(labelTotalTime);

		JPanel panelCenter = new JPanel();
		panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		currentTimeField = new JTextField(8);
		currentTimeField.setHorizontalAlignment(JTextField.CENTER);
		currentTimeField.setFont(FONT_CENTER_PANEL);
		currentTimeField.setEditable(false);
		currentTimeField.setBackground(Color.white);
		todayTimeField = new JTextField(8);
		todayTimeField.setHorizontalAlignment(JTextField.CENTER);
		todayTimeField.setFont(FONT_CENTER_PANEL);
		todayTimeField.setEditable(false);
		todayTimeField.setBackground(Color.white);
		totalTimeField = new JTextField(8);
		totalTimeField.setHorizontalAlignment(JTextField.CENTER);
		totalTimeField.setFont(FONT_CENTER_PANEL);
		totalTimeField.setEditable(false);
		totalTimeField.setBackground(Color.white);
		panelCenter.add(currentTimeField);
		panelCenter.add(todayTimeField);
		panelCenter.add(totalTimeField);

		JPanel panelRight = new JPanel();
		panelRight.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 10));
		buttonStartStop = new JButton();
		buttonStartStop.setPreferredSize(new Dimension(70, 25));
		buttonStartStop.addActionListener(startStopTimeListener);
		panelRight.add(buttonStartStop);

		// Create counter item of the menu
		menuCounter = new JMenu();
		menuCounterLoad = new JMenuItem();
		menuCounterLoad.addActionListener(loadTimeListener);
		menuCounterSave = new JMenuItem();
		menuCounterSave.addActionListener(saveTimeListener);
		menuCounter.add(menuCounterLoad);
		menuCounter.add(menuCounterSave);
		// Create erase item of the menu
		menuErase = new JMenu();
		menuEraseCurrent = new JMenuItem();
		menuEraseCurrent.addActionListener(eraseCurrentTimeListener);
		menuEraseToday = new JMenuItem();
		menuEraseToday.addActionListener(eraseTodayTimeListener);
		menuEraseTotal = new JMenuItem();
		menuEraseTotal.addActionListener(eraseTotalTimeListener);
		menuEraseApplication = new JMenuItem();
		menuEraseApplication.addActionListener(eraseApplicationListener);
		menuErase.add(menuEraseCurrent);
		menuErase.add(menuEraseToday);
		menuErase.add(menuEraseTotal);
		menuErase.add(menuEraseApplication);
		// Create setting item of the menu
		menuSetting = new JMenu();
		menuSettingApplication = new JMenuItem();
		menuSettingLocale = new JMenuItem();
		menuSettingApplication.addActionListener(applicationListener);
		menuSettingLocale.addActionListener(localeListener);
		// Checkboxes
		checkBreak = new JCheckBox();
		checkBreak.setSelected(true);
		checkDate = new JCheckBox();
		checkDate.setSelected(true);
		menuSetting.add(menuSettingApplication);
		menuSetting.add(menuSettingLocale);
		menuSetting.add(checkBreak);
		menuSetting.add(checkDate);

		JMenuBar menu = new JMenuBar();
		menu.setBackground(Color.LIGHT_GRAY);
		menu.setBorder(BorderFactory.createEmptyBorder());
		menu.add(menuCounter);
		menu.add(menuErase);
		menu.add(menuSetting);

		frame = new JFrame();
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.setJMenuBar(menu);
		frame.getContentPane().add(BorderLayout.NORTH, panelTop);
		frame.getContentPane().add(BorderLayout.WEST, panelLeft);
		frame.getContentPane().add(BorderLayout.CENTER, panelCenter);
		frame.getContentPane().add(BorderLayout.EAST, panelRight);
		frame.setPreferredSize(new Dimension(380, 185));
		frame.pack();
		frame.setVisible(true);
		// Centering of frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
		Point newLocation = new Point(middle.x - (frame.getWidth() / 2),
				middle.y - (frame.getHeight() / 2));
		frame.setLocation(newLocation);
		Image image = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(ICON_NAME));
		frame.setIconImage(image.getScaledInstance(32, 32, Image.SCALE_AREA_AVERAGING));
		initText();

		// Hide frame to tray and close the controlling application when the frame is closing
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent we)
			{
				if (timeCounter.closeTimeCounter(false))
				{
					int select = JOptionPane.showConfirmDialog(frame, bundle.getString("message_save"),
							bundle.getString("title_save"),
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (select != JOptionPane.YES_OPTION && select != JOptionPane.NO_OPTION)
					{
						return;
					}
					timeCounter.closeTimeCounter(select == JOptionPane.YES_OPTION);
				}
				System.exit(0);
			}

			@Override
			public void windowIconified(WindowEvent we)
			{
				// Hide window to tray
				frame.setVisible(false);
				TrayIcon trayIcon = new TrayIcon(image.getScaledInstance(16, 16, Image.SCALE_AREA_AVERAGING));
				trayIcon.setToolTip(bundle.getString("title"));
				try
				{
					SystemTray.getSystemTray().add(trayIcon);
				}
				catch (AWTException e)
				{
					MainLogger.getLogger().severe(e.toString());
				}
				trayIcon.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						frame.setState(JFrame.NORMAL);
						SystemTray.getSystemTray().remove(trayIcon);
						frame.setVisible(true);
					}
				});
			}
		});
	}

	private void initText()
	{
		frame.setTitle(bundle.getString("title"));
		if (labelApplication.isEnabled())
		{
			labelApplication.setText(bundle.getString("label_application"));
			labelApplication.setForeground(Color.RED);
		}
		labelCurrentTime.setText(bundle.getString("label_current_time"));
		labelTodayTime.setText(bundle.getString("label_today_time"));
		labelTotalTime.setText(bundle.getString("label_total_time"));
		menuCounter.setText(bundle.getString("menu_item_count"));
		menuCounterLoad.setText(bundle.getString("menu_item_count_load"));
		menuCounterSave.setText(bundle.getString("menu_item_count_save"));
		menuErase.setText(bundle.getString("menu_item_erase"));
		menuEraseApplication.setText(bundle.getString("menu_item_erase_application"));
		menuEraseCurrent.setText(bundle.getString("menu_item_erase_current"));
		menuEraseToday.setText(bundle.getString("menu_item_erase_today"));
		menuEraseTotal.setText(bundle.getString("menu_item_erase_total"));
		menuSetting.setText(bundle.getString("menu_item_setting"));
		menuSettingApplication.setText(bundle.getString("menu_item_setting_application"));
		menuSettingLocale.setText(bundle.getString("menu_item_setting_locale"));
		checkBreak.setText(bundle.getString("label_break"));
		checkDate.setText(bundle.getString("label_date"));
		if (buttonStartStop.isDefaultCapable())
		{
			buttonStartStop.setText(bundle.getString("text_button_start"));
		}
		else
		{
			buttonStartStop.setText(bundle.getString("text_button_stop"));
		}
	}

	@Override
	public void setStopTextButton()
	{
		buttonStartStop.setText(bundle.getString("text_button_stop"));
		buttonStartStop.setDefaultCapable(false);
	}

	@Override
	public void setStartTextButton()
	{
		buttonStartStop.setText(bundle.getString("text_button_start"));
		buttonStartStop.setDefaultCapable(true);
	}

	@Override
	public boolean timeRelaxReminder()
	{
		int select = JOptionPane.showConfirmDialog(frame, bundle.getString("message_relax_time"),
				bundle.getString("title_relax_time"),
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return select == JOptionPane.YES_OPTION;
	}

	@Override
	public boolean isAutoChangeDate()
	{
		return checkDate.isSelected();
	}

	@Override
	public void setAutoChangeDate(boolean check)
	{
		checkDate.setSelected(check);
	}

	@Override
	public JTextField getCurrentTimeField()
	{
		return currentTimeField;
	}

	@Override
	public JTextField getTodayTimeField()
	{
		return todayTimeField;
	}

	@Override
	public JTextField getTotalTimeField()
	{
		return totalTimeField;
	}

	@Override
	public boolean isRelaxReminder()
	{
		return checkBreak.isSelected();
	}

	@Override
	public void setRelaxReminder(boolean check)
	{
		checkBreak.setSelected(check);
	}

	@Override
	public void changeLocale()
	{
		if (bundle.getLocale().equals(LOCALE_EN))
		{
			bundle = ResourceBundle.getBundle(LOCALE_NAME, LOCALE_RU);
		}
		else
		{
			bundle = ResourceBundle.getBundle(LOCALE_NAME, LOCALE_EN);
		}
		initText();
	}

	@Override
	public File chooseApplication()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter(DOT + EXE_EXTENSION, EXE_EXTENSION));
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			String fileName = fileChooser.getSelectedFile().getName();
			int index = fileName.lastIndexOf(DOT);
			if (index > 0 && index < fileName.length() - 1 && fileName.substring(index + 1).equalsIgnoreCase(
					EXE_EXTENSION))
			{
				return fileChooser.getSelectedFile();
			}
		}
		return null;
	}

	@Override
	public void setApplicationLabel(String name)
	{
		if (name != null)
		{
			labelApplication.setText(name);
			labelApplication.setEnabled(false);
		}
		else
		{
			labelApplication.setText(bundle.getString("label_application"));
			labelApplication.setForeground(Color.RED);
			labelApplication.setEnabled(true);
		}
	}

	@Override
	public boolean runningApplicationNotice()
	{
		int select = JOptionPane.showConfirmDialog(frame, bundle.getString("massage_application_restart"),
				bundle.getString("title_application_restart"),
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return select == JOptionPane.YES_OPTION;
	}
}