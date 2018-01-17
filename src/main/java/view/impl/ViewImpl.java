package main.java.view.impl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.command.Command;
import main.java.command.CommandName;
import main.java.init.annotation.Setter;
import main.java.logger.MainLogger;
import main.java.observer.TimeObserver;
import main.java.view.View;

public class ViewImpl implements View, ActionListener
{
	private static final Font FONT_TOP_PANEL = new Font("sanserif", Font.BOLD, 12);
	private static final Font FONT_LEFT_PANEL = new Font("sanserif", Font.BOLD, 15);
	private static final Font FONT_CENTER_PANEL = new Font("sanserif", Font.BOLD, 16);

	private static final String LOCALE_NAME = "main.resources.locale";
	private static final Locale LOCALE_EN = Locale.ENGLISH;
	private static final Locale LOCALE_RU = new Locale("ru");
	private ResourceBundle bundle = ResourceBundle.getBundle(LOCALE_NAME, LOCALE_EN);

	private static final String ICON_NAME = "main/resources/image/icon.png";
	private static final String EXE_EXTENSION = "exe";
	private static final String DOT = ".";
	private static final String TIME_DELIMITER = ":";

	private JFrame frame;
	private JLabel applicationLabel;
	private JLabel currentTimeLabel;
	private JLabel todayTimeLabel;
	private JLabel totalTimeLabel;
	private JTextField currentTimeField;
	private JTextField todayTimeField;
	private JTextField totalTimeField;
	private JMenu countingMenu;
	private JMenuItem loadDataCountingMenu;
	private JMenuItem saveDataCountingMenu;
	private JMenu eraseMenu;
	private JMenuItem applicationEraseMenu;
	private JMenuItem currentTimeEraseMenu;
	private JMenuItem todayTimeEraseMenu;
	private JMenuItem totalTimeEraseMenu;
	private JMenu settingMenu;
	private JMenuItem applicationSettingMenu;
	private JMenuItem localeSettingMenu;
	private JButton startStopButton;
	private JCheckBox checkBreak;
	private JCheckBox checkDate;
	private JFileChooser fileChooser;
	private Image iconImage;

	@Setter
	private Command command;

	private List<TimeObserver> observers = new ArrayList<>();

	@Override
	public void createView()
	{
		frame = new JFrame();
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new HideAndCloseWindowButtonsListener());
		frame.setResizable(false);
		frame.setJMenuBar(createMenu());
		frame.getContentPane().add(BorderLayout.NORTH, createTopPanel());
		frame.getContentPane().add(BorderLayout.WEST, createLeftPanel());
		frame.getContentPane().add(BorderLayout.CENTER, createCenterPanel());
		frame.getContentPane().add(BorderLayout.EAST, createRightPanel());
		frame.setPreferredSize(new Dimension(380, 185));
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		iconImage = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(ICON_NAME));
		frame.setIconImage(iconImage.getScaledInstance(32, 32, Image.SCALE_AREA_AVERAGING));

		initText();
	}

	private JMenuBar createMenu()
	{
		JMenuBar menu = new JMenuBar();
		menu.setBackground(Color.LIGHT_GRAY);
		menu.setBorder(BorderFactory.createEmptyBorder());
		menu.add(createCountingMenu());
		menu.add(createEraseMenu());
		menu.add(createSettingMenu());
		return menu;
	}

	private JMenu createCountingMenu()
	{
		countingMenu = new JMenu();
		loadDataCountingMenu = new JMenuItem();
		loadDataCountingMenu.addActionListener(this);
		loadDataCountingMenu.setActionCommand(CommandName.TIME_COUNTER_LOAD_DATA.name());
		saveDataCountingMenu = new JMenuItem();
		saveDataCountingMenu.addActionListener(this);
		saveDataCountingMenu.setActionCommand(CommandName.TIME_COUNTER_SAVE_DATA.name());
		countingMenu.add(loadDataCountingMenu);
		countingMenu.add(saveDataCountingMenu);
		return countingMenu;
	}

	private JMenu createEraseMenu()
	{
		eraseMenu = new JMenu();
		currentTimeEraseMenu = new JMenuItem();
		currentTimeEraseMenu.addActionListener(e -> {
			setCurrentTime(0L);
			notifyTimeObserversAboutTime();
		});
		todayTimeEraseMenu = new JMenuItem();
		todayTimeEraseMenu.addActionListener(e -> {
			setTodayTime(0L);
			notifyTimeObserversAboutTime();
		});
		totalTimeEraseMenu = new JMenuItem();
		totalTimeEraseMenu.addActionListener(e -> {
			setTotalTime(0L);
			notifyTimeObserversAboutTime();
		});
		applicationEraseMenu = new JMenuItem();
		applicationEraseMenu.addActionListener(e -> {
			changeApplicationLabel(null);
			notifyTimeObserversAboutSettings();
		});
		eraseMenu.add(currentTimeEraseMenu);
		eraseMenu.add(todayTimeEraseMenu);
		eraseMenu.add(totalTimeEraseMenu);
		eraseMenu.add(applicationEraseMenu);
		return eraseMenu;
	}

	private void changeApplicationLabel(File file)
	{
		String name = file != null ? file.getName() : null;
		if (name != null)
		{
			applicationLabel.setText(name);
			applicationLabel.setEnabled(false);
		}
		else
		{
			applicationLabel.setText(bundle.getString("label_application"));
			applicationLabel.setForeground(Color.RED);
			applicationLabel.setEnabled(true);
		}
	}

	private JMenu createSettingMenu()
	{
		settingMenu = new JMenu();
		applicationSettingMenu = new JMenuItem();
		localeSettingMenu = new JMenuItem();
		applicationSettingMenu.addActionListener(e -> chooseApplication());
		localeSettingMenu.addActionListener(e -> changeLocale());
		// Checkboxes
		checkBreak = new JCheckBox();
		checkBreak.addActionListener(e -> notifyTimeObserversAboutSettings());
		checkDate = new JCheckBox();
		checkDate.addActionListener(e -> notifyTimeObserversAboutSettings());
		settingMenu.add(applicationSettingMenu);
		settingMenu.add(localeSettingMenu);
		settingMenu.add(checkBreak);
		settingMenu.add(checkDate);
		return settingMenu;
	}

	private void chooseApplication()
	{
		fileChooser = new JFileChooser();
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
				changeApplicationLabel(fileChooser.getSelectedFile());
				notifyTimeObserversAboutSettings();
			}
		}
	}

	private void changeLocale()
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

	private JPanel createTopPanel()
	{
		JPanel topPanel = new JPanel();
		applicationLabel = new JLabel();
		applicationLabel.setFont(FONT_TOP_PANEL);
		topPanel.add(applicationLabel);
		topPanel.setPreferredSize(new Dimension(370, 30));
		topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		return topPanel;
	}

	private JPanel createLeftPanel()
	{
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(165, 100));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
		currentTimeLabel = new JLabel();
		currentTimeLabel.setFont(FONT_LEFT_PANEL);
		todayTimeLabel = new JLabel();
		todayTimeLabel.setFont(FONT_LEFT_PANEL);
		totalTimeLabel = new JLabel();
		totalTimeLabel.setFont(FONT_LEFT_PANEL);
		leftPanel.add(currentTimeLabel);
		leftPanel.add(Box.createVerticalStrut(12));
		leftPanel.add(todayTimeLabel);
		leftPanel.add(Box.createVerticalStrut(12));
		leftPanel.add(totalTimeLabel);
		return leftPanel;
	}

	private JPanel createRightPanel()
	{
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 10));
		startStopButton = new JButton();
		startStopButton.setPreferredSize(new Dimension(70, 25));
		startStopButton.addActionListener(e -> {
			updateTiming(!startStopButton.getText().equalsIgnoreCase(bundle.getString("text_button_stop")));
			notifyTimeObserversAboutTiming();
		});
		rightPanel.add(startStopButton);
		return rightPanel;
	}

	private JPanel createCenterPanel()
	{
		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
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
		centerPanel.add(currentTimeField);
		centerPanel.add(todayTimeField);
		centerPanel.add(totalTimeField);
		return centerPanel;
	}

	/**
	 * Class for hiding frame to tray and closing the controlling application when the frame is closing.
	 */
	private class HideAndCloseWindowButtonsListener extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent we)
		{
			if (command.executeCommand(CommandName.TIME_COUNTER_IS_CHANGED_TIME_OR_SETTINGS.name()))
			{
				int select = showAndGetResultOfConfirmDialogWindowWithTitleAndMessage("title_save", "message_save");
				if (select != JOptionPane.YES_OPTION && select != JOptionPane.NO_OPTION)
				{
					return;
				}
				if (select == JOptionPane.YES_OPTION)
				{
					command.executeCommand(CommandName.TIME_COUNTER_SAVE_DATA.name());
				}
			}
			command.executeCommand(CommandName.TIME_COUNTER_CLOSE_APP.name());
			System.exit(0);
		}

		@Override
		public void windowIconified(WindowEvent we)
		{
			// Hide window to tray
			frame.setVisible(false);
			TrayIcon trayIcon = new TrayIcon(
					iconImage.getScaledInstance(16, 16, Image.SCALE_AREA_AVERAGING));
			trayIcon.setToolTip(bundle.getString("title"));
			try
			{
				SystemTray.getSystemTray().add(trayIcon);
			}
			catch (AWTException e)
			{
				MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
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
	}

	private int showAndGetResultOfConfirmDialogWindowWithTitleAndMessage(String title, String message)
	{
		return JOptionPane.showConfirmDialog(frame, bundle.getString(message), bundle.getString(title),
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}

	private void initText()
	{
		frame.setTitle(bundle.getString("title"));
		if (applicationLabel.isEnabled())
		{
			applicationLabel.setText(bundle.getString("label_application"));
			applicationLabel.setForeground(Color.RED);
		}
		currentTimeLabel.setText(bundle.getString("label_current_time"));
		todayTimeLabel.setText(bundle.getString("label_today_time"));
		totalTimeLabel.setText(bundle.getString("label_total_time"));
		countingMenu.setText(bundle.getString("menu_item_count"));
		loadDataCountingMenu.setText(bundle.getString("menu_item_count_load"));
		saveDataCountingMenu.setText(bundle.getString("menu_item_count_save"));
		eraseMenu.setText(bundle.getString("menu_item_erase"));
		applicationEraseMenu.setText(bundle.getString("menu_item_erase_application"));
		currentTimeEraseMenu.setText(bundle.getString("menu_item_erase_current"));
		todayTimeEraseMenu.setText(bundle.getString("menu_item_erase_today"));
		totalTimeEraseMenu.setText(bundle.getString("menu_item_erase_total"));
		settingMenu.setText(bundle.getString("menu_item_setting"));
		applicationSettingMenu.setText(bundle.getString("menu_item_setting_application"));
		localeSettingMenu.setText(bundle.getString("menu_item_setting_locale"));
		checkBreak.setText(bundle.getString("label_break"));
		checkDate.setText(bundle.getString("label_date"));
		if (startStopButton.getText().equalsIgnoreCase(bundle.getString("text_button_start")))
		{
			startStopButton.setText(bundle.getString("text_button_stop"));
		}
		else
		{
			startStopButton.setText(bundle.getString("text_button_start"));
		}
	}

	@Override
	public boolean isChosenRelax()
	{
		int select = showAndGetResultOfConfirmDialogWindowWithTitleAndMessage("title_relax_time", "message_relax_time");
		return select == JOptionPane.YES_OPTION;
	}

	@Override
	public boolean isUserAgreeToConnectSelectedApplication()
	{
		int select = showAndGetResultOfConfirmDialogWindowWithTitleAndMessage("title_application_restart",
				"massage_application_restart");
		return select == JOptionPane.YES_OPTION;
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		command.executeCommand(event.getActionCommand());
	}

	@Override
	public void updateTime(List<Long> timeList)
	{
		if (timeList != null && timeList.size() == 3)
		{
			setCurrentTime(timeList.get(0));
			setTodayTime(timeList.get(1));
			setTotalTime(timeList.get(2));
		}
	}

	private void setCurrentTime(Long time)
	{
		if (time != null)
		{
			currentTimeField.setText(convertTimeToViewFormat(time));
		}
	}

	private void setTodayTime(Long time)
	{
		if (time != null)
		{
			todayTimeField.setText(convertTimeToViewFormat(time));
		}
	}

	private void setTotalTime(Long time)
	{
		if (time != null)
		{
			totalTimeField.setText(convertTimeToViewFormat(time));
		}
	}

	private String convertTimeToViewFormat(long sec)
	{
		long hour = sec / (60 * 60);
		long day = hour / 24;
		long min = (sec - hour * 60 * 60) / 60;
		sec = sec - hour * 60 * 60 - min * 60;
		hour = hour - day * 24;
		if (day != 0)
		{
			return String.format(
					"%1$02d" + TIME_DELIMITER + "%2$02d" + TIME_DELIMITER + "%3$02d" + TIME_DELIMITER + "%4$02d", day,
					hour, min, sec);
		}
		return String.format("%1$02d" + TIME_DELIMITER + "%2$02d" + TIME_DELIMITER + "%3$02d", hour, min, sec);
	}

	@Override
	public void updateSettings(boolean autoChangeDate, boolean relaxReminder, File file)
	{
		checkDate.setSelected(autoChangeDate);
		checkBreak.setSelected(relaxReminder);
		changeApplicationLabel(file);
	}

	@Override
	public void updateTiming(boolean isStart)
	{
		startStopButton.setText(isStart ? bundle.getString("text_button_stop") : bundle.getString("text_button_start"));
	}

	@Override
	public void addTimeObserver(TimeObserver observer)
	{
		observers.add(observer);
	}

	@Override
	public void notifyTimeObserversAboutTime()
	{
		observers.forEach(obs -> obs.updateTime(Arrays.asList(convertViewFormatToTime(currentTimeField.getText()),
				convertViewFormatToTime(todayTimeField.getText()), convertViewFormatToTime(totalTimeField.getText()))));
	}

	private long convertViewFormatToTime(String timeStr)
	{
		long[] tmp = Arrays.stream(timeStr.split(TIME_DELIMITER)).mapToLong(Long::parseLong).toArray();
		if (tmp.length == 3)
		{
			return tmp[0] * 60 * 60 + tmp[1] * 60 + tmp[2];
		}
		else
		{
			return tmp[0] * 24 * 60 * 60 + tmp[0] * 60 * 60 + tmp[1] * 60 + tmp[2];
		}
	}

	@Override
	public void notifyTimeObserversAboutSettings()
	{
		observers.forEach(obs -> obs.updateSettings(checkDate.isSelected(), checkBreak.isSelected(),
				fileChooser != null ? fileChooser.getSelectedFile() : null));
	}

	@Override
	public void notifyTimeObserversAboutTiming()
	{
		observers.forEach(obs -> obs
				.updateTiming(startStopButton.getText().equalsIgnoreCase(bundle.getString("text_button_stop"))));
	}
}