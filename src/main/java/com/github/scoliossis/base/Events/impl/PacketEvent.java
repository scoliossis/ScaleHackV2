package com.github.scoliossis.base.Events.impl;

import com.github.scoliossis.base.Events.Event;
import lombok.AllArgsConstructor;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;


// https://www.youtube.com/watch?v=g9xopViBKOQ

public class PacketEvent extends Event {
    public static class Send extends Event {

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