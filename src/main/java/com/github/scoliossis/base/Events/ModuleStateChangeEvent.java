package com.github.scoliossis.base.Events;

import com.github.scoliossis.base.modules.Module;
import lombok.AllArgsConstructor;
import net.minecraftforge.fml.common.eventhandler.Event;

@AllArgsConstructor
public class ModuleStateChangeEvent extends Event {
    public final Class<? extends Module> module;
    public final boolean oldState;
    public final boolean newState;
}