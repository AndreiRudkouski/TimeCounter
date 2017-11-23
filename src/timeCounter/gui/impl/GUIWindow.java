package timeCounter.gui.impl;

import static timeCounter.main.Main.TIME_COUNTER;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import timeCounter.gui.IGUIWindow;
import timeCounter.listener.ITimeListener;

public class GUIWindow implements IGUIWindow
{
	private final String LABEL_CURRENT_TIME = "label_current_time";
	private final String LABEL_APPLICATION = "label_application";
	private final String LABEL_RELAX = "label_relax";
	private final String LABEL_TODAY_TIME = "label_today_time";
	private final String LABEL_TOTAL_TIME = "label_total_time";
	private final String MENU_ITEM_COUNT = "menu_item_count";
	private final String MENU_ITEM_COUNT_LOAD = "menu_item_count_load";
	private final String MENU_ITEM_COUNT_SAVE = "menu_item_count_save";
	private final String MENU_ITEM_ERASE = "menu_item_erase";
	private final String MENU_ITEM_ERASE_CURRENT = "menu_item_erase_current";
	private final String MENU_ITEM_ERASE_TODAY = "menu_item_erase_today";
	private final String MENU_ITEM_ERASE_TOTAL = "menu_item_erase_total";
	private final String MENU_ITEM_SETTING = "menu_item_setting";
	private final String MENU_ITEM_SETTING_APPLICATION = "menu_item_setting_application";
	private final String MENU_ITEM_SETTING_LOCALE = "menu_item_setting_locale";
	private final String MESSAGE_APPLICATION_RESTART = "massage_application_restart";
	private final String MESSAGE_CHANGE_DATE = "message_change_date";
	private final String MESSAGE_RELAX_TIME = "message_relax_time";
	private final String TEXT_BUTTON_START = "text_button_start";
	private final String TEXT_BUTTON_STOP = "text_button_stop";
	private final String TITLE = "title";
	private final String TITLE_APPLICATION_RESTART = "title_application_restart";
	private final String TITLE_CHANGE_DATE = "title_change_date";
	private final String TITLE_RELAX_TIME = "title_relax_time";

	private final Font fontTopPanel = new Font("sanserif", Font.BOLD, 12);
	private final Font fontLeftPanel = new Font("sanserif", Font.BOLD, 15);
	private final Font fontCenterPanel = new Font("sanserif", Font.BOLD, 16);

	private static final String LOCALE_NAME = "timeCounter.resource.locale";
	private static final Locale LOCALE_EN = Locale.ENGLISH;
	private static final Locale LOCALE_RU = new Locale("ru");
	private ResourceBundle bundle = ResourceBundle.getBundle(LOCALE_NAME, LOCALE_EN);

	private static final String ICON_NAME = "icon.png";

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
	private JMenuItem menuEraseCurrent;
	private JMenuItem menuEraseToday;
	private JMenuItem menuEraseTotal;
	private JMenu menuSetting;
	private JMenuItem menuSettingApplication;
	private JMenuItem menuSettingLocale;
	private JButton buttonStartStop;
	private JCheckBox checkRelaxReminder;
	private ITimeListener startStopTimeListener;
	private ITimeListener loadTimeListener;
	private ITimeListener saveTimeListener;
	private ITimeListener eraseCurrentTimeListener;
	private ITimeListener eraseTodayTimeListener;
	private ITimeListener eraseTotalTimeListener;
	private ITimeListener applicationListener;
	private ITimeListener localeListener;

	private void create()
	{
		JPanel panelTop = new JPanel();
		labelApplication = new JLabel();
		labelApplication.setFont(fontTopPanel);
		panelTop.add(labelApplication);
		panelTop.setPreferredSize(new Dimension(370, 30));
		panelTop.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

		JPanel panelLeft = new JPanel();
		panelLeft.setPreferredSize(new Dimension(165, 100));
		panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
		panelLeft.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
		labelCurrentTime = new JLabel();
		labelCurrentTime.setFont(fontLeftPanel);
		labelTodayTime = new JLabel();
		labelTodayTime.setFont(fontLeftPanel);
		labelTotalTime = new JLabel();
		labelTotalTime.setFont(fontLeftPanel);
		panelLeft.add(labelCurrentTime);
		panelLeft.add(Box.createVerticalStrut(12));
		panelLeft.add(labelTodayTime);
		panelLeft.add(Box.createVerticalStrut(12));
		panelLeft.add(labelTotalTime);

		JPanel panelCenter = new JPanel();
		panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		currentTimeField = new JTextField(8);
		currentTimeField.setHorizontalAlignment(JTextField.CENTER);
		currentTimeField.setFont(fontCenterPanel);
		currentTimeField.setEditable(false);
		currentTimeField.setBackground(Color.white);
		todayTimeField = new JTextField(8);
		todayTimeField.setHorizontalAlignment(JTextField.CENTER);
		todayTimeField.setFont(fontCenterPanel);
		todayTimeField.setEditable(false);
		todayTimeField.setBackground(Color.white);
		totalTimeField = new JTextField(8);
		totalTimeField.setHorizontalAlignment(JTextField.CENTER);
		totalTimeField.setFont(fontCenterPanel);
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

		checkRelaxReminder = new JCheckBox();

		JMenuBar menu = new JMenuBar();
		menu.setBackground(Color.LIGHT_GRAY);
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
		menuErase.add(menuEraseCurrent);
		menuErase.add(menuEraseToday);
		menuErase.add(menuEraseTotal);
		// Create setting item of the menu
		menuSetting = new JMenu();
		menuSettingApplication = new JMenuItem();
		menuSettingLocale = new JMenuItem();
		menuSettingApplication.addActionListener(applicationListener);
		menuSettingLocale.addActionListener(localeListener);
		menuSetting.add(menuSettingApplication);
		menuSetting.add(menuSettingLocale);

		menu.add(menuCounter);
		menu.add(menuErase);
		menu.add(menuSetting);

		frame = new JFrame();
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setJMenuBar(menu);
		frame.getContentPane().add(BorderLayout.NORTH, panelTop);
		frame.getContentPane().add(BorderLayout.WEST, panelLeft);
		frame.getContentPane().add(BorderLayout.CENTER, panelCenter);
		frame.getContentPane().add(BorderLayout.EAST, panelRight);
		frame.getContentPane().add(BorderLayout.SOUTH, checkRelaxReminder);
		frame.setPreferredSize(new Dimension(380, 210));
		frame.pack();
		frame.setVisible(true);
		// Centering of frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
		Point newLocation = new Point(middle.x - (frame.getWidth() / 2),
				middle.y - (frame.getHeight() / 2));
		frame.setLocation(newLocation);
		ImageIcon img = new ImageIcon(ICON_NAME);
		frame.setIconImage(img.getImage());
		initText();
		// Close the controlling application when the frame is closing
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				TIME_COUNTER.closeApplication();
			}
		});
	}

	private void initText()
	{
		frame.setTitle(bundle.getString(TITLE));
		if (labelApplication.isEnabled())
		{
			labelApplication.setText(bundle.getString(LABEL_APPLICATION));
		}
		labelCurrentTime.setText(bundle.getString(LABEL_CURRENT_TIME));
		labelTodayTime.setText(bundle.getString(LABEL_TODAY_TIME));
		labelTotalTime.setText(bundle.getString(LABEL_TOTAL_TIME));
		menuCounter.setText(bundle.getString(MENU_ITEM_COUNT));
		menuCounterLoad.setText(bundle.getString(MENU_ITEM_COUNT_LOAD));
		menuCounterSave.setText(bundle.getString(MENU_ITEM_COUNT_SAVE));
		menuErase.setText(bundle.getString(MENU_ITEM_ERASE));
		menuEraseCurrent.setText(bundle.getString(MENU_ITEM_ERASE_CURRENT));
		menuEraseToday.setText(bundle.getString(MENU_ITEM_ERASE_TODAY));
		menuEraseTotal.setText(bundle.getString(MENU_ITEM_ERASE_TOTAL));
		menuSetting.setText(bundle.getString(MENU_ITEM_SETTING));
		menuSettingApplication.setText(bundle.getString(MENU_ITEM_SETTING_APPLICATION));
		menuSettingLocale.setText(bundle.getString(MENU_ITEM_SETTING_LOCALE));
		checkRelaxReminder.setText(bundle.getString(LABEL_RELAX));
		if (buttonStartStop.isDefaultCapable())
		{
			buttonStartStop.setText(bundle.getString(TEXT_BUTTON_START));
		}
		else
		{
			buttonStartStop.setText(bundle.getString(TEXT_BUTTON_STOP));
		}
	}

	@Override
	public void setStopTextButton()
	{
		buttonStartStop.setText(bundle.getString(TEXT_BUTTON_STOP));
		buttonStartStop.setDefaultCapable(false);
	}

	@Override
	public void setStartTextButton()
	{
		buttonStartStop.setText(bundle.getString(TEXT_BUTTON_START));
		buttonStartStop.setDefaultCapable(true);
	}

	@Override
	public boolean timeRelaxReminder()
	{
		int select = JOptionPane.showConfirmDialog(frame, bundle.getString(MESSAGE_RELAX_TIME),
				bundle.getString(TITLE_RELAX_TIME),
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return select == JOptionPane.YES_OPTION;
	}

	@Override
	public boolean changeDate()
	{
		int select = JOptionPane.showConfirmDialog(frame, bundle.getString(MESSAGE_CHANGE_DATE),
				bundle.getString(TITLE_CHANGE_DATE),
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return select == JOptionPane.YES_OPTION;
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
		return checkRelaxReminder.isSelected();
	}

	@Override
	public void setListenersAndCreate(ITimeListener startStopTimeListener, ITimeListener loadTimeListener,
			ITimeListener saveTimeListener, ITimeListener eraseCurrentTimeListener,
			ITimeListener eraseTodayTimeListener, ITimeListener eraseTotalTimeListener,
			ITimeListener applicationListener, ITimeListener localeListener)
	{
		this.startStopTimeListener = startStopTimeListener;
		this.loadTimeListener = loadTimeListener;
		this.saveTimeListener = saveTimeListener;
		this.eraseCurrentTimeListener = eraseCurrentTimeListener;
		this.eraseTodayTimeListener = eraseTodayTimeListener;
		this.eraseTotalTimeListener = eraseTotalTimeListener;
		this.applicationListener = applicationListener;
		this.localeListener = localeListener;
		create();
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
		int result = fileChooser.showOpenDialog(frame);
		return result == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null;
	}

	@Override
	public void setApplicationLabel(String name)
	{
		labelApplication.setText(name);
		labelApplication.setEnabled(false);
	}

	@Override
	public boolean runningApplicationNotice()
	{
		int select = JOptionPane.showConfirmDialog(frame, bundle.getString(MESSAGE_APPLICATION_RESTART),
				bundle.getString(TITLE_APPLICATION_RESTART),
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return select == JOptionPane.YES_OPTION;
	}
}