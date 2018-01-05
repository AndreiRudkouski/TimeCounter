package timeCounter.load.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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
			try
			{
				result = Files.lines(Paths.get(file.getName())).map(this::decode).collect(Collectors.toList());
			}
			catch (IOException e)
			{
				MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
			}
		}
		return result;
	}

	@Override
	public void saveData(List<String> dataToSave)
	{
		try
		{
			Files.write(Paths.get(file.getName()), (Iterable<String>) dataToSave.stream().map(this::encode)::iterator);
		}
		catch (IOException e)
		{
			MainLogger.getLogger().severe(MainLogger.getStackTrace(e));
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

	@Setter
	public void setFile(File file)
	{
		this.file = file;
	}
}