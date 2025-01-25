package com.github.scoliossis.base.commands.Commands;

import com.github.scoliossis.base.commands.Command;
import com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain;
import com.github.scoliossis.base.utils.C;
import com.github.scoliossis.base.utils.ChatUtil;

public class ClickGUICommand extends Command {
    public ClickGUICommand() {
        this.commandName = "clickgui";
        this.help = "Opens The ClickGUI";
    }

    @Override
    public boolean handleCommand(String message) {
        C.screenToDisplay = new ClickGUIScreenMain();
        return true;
    }
}
