package com.github.scoliossis.base.Events.impl;

import com.github.scoliossis.base.Events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KeyPressEvent extends Event {
    protected int key;
    protected char character;

    public static class Pressed extends KeyPressEvent {
        public Pressed(int key, char character) {
            super(key, character);
        }
    }

    public static class Released extends KeyPressEvent {
        public Released(int key, char character) {
            super(key, character);
        }
    }
}
