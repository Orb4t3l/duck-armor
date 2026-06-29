package com.orbital.duckarmor;

import com.orbital.duckarmor.init.ModCreativeTabs;
import com.orbital.duckarmor.init.ModItems;
import com.orbital.duckarmor.platform.Services;

public class DuckarmorCommon {

    public static final String MODID = "duckarmor";

    public static void init() {
        ModItems.init();
        ModCreativeTabs.init();
        Services.registerGameEvents();
        Services.populateCreativeTab();
    }

    public static void clientInit() {
        Services.registerClientEvents();
    }
}