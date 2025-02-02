package com.github.scoliossis.base.utils.font;

import com.github.scoliossis.base.Main;
import com.github.scoliossis.base.bridge.net.minecraft.client.renderer.WorldRendererBridge;
import com.github.scoliossis.base.utils.C;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class CustomFont {
    private float imgSize = 1048f;
    protected CharData[] charData = new CharData[256];
    @Getter
    protected Font font;
    @Getter
    protected boolean antiAlias;
    @Getter
    protected boolean fractionalMetrics;
    protected int fontHeight = -1;
    protected int charOffset = 0;
    protected int tex;
    private final ResourceLocation resourceLocation;
    public final float size;

    public CustomFont(ResourceLocation location, float size) {
        this.resourceLocation = location;
        this.size = size;
        this.antiAlias = true;
        this.fractionalMetrics = true;
    }

    public boolean isLoaded() {
        Font font;
        try {
            InputStream trueTypeInputStream = null;
            if (Minecraft.getMinecraft() != null) {
                if (Minecraft.getMinecraft().getResourceManager() != null) {
                    trueTypeInputStream = Minecraft.getMinecraft().getResourceManager()
                            .getResource(this.resourceLocation).getInputStream();
                } else {
                    trueTypeInputStream = Minecraft.getMinecraft().mcDefaultResourcePack
                            .getInputStream(this.resourceLocation);
                }
            }

            if (trueTypeInputStream == null) {
                throw new Exception("Failed to get TrueType input stream.");
            }

            font = Font.createFont(Font.TRUETYPE_FONT, trueTypeInputStream).deriveFont(this.size);
        } catch (Exception var3) {
            String dialogMsg = String.format("Failed to load font \"%s:%s\" - using fallback Arial.",
                    this.resourceLocation.getResourceDomain(), this.resourceLocation.getResourcePath());
            JOptionPane.showMessageDialog(null, dialogMsg, Main.CLIENT_NAME,
                    JOptionPane.ERROR_MESSAGE);
            font = new Font("Arial", Font.PLAIN, (int)this.size);
        }

        this.font = font;
        this.tex = this.setupTexture(this.font, this.antiAlias, this.fractionalMetrics, this.charData);
        return true;
    }

    public String getTranslation() {
        return this.resourceLocation.getResourcePath();
    }

    protected int setupTexture(Font var1, boolean var2, boolean var3, CharData[] var4) {
        BufferedImage fontImage = this.generateFontImage(var1, var2, var3, var4);
        int textureID = TextureUtil.glGenTextures();
        TextureUtil.uploadTextureImage(textureID, fontImage);
        return textureID;
    }

    private BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics,
                                            CharData[] charData) {
        int imgSize = (int)this.imgSize;
        BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setFont(font);
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, imgSize, imgSize);
        graphics.setColor(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics
                ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias
                ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias
                ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        FontMetrics fontMetrics = graphics.getFontMetrics();

        int tallestCharacterOnLine = 0;
        int currentX = 0;
        int currentY = 1;

        for (int x = 0; x < charData.length; ++x) {
            char character = (char)x;
            CharData newData = new CharData();
            Rectangle2D var15 = fontMetrics.getStringBounds(String.valueOf(character), graphics);
            newData.width = var15.getBounds().width + 8;
            newData.height = var15.getBounds().height;
            if (currentX + newData.width >= imgSize) {
                currentX = 0;
                currentY += tallestCharacterOnLine;
                tallestCharacterOnLine = 0;
            }

            if (newData.height > tallestCharacterOnLine) {
                tallestCharacterOnLine = newData.height;
            }

            newData.storedX = currentX;
            newData.storedY = currentY;
            if (newData.height > this.fontHeight) {
                this.fontHeight = newData.height;
            }

            charData[x] = newData;
            graphics.drawString(String.valueOf(character), currentX + 2, currentY + fontMetrics.getAscent());
            currentX += newData.width;
        }

        return image;
    }

    protected void drawChar(CharData[] var2, char index, float x, float y) {
        Tessellator tessellator = Tessellator.getInstance();
        boolean currentlyDrawing = WorldRendererBridge.from(tessellator.getWorldRenderer()).bridge$isDrawing();

        if (!currentlyDrawing) {
            tessellator.getWorldRenderer().begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        }

        try {
            this.drawQuad(tessellator.getWorldRenderer(), x, y,
                    (float)var2[index].width, (float)var2[index].height,
                    (float)var2[index].storedX, (float)var2[index].storedY,
                    (float)var2[index].width, (float)var2[index].height);
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        if (!currentlyDrawing) {
            tessellator.draw();
        }
    }

    private void drawQuad(WorldRenderer wr, float x, float y, float width, float height, float srcX, float srcY,
                          float srcWidth, float srcHeight) {
        float renderSrcX = srcX / this.imgSize;
        float renderSrcY = srcY / this.imgSize;
        float renderSrcWidth = srcWidth / this.imgSize;
        float renderSrcHeight = srcHeight / this.imgSize;

        wr.pos(x + width, y, 0.0)
                .tex(renderSrcX + renderSrcWidth, renderSrcY).endVertex();
        wr.pos(x, y, 0.0)
                .tex(renderSrcX, renderSrcY).endVertex();
        wr.pos(x, y + height, 0.0)
                .tex(renderSrcX, renderSrcY + renderSrcHeight).endVertex();
        wr.pos(x, y + height, 0.0)
                .tex(renderSrcX, renderSrcY + renderSrcHeight).endVertex();
        wr.pos(x + width, y + height, 0.0)
                .tex(renderSrcX + renderSrcWidth, renderSrcY + renderSrcHeight).endVertex();
        wr.pos(x + width, y, 0.0)
                .tex(renderSrcX + renderSrcWidth, renderSrcY).endVertex();
    }

    public int getHeight() {
        float scaleFactor = C.res().getScaleFactor();
        float scale = (2.0f / scaleFactor);

        boolean followMinecraftScale = true;

        try {
            // i did NOT steal my own code from like 2 years ago wtfrick
            //followMinecraftScale = CheatBreaker.getInstance().globalSettings.followMinecraftScale.<Boolean>value();
        } catch (Exception ignored) {

        }

        if (followMinecraftScale) {
            return (int) (((this.fontHeight * scale) - 8) / 2);
        } else {
            return (this.fontHeight - 8) / 2;
        }
    }

    public int getStringWidth(String string) {
        int var2 = 0;
        char[] var3 = string.toCharArray();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            char var6 = var3[var5];
            if (var6 < this.charData.length && var6 >= 0) {
                var2 += this.charData[var6].width - 8 + this.charOffset;
            }
        }

        return var2 / 2;
    }

    public void setAntiAlias(boolean antiAlias) {
        if (this.antiAlias != antiAlias) {
            this.antiAlias = antiAlias;
            TextureUtil.deleteTexture(this.tex);
            this.tex = this.setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
        }
    }

    public void setFractionalMetrics(boolean fractionalMetrics) {
        if (this.fractionalMetrics != fractionalMetrics) {
            this.fractionalMetrics = fractionalMetrics;
            TextureUtil.deleteTexture(this.tex);
            this.tex = this.setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
        }
    }

    public void setFont(Font font) {
        this.font = font;
        TextureUtil.deleteTexture(this.tex);
        this.tex = this.setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
    }

    public static class CharData {
        public int width;
        public int height;
        public int storedX;
        public int storedY;

        protected CharData() {
        }
    }
}
