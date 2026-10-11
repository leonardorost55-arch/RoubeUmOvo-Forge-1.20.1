package br.com.roubeumovo.gameplay;

import br.com.roubeumovo.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
        ServerPlayer target = level.getPlayerByUUID(targetId);
        if (target == null || !target.isAlive()
                || chicken.distanceToSqr(target) > CHASE_DISTANCE * CHASE_DISTANCE) {
            stopChasing(chicken);
            return;
        }

        chicken.getLookControl().setLookAt(target, 30.0F, 30.0F);
        chicken.getNavigation().moveTo(target, 1.35D);
    }

    private static void stopChasing(Chicken chicken) {
        chicken.getPersistentData().remove(GUARDIAN_TARGET_KEY);
        chicken.getPersistentData().remove(GUARDIAN_UNTIL_KEY);
        chicken.getNavigation().stop();
    }
}
