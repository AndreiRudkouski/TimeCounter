package timeCounter.load;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

public interface ILoadSaveToFile
{
	void loadTime(Map<LocalDate, Long> dateTimeMap);

	void saveTime(Map<LocalDate, Long> dateTimeMap);

	void setFile(File file);
}
