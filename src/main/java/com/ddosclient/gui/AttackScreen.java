package com.ddosclient.gui;

import com.ddosclient.DdosClient;
import com.ddosclient.attack.AttackType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class AttackScreen extends Screen {

    private AttackType selectedAttack = AttackType.PACKET_FLOOD;
    private TextFieldWidget countField;
    private int scrollOffset = 0;

    public AttackScreen() {
        super(Text.literal("DDoS Client — Attack Panel"));
    }

    @Override
    protected void init() {
        int panelX = this.width / 2 - 160;
        int panelY = 20;
        int btnW = 220;
        int btnH = 18;
        int gap = 22;

        AttackType[] types = AttackType.values();
        for (int i = 0; i < types.length; i++) {
            final AttackType type = types[i];
            int y = panelY + i * gap;
            ButtonWidget btn = ButtonWidget.builder(
                Text.literal(type.displayName + (selectedAttack == type ? " ✓" : "")),
                b -> {
                    selectedAttack = type;
                    this.clearAndInit();
                }
            ).dimensions(panelX, y, btnW, btnH).build();
            this.addDrawableChild(btn);
        }

        int rightX = this.width / 2 + 10;

        // Description label handled in render()
        // Count field
        countField = new TextFieldWidget(
            this.textRenderer,
            rightX,
            panelY + 60,
            160, 20,
            Text.literal("Packet Count")
        );
        countField.setPlaceholder(Text.literal("e.g. 1000"));
        countField.setMaxLength(10);
        this.addDrawableChild(countField);

        // Done (start) button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("[ DONE — START ATTACK ]"),
            b -> {
                try {
                    int count = Integer.parseInt(countField.getText().trim());
                    if (count > 0) {
                        DdosClient.attackManager.startAttack(selectedAttack, count);
                        this.close();
                        if (this.client != null && this.client.player != null) {
                            this.client.player.sendMessage(
                                Text.literal("[DDoS] Started: " + selectedAttack.displayName +
                                    " x" + count), false
                            );
                        }
                    }
                } catch (NumberFormatException ex) {
                    // ignore invalid input
                }
            }
        ).dimensions(rightX, panelY + 90, 160, 20).build());

        // Stop button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("[ STOP ATTACK ]"),
            b -> {
                DdosClient.attackManager.stopAttack();
                if (this.client != null && this.client.player != null) {
                    this.client.player.sendMessage(Text.literal("[DDoS] Attack stopped."), false);
                }
            }
        ).dimensions(rightX, panelY + 115, 160, 20).build());

        // Close button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Close"),
            b -> this.close()
        ).dimensions(rightX, panelY + 145, 160, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);

        int rightX = this.width / 2 + 10;
        int panelY = 20;

        context.drawText(this.textRenderer, "Attack Panel", this.width / 2 - 160, 8, 0xFFFF55, true);
        context.drawText(this.textRenderer, "Selected: " + selectedAttack.displayName, rightX, panelY, 0x55FFFF, true);
        context.drawText(this.textRenderer, selectedAttack.description, rightX, panelY + 12, 0xAAAAAA, false);
        context.drawText(this.textRenderer, "Packet Count:", rightX, panelY + 48, 0xFFFFFF, false);

        if (DdosClient.attackManager.isRunning()) {
            String status = "[RUNNING] " + DdosClient.attackManager.getCurrentType().displayName +
                " | " + DdosClient.attackManager.getSentCount() +
                "/" + DdosClient.attackManager.getPacketCount();
            context.drawText(this.textRenderer, status, rightX, panelY + 175, 0x55FF55, false);
        } else {
            context.drawText(this.textRenderer, "[IDLE]", rightX, panelY + 175, 0xFF5555, false);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
