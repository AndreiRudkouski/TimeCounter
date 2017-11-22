package timeCounter.load.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timeCounter.load.ILoadSaveToFile;

public class LoadSaveToFile implements ILoadSaveToFile
{
	private File file;
	private static final String DELIMITER = "/";

	@Override
	public void loadData(Map<LocalDate, AtomicLong> dateTimeMap)
	{
		if (file.exists() && !file.isDirectory())
		{
			try (BufferedReader reader = new BufferedReader(new FileReader(file)))
			{
				String tmp;
				while ((tmp = reader.readLine()) != null)
				{
					String[] stringTmp = decode(tmp).split(DELIMITER);
					if (stringTmp.length != 1)
					{
						dateTimeMap.put(LocalDate.of(Integer.parseInt(stringTmp[2]),
								Integer.parseInt(stringTmp[1]), Integer.parseInt(stringTmp[0])),
								new AtomicLong(Long.parseLong(stringTmp[3])));
					}
				}
			}
			catch (IOException ignore)
			{
				System.out.println("Some issues with data loading!");
			}
		}
	}

	@Override
	public String loadApplication()
	{
		if (file.exists() && !file.isDirectory())
		{
			try (BufferedReader reader = new BufferedReader(new FileReader(file)))
			{
				String tmp;
				while ((tmp = reader.readLine()) != null)
				{
					String[] stringTmp = decode(tmp).split(DELIMITER);
					if (stringTmp.length == 1)
					{
						return stringTmp[0];
					}
				}
			}
			catch (IOException ignore)
			{
				System.out.println("Some issues with data loading!");
			}
		}
		return null;
	}

	@Override
	public void saveData(Map<LocalDate, AtomicLong> dateTimeMap, String name)
	{
		try (FileWriter writer = new FileWriter(file))
		{
			writer.write(encode(name) + System.getProperty("line.separator"));
			for (Map.Entry<LocalDate, AtomicLong> tmp : dateTimeMap.entrySet())
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

	// encode string
	private String encode(String input) {
		byte[] salt = new byte[8];
		new SecureRandom().nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt)
				+ Base64.getEncoder().encodeToString(input.getBytes());
	}

	// decode string
	private String decode(String output) {
		if (output.length() > 12)
		{
			output = output.substring(12);
		}
		return new String(Base64.getDecoder().decode(output));
	}

}