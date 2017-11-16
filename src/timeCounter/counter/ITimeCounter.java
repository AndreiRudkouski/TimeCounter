package timeCounter.counter;

import timeCounter.gui.IGUIWindow;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.timing.ITimeRunning;

public interface ITimeCounter
{
	void incrementCurrentTime();

	void eraseCurrentTime();

	void incrementTodayTime();

	void eraseTodayTime();

	void incrementTotalTime();

	void eraseTotalTime();

	boolean isTimeRelaxSelect();

	void checkRelaxTime();

	boolean isBeginCount();

	boolean isPause();

	void setStopButton();

	void setStartButton();

	ITimeRunning getTimer();

	void setTimer(ITimeRunning timer);

	void checkChangeDate();

	void loadTime();

	void saveTime();

	void setWindow(IGUIWindow window);

	void setLoadSaveToFile(ILoadSaveToFile saver);
}