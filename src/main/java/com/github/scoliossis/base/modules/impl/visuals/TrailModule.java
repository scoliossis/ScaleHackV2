package com.github.scoliossis.base.modules.impl.visuals;

import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;
import com.github.scoliossis.base.modules.Module;

@RegisterModule(
        name = "Trail",
        description = "\"fps is kil\"... no",
        category = ModuleCategory.Render
)
public class TrailModule extends Module {
    /*
    private final NumberOption<Integer> trailLength = new NumberOption<>("Trail Length", 10,
            1, 20);

    private final List<Vec3> cachedPositions = new ArrayList<>();

    public TrailModule() {
        super("Trail", "\"fps is kil\"... no", ModuleCategory.VISUALS);
    }

    @SubscribeEvent
    public void onMotionPost(MotionEvent.Post event) {
        this.cachedPositions.add(ClientUtil.getPlayer().getPositionVector());

        while (this.cachedPositions.size() > this.trailLength.getValue() && !this.cachedPositions.isEmpty()) {
            this.cachedPositions.remove(0);
        }
    }

    @SubscribeEvent
    public void onRender3d(RenderWorldLastEvent event) {
        for (Vec3 pos : this.cachedPositions) {
            RenderUtil.drawCool3dGlow(pos.xCoord, pos.yCoord, pos.zCoord, 0.2f, 0.2f, event.partialTicks,
                    RenderUtil.getColorsFade((pos.xCoord + pos.zCoord) * 200, RenderUtil.rainbowColors,
                            5), true);
        }
    }
     */

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
