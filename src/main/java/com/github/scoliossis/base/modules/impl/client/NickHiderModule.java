package com.github.scoliossis.base.modules.impl.client;

import com.github.scoliossis.base.modules.Module;

import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;
import net.minecraft.client.Minecraft;

@RegisterModule(
        name = "Nick Hider",
        description = "epic gaming on twitch????",
        category = ModuleCategory.Client
)
public class NickHiderModule extends Module {
    private String spoofedName = "scale";

    public String fixText(String text) {
        return text.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), this.spoofedName);
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
