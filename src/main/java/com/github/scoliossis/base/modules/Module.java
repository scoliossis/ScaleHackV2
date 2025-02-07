package com.github.scoliossis.base.modules;

import com.github.scoliossis.base.Events.Bus;
import com.github.scoliossis.base.Events.impl.ModuleStateChangeEvent;
import com.github.scoliossis.base.config.ConfigOption;
import com.github.scoliossis.base.modules.impl.client.ClickGUIModule;
import com.github.scoliossis.base.utils.AnimationUtil;
import com.github.scoliossis.base.utils.RenderUtil;
import com.github.scoliossis.base.utils.font.FontRegistry;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;

import static com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain.*;

public abstract class Module {
    public RegisterModule module;
    public ArrayList<ConfigOption<?>> children = new ArrayList<>();


    @Getter
    public boolean enabled;

    public void setEnabled(boolean flag) {
        boolean changed = enabled != flag;
        if (changed) {
            // needed for notifications / arraylist and maybe others
            if (Bus.post(new ModuleStateChangeEvent(this.getClass(), enabled, flag))) return;

            enabled = flag;

            if (enabled) onEnable();
            else onDisable();
        }
    }

    // Returns the new state
    public boolean toggle() {
        setEnabled(!enabled);
        return enabled;
    }

    private Color moduleToggledColor = new Color(2, 159, 95);
    private Color moduleDisabledColor = new Color(108, 3, 3);

    public void draw(int categoryNumber) {
        Color color = getColor(categoryNumber);

        moduleToggledColor = new Color(87, 3, 126);
        moduleDisabledColor = new Color(38, 3, 65);

        String settingName = formatSettingName(this.module.name());

        // draw background
        RenderUtil.drawRect(x,y,width,moduleHeight+moduleGap+5,moduleColor);

        // draw button
        RenderUtil.drawRoundedRect(x+xOff, y, width-xOff*2, moduleHeight, moduleRadius, moduleOutlineColor);
        RenderUtil.drawRoundedRect(x+xOff+1, y+1, width-xOff*2-2, moduleHeight-2, moduleRadius-2, moduleColor);

        String animationKey = getAnimationKey();
        AnimationUtil.Animation clickedInfo = AnimationUtil.getAnimation(animationKey);

        if (isHovered(x,y,width,moduleHeight)) {
            if (leftMousePressed) {
                clickedInfo = onLeftClick(clickedInfo, animationKey);
                leftMousePressed = false;
            }
            if (rightMousePressed) {
                onRightClick(getAltAnimationKey());
                rightMousePressed = false;
            }
        }

        int textY = y + moduleHeight / 2;

        int xOfEnabledButton = x + xOff + 10 + FontRegistry.getSpaceGroteskRegular18px().getStringWidth(moduleTitleModifiers + settingName);
        int toggleButtonWidth = 24;
        int toggleButtonHeight = 10;
        int yOfEnabledButton = textY - toggleButtonHeight / 2;

        int sliderBallSize = 8;

        RenderUtil.drawRoundedRect(
                xOfEnabledButton,
                yOfEnabledButton, toggleButtonWidth, toggleButtonHeight, toggleButtonHeight,
                this.enabled ? moduleToggledColor : moduleDisabledColor
        );

        RenderUtil.drawRoundedRect(
                xOfEnabledButton + (this.enabled ? toggleButtonWidth-sliderBallSize-2 : 2),
                yOfEnabledButton + toggleButtonHeight / 2f - sliderBallSize / 2f,
                sliderBallSize, sliderBallSize, sliderBallSize,
                new Color(0xffffffff)
        );

        // draw text
        RenderUtil.drawStringCenteredY(moduleTitleModifiers + settingName, x + xOff + 5, textY, stringColor);

        RenderUtil.enableTranslate(0, moduleHeight + moduleGap);
    }

    private AnimationUtil.Animation onLeftClick(AnimationUtil.Animation clickedInfo, String animationKey) {
        float animationX = mX - RenderUtil.getTranslate()[0];
        float animationY = mY - RenderUtil.getTranslate()[1];

        //if (clickedInfo != null) {
        //    animationX = clickedInfo.extra[0];
        //    animationY = clickedInfo.extra[1];
        //}


        if (!this.enabled) AnimationUtil.addEasingAnimation(animationKey, new float[]{animationX,animationY});
        else AnimationUtil.removeEasingAnimation(animationKey, new float[]{animationX,animationY});
        this.toggle();

        return AnimationUtil.getAnimation(animationKey);
    }

    private void onRightClick(String animationKey) {
        if (!ClickGUIModule.rightClickedModules.contains(this)) {
            ClickGUIModule.rightClickedModules.add(this);
            AnimationUtil.addEasingAnimation(animationKey);

            return;
        }

        ClickGUIModule.rightClickedModules.remove(this);
        AnimationUtil.removeEasingAnimation(animationKey);
    }

    private void drawToggledModule(AnimationUtil.Animation clickedInfo, int categoryNumber) {
        Color color = getColor(categoryNumber);
        if (clickedInfo != null) {
            RenderUtil.scissorBeginMatrix(x + xOff, y, width - xOff * 2, moduleHeight);

            double growWidth = AnimationUtil.getEasingAnimation(getAnimationKey());
            float wid = (float) ((width*2) * growWidth);

            RenderUtil.drawRoundedRect(clickedInfo.extra[0] - wid / 2f, clickedInfo.extra[1] - wid / 2f, wid, wid, wid, color);
            RenderUtil.scissorEndMatrix();
        }
        else if (this.enabled) RenderUtil.drawRect(x + xOff, y, width - xOff*2, moduleHeight, color);
    }

    private String getAnimationKey() {
        return this.module.name()+this.module.description();
    }

    private String getAltAnimationKey() {
        return this.module.name()+this.module.category();
    }

    protected abstract void onEnable();

    protected abstract void onDisable();
}
