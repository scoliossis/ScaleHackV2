package com.github.scoliossis.base.commands;

import com.github.scoliossis.base.utils.C;
import com.github.scoliossis.base.Events.PacketEvent;
import com.github.scoliossis.base.annotations.RegisterListeners;
import com.github.scoliossis.base.utils.ChatUtil;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@RegisterListeners
public class CommandMain {
    private String commandPrefix = ".";
    static ArrayList<Command> commands = new ArrayList<>();

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send.Pre event) {
        if (event.packet instanceof C01PacketChatMessage) {
            // not java 21! i dont remember why! bad instanceof :(
            String message = ((C01PacketChatMessage) event.packet).getMessage();

            if (message.startsWith(commandPrefix) && !message.startsWith(commandPrefix+commandPrefix)) {
                handleCommand(message.substring(1));

                event.setCanceled(true);
            }
        }
    }

    public static void loadCommands() {
        for (Class<? extends Command> clazz : C.reflections.getSubTypesOf(Command.class)) {
            try {
                // originally was gonna do this w annotations like i did for settings, but effort.
                commands.add(clazz.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void handleCommand(String message) {
        for (Command command : commands) {
            if (command.commandName.equalsIgnoreCase(message.split(" ")[0])) {
                String commandMessage = message.replaceFirst(command.commandName, "");
                if (!commandMessage.isEmpty()) commandMessage = commandMessage.substring(1);

                if (command.handleCommand(commandMessage)) return;

                break;
            }
        }

        sendHelpMessage(message);
    }

    public static void sendHelpMessage(String message) {
        String helpMessage = "&cInvalid Command: &4" + message;

        for (Command command : commands) {
            helpMessage += "\n&b." + command.commandName +
                    (command.usage.isEmpty() ? "" : " &c" + command.usage) +
                    " &3- &a" + command.help;
        }

        ChatUtil.chat(helpMessage);

    }
}
