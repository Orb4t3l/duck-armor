package com.orbital.duckarmor;

import com.orbital.duckarmor.init.ModCreativeTabs;
import com.orbital.duckarmor.init.ModItems;

public class DuckarmorCommon {

    public static final String MODID = "duckarmor";

    public static void init() {
        ModItems.init();
        ModCreativeTabs.init();
    }
}