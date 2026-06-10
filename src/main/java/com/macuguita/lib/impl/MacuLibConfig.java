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
package com.macuguita.lib.impl;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MacuLibConfig extends WrappedConfig {

	@Comment("Config for the supporter perks")
	public Supporters supporters = new Supporters();

	public static class Supporters implements Section {
		@Comment("The amount of minutes to fetch changes in the role versions")
		@Comment("if the value is negative or 0 it only checks at startup")
		public int roleCheckerMinutesInterval = 60 * 12;
	}
}
