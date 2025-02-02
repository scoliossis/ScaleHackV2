package com.github.scoliossis.base.commands.Commands;

import com.github.scoliossis.base.commands.Command;
import com.github.scoliossis.base.modules.ModuleManager;
import com.github.scoliossis.base.modules.impl.client.ClickGUIModule;

public class ClickGUICommand extends Command {
    public ClickGUICommand() {
        this.commandName = "clickgui";
        this.help = "Opens The ClickGUI";
    }

    @Override
    public boolean handleCommand(String message) {
        ModuleManager.toggle(ClickGUIModule.class);
        return true;
    }
}
