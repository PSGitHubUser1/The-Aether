package com.aetherteam.aether.network.packet.client;

import com.aetherteam.aether.event.hooks.AbilityHooks;
import com.aetherteam.aether.network.AetherPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record ToolDebuffPacket(boolean debuffTools) implements AetherPacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.debuffTools());
    }

    public static ToolDebuffPacket decode(FriendlyByteBuf buf) {
        boolean debuffTools = buf.readBoolean();
        return new ToolDebuffPacket(debuffTools);
    }

    @Override
    public void execute(Player playerEntity) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            AbilityHooks.ToolHooks.debuffTools = this.debuffTools();
        }
    }
}
