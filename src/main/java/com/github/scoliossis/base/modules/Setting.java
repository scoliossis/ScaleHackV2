package com.github.scoliossis.base.modules;

import com.github.scoliossis.base.config.ConfigOption;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Setting {
    String name();
    String description() default "";

    float order() default 0;

    // for sliders
    double min() default 0;
    double max() default 0;
    int precision() default 2;

    // todo: move to parent
    String modeParentString() default "";
}