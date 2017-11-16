package timeCounter.gui.impl;

import java.awt.*;

import javax.swing.*;

import timeCounter.gui.IGUIWindow;
import timeCounter.listener.ITimeListener;

public class GUIWindow implements IGUIWindow
{
	private final String LABEL_CURRENT_TIME = "Текущее время:";
	private final String LABEL_RELAX = "Напоминание об отдыхе";
	private final String LABEL_TODAY_TIME = "Время за сегодня:";
	private final String LABEL_TOTAL_TIME = "Общее время:";
	private final String MENU_ITEM_COUNT = "Отсчёт";
	private final String MENU_ITEM_COUNT_LOAD = "Загрузить";
	private final String MENU_ITEM_COUNT_SAVE = "Сохранить";
	private final String MENU_ITEM_ERASE = "Обнуление";
	private final String MENU_ITEM_ERASE_CURRENT = "Текущее";
	private final String MENU_ITEM_ERASE_TODAY = "Сегодня";
	private final String MENU_ITEM_ERASE_TOTAL = "Общее";
	private final String MESSAGE_CHANGE_DATE = "Дата изменилась. Сохранить и обнулить данные?";
	private final String MESSAGE_RELAX_TIME = "Вы уже достаточно позанимались. Пора сделать перерыв!";
	private final String TEXT_BUTTON_START = "Старт";
	private final String TEXT_BUTTON_STOP = "Стоп";
	private final String TITLE = "Счётчик времени";
	private final String TITLE_CHANGE_DATE = "Изменение даты";
	private final String TITLE_RELAX_TIME = "Пора отдохнуть!";

	private final Font fontLeftPanel = new Font("sanserif", Font.BOLD, 16);
	private final Font fontCenterPanel = new Font("sanserif", Font.BOLD, 16);

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

	private void create()
	{
		JPanel panelLeft = new JPanel();
		panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
		panelLeft.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
		JLabel labelTimeCurrent = new JLabel(LABEL_CURRENT_TIME);
		labelTimeCurrent.setFont(fontLeftPanel);
		JLabel labelTimeToday = new JLabel(LABEL_TODAY_TIME);
		labelTimeToday.setFont(fontLeftPanel);
		JLabel labelTimeTotal = new JLabel(LABEL_TOTAL_TIME);
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
		buttonStartStop = new JButton(TEXT_BUTTON_START);
		buttonStartStop.setPreferredSize(new Dimension(70, 25));
		buttonStartStop.addActionListener(startStopTimeListener);
		panelRight.add(buttonStartStop);

		checkRelaxReminder = new JCheckBox(LABEL_RELAX);

		JMenuBar menu = new JMenuBar();
		JMenu menuCounter = new JMenu(MENU_ITEM_COUNT);
		JMenuItem menuCounterLoad = new JMenuItem(MENU_ITEM_COUNT_LOAD);
		menuCounterLoad.addActionListener(loadTimeListener);
		JMenuItem menuCounterSave = new JMenuItem(MENU_ITEM_COUNT_SAVE);
		menuCounterSave.addActionListener(saveTimeListener);
		menuCounter.add(menuCounterLoad);
		menuCounter.add(menuCounterSave);
		JMenu menuDelete = new JMenu(MENU_ITEM_ERASE);
		JMenuItem menuCurrentDelete = new JMenuItem(MENU_ITEM_ERASE_CURRENT);
		menuCurrentDelete.addActionListener(eraseCurrentTimeListener);
		JMenuItem menuTodayDelete = new JMenuItem(MENU_ITEM_ERASE_TODAY);
		menuTodayDelete.addActionListener(eraseTodayTimeListener);
		JMenuItem menuTotalDelete = new JMenuItem(MENU_ITEM_ERASE_TOTAL);
		menuTotalDelete.addActionListener(eraseTotalTimeListener);
		menuDelete.add(menuCurrentDelete);
		menuDelete.add(menuTodayDelete);
		menuDelete.add(menuTotalDelete);
		menu.add(menuCounter);
		menu.add(menuDelete);

		frame = new JFrame(TITLE);
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
		buttonStartStop.setText(TEXT_BUTTON_STOP);
	}

	@Override
	public void setStartTextButton()
	{
		buttonStartStop.setText(TEXT_BUTTON_START);
	}

	@Override
	public boolean timeRelaxReminder()
	{
		int select = JOptionPane.showConfirmDialog(frame, MESSAGE_RELAX_TIME, TITLE_RELAX_TIME,
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		return select == JOptionPane.YES_OPTION;
	}

	@Override
	public boolean changeDate()
	{
		int select = JOptionPane.showConfirmDialog(frame, MESSAGE_CHANGE_DATE, TITLE_CHANGE_DATE,
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
			ITimeListener eraseTodayTimeListener, ITimeListener eraseTotalTimeListener)
	{
		this.startStopTimeListener = startStopTimeListener;
		this.loadTimeListener = loadTimeListener;
		this.saveTimeListener = saveTimeListener;
		this.eraseCurrentTimeListener = eraseCurrentTimeListener;
		this.eraseTodayTimeListener = eraseTodayTimeListener;
		this.eraseTotalTimeListener = eraseTotalTimeListener;
		create();
	}
}