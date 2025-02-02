package com.github.scoliossis.base.utils.font;

import com.github.scoliossis.base.utils.C;
import net.minecraft.util.ResourceLocation;

public class FontRegistry {
    private static int fixFontSize(int size, float scale) {
        if (scale - ((int) scale) == 0f) { // if scale == 1 (mc:2), == 2 (mc:4), == 3 (mc:6), and so on
            return size * (int) scale;
        }

        float scaledSize = size * scale;
        if (scaledSize - ((int) scaledSize) == 0) {
            if (scaledSize % 2 == 0) {
                return (int) scaledSize;
            }
        }

        int castedSize = (int) scaledSize - 1;
        float sizeRemaining = (size - 1) - castedSize;

        if (sizeRemaining >= 0.5f) {
            castedSize += 1;
        }

        if (castedSize % 2 != 0) {
            castedSize += 1;
        }

        return castedSize;
    }

    private static CustomFontRenderer createNewFont(ResourceLocation font, int size, float scale) {
        int castedSize = fixFontSize(size, scale);
        C.getLogger().info("{} {} -> {}", font.getResourcePath(), size, castedSize);
        boolean followMinecraftScale = true;
        CustomFontRenderer instance = new CustomFontRenderer(font, followMinecraftScale ? castedSize : size * scale);
        if (instance.isLoaded()) {
            return instance;
        } else {
            return null;
        }
    }

    private static boolean validFont(CustomFontRenderer font, int ogSize) {
        boolean followMinecraftScale = true;
        if (followMinecraftScale) {
            float scale = (C.res().getScaleFactor() / 2f);
            return fixFontSize(ogSize, scale) == font.size;
        } else {
            return ogSize == font.size;
        }
    }

    private static CustomFontRenderer getFont(String fontName, int ogSize) {
        float scale = (C.res().getScaleFactor() / 2f);
        boolean followMinecraftScale = true;
        if (!followMinecraftScale) {
            scale = 1f;
        }

        // godly.
        switch (fontName) {
            case "SpaceGrotesk-Regular":
                switch (ogSize) {
                    case 14:
                        if (!validFont(spaceGroteskRegular14px, ogSize)) {
                            spaceGroteskRegular14px = createNewFont(spaceGroteskRegular, ogSize, scale);
                        }
                        return spaceGroteskRegular14px;
                    case 18:
                        if (!validFont(spaceGroteskRegular18px, ogSize)) {
                            spaceGroteskRegular18px = createNewFont(spaceGroteskRegular, ogSize, scale);
                        }
                        return spaceGroteskRegular18px;
                }
                break;
        }

        // This should never happen BUT in the off chance it does
        return new CustomFontRenderer(new ResourceLocation("null"), 20f);
    }

    // Textures
    private static final ResourceLocation spaceGroteskRegular =
            new ResourceLocation("scalehack:fonts/spacegrotesk/regular.ttf");

    // Font objects
    private static CustomFontRenderer spaceGroteskRegular14px = createNewFont(spaceGroteskRegular, 14, 1f);
    private static CustomFontRenderer spaceGroteskRegular18px = createNewFont(spaceGroteskRegular, 18, 1f);

    // Getters
    public static CustomFontRenderer getSpaceGroteskRegular14px() {
        return getFont("SpaceGrotesk-Regular", 14);
    }

    public static CustomFontRenderer getSpaceGroteskRegular18px() {
        return getFont("SpaceGrotesk-Regular", 18);
    }
}
