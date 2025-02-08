package com.github.scoliossis.base.config.types;

import com.github.scoliossis.base.config.ConfigOption;
import com.github.scoliossis.base.modules.Setting;
import com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain;
import com.github.scoliossis.base.utils.MathUtil;
import com.github.scoliossis.base.utils.RenderUtil;
import com.github.scoliossis.base.utils.font.FontRegistry;

import java.awt.*;
import java.lang.reflect.Field;

import static com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain.*;

public class ConfigOptionNumber<T> extends ConfigOption<T> {
    public ConfigOptionNumber(Field field, Object instance, Setting annotation) {
        super(field, instance, annotation);
    }

    int sliderIndent = 5;
    double sliderWidth = width - xOff*2 - sliderIndent*2;
    double settingGap = this.annotation.max() - this.annotation.min();

    @Override
    public void draw(int categoryNumber) {

        String settingName = formatSettingName(this.name());
        // draw background
        RenderUtil.drawRect(x, y, width, childHeight, backgroundColor);

        // u cant cast Integer or any other non-primitive to double, so make it a string, and parse.
        double settingNumber = Double.parseDouble(get().toString());
        double settingFraction = (settingNumber-this.annotation.min()) / settingGap;

        double currentSliderNumber = settingFraction * sliderWidth;

        onDragSlider();

        // draws slider %
        RenderUtil.drawRect(x + xOff + sliderIndent, y + childHeight - 8, (float) currentSliderNumber, 2, enabledColor);
        RenderUtil.drawRoundedRect((float) (x + xOff + sliderIndent + currentSliderNumber - 3), y + childHeight - 10, 6, 6, 6, new Color(255,255,255));


        RenderUtil.drawStringCenteredY(settingName, x + xOff + 5, y + (childHeight / 4f), stringColor);
        RenderUtil.drawStringCenteredY(""+settingNumber, x + width - FontRegistry.getSpaceGroteskRegular18px().getStringWidth("" + settingNumber) - xOff - 10, y + (childHeight / 4f), stringColor);

        RenderUtil.enableTranslate(0, childHeight);

        // draw children of this child. (will never have children in an ideal world)
        //for (ConfigOption<?> child : this.children) child.draw(categoryNumber);
    }

    private ConfigOptionNumber<?> currentSlider;

    private void onDragSlider() {
        if (isHovered(x, y, (float) sliderWidth, childHeight) && leftMouseDown && ClickGUIScreenMain.currentDraggingCategory == null)
            currentSlider = this;

        if (currentSlider == this) {
            if (!leftMouseDown) {
                currentSlider = null;
                return;
            }

            double mouseXonSlider = mX - RenderUtil.getTranslate()[0] - x - xOff - sliderIndent;
            double sliderSelectedFraction = mouseXonSlider / sliderWidth;
            double numberSetting = (sliderSelectedFraction * settingGap) + this.annotation.min();

            this.set(numberSetting);
        }
    }


    public void set(double value) {
        if (this.field.getType() == Integer.class) {
            int v = (int) value;
            Integer rV = Math.min((int) annotation.max(), Math.max((int) annotation.min(), v));
            internalSet((T) rV);
        } else if (this.field.getType() == Long.class) {
            long v = (long) value;
            Long rV = Math.min((long) annotation.max(), Math.max((long) annotation.min(), v));
            internalSet((T) rV);
        } else if (this.field.getType() == Float.class) {
            float v = (float) value;
            float rV = Math.min((float) annotation.max(), Math.max((float) annotation.min(), v));
            Float rrV = MathUtil.roundTo(rV, annotation.precision());
            internalSet((T) rrV);
        } else if (this.field.getType() == Double.class) {
            double v = (double) value;
            double rV = Math.min(annotation.max(), Math.max(annotation.min(), v));
            Double rrV = MathUtil.roundTo(rV, annotation.precision());
            internalSet((T) rrV);
        }
    }
}