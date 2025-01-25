package com.github.scoliossis.base.Events;

import lombok.AllArgsConstructor;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;


// https://www.youtube.com/watch?v=g9xopViBKOQ

public class PacketEvent extends Event {
    public static class Send extends Event  {

        @Cancelable
        @AllArgsConstructor
        public static class Pre extends Event {
            public Packet<?> packet;
        }

        @AllArgsConstructor
        public static class Post extends Event {
            public Packet<?> packet;
        }
    }

    public static class Receive extends Event  {

        @Cancelable
        @AllArgsConstructor
        public static class Pre extends Event {
            public Packet<?> packet;
        }

        @AllArgsConstructor
        public static class Post extends Event {
            public Packet<?> packet;
        }
    }
}