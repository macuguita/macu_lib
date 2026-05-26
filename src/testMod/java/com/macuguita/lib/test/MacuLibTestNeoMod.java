/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.test;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;


@Mod(MacuLibTest.MOD_ID)
public class MacuLibTestNeoMod {

    public MacuLibTestNeoMod(IEventBus modBus) {
        MacuLibTest.init();
    }
}
