package com.github.scoliossis.base.modules.impl.visuals;

import com.github.scoliossis.base.modules.Module;

import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;

import java.awt.*;

@RegisterModule(
        name = "Name Tags",
        description = "funny nametag",
        category = ModuleCategory.Render
)
public class NameTagsModule extends Module {

    //@SubscribeEvent
    //public void onRenderEntity(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
    //    if (!(event.entity instanceof EntityPlayer)) {
    //        return;
    //    }
    //
    //    event.setCanceled(true);
    //    RenderUtil.renderNameTag(event.entity, event, new Color(22, 22, 22, 100));
    //}

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
