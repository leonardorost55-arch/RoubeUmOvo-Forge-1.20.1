package br.com.roubeumovo.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import br.com.roubeumovo.registry.ModBlocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = "roubeumovo",
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public final class ClientScreenEvents {
    private ClientScreenEvents() {}

    @SubscribeEvent
    public static void onGoldenBaseInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().getBlockState(event.getPos()).is(ModBlocks.BASE_DOURADA.get())) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null && minecraft.screen == null) {
                minecraft.setScreen(new BaseDouradaScreen());
            }
        }
    }

    @SubscribeEvent
    public static void onScreenOpening(ScreenEvent.Opening event) {
        // Verifica a classe exata para não reabrir nossa própria tela.
        if (event.getScreen() != null
                && event.getScreen().getClass() == InventoryScreen.class) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                event.setNewScreen(new GoldenInventoryScreen(minecraft.player));
            }
        }
    }
}
