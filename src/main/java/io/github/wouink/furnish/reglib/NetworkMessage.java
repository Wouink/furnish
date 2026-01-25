package io.github.wouink.furnish.reglib;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * This class is a wrapper around CustomPacketPayload to ease its registration.
 */
public interface NetworkMessage extends CustomPacketPayload {
    enum MessageDirection {
        S2C,
        C2S
    }

    MessageDirection getDirection();
    StreamCodec getCoded();
}
