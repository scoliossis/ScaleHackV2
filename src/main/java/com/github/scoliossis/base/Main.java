package com.github.scoliossis.base;

import com.github.scoliossis.base.modules.ModuleManager;
import com.github.scoliossis.base.utils.C;
import com.github.scoliossis.base.utils.FontUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

@Mod(modid = Main.MOD_ID, useMetadata = true)
public class Main {
    public static final String MOD_ID = "ScaleHack";
    public static final String MOD_VERSION = "2.0";
    public static final String CLIENT_NAME = "Scale Hack";



    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Display.setTitle(String.format("%s %s", CLIENT_NAME, MOD_VERSION));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModuleManager.init();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (C.screenToDisplay != null) {
            C.mc.displayGuiScreen(C.screenToDisplay);
            C.screenToDisplay = null;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        FontUtil.renderStrings();
    }
}