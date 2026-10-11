package br.com.roubeumovo.gameplay;

import br.com.roubeumovo.registry.ModBlocks;
import br.com.roubeumovo.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/**
 * Roubar um ovo enquanto agacha provoca a galinha escolhida, que persegue
 * o ladrão por até 15 segundos. A perseguição é controlada no servidor.
 */
@Mod.EventBusSubscriber(modid = "roubeumovo", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EggStealEvents {
    private static final String COOLDOWN_KEY = "roubeumovo_egg_steal_cooldown";
    private static final String GUARDIAN_TARGET_KEY = "roubeumovo_guardian_target";
    private static final String GUARDIAN_UNTIL_KEY = "roubeumovo_guardian_until";
    private static final long STEAL_COOLDOWN_TICKS = 100L;
    private static final long CHASE_DURATION_TICKS = 300L;
    private static final double CHASE_DISTANCE = 24.0D;
    private static final String PET_KEY = "roubeumovo_pet";
    private static final String PET_OWNER_KEY = "roubeumovo_pet_owner";
    private static final String PET_PARKED_KEY = "roubeumovo_pet_parked";
    private static final String PET_LAST_INCOME_KEY = "roubeumovo_pet_last_income";
    private static final String MONEY_KEY = "roubeumovo_money";
    private static final long PET_INCOME_INTERVAL_TICKS = 1200L;

    private EggStealEvents() {}

    @SubscribeEvent
    public static void onChickenInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Chicken chicken) || !event.getEntity().isShiftKeyDown()) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        long now = player.level().getGameTime();
        long nextAllowed = player.getPersistentData().getLong(COOLDOWN_KEY);
        if (now < nextAllowed) {
            long seconds = Math.max(1, (nextAllowed - now + 19) / 20);
            player.displayClientMessage(
                    Component.literal("Espere " + seconds + "s antes de tentar roubar outro ovo.")
                            .withStyle(ChatFormatting.YELLOW), true);
            event.setCanceled(true);
            return;
        }

        ItemStack egg = new ItemStack(ModItems.OVO_COMUM.get());
        if (!player.getInventory().add(egg)) {
            player.drop(egg, false);
        }

        chicken.getPersistentData().putUUID(GUARDIAN_TARGET_KEY, player.getUUID());
        chicken.getPersistentData().putLong(
                GUARDIAN_UNTIL_KEY, chicken.level().getGameTime() + CHASE_DURATION_TICKS);
        player.getPersistentData().putLong(COOLDOWN_KEY, now + STEAL_COOLDOWN_TICKS);

        player.displayClientMessage(
                Component.literal("Você roubou um Ovo Comum! A galinha guardiã está atrás de você — corra!")
                        .withStyle(ChatFormatting.GOLD), true);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onGuardianTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Chicken chicken)
                || !(chicken.level() instanceof ServerLevel level)) {
            return;
        }

        var data = chicken.getPersistentData();
        if (!data.hasUUID(GUARDIAN_TARGET_KEY)) {
            return;
        }

        long now = level.getGameTime();
        if (now >= data.getLong(GUARDIAN_UNTIL_KEY)) {
            stopChasing(chicken);
            return;
        }

        UUID targetId = data.getUUID(GUARDIAN_TARGET_KEY);
        Player target = level.getPlayerByUUID(targetId);
        if (target == null || !target.isAlive()
                || chicken.distanceToSqr(target) > CHASE_DISTANCE * CHASE_DISTANCE) {
            stopChasing(chicken);
            return;
        }

        chicken.getLookControl().setLookAt(target, 30.0F, 30.0F);
        chicken.getNavigation().moveTo(target, 1.35D);
    }


    @SubscribeEvent
    public static void onPetFollow(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Chicken pet)
                || !(pet.level() instanceof ServerLevel level)) {
            return;
        }

        var data = pet.getPersistentData();
        if (!data.getBoolean(PET_KEY) || !data.hasUUID(PET_OWNER_KEY)) {
            return;
        }

        // Atualiza a navegação a cada meio segundo para evitar recalcular a rota em todo tick.
        if (level.getGameTime() % 10L != 0L) {
            return;
        }

        BlockPos petPos = pet.blockPosition();
        boolean nearBase = BlockPos.betweenClosedStream(
                petPos.offset(-3, -2, -3), petPos.offset(3, 2, 3))
                .anyMatch(pos -> level.getBlockState(pos).is(ModBlocks.BASE_DOURADA.get()));
        if (nearBase) {
            pet.getNavigation().stop();
            return;
        }

        Player owner = level.getPlayerByUUID(data.getUUID(PET_OWNER_KEY));
        if (owner == null || !owner.isAlive()) {
            pet.getNavigation().stop();
            return;
        }

        double distance = pet.distanceToSqr(owner);
        if (distance > 4.0D * 4.0D) {
            pet.getLookControl().setLookAt(owner, 30.0F, 30.0F);
            pet.getNavigation().moveTo(owner, 1.1D);
        } else if (distance < 2.0D * 2.0D) {
            pet.getNavigation().stop();
        }
    }

    @SubscribeEvent
    public static void onPetIncome(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Chicken pet)
                || !(pet.level() instanceof ServerLevel level)) {
            return;
        }
        var data = pet.getPersistentData();
        if (!data.getBoolean(PET_KEY) || !data.hasUUID(PET_OWNER_KEY)) {
            return;
        }

        long now = level.getGameTime();
        long lastIncome = data.getLong(PET_LAST_INCOME_KEY);
        if (now - lastIncome < PET_INCOME_INTERVAL_TICKS) {
            return;
        }

        Player owner = level.getPlayerByUUID(data.getUUID(PET_OWNER_KEY));
        if (owner == null || !owner.isAlive()) {
            return;
        }

        long minutes = Math.max(1L, (now - lastIncome) / PET_INCOME_INTERVAL_TICKS);
        long earned = Math.min(minutes, 1000L);
        long balance = owner.getPersistentData().getLong(MONEY_KEY) + earned;
        owner.getPersistentData().putLong(MONEY_KEY, balance);
        data.putLong(PET_LAST_INCOME_KEY, lastIncome + earned * PET_INCOME_INTERVAL_TICKS);
        owner.displayClientMessage(Component.literal("+" + earned + " moeda(s) | Saldo: " + balance)
                .withStyle(ChatFormatting.GOLD), true);
    }

    private static void stopChasing(Chicken chicken) {
        chicken.getPersistentData().remove(GUARDIAN_TARGET_KEY);
        chicken.getPersistentData().remove(GUARDIAN_UNTIL_KEY);
        chicken.getNavigation().stop();
    }
}
