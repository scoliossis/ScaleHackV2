package com.github.scoliossis.base.mixin.net.minecraft.network;

import com.github.scoliossis.base.Events.PacketEvent;
import com.github.scoliossis.base.utils.PacketUtil;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetworkManager.class)
public class NetworkManagerMixin {

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacketPre(Packet<?> packet, CallbackInfo ci) {
        // dont post packetevent if i used packetutil.sendpacketNOevent
        // i dont want packet send events triggering from client sent packets!!
        // but for blink i gonna need it to get ALL sent packets, so idrk what to do for that.
        if (!PacketUtil.noEvent.contains(packet) && MinecraftForge.EVENT_BUS.post(new PacketEvent.Send.Pre(packet)))
            ci.cancel();
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("RETURN"), cancellable = true)
    private void sendPacketPost(Packet<?> packet, CallbackInfo ci) {
        if (!PacketUtil.noEvent.contains(packet) && MinecraftForge.EVENT_BUS.post(new PacketEvent.Send.Post(packet)))
            ci.cancel();

        PacketUtil.noEvent.remove(packet);
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void packetReceievePre(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new PacketEvent.Receive.Pre(packet)))
            ci.cancel();
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("RETURN"), cancellable = true)
    private void packetReceievePost(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new PacketEvent.Receive.Post(packet)))
            ci.cancel();


        PacketUtil.noEvent.remove(packet);
    }
}