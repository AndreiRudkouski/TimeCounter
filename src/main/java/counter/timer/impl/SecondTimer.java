package main.java.counter.timer.impl;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import main.java.counter.timer.Timer;

public class SecondTimer extends ScheduledThreadPoolExecutor implements Timer
{
	private LocalThread executingThread;

	public SecondTimer()
	{
		super(1);
	}

	@Override
	public void stop()
	{
		executingThread.isPause = true;
	}

	@Override
	public void start()
	{
		executingThread.isPause = false;
	}

	@Override
	public boolean isRunning()
	{
		return !executingThread.isPause;
	}

	@Override
	public void setCommand(Runnable command)
	{
		executingThread = new LocalThread(command);
		executingThread.isPause = true;
		super.scheduleAtFixedRate(executingThread, 1, 1, TimeUnit.SECONDS);
	}

	@Override
	public boolean hasCommand()
	{
		return executingThread != null;
	}

	/**
	 * Class for starting and pausing of executing command.
	 */
	private class LocalThread implements Runnable
	{
		Runnable executingCommand;
		volatile boolean isPause;

		LocalThread(Runnable command)
		{
			executingCommand = command;
		}

		@Override
		public void run()
		{
			if (isRunning())
			{
				executingCommand.run();
			}
		}
	}
}
