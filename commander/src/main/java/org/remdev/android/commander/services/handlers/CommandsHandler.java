package org.remdev.android.commander.services.handlers;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;

import org.remdev.android.commander.models.InteractionResult;
import org.remdev.android.commander.models.MessageBundle;
import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;


public abstract class CommandsHandler extends AbstractCommandsHandler {

    private static final Log log = LogFactory.create(CommandsHandler.class);

    protected abstract InteractionResult executeCommand(int deviceCommand, Bundle data);

    protected Runnable createTask(final int deviceCommand, final ResultReceiver resultReceiver, Bundle data) {
        log.d("Creating task for command: %s", deviceCommand);
        return generateTask(deviceCommand, resultReceiver, data);
    }

    @NonNull
    private Runnable generateTask(final int deviceCommand, final ResultReceiver resultReceiver, final Bundle data) {
        return new Runnable() {
            @Override
            public void run() {
                InteractionResult payload = executeCommand(deviceCommand, data);
                if (payload == null) {
                    return;
                }
                resultReceiver.send(payload.getCode(), MessageBundle.build(deviceCommand, payload).toBundle());
            }
        };
    }
}
