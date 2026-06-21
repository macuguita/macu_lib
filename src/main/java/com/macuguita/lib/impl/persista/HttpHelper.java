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
package com.macuguita.lib.impl.persista;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import com.macuguita.lib.impl.MacuLib;

final class HttpHelper {

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private HttpHelper() {}

	static HttpClient client() {
		return CLIENT;
	}

	static HttpRequest.Builder get(URI uri) {
		return HttpRequest.newBuilder(uri)
			.GET()
			.header("User-Agent", Persista.USER_AGENT == null ? MacuLib.MOD_ID + "/unknown" : Persista.USER_AGENT)
			.timeout(Persista.REQUEST_TIMEOUT);
	}

	static HttpRequest.Builder post(URI uri, String body) {
		return HttpRequest.newBuilder(uri)
			.POST(HttpRequest.BodyPublishers.ofString(body))
			.header("Content-Type", "application/json")
			.header("User-Agent", Persista.USER_AGENT)
			.timeout(Persista.REQUEST_TIMEOUT);
	}
}
