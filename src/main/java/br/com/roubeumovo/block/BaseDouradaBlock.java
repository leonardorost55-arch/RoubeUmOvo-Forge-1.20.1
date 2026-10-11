package br.com.roubeumovo.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class BaseDouradaBlock extends Block {
    private static final String PET_KEY = "roubeumovo_pet";
    private static final String PET_OWNER_KEY = "roubeumovo_pet_owner";
    private static final String PET_PARKED_KEY = "roubeumovo_pet_parked";
    private static final int SLOT_COUNT = 4;
    private static final double RANGE = 8.0D;

    public BaseDouradaBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.PASS;

        UUID ownerId = player.getUUID();
        List<Chicken> pets = serverLevel.getEntitiesOfClass(Chicken.class,
                new AABB(pos).inflate(RANGE),
                chicken -> chicken.getPersistentData().getBoolean(PET_KEY)
                        && chicken.getPersistentData().hasUUID(PET_OWNER_KEY)
                        && ownerId.equals(chicken.getPersistentData().getUUID(PET_OWNER_KEY)));

        List<Chicken> parked = pets.stream()
                .filter(chicken -> chicken.getPersistentData().getBoolean(PET_PARKED_KEY))
                .toList();

        if (!parked.isEmpty()) {
            for (Chicken pet : parked) {
                pet.getPersistentData().putBoolean(PET_PARKED_KEY, false);
                pet.getNavigation().stop();
            }
            player.displayClientMessage(Component.literal("✦ BASE DOURADA ✦  "
                    + parked.size() + " pet(s) liberado(s)!").withStyle(ChatFormatting.GREEN), true);
            return InteractionResult.CONSUME;
        }

        List<Chicken> available = pets.stream()
                .filter(chicken -> !chicken.getPersistentData().getBoolean(PET_PARKED_KEY))
                .sorted(Comparator.comparingDouble(chicken -> chicken.distanceToSqr(pos.getX() + 0.5D,
                        pos.getY() + 0.5D, pos.getZ() + 0.5D)))
                .limit(SLOT_COUNT)
                .toList();

        for (Chicken pet : available) {
            pet.getPersistentData().putBoolean(PET_PARKED_KEY, true);
            pet.getNavigation().stop();
        }

        int occupied = available.size();
        int total = pets.size();
        player.displayClientMessage(Component.literal("✦ BASE DOURADA ✦  Slots: "
                + occupied + "/" + SLOT_COUNT + " | Pets próximos: " + total
                + " | Clique novamente para liberar").withStyle(ChatFormatting.GOLD), true);
        return InteractionResult.CONSUME;
    }
}
