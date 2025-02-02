package com.github.scoliossis.base.Events;

import lombok.Getter;
import lombok.Setter;

// stole this eventbus from mushroom!
@Setter
@Getter
public class Event {
    private boolean cancelled = false;

    public void cancel() {
        setCancelled(true);
    }

}
