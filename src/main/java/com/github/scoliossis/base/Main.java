package com.github.scoliossis.base;

import com.github.scoliossis.base.Events.EventBus;
import com.github.scoliossis.base.Events.SubscribeEvent;
import com.github.scoliossis.base.Events.impl.ClientTickEvent;
import com.github.scoliossis.base.Events.impl.PlayerTickEvent;
import com.github.scoliossis.base.annotations.RegisterListeners;
import com.github.scoliossis.base.modules.ModuleManager;
import com.github.scoliossis.base.utils.C;
import com.github.scoliossis.base.utils.MovementUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;

@RegisterListeners
@Mod(modid = Main.MOD_ID, useMetadata = true)
public class Main {
    public static final String MOD_ID = "ScaleHack";
    public static final String MOD_VERSION = "2.0";
    public static final String CLIENT_NAME = "Scale Hack";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Display.setTitle(String.format("%s %s", CLIENT_NAME, MOD_VERSION));
        C.getLogger().info("Starting {} {} (god strike me down)", CLIENT_NAME, MOD_VERSION);
        ModuleManager.init();
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (C.screenToDisplay != null) {
            C.mc.displayGuiScreen(C.screenToDisplay);
            C.screenToDisplay = null;
        }
    }

    @SubscribeEvent(EventBus.Priority.HIGHEST)
    public void onPlayerTick(PlayerTickEvent event) {
        MovementUtil.playerYaw = event.getYaw();
        MovementUtil.playerPitch = event.getPitch();
    }
}