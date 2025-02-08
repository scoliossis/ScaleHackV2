package com.github.scoliossis.base.config.types;

import com.github.scoliossis.base.config.Category;
import com.github.scoliossis.base.config.ConfigOption;
import com.github.scoliossis.base.modules.Setting;
import com.github.scoliossis.base.utils.AnimationUtil;
import com.github.scoliossis.base.utils.RenderUtil;
import com.github.scoliossis.base.utils.font.FontRegistry;

import java.lang.reflect.Field;

import static com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain.*;

public class ConfigOptionCategory extends ConfigOption<Category> {

    public ConfigOptionCategory(Field field, Object instance, Setting annotation) {
        super(field, instance, annotation);
    }

    @Override
    public void draw(int categoryNumber) {
        String settingName = formatSettingName(this.name());

        // draw background
        RenderUtil.drawRect(x, y, width, childHeight, backgroundColor);

        onLeftClick();

        float textX = x + (width/2f) - (FontRegistry.getSpaceGroteskRegular18px().getStringWidth(settingName)/2f);

        RenderUtil.drawStringCenteredY(settingName, textX, y + (childHeight / 2f), stringColor);

        RenderUtil.enableTranslate(0,childHeight);

        // draw children of this child.
        if (this.get().open)
            for (ConfigOption<?> child : this.children) child.draw(categoryNumber);
    }


    private void onLeftClick() {
        if (isHovered(x, y, width, childHeight)) {
            if (leftMousePressed || rightMousePressed) {
                leftMousePressed = rightMousePressed = false;

                this.get().open = !this.get().open;

                if (this.get().open) AnimationUtil.addEasingAnimation(getAnimationKey());
                else AnimationUtil.removeEasingAnimation(getAnimationKey());

            }
        }
    }

    private String getAnimationKey() {
        return this.name() + this.annotation.description();
    }
}