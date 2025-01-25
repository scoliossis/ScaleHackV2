package com.github.scoliossis.base.modules.impl.visuals;

import com.github.scoliossis.base.modules.Module;

import com.github.scoliossis.base.modules.ModuleCategory;
import com.github.scoliossis.base.modules.RegisterModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

@RegisterModule(
        name = "LSD",
        description = "get that high :speaking_head::100:",
        category = ModuleCategory.Render
)
public class LSDModule extends Module {
    /*
    private final ArrayList<CirceProperties> circles = new ArrayList<>();

    public final Option<Boolean> lesbianMode = new Option<>("Lesbian mode", true);

    public LSDModule() {
        super("LSD", "get that high :speaking_head::100:", ModuleCategory.VISUALS);
        this.offerOptions(this.lesbianMode);
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event) {
        ScaledResolution sr = ClientUtil.res();

        if (this.circles.size() < 20) {
            double sw = sr.getScaledWidth_double();
            double sh = sr.getScaledHeight_double();

            double x = (sw * Math.random()) - (sw / 2f);
            double y = (sh * Math.random()) - (sh / 2f);
            double speed = Math.random();
            long time = System.currentTimeMillis();

            this.circles.add(new CirceProperties(x, y, speed, time));
        }

        List<CirceProperties> removeQueue = new ArrayList<>();
        for (CirceProperties circle : this.circles) {
            float x = (float) (circle.x + ((System.currentTimeMillis() - circle.time) / 200f * circle.speed));
            float y = (float) (circle.y + ((System.currentTimeMillis() - circle.time) / 400f * circle.speed));
            float size = (float) (((System.currentTimeMillis() - circle.time) / 1000f) * circle.speed) * 200;

            RenderUtil.drawGlow(x, y, size, size, size, RenderUtil.getColorsFade(circle.x,
                    this.lesbianMode.getValue() ? RenderUtil.lesbianColors : RenderUtil.rainbowColors,
                    2, 100));

            if (x + size / 2f > sr.getScaledWidth()) {
                removeQueue.add(circle);
            }
        }

        this.circles.removeIf(removeQueue::contains);
    }
     */

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class CirceProperties {
        private double x;
        private double y;
        private double speed;
        private long time;
    }
}
