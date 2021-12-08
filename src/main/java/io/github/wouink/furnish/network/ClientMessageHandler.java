package io.github.wouink.furnish.network;

import io.github.wouink.furnish.Furnish;

public class ClientMessageHandler {

	public static boolean acceptsProtocol(String protocol) {
		return Furnish.MESSAGE_PROTOCOL_VERSION.equals(protocol);
	}
}
