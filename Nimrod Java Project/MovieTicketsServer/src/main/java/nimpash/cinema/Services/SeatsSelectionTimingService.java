package nimpash.cinema.Services;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SeatsSelectionTimingService
{
	private static HashMap<String, Timer> selectionTimers = new HashMap<>();

	public static void AddSelection(String selectionId, SeatsSelectionTimeoutHandler handler)
	{
		Timer releaseSeatsTimer = new Timer();

		releaseSeatsTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				handler.SeatsSelectionTimedOut(selectionId);
			}
		}, 900000);

		selectionTimers.put(selectionId, releaseSeatsTimer);
	}

	public static boolean StopTimerSelection(String selectionId)
	{
		selectionTimers.get(selectionId).cancel();

		return (selectionTimers.remove(selectionId) == null ||
				selectionTimers.remove(selectionId) == null);
	}
}
