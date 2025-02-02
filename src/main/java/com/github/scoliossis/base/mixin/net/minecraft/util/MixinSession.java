package com.github.scoliossis.base.mixin.net.minecraft.util;

import com.github.scoliossis.base.bridge.net.minecraft.util.SessionBridge;
import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Session.class)
public class MixinSession implements SessionBridge {
}
