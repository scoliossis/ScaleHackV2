package com.github.scoliossis.base.config;

import lombok.AllArgsConstructor;

@AllArgsConstructor
// fancier boolean. wild.
public class Category {
    public boolean open;

    public static Category New() {
        return new Category(false);
    }
}
