package timeCounter.load.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

import timeCounter.load.ILoadSaveToFile;

public class LoadSaveToFile implements ILoadSaveToFile
{
	private File file;
	private static final String DELIMITER = "/";

	@Override
	public void loadTime(Map<LocalDate, Long> dateTimeMap)
	{
		if (file.exists() && !file.isDirectory())
		{
			try (BufferedReader reader = new BufferedReader(new FileReader(file)))
			{
				String tmp;
				while ((tmp = reader.readLine()) != null)
				{
					String[] stringTmp = decode(tmp).split(DELIMITER);
					dateTimeMap.put(LocalDate.of(Integer.parseInt(stringTmp[2]),
							Integer.parseInt(stringTmp[1]), Integer.parseInt(stringTmp[0])),
							Long.parseLong(stringTmp[3]));
				}
			}
			catch (IOException ignore)
			{
				System.out.println("Some issues with data loading!");
			}
		}
	}

	@Override
	public void saveTime(Map<LocalDate, Long> dateTimeMap)
	{
		try (FileWriter writer = new FileWriter(file))
		{
			for (Map.Entry<LocalDate, Long> tmp : dateTimeMap.entrySet())
			{
				writer.write(encode(tmp.getKey().getDayOfMonth() +
						DELIMITER + tmp.getKey().getMonthValue() + DELIMITER + tmp.getKey().getYear() +
						DELIMITER + tmp.getValue()) + System.getProperty("line.separator"));
			}
		}
		catch (IOException e)
		{
			System.out.println("Some issues with data saving!");
		}
	}

	@Override
	public void setFile(File file)
	{
		this.file = file;
	}

	private String encode(String str)
	{
		return new String(Base64.getEncoder().encode((str.getBytes())));
	}

	private String decode(String str)
	{
		return new String(Base64.getDecoder().decode(str));
	}
}
