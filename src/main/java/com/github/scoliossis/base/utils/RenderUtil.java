package com.github.scoliossis.base.utils;

import com.github.scoliossis.base.utils.font.FontRegistry;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class RenderUtil {
    // @scale do this before rendering 2d shi!
    public static void start2d() {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        // need for glow
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
    }

    // @scale do this after u done pasting other peoples 2d visuals :smile:
    public static void finish2d() {
        getTessalator().draw();

        glColor(new Color(255,255,255));

        GlStateManager.shadeModel(GL11.GL_FLAT);

        GL11.glEnable(GL11.GL_DEPTH_TEST);

        // need for glow
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    // is this really needed...
    public static Tessellator getTessalator() {
        return Tessellator.getInstance();
    }

    // oh wow i really need this to be a function. thank you scale your so smart :smile:
    private static WorldRenderer getWorldRenderer() {
        return getTessalator().getWorldRenderer();
    }


    // --------------------------------------------------------------------------------
    // custom gl shit

    // better than the past 2 functions
    public static void glColor(Color color) {
        GlStateManager.color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
    }

    public static ArrayList<ArrayList<ArrayList<float[]>>> glPushArray = new ArrayList<>();

    public static void push() {
        GL11.glPushMatrix();

        ArrayList<ArrayList<float[]>> settings = new ArrayList<>();
        settings.add((ArrayList<float[]>) currentScale.clone());
        settings.add((ArrayList<float[]>) currentTranslation.clone());

        glPushArray.add(settings);
    }

    public static void pop() {
        if (!glPushArray.isEmpty()) {
            ArrayList<ArrayList<float[]>> translations = glPushArray.get(glPushArray.size()-1);

            currentScale = translations.get(0);
            currentTranslation = translations.get(1);

            glPushArray.remove(glPushArray.get(glPushArray.size()-1));
        }

        GL11.glPopMatrix();
    }

    public static void resetCustomGL() {
        currentScale.clear();
        currentTranslation.clear();
    }


    public static void enableScissor() {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void scissor(int x, int y, int width, int height) {
        GL11.glScissor(x, y, width, height);
    }

    public static void scissor(float x, float y, float width, float height) {
        scissor((int) x, (int) y, (int) width, (int) height);
    }

    public static void scissorArea(float left, float top, float right, float bottom, float scale, float scaledHeight) {
        float width = right - left;
        float height = bottom - top;
        float invertedBottomPos = scaledHeight - bottom;
        scissor(left * scale, invertedBottomPos * scale, width * scale, height * scale);
    }

    public static void scissorArea(float left, float top, float right, float bottom, float scaledHeight) {
        scissorArea(left, top, right, bottom, C.res().getScaleFactor(), scaledHeight);
    }

    public static void scissorArea(float left, float top, float right, float bottom) {
        scissorArea(left, top, right, bottom, (float) C.res().getScaledHeight_double());
    }

    public static void scissorBeginMatrix(float left, float top, float right, float bottom) {
        GlStateManager.pushMatrix();
        enableScissor();

        // pain
        right += left;

        left += getTranslate()[0];
        right += getTranslate()[0];

        top += getTranslate()[1];
        bottom += getTranslate()[1];

        scissorArea(left, top, right, bottom);
    }

    public static void scissorEndMatrix() {
        disableScissor();
        GlStateManager.popMatrix();
    }

    public static ArrayList<float[]> currentScale = new ArrayList<>();

    public static void enableScale(float x, float y) {
        if (!currentScale.isEmpty()) {
            float[] csn = currentScale.get(currentScale.size()-1);

            x*=csn[0];
            y*=csn[1];
        }

        currentScale.add(new float[] {x ,y});

        GL11.glScaled(x,y,1);
    }

    public static void setScale(float x, float y) {
        currentScale.add(new float[] {x ,y});
    }

    public static void disableScale() {
        if (!currentScale.isEmpty()) {
            GL11.glScaled(1f / currentScale.get(currentScale.size() - 1)[0], 1f / currentScale.get(currentScale.size() - 1)[1], 1);
            currentScale.remove(currentScale.size() - 1);
        }
    }

    public static void resetScale() {
        if (!currentScale.isEmpty()) {
            GL11.glScaled(1f / currentScale.get(currentScale.size() - 1)[0], 1f / currentScale.get(currentScale.size() - 1)[1], 1);
            currentScale.clear();
        }
    }

    public static float[] getScale() {
        if (!currentScale.isEmpty()) return currentScale.get(currentScale.size()-1);
        return new float[] {1,1};
    }

    // custom gl translate, literally just for font renderer
    public static ArrayList<float[]> currentTranslation = new ArrayList<>();

    public static void enableTranslate(float x, float y) {
        GL11.glTranslated(x,y,0);

        if (!currentTranslation.isEmpty()) {
            float[] csn = currentTranslation.get(currentTranslation.size()-1);

            x *= getScale()[0];
            y *= getScale()[1];

            x+=csn[0];
            y+=csn[1];
        }

        currentTranslation.add(new float[] {x ,y});
    }

    public static void setTranslate(float x, float y) {
        if (currentTranslation.isEmpty()) GL11.glTranslated(x,y,0);
        else {
            float xT = currentTranslation.get(currentTranslation.size()-1)[0];
            float yT = currentTranslation.get(currentTranslation.size()-1)[1];

            GL11.glTranslated(x-xT, y-yT, 0);
        }
        currentTranslation.add(new float[] {x ,y});
    }

    public static void disableTranslate() {
        if (!currentTranslation.isEmpty()) {
            float xT = currentTranslation.get(currentTranslation.size()-1)[0];
            float yT = currentTranslation.get(currentTranslation.size()-1)[1];

            currentTranslation.add(new float[] {-xT, -yT});
            currentTranslation.remove(currentTranslation.size() - 1);
        }
    }

    public static void resetTranslate() {
        currentTranslation.clear();
    }

    public static float[] getTranslate() {
        if (!currentTranslation.isEmpty()) return currentTranslation.get(currentTranslation.size()-1);
        return new float[] {0,0};
    }

    // --------------------------------------------------------------------------------


    // i couldn't find a class with these named
    // googled it: https://www.chattriggers.com/javadocs/-chat-triggers/com.chattriggers.ctjs.minecraft.libs.renderer/-shape/set-draw-mode.html

    // using GL11. has the right numbers,
    // eg: GL11.GL_QUADS is 7, oh well i alr made this class
    static class drawMode {
        static int POINTS = 0;
        static int LINES = 1;
        static int LINE_LOOP = 2;
        static int LINE_STRIP = 3;
        static int TRIANGLES = 4;
        static int TRIANGLE_STRIP = 5;
        static int TRIANGLE_FAN = 6;
        static int QUADS = 7;
        static int QUAD_STRIP = 8;
        static int POLYGON = 9;
    }

    public static void drawRectBasic(float x, float y, float w, float h, Color color) {
        drawShape(new float[] {x, x+w, x+w, x}, new float[] {y+h, y+h, y, y}, new Color[]{color});
    }

    public static void drawRect(float x, float y, float w, float h, Color color) {
        start2d();
        drawShape(new float[] {x, x+w, x+w, x}, new float[] {y+h, y+h, y, y}, new Color[]{color});
        finish2d();
    }

    public static void addRectVertex(float x, float y, float w, float h, Color color) {
        beginAddingVertex(drawMode.QUADS, DefaultVertexFormats.POSITION_COLOR);
        addVertexArray(new float[] {x, x+w, x+w, x}, new float[] {y+h, y+h, y, y}, new Color[]{color,color,color,color});
    }

    public static void drawFade(float x, float y, float w, float h, Color color, Color color2) {
        start2d();
        drawShape(new float[] {x, x+w, x+w, x}, new float[] {y+h, y+h, y, y}, new Color[]{color, color2, color2, color});
        finish2d();
    }

    public static void draw4ColorRect(float x, float y, float w, float h, Color color, Color color2, Color color3, Color color4) {
        start2d();
        drawShape(new float[] {x, x+w, x+w, x}, new float[] {y+h, y+h, y, y}, new Color[]{color, color2, color3, color4});
        finish2d();
    }

    public static void drawFade(float x, float y, float w, float h, Color[] colors, int parts, float speed) {
        // idrc that this is autistic
        float add = 0;
        start2d();
        beginAddingVertex(drawMode.TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < parts; i++) {
            float newX = x+add;
            float newW = w/parts;
            Color[] colorsFade = RenderUtil.getColorsFade(newX, newW, colors, speed);
            addVertexColor(newX,y+h, colorsFade[0]);
            addVertexColor(newX+newW,y+h, colorsFade[1]);
            addVertexColor(newX+newW,y, colorsFade[1]);
            addVertexColor(newX,y, colorsFade[0]);

            add += w/parts;
        }
        finish2d();
    }


    public static void drawFadeDown(float x, float y, float w, float h, Color color, Color color2) {
        start2d();
        drawShape(new float[] {x, x+w, x+w, x}, new float[] {y+h, y+h, y, y}, new Color[]{color2, color2, color, color});
        finish2d();
    }

    // people gonna see that an open source client has rounded rect and will immediately say "PASTED ROUNDED RECT"
    // this is an awesome community.

    // really long function, i can prob make it smaller
    public static void drawRoundedRect(float x, float y, float w, float h, float radius, Color color) {
        // makes sure the rectangle doesn't bend in on itself
        radius = Math.min(radius, w / 2);
        radius = Math.min(radius, h / 2);

        // setup drawing
        start2d();
        beginAddingVertex(drawMode.TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);

        // loop through a whole circle,
        // every 90 degrees move to a corner
        // 0 is top left, 90 is bottom left, 180 is bottom right, 270 is top right

        // looping 360 times sorta poops on fps, maybe i+=5 would help,
        // but i dont draw many rounded rectangles anyway
        for (int i = 0; i <= 360; i++) {
            float xN = getRoundedRectPoint(x, radius, i, 1) + (i > 180 ? w-(radius*2) : 0);
            float yN = getRoundedRectPoint(y, radius, i, 2) + (i > 90 && i <= 270 ? h-(radius*2) : 0);

            addVertexColor(xN, yN, color);
        }

        // we have finished! draw!
        finish2d();
    }

    public static void drawTexture(ResourceLocation resourceLocation, float x, float y, float w, float h) {
        drawTexture(resourceLocation, x, y, w, h, w, h, 0, 0);
    }
    public static void drawTexture(ResourceLocation resourceLocation, float x, float y, float w1, float h1, float w2, float h2, float u, float v) {
        C.mc.getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, u, v, (int) w1, (int) h1, w2, h2);
    }

    public static void drawGlow(float x, float y, float w, float h, float radius, Color color) {
        // setup drawing
        start2d();

        addGlowVertex(x,y,w,h,radius,color);

        // we have finished! draw!
        finish2d();
    }


    public static void addGlowVertex(double x, double y, double w, double h, float radius, Color color) {
        // makes sure the rectangle doesn't bend in on itself
        radius = (float) Math.min(radius, w / 2);
        radius = (float) Math.min(radius, h / 2);

        // setup drawing
        beginAddingVertex(drawMode.TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);

        // loop through a whole circle,
        // every 90 degrees move to a corner
        // 0 is top left, 90 is bottom left, 180 is bottom right, 270 is top right

        // looping 360 times sorta poops on fps, maybe i+=5 would help,
        // but i dont draw many rounded rectangles anyway
        addVertexColor((float) (x+w/2f), (float) (y+h/2f), color);
        for (int i = 0; i <= 360; i+=10) {
            float xN = (float) (getRoundedRectPoint((float) x, radius, i, 1) + (i > 180 ? w-(radius*2) : 0));
            float yN = (float) (getRoundedRectPoint((float) y, radius, i, 2) + (i > 90 && i <= 270 ? h-(radius*2) : 0));

            addVertexColor(xN, yN, setColorOpacity(color, 0));
        }

        float xN = (getRoundedRectPoint((float) x, radius, 0, 1));
        float yN = (getRoundedRectPoint((float) y, radius, 0, 2));

        addVertexColor(xN, yN, setColorOpacity(color, 0));
    }



    static WorldRenderer worldRenderer;

    static void beginAddingVertex(int DrawMode, VertexFormat vertexFormat) {
        worldRenderer = getWorldRenderer();
        worldRenderer.begin(DrawMode, vertexFormat);
    }

    public static float[] scalePoints(double x, double y) {
        float[] currentScale = getScale();
        float[] currentTranslation = getTranslate();

        x*=currentScale[0];
        y*=currentScale[1];

        x+=currentTranslation[0];
        y+=currentTranslation[1];

        // floats are cool
        return new float[] {(float) x, (float) y};
    }

    static void addVertex(float x, float y) {
        worldRenderer.pos(x, y, 0).endVertex();
    }

    static void addVertexColor(float x, float y, Color color) {
        worldRenderer.pos(x, y, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    static void addVertexTexture(float x, float y, float textureX, float textureY) {
        worldRenderer.pos(x, y, 0).tex(textureX, textureY).endVertex();
    }

    static void addVertexTextureColor(float x, float y, float textureX, float textureY, Color color) {
        worldRenderer.pos(x, y, 0).tex(textureX, textureY).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void drawTextureRectangle(float x, float y, float w, float h, Color[] color) {
        if (color != null) {
            RenderUtil.addVertexTextureColor(x, y + h, 0, 1, color[0]);
            RenderUtil.addVertexTextureColor(x + w, y + h, 1, 1, color[1]);
            RenderUtil.addVertexTextureColor(x + w, y, 1, 0, color[1]);
            RenderUtil.addVertexTextureColor(x, y, 0, 0, color[0]);
        }
        else {
            RenderUtil.addVertexTexture(x, y + h, 0, 1);
            RenderUtil.addVertexTexture(x + w, y + h, 1, 1);
            RenderUtil.addVertexTexture(x + w, y, 1, 0);
            RenderUtil.addVertexTexture(x, y, 0, 0);
        }
    }

    static void addVertexArray(float[] x, float[] y, Color[] colors) {
        for (int i = 0; i < x.length; i++) {
            if (colors.length == 1) addVertex(x[i],y[i]);
            else addVertexColor(x[i],y[i],colors[i]);
        }
    }

    public static void drawShape(float[] x, float[] y, Color[] colors) {
        VertexFormat vertexFormat = DefaultVertexFormats.POSITION_COLOR;
        if (colors.length < 2) {
            glColor(colors[0]);
            vertexFormat = DefaultVertexFormats.POSITION;
        }

        int DrawMode = drawMode.TRIANGLE_FAN;
        if (x.length == 4) DrawMode = drawMode.QUADS;

        beginAddingVertex(DrawMode, vertexFormat);

        addVertexArray(x,y,colors);
    }

    // maths!
    public static float getRoundedRectPoint(float point, float radius, float i, int corner) {
        if (corner == 1) return (float) (point + radius + (Math.sin((i * 3.141592653589793f) / 180f)) * (radius * -1.0f));
        else return (float) (point + radius + (Math.cos((i * 3.141592653589793f) / 180f)) * (radius * -1.0f));

        //double dPoint = point + radius;
        //
        //if (corner == 1) dPoint += Math.sin((i * Math.PI) / 180);
        //else dPoint += Math.cos((i * Math.PI) / 180);
        //
        //return (float) (dPoint * (radius * -1));
    }



    public static Color[] lesbianColors = new Color[] {
            new Color(213, 45, 0),
            new Color(239, 118, 39),
            new Color(255, 154, 86),
            new Color(255, 255, 255),
            new Color(209, 98, 164),
            new Color(181, 86, 144),
            new Color(163, 2, 98),
    };

    public static Color[] rainbowColors = new Color[] {
            new Color(255, 0, 0),
            new Color(255, 255, 0),
            new Color(0, 255, 0),
            new Color(0, 255, 255),
            new Color(0, 0, 255),
            new Color(255, 0, 255),
            new Color(255, 0, 0),
    };

    public static int getColorsFadeSingle(int num1, int num2, double fadeNumber) {
        // get dif from start to end
        int numDif = num2 - num1;

        // start color + (difference between start n end · percent down screen)
        return (int) (num1 + (numDif * fadeNumber));
    }


    public static Color getColorsFade(double startNumber, Color[] colors, double fadeSpeed) {
        return getColorsFade(startNumber,colors,fadeSpeed, 255);
    }
    public static Color getColorsFade(double startNumber, Color[] colors, double fadeSpeed, int opacity) {
        double number = (System.currentTimeMillis() * fadeSpeed * 0.001 + startNumber * 0.001 * colors.length);

        // finding which color we are fading from and into
        int num1 = (int) (number % colors.length);
        int num2 = ((num1 + 1) % colors.length);

        return setColorOpacity(new Color(
                getColorsFadeSingle(colors[num1].getRed(), colors[num2].getRed(), number % 1),
                getColorsFadeSingle(colors[num1].getGreen(), colors[num2].getGreen(), number % 1),
                getColorsFadeSingle(colors[num1].getBlue(), colors[num2].getBlue(), number % 1),
                getColorsFadeSingle(colors[num1].getAlpha(), colors[num2].getAlpha(), number % 1)
        ), opacity);
    }

    public static Color[] getColorsFade(double startNumber, double endNumber, Color[] colors, double fadeSpeed) {
        startNumber *= -1;
        endNumber *= -1;

        return new Color[] {getColorsFade(startNumber, colors, fadeSpeed), getColorsFade(startNumber+endNumber, colors, fadeSpeed)};
    }

    public static Color reverseColor(Color color, int opacity) {
        return new Color(255-color.getRed(), 255-color.getGreen(), 255-color.getBlue(), opacity);
    }

    public static Color setColorOpacity(Color color, int opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    public static String getCharFromInt(int num) {
        switch (num) {
            case 54:
                return "R SHIFT";
            case 42:
                return "L SHIFT";
            case 29:
                return "L CTRL";
            case 159:
                return "R CTRL";
            case 200:
                return "UP";
            case 205:
                return "RIGHT";
            case 208:
                return "DOWN";
            case 203:
                return "LEFT";
            case 219:
                return "HOME";
            case 56:
                return "L ALT";
            case 184:
                return "R ALT";
            case 58:
                return "CAPS";
            case 211:
                return "DELETE";

            case 59:
                return "F1";
            case 60:
                return "F2";
            case 61:
                return "F3";
            case 62:
                return "F4";
            case 63:
                return "F5";
            case 64:
                return "F6";
            case 65:
                return "F7";
            case 66:
                return "F8";
            case 67:
                return "F9";
            case 68:
                return "F10";
        }

        return "UNKNOWN";
    }


    // stuff that should be in fontutil but i prefer doing renderutil.drawString

    // class for these so i know which one is which easier
    public static class StringModifiers {
        public final static int RAINBOW = 0;
        public final static int BOLD = 1;
        public final static int SHADOW = 2;
        public final static int OBFUSCATED = 3;
        public final static int UNDERLINE = 4;
        public final static int STRIKETHROUGH = 5;
        public final static int OUTLINE = 6;

        public final static int AMOUNT_OF_MODIFIERS = 7;
    }

    // ew, so many
    public static void drawString(String string, float x, float y, Color color) {
        FontRegistry.getSpaceGroteskRegular18px().drawString(string, x, y, color);
    }

    public static void drawStringCenteredY(String string, float x, float y, Color color) {
        float fontHeight = FontRegistry.getSpaceGroteskRegular18px().getHeight();
        FontRegistry.getSpaceGroteskRegular18px().drawString(string, x, y - fontHeight/2, color);
    }


    // 3d rendering WOW!!!!!

    static void addVertex3D(float x, float y, float z) {
        worldRenderer.pos(x, y, z).endVertex();
    }

    static void addVertex3Dcolor(float x, float y, float z, Color color) {
        worldRenderer.pos(x, y, z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void renderBox3D(double x1, double y1, double z1, float width, float height, float partialTicks, Color color) {
        render3Dstart(x1,y1,z1,partialTicks);

        beginAddingVertex(drawMode.QUADS, DefaultVertexFormats.POSITION);

        addVertex3Dcolor(0, 0, 0, color);
        addVertex3Dcolor(width, 0, 0, color);
        addVertex3Dcolor(width, 0, width, color);
        addVertex3Dcolor(0, 0, width, color);

        addVertex3Dcolor(0, height, width, color);
        addVertex3Dcolor(width, height, width, color);
        addVertex3Dcolor(width, height, 0, color);
        addVertex3Dcolor(0, height, 0, color);

        addVertex3Dcolor(0, 0, width, color);
        addVertex3Dcolor(0, height, width, color);
        addVertex3Dcolor(0, height, 0, color);
        addVertex3Dcolor(0, 0, 0, color);

        addVertex3Dcolor(width, 0, 0, color);
        addVertex3Dcolor(width, height, 0, color);
        addVertex3Dcolor(width, height, width, color);
        addVertex3Dcolor(width, 0, width, color);

        addVertex3Dcolor(0, height, 0, color);
        addVertex3Dcolor(width, height, 0, color);
        addVertex3Dcolor(width, 0, 0, color);
        addVertex3Dcolor(0, 0, 0, color);

        addVertex3Dcolor(0, 0, width, color);
        addVertex3Dcolor(width, 0, width, color);
        addVertex3Dcolor(width, height, width, color);
        addVertex3Dcolor(0, height, width, color);

        render3Dend();
    }

    public static void renderBox3Dfade(double x1, double y1, double z1, float width, float height, float partialTicks, Color color, Color color2) {
        render3Dstart(x1,y1,z1,partialTicks);

        beginAddingVertex(drawMode.QUADS, DefaultVertexFormats.POSITION_COLOR);

        addVertex3Dcolor(0, 0, 0, color);
        addVertex3Dcolor(width, 0, 0, color);
        addVertex3Dcolor(width, 0, width, color);
        addVertex3Dcolor(0, 0, width, color);

        addVertex3Dcolor(0, height, width, color2);
        addVertex3Dcolor(width, height, width, color2);
        addVertex3Dcolor(width, height, 0, color2);
        addVertex3Dcolor(0, height, 0, color2);

        addVertex3Dcolor(0, 0, width, color);
        addVertex3Dcolor(0, height, width, color2);
        addVertex3Dcolor(0, height, 0, color2);
        addVertex3Dcolor(0, 0, 0, color);

        addVertex3Dcolor(width, 0, 0, color);
        addVertex3Dcolor(width, height, 0, color2);
        addVertex3Dcolor(width, height, width, color2);
        addVertex3Dcolor(width, 0, width, color);

        addVertex3Dcolor(0, height, 0, color2);
        addVertex3Dcolor(width, height, 0, color2);
        addVertex3Dcolor(width, 0, 0, color);
        addVertex3Dcolor(0, 0, 0, color);

        addVertex3Dcolor(0, 0, width, color);
        addVertex3Dcolor(width, 0, width, color);
        addVertex3Dcolor(width, height, width, color2);
        addVertex3Dcolor(0, height, width, color2);

        render3Dend();
    }

    public static float[] correct3DForTicks(double x, double y, double z, float partialTicks) {
        float x2 = (float) (x - (C.p().lastTickPosX + (C.p().posX - C.p().lastTickPosX) * partialTicks));
        float y2 = (float) (y - (C.p().lastTickPosY + (C.p().posY - C.p().lastTickPosY) * partialTicks));
        float z2 = (float) (z - (C.p().lastTickPosZ + (C.p().posZ - C.p().lastTickPosZ) * partialTicks));

        return new float[]{x2,y2,z2};
    }


    public static void render3Dstart() {
        // shouldn't be called start 2d... it starts everything
        start2d();

        GL11.glPushMatrix();

        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        // shows through walls, woah
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.disableLighting();
    }

    public static void render3Dstart(double x1, double y1, double z1, float partialTicks) {
        render3Dstart();

        float[] poses = correct3DForTicks(x1,y1,z1,partialTicks);
        float x = poses[0];
        float y = poses[1];
        float z = poses[2];

        GL11.glTranslated(x, y, z);
    }

    public static void render3Dend() {
        finish2d();

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableLighting();
    }

    public static void drawCool3dGlow(double x, double y, double z, float width, float height, float partialticks, Color color, boolean pitch) {
        render3Dstart(x,y,z,partialticks);

        GL11.glRotatef(-C.p().rotationYaw, 0,1,0);
        // what year was autism invented?
        if (C.mc.gameSettings.thirdPersonView == 2) GL11.glRotatef(180, 0,1,0);
        if (pitch) GL11.glRotatef(C.p().rotationPitch, 1,0,0);

        addGlowVertex(-width/2,0,width,height, 100, color);
        //addRectVertex(-width/2f,0,width,height, color);

        render3Dend();
    }

    public static void render3Drectangle(double x, double y, double z, float width, float height, float partialticks, Color color, boolean pitch) {
        render3Dstart(x,y,z,partialticks);

        GL11.glRotatef(-C.p().rotationYaw, 0,1,0);
        // what year was autism invented?
        if (C.mc.gameSettings.thirdPersonView == 2) GL11.glRotatef(180, 0,1,0);
        if (pitch) GL11.glRotatef(C.p().rotationPitch, 1,0,0);

        addRectVertex(-width/2f,0,width,height, color);

        render3Dend();
    }

    public static void renderNametag(Entity ent, RenderLivingEvent.Specials.Pre<EntityLivingBase> event, Color color, boolean pitch) {
        float x = (float) event.x;
        float y = (float) (event.y+2.4f);
        float z = (float) event.z;


        render3Dstart();
        GL11.glTranslated(x, y, z);

        GL11.glRotatef(-C.mc.getRenderManager().playerViewY, 0, 1, 0);
        //GL11.glRotatef(C.mc.getRenderManager().playerViewX, 1, 0, 0);
        GL11.glRotatef(180, 0,0,1);

        String name = ent.getName();

        GL11.glNormal3f(0.0f, 1.0f, 0.0f);

        float extraW = 0.15f;
        float width = FontRegistry.getSpaceGroteskRegular18px().getStringWidth(name) + extraW;

        addRectVertex(-width/2f - extraW/2f,0,width,0.35f, color);

        RenderUtil.drawString("&s"+name, -width/2f,-0.03f, new Color(0xfffffff));

        finish2d();

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}