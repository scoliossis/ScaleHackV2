package com.github.scoliossis.base.Events.impl;

import com.github.scoliossis.base.Events.Event;
import com.github.scoliossis.base.modules.Module;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ModuleStateChangeEvent extends Event {
    public final Class<? extends Module> module;
    public final boolean oldState;
    public final boolean newState;
}