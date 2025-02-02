package com.github.scoliossis.base.Events.impl;

import com.github.scoliossis.base.Events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

@Getter
@Setter
@AllArgsConstructor
public class BlockChangeEvent extends Event {
    private BlockPos pos;
    private IBlockState old;
    private IBlockState update;
}
