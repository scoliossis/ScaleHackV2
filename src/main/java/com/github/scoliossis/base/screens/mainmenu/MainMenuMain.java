package com.github.scoliossis.base.screens.mainmenu;

import com.github.scoliossis.base.Main;
import com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain;
import com.github.scoliossis.base.utils.C;
import com.github.scoliossis.base.utils.RenderUtil;
import com.github.scoliossis.base.utils.font.FontRegistry;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;

import java.awt.*;
import java.io.IOException;

public class MainMenuMain extends GuiScreen {
    public MainMenuMain() {

    }

    String[] strings = {"singleplayer", "multiplayer", "settings", "alts"};

    @Override
    public void drawScreen(int mX, int mY, float partialTicks) {
        MainMenuBackground.drawBackground();
        drawButtons(mX, mY);

        leftMousePressed = false;
    }

    public void drawButtons(int mX, int mY) {
        int screenWidth = C.res().getScaledWidth();
        int screenHeight = C.res().getScaledHeight();


        float w = 300;
        float h = 220;
        float x = screenWidth/2f - w/2;
        float y = screenHeight/2f - h/2;


        String clientNameText = Main.CLIENT_NAME + " " + Main.MOD_VERSION;
        float textW = FontRegistry.getStringWidth(clientNameText, 40);
        float textX = screenWidth/2f - textW/2;
        float textY = y+10;

        //RenderUtil.drawGlow(textX-10, textY, textW+20, textH, 10, new Color(150,150,150));
        RenderUtil.drawRoundedRect(x, y, w, h, 5, new Color(10,10,10, 150));
        RenderUtil.drawString(clientNameText, textX, textY, new Color(0xffffffff), 40);

        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];

            float newW = w / 1.3f;
            float newH = h/8;
            float newX = (x + w/2f) - newW/2f;
            float newY = y+newH*(i+1) + i * 10 + 30;

            float newTextW = FontRegistry.getSpaceGroteskRegular18px().getStringWidth(string);
            float newTextX = (newX + newW/2f) - newTextW/2f;
            float newTextY = newY+(newH/2);


            if (ClickGUIScreenMain.isHovered(mX, mY, newX, newY, newW, newH)) {
                RenderUtil.drawRoundedRect(newX, newY, newW, newH, 1, new Color(22, 22, 22, 200));
                RenderUtil.drawStringCenteredY(string, newTextX, newTextY, new Color(0xffffffff));

                if (leftMousePressed) {
                    switch (i) {
                        case 0:
                            mc.displayGuiScreen(new GuiSelectWorld(this));
                            break;
                        case 1:
                            mc.displayGuiScreen(new GuiMultiplayer(this));
                            break;
                        case 2:
                            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                            break;
                        //case 3:
                        // mc.displayGuiScreen(new AltManagerGUI());
                        // break;
                    }
                }
            }
            else {
                RenderUtil.drawRoundedRect(newX, newY, newW, newH, 1, new Color(22, 22, 22, 100));
                RenderUtil.drawStringCenteredY(string, newTextX, newTextY, new Color(255,255,255,190));
            }
        }
    }

    public static boolean leftMousePressed = false;

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);

        if (button == 0) leftMousePressed = true;
    }

    @Override
    protected void mouseReleased(int x, int y, int button) {
        super.mouseReleased(x, y, button);

        if (button == 0) leftMousePressed = false;
    }
}
