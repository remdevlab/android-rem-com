package org.remdev.android.common.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.remdev.android.common.BaseSingletonApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Alexandr.Salin on 1/17/16.
 */
public class Scheduler {
    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    public static void registerNewAlarmReceiver(Context context, Class<? extends BroadcastReceiver> clazz, long timeIntervalMs) {
        PendingIntent pi = generateUpdatePendingIntent(context, clazz);
        AlarmScheduler.startRepeatingAlarmFromNow(context, timeIntervalMs, pi);
    }

    public static void registerNewAlarmReceiver(Class<? extends BroadcastReceiver> clazz, long timeIntervalMs) {
        registerNewAlarmReceiver(BaseSingletonApplication.getContext(), clazz, timeIntervalMs);
    }

    public static void unregisterAlarmReceiver(Context context, Class<? extends BroadcastReceiver> clazz) {
        PendingIntent sender = generateUpdatePendingIntent(context, clazz);
        AlarmScheduler.stopAlarm(context, sender);
    }

    public static void unregisterAlarmReceiver(Class<? extends BroadcastReceiver> clazz) {
        unregisterAlarmReceiver(BaseSingletonApplication.getContext(), clazz);
    }

    public static void reApply(Class<? extends BroadcastReceiver> clazz, long timeIntervalMs) {
        unregisterAlarmReceiver(clazz);
        registerNewAlarmReceiver(clazz, timeIntervalMs);
        logger.info("alarm interval set to " + timeIntervalMs + " for " + clazz.getSimpleName());
    }

    private static PendingIntent generateUpdatePendingIntent(Context context, Class<? extends BroadcastReceiver> clazz) {
        Intent intent = new Intent(context, clazz);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
