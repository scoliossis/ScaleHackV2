package com.github.scoliossis.base.screens.clickgui;

import com.github.scoliossis.base.config.Category;
import com.github.scoliossis.base.config.ConfigOption;
import com.github.scoliossis.base.modules.Module;
import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.ModuleManager;
import com.github.scoliossis.base.modules.impl.client.ClickGUIModule;
import com.github.scoliossis.base.utils.C;
import com.github.scoliossis.base.utils.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

public class ClickGUIScreenMain extends GuiScreen {
    public ClickGUIScreenMain() {

    }


    public static float fontSize = 0.1f;
    public static int width = 120;
    public static int x = -width/2;
    public static int y = 0;

    public static int xOff = 4;

    public static int outlineWidth = 1;

    public static int mX, mY;

    static int height = 20;

    public static int moduleHeight = 16;

    public static int childHeight = 13;

    public static Category currentDraggingCategory;

    // DRAWING
    @Override
    public void drawScreen(int mX, int mY, float partialTicks) {
        ClickGUIScreenMain.mX = mX; ClickGUIScreenMain.mY = mY;

        // how far scrolled since last tick
        ClickGUIModule.scrolledAmount = Mouse.getDWheel();

        // draws background
        RenderUtil.drawRect(0, 0, C.res().getScaledWidth(), C.res().getScaledHeight(), new Color(0,0,0, 120));

        // loop through categorys!
        for (int i = 0; i < ModuleCategory.values().length; i++) {
            // ready to rumble
            RenderUtil.push();

            RenderUtil.enableTranslate(ClickGUIModule.categoryPos[i][0] + width/2f, ClickGUIModule.categoryPos[i][1]);

            // draws a whole category, (draws title, start loop, loops modules, loops submodules, back to start loop)
            drawCategory(i);

            RenderUtil.pop();
        }

        // reset mouse pressed state.
        resetPressStates();
    }


    public void drawCategory(int categoryNumber) {
        drawHeader(categoryNumber);

        ModuleCategory category = ModuleCategory.values()[categoryNumber];
        for (Module module : ModuleManager.getModulesByCategory(category)) {
            module.draw(categoryNumber);

            // draw children!
            if (ClickGUIModule.rightClickedModules.contains(module))
                for (ConfigOption<?> child : module.children) {
                    if (child == null) {
                        System.out.println("bro why " + module.module.name());
                        continue;
                    }
                    child.draw(categoryNumber);
                }
        }

        // draws big bottom line bar
        drawFooter(categoryNumber);
    }


    private void drawHeader(int categoryNumber) {
        ModuleCategory category = ModuleCategory.values()[categoryNumber];
        Color color = ClickGUIModule.categoryColors[categoryNumber];

        // background and title name
        RenderUtil.drawRect(x, y, width, height, new Color(26,26,26));
        RenderUtil.drawStringCenteredY("&l"+formatSettingName(category.name()), x + 5, y + (height/2f), fontSize, new Color(0xffffff));

        // outline at top
        RenderUtil.drawRect(x, y, width, ClickGUIScreenMain.outlineWidth, color);

        // draws cool outline on sides
        RenderUtil.drawRect(x, y, ClickGUIScreenMain.outlineWidth, height+ClickGUIScreenMain.outlineWidth, color);
        RenderUtil.drawRect(x+width-ClickGUIScreenMain.outlineWidth, y, ClickGUIScreenMain.outlineWidth, height+ClickGUIScreenMain.outlineWidth, color);

        // offset
        RenderUtil.enableTranslate(0, height);
    }

    private void drawFooter(int categoryNumber) {
        Color color = ClickGUIModule.categoryColors[categoryNumber];
        RenderUtil.drawRect(x, y, width, xOff, new Color(26,26,26));

        RenderUtil.drawRect(x, y, outlineWidth, xOff, color);
        RenderUtil.drawRect(x+width-outlineWidth, y, outlineWidth, xOff, color);

        RenderUtil.drawRect(x, y + xOff, width, outlineWidth, color);
    }

    public static Color getColor(int categoryNumber) {
        return ClickGUIModule.categoryColors[categoryNumber];
    }

    // functions to compact code!
    private void resetPressStates() {
        leftMousePressed = false;
        rightMousePressed = false;

        lastKey = -1;
        lastChar = '\u0000';
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static Module currentKeybind = null;

    public static int lastKey = -1;
    public static char lastChar = '\u0000';

    @Override
    public void keyTyped(char aChar, int key) {
        // close when escape pressed.
        if (key == 1 && currentKeybind == null) {
            ModuleManager.save("config");
            C.p().closeScreen();
        }

        lastKey = key;
        lastChar = Character.toUpperCase(aChar);
    }


    public static boolean leftMouseDown = false;
    public static boolean leftMousePressed = false;

    public static boolean rightMouseDown = false;
    public static boolean rightMousePressed = false;

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);

        if (button == 0) {
            leftMouseDown = true;
            leftMousePressed = true;
        }
        else if (button == 1) {
            rightMouseDown = true;
            rightMousePressed = true;
        }
    }

    @Override
    protected void mouseReleased(int x, int y, int button) {
        super.mouseReleased(x, y, button);

        if (button == 0) {
            leftMouseDown = false;
            leftMousePressed = false;
        }
        else if (button == 1) {
            rightMouseDown = false;
            rightMousePressed = false;
        }
    }


    public static String formatSettingName(String settingName) {
        //return settingName;
        return settingName.toLowerCase().replaceAll(" ", "").replaceAll("_", "");
    }

    public static int getParentsNumber(ConfigOption<?> setting) {
        int parents = 0;

        while (setting.parent != null) {
            setting = setting.parent;
            parents++;
        }

        return parents;
    }

    public static Color getBackgroundColor(ConfigOption<?> configOption, int extra) {
        switch (getParentsNumber(configOption) + extra) {
            case 0:
                return new Color(20,20,20);
            case 1:
                return new Color(15, 15, 15);
            case 2:
                return new Color(10, 10, 10);
            default:
                return new Color(0, 0, 0);
        }
    }

    public static Color getBackgroundColor(ConfigOption<?> configOption) {
        return getBackgroundColor(configOption, 0);
    }

    public static void drawOutline(int height, Color color) {
        RenderUtil.drawRect(x, y, outlineWidth, height, color);
        RenderUtil.drawRect(x+width-outlineWidth, y, outlineWidth, height, color);
    }


    public static boolean isHovered(float x, float y, float w, float h) {
        float[] pointsXY = RenderUtil.scalePoints(x, y);

        // checks if mouseX/Y is within rectangle bounds
        return mX >= pointsXY[0] && mX <= pointsXY[0] + w && mY >= pointsXY[1] && mY <= pointsXY[1] + h;
    }

}