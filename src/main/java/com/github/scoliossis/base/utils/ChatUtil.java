package com.github.scoliossis.base.utils;

import com.github.scoliossis.base.Main;
import net.minecraft.util.ChatComponentText;

// if someone finds something wrong with this code its actually over for me.
public class ChatUtil {

    public static void chat(Object message) {
        // i dont have a "§" on my keyboard, easier to type & if i want a color code.
        if (C.isInGame()) C.p().addChatMessage(new ChatComponentText(String.valueOf(message).replaceAll("&", "§")));
    }

    public static void prefixMessage(Object message) {
        message = "&c[&f"+ Main.CLIENT_NAME +"&c] " + message;
        if (C.isInGame()) C.p().addChatMessage(new ChatComponentText(String.valueOf(message).replaceAll("&", "§")));
    }
}