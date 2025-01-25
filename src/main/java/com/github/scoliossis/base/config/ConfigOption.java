package com.github.scoliossis.base.config;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.github.scoliossis.base.modules.Setting;
import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract class ConfigOption<T> {
    public ConfigOption<?> parent;
    public ArrayList<ConfigOption<?>> children = new ArrayList<>();;

    public ConfigOption(Field field, Object instance, Setting annotation) {
        this.field = field;
        this.instance = instance;
        this.annotation = annotation;
    }

    @Getter
    protected final Field field;

    public final Object instance;
    @Getter
    protected final Setting annotation;

    public String name() {
        return annotation.name();
    }

    public T get() {
        try {
            return (T) field.get(instance);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to get field");
        }
    }

    protected void internalSet(T value) {
        try {
            field.set(instance, value);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Couldn't set field");
        }
    }

    public void set(T value) {
        internalSet(value);
    }

    public abstract void draw(int categoryNumber);
}