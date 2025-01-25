package com.github.scoliossis.base.utils;

import lombok.AllArgsConstructor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

// fuck making my own font renderer. it sounds like a lot of effort.
// thankfully the java graphics library has a font renderer
// this is probably a temporary fix, theres issues with it.

// im very sorry for anyone who reads this code.
// i have only recently began learning java and idrk whats going on
public class FontUtil {

    // the bigger the font the better they look!
    // 100 is a nice number too, because 0.30 would be size 30
    // best number
    public static int fontSize = 100;


    // java.awt.Font is op !!!!
    // generates a buffered image for a given string / character
    public static BufferedImage generateStringImage(String string, boolean antiAliasing, Font font) {
        font = new Font(font.getFontName(), font.getStyle(), fontSize);

        int width = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics().getFontMetrics(font).stringWidth(string);

        BufferedImage image = new BufferedImage(width, font.getSize() + extraHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // improves it a lot.
        if (antiAliasing) {
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        graphics.setFont(font);
        graphics.drawString(string, 0, font.getSize());

        return image;
    }

    // missing a lot of characters prob, generating a shit ton of images isnt a good idea tho.
    public static String letters = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ,<>.?!@#$%^&&*()_+=-[{]}\\|'\";:1234567890✓~";
    //ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ        !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~ ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■
    // ^ list by minecraft in fontrenderer.class

    static StringInfo[] textures = new StringInfo[]{};

    // some characters go lower, eg: _ and y.
    static int extraHeight = 30;


    public static Font currentFont;

    // this is prob not a smart way to use this. its the fastest i could think of.
    public static void loadFont(Font font, boolean antiAliasing) {
        currentFont = font;
        ArrayList<StringInfo> texturesArray = new ArrayList<>();

        for (int i = 0; i < letters.length(); i++) {
            String character = String.valueOf(letters.charAt(i));

            BufferedImage image = generateStringImage(character, antiAliasing, font);
            DynamicTexture texture = new DynamicTexture(image);
            texturesArray.add(new StringInfo('h', 0, 0, 0, 0, 0, image.getWidth(), texture, null, null));
        }


        textures = texturesArray.toArray(new StringInfo[]{});
    }

    static ArrayList<StringInfo> chars = new ArrayList<>();

    // this seems dumb.

    static class StringInfo {
        char character;
        float x;
        // xOffset variable saves me doing very basic maths, dw
        int xOffset;

        // OMG SCALE !! U CAN DO float y, sizeX, sizeY; !!!! (no that looks ugly!! i like new lines)
        float y;
        float sizeX;
        float sizeY;
        int width;
        DynamicTexture texture;
        Color color;
        boolean[] modifiers;
        float[] currentScale;
        float[] currentTranslation;

        public StringInfo(char character, float x, int xOffset, float y, float sizeX, float sizeY, int width, DynamicTexture texture, Color color, boolean[] modifiers) {
            this.character = character;
            this.x = x;
            this.xOffset = xOffset;
            this.y = y;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.width = width;
            this.texture = texture;
            this.color = color;
            this.modifiers = modifiers;
            this.currentScale = RenderUtil.getScale();
            this.currentTranslation = RenderUtil.getTranslate();
        }
    }

    // "r" is not in here, any other letter just resets color.
    String colorCodes = "abcdef0123456789klmnoz";

    // add each character to an array so we can render them all at once.
    public static void addString(String string, float x, float y, float sizeX, float sizeY, Color color, Integer[] modifiers) {
        //string = NickHider.fixText(string);

        int xAdd = 0;

        boolean colorCode = false;
        boolean[] modifs = new boolean[RenderUtil.StringModifiers.AMOUNT_OF_MODIFIERS];
        for (int i = 0; i < modifiers.length; i++) modifs[i] = true;

        //Collections.addAll(modifs, modifiers);

        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            int characterINT = letters.indexOf(character);

            if (characterINT == -1 || textures.length < characterINT) {
                xAdd += 20;
            } else {
                StringInfo info = textures[characterINT];

                if (colorCode) {
                    colorCode = false;
                    switch (character) {
                        case 'a':
                            color = new Color(0, 255, 74);
                            break;
                        case 'b':
                            color = new Color(0, 255, 223);
                            break;
                        case 'c':
                            color = new Color(255, 117, 117);
                            break;
                        case 'd':
                            color = new Color(253, 120, 255);
                            break;
                        case 'e':
                            color = new Color(245, 241, 67);
                            break;
                        case 'f':
                            color = new Color(255, 255, 255);
                            break;
                        case '0':
                            color = new Color(0, 0, 0);
                            break;
                        case '1':
                            color = new Color(0, 56, 255);
                            break;
                        case '2':
                            color = new Color(2, 107, 9);
                            break;
                        case '3':
                            color = new Color(5, 100, 94);
                            break;
                        case '4':
                            color = new Color(122, 1, 1);
                            break;
                        case '5':
                            color = new Color(132, 0, 255);
                            break;
                        case '6':
                            color = new Color(238, 165, 5);
                            break;
                        case '7':
                            color = new Color(123, 162, 162);
                            break;
                        case '8':
                            color = new Color(88, 105, 99);
                            break;
                        case '9':
                            color = new Color(54, 143, 243);
                            break;

                        case 'k':
                            modifs[RenderUtil.StringModifiers.OBFUSCATED] = true;
                            break;
                        case 'l':
                            modifs[RenderUtil.StringModifiers.BOLD] = true;
                            break;
                        case 'm':
                            modifs[RenderUtil.StringModifiers.STRIKETHROUGH] = true;
                            break;
                        case 'n':
                            modifs[RenderUtil.StringModifiers.UNDERLINE] = true;
                            break;
                        case 'o':
                            modifs[RenderUtil.StringModifiers.OUTLINE] = true;
                            break;

                        // italics sound like effort
                        //case 'o':
                        //    color = new Color(0, 255, 74);
                        //    break;

                        case 's':
                            modifs[RenderUtil.StringModifiers.SHADOW] = true;
                            break;
                        case 'z':
                            modifs[RenderUtil.StringModifiers.RAINBOW] = true;
                            break;

                        default:
                            modifs = new boolean[RenderUtil.StringModifiers.AMOUNT_OF_MODIFIERS];
                            break;
                    }
                }
                else {

                    if (character == '&' || character == '§') {
                        colorCode = true;
                    } else {
                        if (modifs[RenderUtil.StringModifiers.OBFUSCATED]) {
                            character = getRandomString(1).charAt(0);
                            characterINT = letters.indexOf(character);
                            info = textures[characterINT];
                        }

                        chars.add(new StringInfo(character, x, xAdd, y, sizeX, sizeY, info.width, info.texture, color, modifs));

                        xAdd += info.width;
                    }
                }
            }
        }
    }

    // maybe dumb. who knows. i wrote this so probably :(
    // much faster than my original attempt
    // no noticable fps drop from 5000 random characters.
    // it benefits from the same character being used tho

    // still a really slow font renderer :(
    // ill work on a new one if i ever really need extra frame / quality
    public static void renderStrings() {
        if (chars.isEmpty()) return;

        // sort list into alphabetical order.
        chars.sort(Comparator.comparing(e -> e.character));

        char aChar = '\u0000';


        GlStateManager.enableBlend();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        for (int i = 0; i < chars.size(); i++) {
            StringInfo info = chars.get(i);

            boolean rainbow = info.modifiers[RenderUtil.StringModifiers.RAINBOW];
            boolean bold = info.modifiers[RenderUtil.StringModifiers.BOLD];
            boolean shadow = info.modifiers[RenderUtil.StringModifiers.SHADOW];
            //boolean underline = info.modifiers[RenderUtil.StringModifiers.RAINBOW];
            //boolean strikethrough = info.modifiers[RenderUtil.StringModifiers.RAINBOW];
            boolean outline = info.modifiers[RenderUtil.StringModifiers.OUTLINE];

            RenderUtil.push();

            if (info.currentScale != null) {
                float[] scale = info.currentScale;
                //RenderUtil.setScale(scale[0], scale[1]);

                info.x*=scale[0];
                info.y*=scale[1];
            }
            //else RenderUtil.setScale(1, 1);

            if (info.currentTranslation != null) {
                float[] translation = info.currentTranslation;

                info.x+=translation[0];
                info.y+=translation[1];
            }

            //RenderUtil.enableTranslate(info.x + (info.xOffset * info.sizeX), info.y);

            float Xcoord = info.x + (info.xOffset * info.sizeX);
            float Ycoord = info.y;


            float transW = info.width * info.sizeX;
            float transH = (fontSize+extraHeight) * info.sizeY;

            float shadowOffset = 6*info.sizeX;

            // every time we need a new texture we draw all the old ones
            if (info.character != aChar) {
                if (i != 0) RenderUtil.getTessalator().draw();

                // new texture :shock:
                GlStateManager.bindTexture(info.texture.getGlTextureId());
                aChar = info.character;

                // resume drawing
                RenderUtil.beginAddingVertex(RenderUtil.drawMode.QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            }

            // gets rainbow colors
            Color[] colors = RenderUtil.getColorsFade(Xcoord, info.width, RenderUtil.rainbowColors, 3f);

            if (!rainbow) colors = new Color[] {info.color, info.color};

            // rectangles my beloved

            // draws the letter again with an x offset of 5 and 5 pixels taller
            if (shadow) RenderUtil.drawTextureRectangle(Xcoord+shadowOffset, Ycoord, transW, transH + shadowOffset, new Color[] {new Color(23,23,23,150),new Color(23,23,23,150)});

            // outline
            if (outline) RenderUtil.drawTextureRectangle(Xcoord-1, Ycoord-1, transW+2, transH+2, new Color[]{RenderUtil.reverseColor(colors[0], 30), RenderUtil.reverseColor(colors[1], 30)});

            // just normal drawing
            RenderUtil.drawTextureRectangle(Xcoord, Ycoord, transW, transH, colors);

            // just draws another right next to it
            if (bold) RenderUtil.drawTextureRectangle(Xcoord-0.5f, Ycoord-0.5f, transW + 1, transH + 1, colors);

            // TODO: add later, lazy
            //if (underline) RenderUtil.drawRect(info.xOffset, fontSize, info.width, -1, colors);
            //if (strikethrough) RenderUtil.drawRect(info.xOffset, info.size/2, info.width, 2, colors);


            // gotta finish drawing before resetting the pos
            if (i == chars.size()-1) RenderUtil.getTessalator().draw();

            // reset drawing stuff
            RenderUtil.pop();
        }

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();

        // clear render array
        chars.clear();
    }



    // this feels dumb.
    // i dont think i like fontutil.java
    // there hasnt been a single competent line of code yet
    // chatgpt accusations are gonna go crazy
    // especially because of the comments
    public static float getStringWidth(String string, float size) {
        //string = NickHider.fixText(string);

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setFont(currentFont);

        return graphics.getFontMetrics().stringWidth(string) * size;
    }


    // idrk where i would put this
    public static String getRandomString(int length) {
        String string = "";

        while (string.length() < length) string += FontUtil.letters.charAt((int) (Math.random() * FontUtil.letters.length()));

        return string;
    }

    /* // works but is slow, keeping for later
    // this is still p slow, with just 5000 letters rendering it drops fps by like 20...
    // ill figure smth out

    // its faster if all characters with the same texture are drawn at once
    public static void drawString(String string, float x, float y, Color color) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);

        int xAdd = 0;

        for (int i = 0; i < string.length(); i++) {
            int character = letters.indexOf(string.charAt(i));

            if (character == -1 || textures.length < character) {
                xAdd += 5;
            }

            else {
                CharactersInfo info = textures[character];

                GlStateManager.bindTexture(info.texture.getGlTextureId());
                GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);

                RenderUtil.drawTexturedRectangle(xAdd, 0, info.width, height);

                xAdd += info.width;
            }
        }

        GL11.glPopMatrix();
    }
     */
}