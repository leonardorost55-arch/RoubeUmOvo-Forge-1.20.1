package br.com.roubeumovo.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EggItem extends Item {
    private final String rarity;
    private final ChatFormatting color;

    public EggItem(Properties properties, String rarity, ChatFormatting color) {
        super(properties);
        this.rarity = rarity;
        this.color = color;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide || !(player instanceof ServerPlayer serverPlayer)
                || !(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        Chicken pet = new Chicken(EntityType.CHICKEN, serverLevel);
        Vec3 look = player.getLookAngle();
        pet.moveTo(player.getX() + look.x * 1.5D, player.getY(), player.getZ() + look.z * 1.5D,
                player.getYRot(), 0.0F);
        pet.setCustomName(Component.literal("Pet de Ovo " + rarity).withStyle(color));
        pet.setCustomNameVisible(true);
        pet.getPersistentData().putBoolean("roubeumovo_pet", true);
        pet.getPersistentData().putUUID("roubeumovo_pet_owner", serverPlayer.getUUID());
        pet.getPersistentData().putString("roubeumovo_pet_rarity", rarity);
        pet.getPersistentData().putLong("roubeumovo_pet_last_income", serverLevel.getGameTime());

        if (!serverLevel.addFreshEntity(pet)) {
            serverPlayer.displayClientMessage(Component.literal("Não foi possível chocar o ovo aqui.")
                    .withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }

        if (!serverPlayer.getAbilities().instabuild) {
            stack.shrink(1);
        }
        serverPlayer.displayClientMessage(Component.literal("Ovo chocado! Seu pet gera 1 moeda a cada minuto.")
                .withStyle(ChatFormatting.GOLD), true);
        return InteractionResultHolder.sidedSuccess(stack, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Raridade: " + rarity).withStyle(color));
        tooltip.add(Component.literal("Use para chocar e obter um pet.")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Cada pet gera 1 moeda por minuto.")
                .withStyle(ChatFormatting.GOLD));
    }
}
