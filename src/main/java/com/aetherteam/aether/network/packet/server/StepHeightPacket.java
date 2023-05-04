package com.aetherteam.aether.network.packet.server;

import com.aetherteam.aether.entity.passive.MountableAnimal;
import com.aetherteam.aether.network.AetherPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

public record StepHeightPacket(int entityID) implements AetherPacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID());
    }

    public static StepHeightPacket decode(FriendlyByteBuf buf) {
        int entityID = buf.readInt();
        return new StepHeightPacket(entityID);
    }

    @Override
    public void execute(Player playerEntity) {
        if (playerEntity != null && playerEntity.getServer() != null && playerEntity.level.getEntity(this.entityID()) instanceof MountableAnimal mountableAnimal) {
            AttributeInstance stepHeight = mountableAnimal.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
            if (stepHeight != null) {
                if (stepHeight.hasModifier(mountableAnimal.getDefaultStepHeightModifier())) {
                    stepHeight.removeModifier(mountableAnimal.getDefaultStepHeightModifier());
                }
                if (!stepHeight.hasModifier(mountableAnimal.getMountStepHeightModifier())) {
                    stepHeight.addTransientModifier(mountableAnimal.getMountStepHeightModifier());
                }
            }
        }
    }
}
