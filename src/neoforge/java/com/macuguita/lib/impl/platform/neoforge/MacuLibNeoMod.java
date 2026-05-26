/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl.platform.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import com.macuguita.lib.impl.MacuLib;

@Mod(MacuLib.MOD_ID)
public class MacuLibNeoMod {

    public MacuLibNeoMod(IEventBus modBus) {
        NeoCommonAbstraction.EVENT_BUS = modBus;
        for (var a : NeoCommonAbstraction.INSTANCE.lateActions()) {
            a.accept(modBus);
        }
        NeoCommonAbstraction.INSTANCE.lateActions().clear();
    }
}
