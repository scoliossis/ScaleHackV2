package com.github.scoliossis.base.commands.Commands;

import com.github.scoliossis.base.commands.Command;
import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.modules.ModuleManager;
import com.github.scoliossis.base.utils.ChatUtil;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        this.commandName = "toggle";
        this.usage = "[module]";
        this.help = "Toggles A Module";
    }

    @Override
    public boolean handleCommand(String message) {
        Module bestModuleFromInput = ModuleManager.getModulesByFuzzySearch(message);

        if (bestModuleFromInput == null) {
            ChatUtil.chat("&cInvalid Module Name: &4" + message);
            return false;
        }

        bestModuleFromInput.toggle();

        boolean enabled = bestModuleFromInput.enabled;

        ChatUtil.prefixMessage((enabled ? "&aEnabled " : "&cDisabled ") + bestModuleFromInput.module.name());

        return true;
    }
}
