package com.github.scoliossis.base.screens.mainmenu;

import com.github.scoliossis.base.utils.C;
import com.github.scoliossis.base.utils.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

public class MainMenuBackground {
    public static void drawBackground() {
        ScaledResolution sr = new ScaledResolution(C.mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();

        if ((scaryCircle.size() < 60 && System.currentTimeMillis() % 10000 > 5000) || scaryCircle.size() < 5) {
            double xPos = screenWidth * Math.random();
            double yPos = screenHeight * Math.random();

            double xSpeed = (-(screenWidth/2f - xPos) / screenWidth) / 3;
            double ySpeed = (-(screenHeight/2f - yPos) / screenHeight) / 3;

            double sizeMultiplier = Math.random()*3;

            scaryCircle.add(new circeProperties(xPos, yPos, xSpeed, ySpeed, sizeMultiplier, System.currentTimeMillis()));
        }


        RenderUtil.drawRect(0,0,screenWidth,screenHeight, new Color(22,22,22));

        for (int i = 0; i < scaryCircle.size(); i++) {
            circeProperties vec = scaryCircle.get(i);


            //float x = (float) vec.x;
            //float y = (float) vec.y;

            float x = (float) (vec.x+((System.currentTimeMillis() - vec.time)*vec.speedX));
            float y = (float) (vec.y+((System.currentTimeMillis() - vec.time)*vec.speedY));
            float size = (float) (((System.currentTimeMillis() - vec.time)*vec.speedSize))/10;
            size = Math.min(700,size);

            x-=size/2f;
            y-=size/2f;


            RenderUtil.drawGlow(x, y, size, size, size, RenderUtil.getColorsFade(x, RenderUtil.rainbowColors, 2, 100));

            if (x > screenWidth || x+size < 0 || y+size < 0 || y > screenHeight) {
                scaryCircle.remove(i);
                i--;
            }
        }
        RenderUtil.drawRect(0,0,screenWidth,screenHeight, new Color(22,22,22, 100));
    }

    private static class circeProperties {
        public double x;
        public double y;
        public double speedX;
        public double speedY;
        public double speedSize;
        public long time;

        public circeProperties(double x, double y, double speedX, double speedY, double speedSize, long time) {
            this.x = x;
            this.y = y;

            this.speedX = speedX;
            this.speedY = speedY;
            this.speedSize = speedSize;
            this.time = time;
        }
    }

    private static final ArrayList<circeProperties> scaryCircle = new ArrayList<>();
}
