package com.github.scoliossis.base.modules;

import com.github.scoliossis.base.modules.impl.client.ClickGUIModule;
import com.github.scoliossis.base.utils.AnimationUtil;
import com.github.scoliossis.base.utils.C;
import com.github.scoliossis.base.Events.ModuleStateChangeEvent;
import com.github.scoliossis.base.config.ConfigOption;
import com.github.scoliossis.base.utils.FontUtil;
import com.github.scoliossis.base.utils.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

import static com.github.scoliossis.base.screens.clickgui.ClickGUIScreenMain.*;

public abstract class Module {
    public RegisterModule module;
    public ArrayList<ConfigOption<?>> children = new ArrayList<>();


    public boolean enabled;

    public void setEnabled(boolean flag) {
        boolean changed = enabled != flag;
        if (changed) {
            // needed for notifications / arraylist and maybe others
            if (C.eventBus.post(new ModuleStateChangeEvent(this.getClass(), enabled, flag))) return;

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

    static int stringIndentLeft = 9;

    public void draw(int categoryNumber) {
        Color color = getColor(categoryNumber);

        String settingName = formatSettingName(this.module.name());

        // draw background
        RenderUtil.drawRect(x, y, width, moduleHeight, new Color(26,26,26));

        // draw button
        RenderUtil.drawRect(x + xOff, y, width - xOff*2, moduleHeight, new Color(37,37,37));

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

        // draws the box filling
        drawToggledModule(clickedInfo, categoryNumber);

        // draw text
        //RenderUtil.drawStringCenteredY("&o" + settingName, x + 5, y + (moduleHeight / 2f), fontSize, new Color(0xEEEEEE));
        RenderUtil.drawStringCenteredY("&o" + settingName, x + width - stringIndentLeft - FontUtil.getStringWidth(settingName, fontSize), y + (moduleHeight / 2f), fontSize, new Color(0xEEEEEE));

        // draws walls
        drawOutline(moduleHeight, color);

        RenderUtil.enableTranslate(0, moduleHeight);
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
