package org.remdev.android.common.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemClock;

/**
 * Created by Alexandr.Salin on 1/17/16.
 */
public class AlarmScheduler {
    public static void startRepeatingAlarm(Context context, int alarmType, long firstRunAtMs, long timeIntervalMs, PendingIntent pendingOperation) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(alarmType, firstRunAtMs, timeIntervalMs, pendingOperation);
    }

    public static void startRepeatingAlarmFromNow(Context context, long timeIntervalMs, PendingIntent pendingOperation) {
        startRepeatingAlarm(context, AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), timeIntervalMs, pendingOperation);
    }

    public static void stopAlarm(Context context, PendingIntent pendingOperation) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingOperation);
    }

    public static void restartRepeatingAlarm(Context context, int alarmType, long firstRunAtMs, long timeIntervalMs, PendingIntent pendingOperation) {
        stopAlarm(context, pendingOperation);
        startRepeatingAlarm(context, alarmType, firstRunAtMs, timeIntervalMs, pendingOperation);
    }

    public static void restartRepeatingAlarmFromNow(Context context, long timeIntervalMs, PendingIntent pendingOperation) {
        stopAlarm(context, pendingOperation);
        startRepeatingAlarmFromNow(context, timeIntervalMs, pendingOperation);
    }

    public static void restartRepeatingAlarmDelayed(Context context, long timeIntervalMs, PendingIntent pendingOperation) {
        stopAlarm(context, pendingOperation);
        startRepeatingAlarm(context, AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + timeIntervalMs, timeIntervalMs,
                pendingOperation);
    }
}
