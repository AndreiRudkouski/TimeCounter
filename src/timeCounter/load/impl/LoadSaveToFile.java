package timeCounter.load.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import timeCounter.init.annotation.Setter;
import timeCounter.load.ILoadSaveToFile;
import timeCounter.logger.MainLogger;

public class LoadSaveToFile implements ILoadSaveToFile
{
	private File file;

	@Override
	public List<String> loadData()
	{
		List<String> result = new ArrayList<>();
		if (file.exists() && !file.isDirectory())
		{
			try (BufferedReader reader = new BufferedReader(new FileReader(file)))
			{
				String tmp;
				while ((tmp = reader.readLine()) != null)
				{
					result.add(decode(tmp));
				}
			}
			catch (IOException e)
			{
				MainLogger.getLogger().severe(e.toString());
			}
		}
		return result;
	}

	@Override
	public void saveData(List<String> dataToSave)
	{
		try (FileWriter writer = new FileWriter(file))
		{
			for (String tmp : dataToSave)
			{
				writer.write(encode(tmp) + System.getProperty("line.separator"));
			}
		}
		catch (IOException e)
		{
			MainLogger.getLogger().severe(e.toString());
		}
	}

	// encode string
	private String encode(String input)
	{
		byte[] salt = new byte[8];
		new SecureRandom().nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt)
				+ Base64.getEncoder().encodeToString(input.getBytes());
	}

	// decode string
	private String decode(String output)
	{
		if (output.length() > 12)
		{
			output = output.substring(12);
		}
		return new String(Base64.getDecoder().decode(output));
	}

	//////////////////////////////////////////////
	//
	// Getters & Setters
	//
	//////////////////////////////////////////////

	public File getFile()
	{
		return file;
	}

	@Setter
	public void setFile(File file)
	{
		this.file = file;
	}
}