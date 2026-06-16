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

public class MacuLibConfig extends WrappedConfig {

	public Persista persista = new Persista();
	public static class Persista implements Section {
		@Comment("The request timeout for persista http requests")
		public long requestTimeout = 6000;

		@Comment("Whether to automatically fetch player data on login")
		public boolean loginAutofetch = true;

		@Comment("Whether to force a refresh of player data on login instead of using cached values")
		public boolean loginForceRefresh = false;
	}
}
