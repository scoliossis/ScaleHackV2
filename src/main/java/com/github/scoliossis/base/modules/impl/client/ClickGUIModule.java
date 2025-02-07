package com.github.scoliossis.base.modules.impl.client;

import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;
import com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain;
import com.github.scoliossis.base.utils.C;

import java.awt.*;
import java.util.ArrayList;

@RegisterModule(
        name = "Click GUI",
        description = "Evil GUI",
        category = ModuleCategory.Client
)
public class ClickGUIModule extends Module {
    public static int scrolledAmount = 0;

    public static long[] categoryDragTimes = new long[ModuleCategory.values().length];
    public static int[][] categoryPos = new int[ModuleCategory.values().length][2];

    static {
        for (int i = 0; i < ModuleCategory.values().length; i++) {
            categoryPos[i][0] = (i * ClickGUIScreenMain.width) + (i * 20) + 20;
            categoryPos[i][1] = 20;
        }
    }

    public static Color[] categoryColors = new Color[] {
            // these were all ints in the old scalehack, too lazy to convert
            new Color(0xE84C3C),
            new Color(0x22B871),
            new Color(0x9352B0),
            new Color(0x3600CE),
            new Color(0xFD9C0C),
            new Color(0x3B92D6),
    };


    public static ArrayList<Module> rightClickedModules = new ArrayList<>();

    public void onEnable() {
        C.screenToDisplay = new ClickGUIScreenMain();
    }

    public void onDisable() {
    }
}
