package com.macuguita.lib;

import com.macuguita.lib.supporters.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MacuguitaLib {

    public static final String MOD_ID = "macu_lib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        RoleChecker.init();
    }

}
