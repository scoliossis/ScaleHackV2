package com.github.scoliossis.base.modules.impl.client;

import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.ModuleManager;
import com.github.scoliossis.base.modules.RegisterModule;
import com.github.scoliossis.base.modules.impl.player.DisablerModule;
import com.github.scoliossis.base.utils.C;

@RegisterModule(
        name = "Nick Hider",
        description = "epic gaming on twitch????",
        category = ModuleCategory.Client
)
public class NickHiderModule extends Module {
    private final static String spoofedName = "scale";

    public static String fixText(String text) {
        if (ModuleManager.isEnabled(NickHiderModule.class)) text = text.replaceAll(C.p().getName(), spoofedName);
        if (DisablerModule.antiRacism && ModuleManager.isEnabled(DisablerModule.class)) {
            for (String word : DisablerModule.racistWords) {
                text = text.replaceAll("(?i)"+word, DisablerModule.censorRacism(word));
            }
        }

        return text;
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
