package com.github.scoliossis.base.utils;

import net.minecraft.network.Packet;

import java.util.ArrayList;

public class PacketUtil {
    public static ArrayList<Packet<?>> noEvent = new ArrayList<>();

    // i dont want to make criticals send an attack packet which will then be canceled
    // and then will trigger criticals again, in an endless loop.
    public static void sendPacketNoEvent(Packet<?> packet) {
        noEvent.add(packet);
        C.mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    // shorter to type PacketUtil.sendPacket
    public static void sendPacket(Packet<?> packet) {
        C.mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }


}