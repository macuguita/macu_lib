package com.macuguita.lib.impl.persista;

import org.jetbrains.annotations.ApiStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@ApiStatus.Internal
final class HttpHelper {

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private HttpHelper() {}

	static HttpClient client() {
		return CLIENT;
	}

	static HttpRequest.Builder get(URI uri) {
		return HttpRequest.newBuilder(uri)
			.GET()
			.header("User-Agent", Persista.USER_AGENT)
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
