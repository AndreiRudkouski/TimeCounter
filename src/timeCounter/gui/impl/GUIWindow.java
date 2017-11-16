package timeCounter.gui.impl;

import java.awt.*;
import java.util.ResourceBundle;

import javax.swing.*;

import timeCounter.gui.IGUIWindow;
import timeCounter.listener.ITimeListener;

public class GUIWindow implements IGUIWindow
{
	private final String LABEL_CURRENT_TIME = "label_current_time";
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
	private final String MENU_ITEM_SETTING_LOCALE = "menu_item_setting_locale";
	private final String MESSAGE_CHANGE_DATE = "message_change_date";
	private final String MESSAGE_RELAX_TIME = "message_relax_time";
	private final String TEXT_BUTTON_START = "text_button_start";
	private final String TEXT_BUTTON_STOP = "text_button_stop";
	private final String TITLE = "title";
	private final String TITLE_CHANGE_DATE = "title_change_date";
	private final String TITLE_RELAX_TIME = "title_relax_time";

	private final Font fontLeftPanel = new Font("sanserif", Font.BOLD, 16);
	private final Font fontCenterPanel = new Font("sanserif", Font.BOLD, 16);

	private ResourceBundle bundle;
	private JFrame frame;
	private JTextField currentTimeField;
	private JTextField todayTimeField;
	private JTextField totalTimeField;
	private JButton buttonStartStop;
	private JCheckBox checkRelaxReminder;
	private ITimeListener startStopTimeListener;
	private ITimeListener loadTimeListener;
	private ITimeListener saveTimeListener;
	private ITimeListener eraseCurrentTimeListener;
	private ITimeListener eraseTodayTimeListener;
	private ITimeListener eraseTotalTimeListener;
	private ITimeListener localeListener;

	private void create()
	{
		JPanel panelLeft = new JPanel();
		panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
		panelLeft.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
		JLabel labelTimeCurrent = new JLabel(bundle.getString(LABEL_CURRENT_TIME));
		labelTimeCurrent.setFont(fontLeftPanel);
		JLabel labelTimeToday = new JLabel(bundle.getString(LABEL_TODAY_TIME));
		labelTimeToday.setFont(fontLeftPanel);
		JLabel labelTimeTotal = new JLabel(bundle.getString(LABEL_TOTAL_TIME));
		labelTimeTotal.setFont(fontLeftPanel);
		panelLeft.add(labelTimeCurrent);
		panelLeft.add(Box.createVerticalStrut(12));
		panelLeft.add(labelTimeToday);
		panelLeft.add(Box.createVerticalStrut(12));
		panelLeft.add(labelTimeTotal);

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
		buttonStartStop = new JButton(bundle.getString(TEXT_BUTTON_START));
		buttonStartStop.setPreferredSize(new Dimension(70, 25));
		buttonStartStop.addActionListener(startStopTimeListener);
		panelRight.add(buttonStartStop);

		checkRelaxReminder = new JCheckBox(bundle.getString(LABEL_RELAX));

		JMenuBar menu = new JMenuBar();
		// Create counter item of the menu
		JMenu menuCounter = new JMenu(bundle.getString(MENU_ITEM_COUNT));
		JMenuItem menuCounterLoad = new JMenuItem(bundle.getString(MENU_ITEM_COUNT_LOAD));
		menuCounterLoad.addActionListener(loadTimeListener);
		JMenuItem menuCounterSave = new JMenuItem(bundle.getString(MENU_ITEM_COUNT_SAVE));
		menuCounterSave.addActionListener(saveTimeListener);
		menuCounter.add(menuCounterLoad);
		menuCounter.add(menuCounterSave);
		// Create erase item of the menu
		JMenu menuDelete = new JMenu(bundle.getString(MENU_ITEM_ERASE));
		JMenuItem menuCurrentDelete = new JMenuItem(bundle.getString(MENU_ITEM_ERASE_CURRENT));
		menuCurrentDelete.addActionListener(eraseCurrentTimeListener);
		JMenuItem menuTodayDelete = new JMenuItem(bundle.getString(MENU_ITEM_ERASE_TODAY));
		menuTodayDelete.addActionListener(eraseTodayTimeListener);
		JMenuItem menuTotalDelete = new JMenuItem(bundle.getString(MENU_ITEM_ERASE_TOTAL));
		menuTotalDelete.addActionListener(eraseTotalTimeListener);
		menuDelete.add(menuCurrentDelete);
		menuDelete.add(menuTodayDelete);
		menuDelete.add(menuTotalDelete);
		// Create setting item of the menu
		JMenu menuSetting = new JMenu(bundle.getString(MENU_ITEM_SETTING));
		JMenuItem menuLocale = new JMenuItem(bundle.getString(MENU_ITEM_SETTING_LOCALE));
		menuLocale.addActionListener(localeListener);
		menuSetting.add(menuLocale);

		menu.add(menuCounter);
		menu.add(menuDelete);
		menu.add(menuSetting);

		frame = new JFrame(bundle.getString(TITLE));
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setJMenuBar(menu);
		frame.getContentPane().add(BorderLayout.WEST, panelLeft);
		frame.getContentPane().add(BorderLayout.CENTER, panelCenter);
		frame.getContentPane().add(BorderLayout.EAST, panelRight);
		frame.getContentPane().add(BorderLayout.SOUTH, checkRelaxReminder);
		frame.setPreferredSize(new Dimension(380, 180));
		frame.pack();
		frame.setVisible(true);
		// centering of frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
		Point newLocation = new Point(middle.x - (frame.getWidth() / 2),
				middle.y - (frame.getHeight() / 2));
		frame.setLocation(newLocation);
	}

	@Override
	public void setStopTextButton()
	{
		buttonStartStop.setText(bundle.getString(TEXT_BUTTON_STOP));
	}

	@Override
	public void setStartTextButton()
	{
		buttonStartStop.setText(bundle.getString(TEXT_BUTTON_START));
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
			ITimeListener eraseTodayTimeListener, ITimeListener eraseTotalTimeListener, ITimeListener localeListener)
	{
		this.startStopTimeListener = startStopTimeListener;
		this.loadTimeListener = loadTimeListener;
		this.saveTimeListener = saveTimeListener;
		this.eraseCurrentTimeListener = eraseCurrentTimeListener;
		this.eraseTodayTimeListener = eraseTodayTimeListener;
		this.eraseTotalTimeListener = eraseTotalTimeListener;
		this.localeListener = localeListener;
		create();
	}

	@Override
	public void setResourceBundle(ResourceBundle bundle)
	{
		this.bundle = bundle;
		if (frame != null)
		{
			frame.setVisible(false);
			create();
		}
	}

	@Override
	public ResourceBundle getResourceBundle()
	{
		return bundle;
	}
}