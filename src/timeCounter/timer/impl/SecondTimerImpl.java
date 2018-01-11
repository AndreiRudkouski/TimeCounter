package timeCounter.timer.impl;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SecondTimerImpl extends ScheduledThreadPoolExecutor implements timeCounter.timer.SecondTimer
{
	private LocalThread executingThread;

	public SecondTimerImpl()
	{
		super(1);
	}

	@Override
	public void stop()
	{
		executingThread.isPause.set(true);
	}

	@Override
	public void start()
	{
		executingThread.isPause.set(false);
	}

	@Override
	public boolean isRunning()
	{
		return !executingThread.isPause.get();
	}

	@Override
	public void setCommand(Runnable command)
	{
		executingThread = new LocalThread(command);
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
		AtomicBoolean isPause = new AtomicBoolean(true);

		LocalThread(Runnable command)
		{
			executingCommand = command;
		}

		@Override
		public void run()
		{
			if (!isPause.get())
			{
				executingCommand.run();
			}
		}
	}
}