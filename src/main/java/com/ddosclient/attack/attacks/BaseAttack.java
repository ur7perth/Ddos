package com.ddosclient.attack.attacks;

import net.minecraft.client.MinecraftClient;

public abstract class BaseAttack {
    public abstract void execute(MinecraftClient mc) throws Exception;
}
