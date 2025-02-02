package com.github.scoliossis.base.utils;

import com.github.scoliossis.base.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

// C for constant, C is short, not named C because i thought C.p() would be funny. (it is)
public class C {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static EntityPlayerSP p() {
        return mc.thePlayer;
    }

    public static WorldClient w() {
        return mc.theWorld;
    }

    public static boolean isInGame() {
        return p() != null && w() != null;
    }

    public final static Reflections reflections = new Reflections(Main.class.getPackage().getName());

    public static ScaledResolution res() {
        return new ScaledResolution(C.mc);
    }

    public static GuiScreen screenToDisplay;

    public static Logger getLogger() {
        return LogManager.getLogger(Main.CLIENT_NAME);
    }
}