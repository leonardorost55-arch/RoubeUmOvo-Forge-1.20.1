package br.com.roubeumovo.gameplay;

import br.com.roubeumovo.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Primeira versão jogável: agachar e clicar com o botão direito numa galinha
 * tenta roubar um ovo. O cooldown fica nos dados persistentes do jogador.
 */
@Mod.EventBusSubscriber(modid = "roubeumovo", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EggStealEvents {
    private static final String COOLDOWN_KEY = "roubeumovo_egg_steal_cooldown";

    private EggStealEvents() {}

    @SubscribeEvent
    public static void onChickenInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Chicken) || !event.getEntity().isShiftKeyDown()) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        long now = player.level().getGameTime();
        long nextAllowed = player.getPersistentData().getLong(COOLDOWN_KEY);
        if (now < nextAllowed) {
            long seconds = Math.max(1, (nextAllowed - now) / 20);
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
        player.getPersistentData().putLong(COOLDOWN_KEY, now + 100);
        player.displayClientMessage(
                Component.literal("Você roubou um Ovo Comum! Fuja antes que a guardiã perceba!")
                        .withStyle(ChatFormatting.GOLD), true);
        event.setCanceled(true);
    }
}
