package org.remdev.android.commander.services;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.remdev.android.commander.models.InteractionResult;
import org.remdev.android.commander.services.handlers.AbstractCommandsHandler;
import org.remdev.android.commander.services.handlers.CommandsHandler;
import org.remdev.timlog.Log;
import org.remdev.timlog.LogFactory;


public abstract class BaseCommanderService extends AbstractBoundedInteractService {

    private static final Log log = LogFactory.create(BaseCommanderService.class);

    @NonNull
    @Override
    protected AbstractCommandsHandler createMessageHandler() {
        return new ServiceHandler(this);
    }

    protected abstract InteractionResult executeCommand(int deviceCommand, Bundle data);

    private static class ServiceHandler extends CommandsHandler {
        private final BaseCommanderService commandsService;

        private ServiceHandler(BaseCommanderService baseCommanderService) {
            this.commandsService = baseCommanderService;
        }

        @Override
        protected InteractionResult executeCommand(int deviceCommand, Bundle data) {
            log.d("Executing command %s", deviceCommand);
            return commandsService.executeCommand(deviceCommand, data);
        }
    }
}
