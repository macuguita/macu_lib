/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.test;


import net.fabricmc.api.ModInitializer;


public class MacuLibTestFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        MacuLibTest.init();
    }
}
