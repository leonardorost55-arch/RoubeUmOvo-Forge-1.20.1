package br.com.roubeumovo.block;

import br.com.roubeumovo.registry.ModItems;
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

import java.util.UUID;

public class BaseDouradaBlock extends Block {
    private static final String PET_KEY = "roubeumovo_pet";
    private static final String PET_OWNER_KEY = "roubeumovo_pet_owner";

    public BaseDouradaBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }

        UUID ownerId = player.getUUID();
        int pets = serverLevel.getEntitiesOfClass(Chicken.class,
                new AABB(pos).inflate(8.0D),
                chicken -> chicken.getPersistentData().getBoolean(PET_KEY)
                        && chicken.getPersistentData().hasUUID(PET_OWNER_KEY)
                        && ownerId.equals(chicken.getPersistentData().getUUID(PET_OWNER_KEY)))
                .size();

        player.displayClientMessage(Component.literal("✦ BASE DOURADA ✦  Pets por perto: "
                + pets + " | Alcance: 8 blocos")
                .withStyle(ChatFormatting.GOLD), true);
        return InteractionResult.CONSUME;
    }
}
