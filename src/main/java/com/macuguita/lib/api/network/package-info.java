/**
 * Networking API for registering and sending custom packets between client and server.
 *
 * <p>This package provides two entry points:
 *
 * <ul>
 *   <li>{@link com.macuguita.lib.api.network.PacketRegistry} — registers serverbound (C2S) and
 *       clientbound (S2C) payload types and their handlers.
 *   <li>{@link com.macuguita.lib.api.network.PacketDistributor} — sends serverbound (C2S) and
 *       clientbound (S2C) packets.
 * </ul>
 *
 * <h2>Clientbound (S2C) sidedness and safety</h2>
 *
 * <p>Clientbound (S2C) payload types are safe to register from common code because their
 * client-side handlers are never attached or invoked on a dedicated server. Registration via {@link
 * com.macuguita.lib.api.network.PacketRegistry#registerClientboundPlayPacket} only declares the
 * type and codec; the actual handler is attached separately through {@link
 * com.macuguita.lib.api.network.PacketRegistry#registerClientboundPacketHandler}, which must only
 * be called from client-only code paths (e.g. a client lifecycle event or client-only entrypoint).
 *
 * <p>This separation ensures that client-only classes (rendering, screens, client state) are never
 * loaded on the server, preventing classloading errors and maintaining strict sided separation.
 *
 * <h2>Example usage</h2>
 *
 * <pre>{@code
 * // Common init — register types and codecs
 * PacketRegistry.registerServerboundPlayPacket(MyC2SPacket.TYPE, MyC2SPacket.CODEC,
 *     (player, packet) -> { ... }); // handle on server
 *
 * PacketRegistry.registerClientboundPlayPacket(MyS2CPacket.TYPE, MyS2CPacket.CODEC);
 *
 * // Client-only init — attach handler
 * PacketRegistry.registerClientboundPacketHandler(MyS2CPacket.TYPE,
 *     (minecraft, player, packet) -> { ... }); // handle on client
 *
 * // Sending
 * PacketDistributor.sendServerboundPacket(new MyC2SPacket(...));               // client -> server
 * PacketDistributor.sendClientboundPacket(serverPlayer, new MyS2CPacket(...)); // server -> client
 * }</pre>
 */
@NullMarked
package com.macuguita.lib.api.network;

import org.jspecify.annotations.NullMarked;
