package com.github.scoliossis.base.mixin.net.minecraft.util;

import com.github.scoliossis.base.Events.Bus;
import com.github.scoliossis.base.Events.impl.MovementInputEvent;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions extends MovementInput {
    @Shadow @Final private GameSettings gameSettings;

    /**
     * @author Lifix
     * @reason because funny
     */
    @Overwrite
    public void updatePlayerMoveState() {
        float strafeNumTemp = 0f;
        float forwardNumTemp = 0f;

        if (this.gameSettings.keyBindForward.isKeyDown()) {
            ++forwardNumTemp;
        }

        if (this.gameSettings.keyBindBack.isKeyDown()) {
            --forwardNumTemp;
        }

        if (this.gameSettings.keyBindLeft.isKeyDown()) {
            ++strafeNumTemp;
        }

        if (this.gameSettings.keyBindRight.isKeyDown()) {
            --strafeNumTemp;
        }

        boolean tempJump = this.gameSettings.keyBindJump.isKeyDown();
        boolean tempSneak = this.gameSettings.keyBindSneak.isKeyDown();

        MovementInputEvent event = new MovementInputEvent(strafeNumTemp, forwardNumTemp, tempJump, tempSneak, 0.3f);

        if (Bus.post(event)) {
            return;
        }

        this.moveStrafe = event.getStrafe();
        this.moveForward = event.getForward();

        this.jump = event.isJump();
        this.sneak = event.isSneak();

        if (this.sneak) {
            this.moveStrafe = (float)((double) event.getStrafe() * event.getSneakSpeed());
            this.moveForward = (float)((double) event.getForward() * event.getSneakSpeed());
        }
    }
}
