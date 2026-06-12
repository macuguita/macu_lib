/*
 * Copyright 2026 macuguita
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.macuguita.lib.impl.platform.neoforge;

import org.jetbrains.annotations.ApiStatus;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import com.macuguita.lib.impl.MacuLib;

@ApiStatus.Internal
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
