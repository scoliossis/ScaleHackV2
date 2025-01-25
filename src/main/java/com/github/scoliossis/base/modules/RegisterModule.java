package com.github.scoliossis.base.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterModule {
    String name();
    String description();

    boolean enabledByDefault() default false;

    ModuleCategory category();
    float order() default 0;
}
