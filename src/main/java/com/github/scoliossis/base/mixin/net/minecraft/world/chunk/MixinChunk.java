package com.github.scoliossis.base.mixin.net.minecraft.world.chunk;

import com.github.scoliossis.base.Events.Bus;
import com.github.scoliossis.base.Events.impl.BlockChangeEvent;
import com.github.scoliossis.base.Events.impl.ChunkLoadEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class MixinChunk {
    @Shadow
    public abstract IBlockState getBlockState(BlockPos paramBlockPos);

    @Inject(method = "setBlockState", at = @At("HEAD"))
    public void impl$setBlockState(BlockPos pos, IBlockState state, CallbackInfoReturnable<IBlockState> cir) {
        IBlockState old = getBlockState(pos);
        if (state != old) {
            Bus.post(new BlockChangeEvent(pos, old, state));
        }
    }

    @Inject(method = "fillChunk", at = @At("RETURN"))
    public void impl$fillChunk(byte[] p_177439_1_, int p_177439_2_, boolean p_177439_3_, CallbackInfo ci) {
        Bus.post(new ChunkLoadEvent((Chunk) (Object) this));
    }
}
