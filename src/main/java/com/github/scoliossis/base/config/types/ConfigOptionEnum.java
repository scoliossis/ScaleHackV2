package com.github.scoliossis.base.config.types;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.github.scoliossis.base.config.ConfigOption;
import com.github.scoliossis.base.modules.Setting;
import com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain;
import com.github.scoliossis.base.utils.ChatUtil;
import com.github.scoliossis.base.utils.FontUtil;
import com.github.scoliossis.base.utils.RenderUtil;
import lombok.Getter;

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
        RenderUtil.drawRect(x, y, width, moduleHeight, new Color(26,26,26));
        RenderUtil.drawRect(x+xOff, y, width-xOff*2, childHeight, getBackgroundColor(this));

        String mode = formatSettingName(this.getOption().name);

        RenderUtil.drawStringCenteredY("&o" + settingName + "   -   " + mode, x + 5, y + (childHeight / 2f), fontSize, new Color(0xEEEEEE));

        Color color = getColor(categoryNumber);

        ClickGUIScreenMain.drawOutline(childHeight, color);

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

        float stringX = x+5;
        int stringY = childHeight;

        T[] enumConstants = (T[]) field.getType().getEnumConstants();

        for (int i = 0; i < enumConstants.length; i++) {
            String settingName = formatSettingName(enumConstants[i].toString());
            if (i != enumConstants.length-1) settingName += ", ";

            float stringWidth = FontUtil.getStringWidth(settingName, fontSize);
            if (stringX + stringWidth >= x + width) {
                RenderUtil.drawRect(x, stringY, width, moduleHeight, new Color(26,26,26));
                RenderUtil.drawRect(x+xOff, stringY, width-xOff*2, childHeight, getBackgroundColor(this, 2));

                stringX = x+5;
                stringY += childHeight;

                //y += height;
            }

            Color fontColor = new Color(0xEEEEEE);
            if (enumConstants[i] == this.get()) fontColor = color;

            if (isHovered(stringX, stringY, stringWidth, childHeight) && (leftMousePressed || rightMousePressed))
                this.set((T) field.getType().getEnumConstants()[i]);

            RenderUtil.drawStringCenteredY("&o" + settingName, stringX, stringY + (childHeight / 2f), fontSize, fontColor);

            stringX += FontUtil.getStringWidth(settingName, fontSize);
        }

        RenderUtil.drawRect(x, stringY, width, moduleHeight, new Color(26,26,26));
        RenderUtil.drawRect(x+xOff, stringY, width-xOff*2, childHeight, getBackgroundColor(this, 2));
        drawOutline(stringY, color);

        //yOffset -= (oldY-y);
        //categoryHeights.set(index, yOffset);
        RenderUtil.enableTranslate(0, stringY);
        drawOutline(stringY, color);
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