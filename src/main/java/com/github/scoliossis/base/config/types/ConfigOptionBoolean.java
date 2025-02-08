package com.github.scoliossis.base.config.types;

import com.github.scoliossis.base.config.ConfigOption;
import com.github.scoliossis.base.modules.Setting;
import com.github.scoliossis.base.utils.AnimationUtil;
import com.github.scoliossis.base.utils.RenderUtil;

import java.lang.reflect.Field;

import static com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain.*;

public class ConfigOptionBoolean extends ConfigOption<Boolean> {

    public ConfigOptionBoolean(Field field, Object instance, Setting annotation) {
        super(field, instance, annotation);
    }

    static int boxSize = 8;

    @Override
    public void draw(int categoryNumber) {
        String settingName = formatSettingName(this.name());

        // draw background
        RenderUtil.drawRect(x, y, width, childHeight, backgroundColor);

        // if hovering over button
        onLeftClick();

        // draws a growing square infront of a white one, square grows until behind square is filled
        drawEnabledBox(categoryNumber);

        RenderUtil.drawStringCenteredY(settingName, x + xOff + 5, y + (childHeight / 2f), stringColor);

        RenderUtil.enableTranslate(0, childHeight);

        // draw children of this child.
        if (this.get())
            for (ConfigOption<?> child : this.children) child.draw(categoryNumber);
    }


    private void drawEnabledBox(int categoryNumber) {
        float smallSquareX = x + width - xOff - 20;
        float smallSquareY = y + childHeight / 2f - boxSize / 2f;

        // draws a small box to be filled
        RenderUtil.drawRoundedRect(smallSquareX, smallSquareY, boxSize, boxSize, 1, disabledColor);

        // if setting enabled + animation done
        float size = (float) AnimationUtil.getEasingAnimation(getAnimationKey());
        if (this.get() && AnimationUtil.getAnimation(getAnimationKey()) == null) size = 1;

        smallSquareX -= boxSize*size / 2f - boxSize / 2f;
        smallSquareY -= boxSize*size / 2f - boxSize / 2f;

        RenderUtil.drawRoundedRect(smallSquareX, smallSquareY, boxSize*size, boxSize * size, 1, enabledColor);
        //RenderUtil.drawRoundedRect(smallSquareX, smallSquareY, boxW*size, boxH, 1, new Color(color));
    }


    private void onLeftClick() {
        if (isHovered(x, y, width, childHeight)) {
            if (leftMousePressed) {
                this.set(!this.get());

                if (this.get()) AnimationUtil.addEasingAnimation(getAnimationKey());
                else AnimationUtil.removeEasingAnimation(getAnimationKey());

                leftMousePressed = false;
            }
        }
    }

    private String getAnimationKey() {
        return this.name() + this.annotation.description();
    }
}