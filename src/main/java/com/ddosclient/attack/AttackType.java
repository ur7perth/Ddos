package com.ddosclient.attack;

public enum AttackType {
    PACKET_FLOOD("Packet Flood", "Floods server with generic packets"),
    CHAT_FLOOD("Chat Flood", "Mass chat message spam"),
    MOVEMENT_FLOOD("Movement Flood", "Rapid position packet spam"),
    ENTITY_PACKET_FLOOD("Entity Packet Flood", "Entity interaction packet flood"),
    KEEP_ALIVE_BYPASS("KeepAlive Bypass", "Drops keepalive responses to confuse server"),
    INVALID_POSITION("Invalid Position Flood", "Sends out-of-bounds position packets"),
    SIGNED_CHAT_BYPASS("Signed Chat Bypass", "Bypasses chat signing with malformed packets"),
    TAB_LIST_FLOOD("TabList Flood", "Floods player list update packets");

    public final String displayName;
    public final String description;

    AttackType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}
