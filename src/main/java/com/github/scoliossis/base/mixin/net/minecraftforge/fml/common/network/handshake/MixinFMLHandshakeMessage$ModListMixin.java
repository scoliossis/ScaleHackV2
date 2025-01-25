package com.github.scoliossis.base.mixin.net.minecraftforge.fml.common.network.handshake;

import com.github.scoliossis.base.Main;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(FMLHandshakeMessage.ModList.class)
public class MixinFMLHandshakeMessage$ModListMixin {
    @Shadow(remap = false) private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"), remap = false)
    private void impl$clinit(List<?> modList, CallbackInfo ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
            this.modTags.remove(Main.MOD_ID);
            System.out.printf("Removed modid of %s", Main.MOD_ID);
        }
    }
}
