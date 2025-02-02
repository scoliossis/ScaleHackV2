package com.github.scoliossis.base.Events.impl;

import com.github.scoliossis.base.Events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.chunk.Chunk;

@Getter
@AllArgsConstructor
public class ChunkLoadEvent extends Event {
    private final Chunk chunk;
}
