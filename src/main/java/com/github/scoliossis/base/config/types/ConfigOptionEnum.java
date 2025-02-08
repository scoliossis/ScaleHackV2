package com.github.scoliossis.base.config.types;

import com.github.scoliossis.base.config.ConfigOption;
import com.github.scoliossis.base.modules.Setting;
import com.github.scoliossis.base.utils.RenderUtil;
import com.github.scoliossis.base.utils.font.FontRegistry;
import lombok.Getter;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain.*;

public class ConfigOptionEnum<T> extends ConfigOption<T> {
    public ConfigOptionEnum(Field field, Object instance, Setting annotation) {
        super(field, instance, annotation);
    }

    private ArrayList<ConfigOptionEnum<?>> shownModes = new ArrayList<>();
    
    @Override
    public void draw(int categoryNumber) {
        String settingName = formatSettingName(this.name());

        // draw background
        RenderUtil.drawRect(x, y, width, childHeight, backgroundColor);

        String mode = formatSettingName(this.getOption().name);

        RenderUtil.drawStringCenteredY(settingName + "   -   " + mode, x + xOff + 5, y + (childHeight / 2f), stringColor);

        Color color = getColor(categoryNumber);

        onClickMain();
        if (shownModes.contains(this)) drawModes(color);

        RenderUtil.enableTranslate(0,childHeight);

        // draw children of this child.
        for (ConfigOption<?> child : this.children) {
            if (child.getAnnotation().modeParentString().equals(this.getOption().name))
                child.draw(categoryNumber);
        }
    }

    private void onClickMain() {
        if (isHovered(x, y, width, childHeight)) {
            if (leftMousePressed || rightMousePressed) {
                if (shownModes.contains(this)) shownModes.remove(this);
                else shownModes.add(this);
            }
        }
    }

    private void drawModes(Color color) {
        //float oldY = y;

        float baseStringX = x+xOff + 10;
        float stringX = baseStringX;
        int stringY = childHeight;

        T[] enumConstants = (T[]) field.getType().getEnumConstants();

        RenderUtil.drawRect(x, stringY, width, childHeight, backgroundColor);

        for (int i = 0; i < enumConstants.length; i++) {
            String settingName = formatSettingName(enumConstants[i].toString());
            if (i != enumConstants.length-1) settingName += ", ";

            float stringWidth = FontRegistry.getSpaceGroteskRegular18px().getStringWidth(settingName);
            if (stringX + stringWidth >= x + width - xOff) {
                stringX = baseStringX;
                stringY += childHeight;

                RenderUtil.drawRect(x, stringY, width, childHeight, backgroundColor);

                //y += height;
            }

            Color fontColor = stringColor;
            if (enumConstants[i] == this.get()) fontColor = enabledColor;

            if (isHovered(stringX, stringY, stringWidth, childHeight) && (leftMousePressed || rightMousePressed))
                this.set((T) field.getType().getEnumConstants()[i]);

            RenderUtil.drawStringCenteredY(settingName, stringX, stringY + (childHeight / 2f), fontColor);

            stringX += FontRegistry.getSpaceGroteskRegular18px().getStringWidth(settingName);
        }

        //yOffset -= (oldY-y);
        //categoryHeights.set(index, yOffset);
        RenderUtil.enableTranslate(0, stringY);
    }


    public EnumOption<T> getOption() {
        return new EnumOption<>(get(), field);
    }

    public static class EnumOption<T> {

        private final @Getter String name;
        private final @Getter T value;
        private final Field field;

        public EnumOption(T value, Field field) {
            name = value.toString();
            this.value = value;
            this.field = field;
        }

        public int getIndex() {
            T[] c = (T[]) field.getType().getEnumConstants();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == value) {
                    return i;
                }
            }
            throw new RuntimeException("Couldn't find index");
        }

        public EnumOption<T> next() {
            int idx = getIndex();
            T[] c = (T[]) field.getType().getEnumConstants();
            if (idx == c.length - 1) {
                // loop around!!!
                return new EnumOption<>(c[0], field);
            } else {
                return new EnumOption<>(c[idx + 1], field);
            }
        }

        public EnumOption<T> setInt(int i) {
            T[] c = (T[]) field.getType().getEnumConstants();
            if (i > c.length - 1) {
                return new EnumOption<>(c[0], field);
            }
            return new EnumOption<>(c[i], field);
        }

        public EnumOption<T> previous() {
            int idx = getIndex();
            T[] c = (T[]) field.getType().getEnumConstants();
            if (idx - 1 < 0) {
                // we back
                return new EnumOption<>(c[c.length - 1], field);
            } else {
                return new EnumOption<>(c[idx - 1], field);
            }
        }
    }
}