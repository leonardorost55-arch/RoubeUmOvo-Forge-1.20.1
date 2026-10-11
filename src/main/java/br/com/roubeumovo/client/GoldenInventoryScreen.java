package br.com.roubeumovo.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;

/**
 * Mantém os slots reais do inventário vanilla para que mover itens,
 * equipar armaduras e usar a bancada continuem funcionando normalmente.
 * A moldura dourada e os indicadores dão a identidade visual do mod.
 */
public final class GoldenInventoryScreen extends InventoryScreen {
    private static final int GOLD = 0xFFFFC83D;
    private static final int DARK_GOLD = 0xFF9B6418;
    private static final int PANEL = 0xD91B1510;

    public GoldenInventoryScreen(Player player) {
        super(player);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTick, mouseX, mouseY);

        int left = this.leftPos;
        int top = this.topPos;
        int right = left + this.imageWidth;
        int bottom = top + this.imageHeight;

        // Moldura dupla dourada em estilo pixel-art.
        graphics.fill(left - 3, top - 3, right + 3, top, DARK_GOLD);
        graphics.fill(left - 3, bottom, right + 3, bottom + 3, DARK_GOLD);
        graphics.fill(left - 3, top, left, bottom, DARK_GOLD);
        graphics.fill(right, top, right + 3, bottom, DARK_GOLD);

        graphics.fill(left - 1, top - 1, right + 1, top + 1, GOLD);
        graphics.fill(left - 1, bottom - 1, right + 1, bottom + 1, GOLD);
        graphics.fill(left - 1, top, left + 1, bottom, GOLD);
        graphics.fill(right - 1, top, right + 1, bottom, GOLD);

        // Cabeçalho compacto sem cobrir os slots do inventário.
        graphics.fill(left + 5, top - 14, right - 5, top - 4, PANEL);
        graphics.drawString(this.font, Component.literal("✦ ROUBE UM OVO ✦"),
                left + 8, top - 12, GOLD, false);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        graphics.drawString(this.font, Component.literal("Aventureiro"),
                8, 6, GOLD, false);
    }
}
