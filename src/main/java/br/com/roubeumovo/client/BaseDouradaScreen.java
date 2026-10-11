package br.com.roubeumovo.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Painel visual da Base Dourada. A interação com o bloco continua controlando
 * o estacionamento dos pets no servidor.
 */
public final class BaseDouradaScreen extends Screen {
    private static final int GOLD = 0xFFFFD45A;
    private static final int DARK_GOLD = 0xFF8A5A16;
    private static final int PANEL = 0xF0211910;
    private static final int SLOT = 0xFF30251A;

    public BaseDouradaScreen() {
        super(Component.literal("Base Dourada"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        int panelWidth = 224;
        int panelHeight = 148;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;

        graphics.fill(left - 3, top - 3, left + panelWidth + 3, top + panelHeight + 3, DARK_GOLD);
        graphics.fill(left, top, left + panelWidth, top + panelHeight, PANEL);
        graphics.fill(left + 4, top + 4, left + panelWidth - 4, top + 6, GOLD);

        drawCentered(graphics, Component.literal("✦ BASE DOURADA ✦").withStyle(ChatFormatting.GOLD),
                this.width / 2, top + 13, GOLD);
        drawCentered(graphics, Component.literal("ESTACIONAMENTO DE PETS"),
                this.width / 2, top + 29, 0xFFE8DCC8);

        int slotWidth = 42;
        int slotGap = 8;
        int slotsWidth = slotWidth * 4 + slotGap * 3;
        int slotStart = this.width / 2 - slotsWidth / 2;
        int slotTop = top + 48;
        for (int i = 0; i < 4; i++) {
            int x = slotStart + i * (slotWidth + slotGap);
            graphics.fill(x - 2, slotTop - 2, x + slotWidth + 2, slotTop + 43, DARK_GOLD);
            graphics.fill(x, slotTop, x + slotWidth, slotTop + 41, SLOT);
            drawCentered(graphics, Component.literal("SLOT " + (i + 1)),
                    x + slotWidth / 2, slotTop + 6, GOLD);
            drawCentered(graphics, Component.literal("✦"), x + slotWidth / 2,
                    slotTop + 19, 0xFFFFE8A0);
        }

        drawCentered(graphics, Component.literal("Clique com o botão direito na base para guardar"),
                this.width / 2, top + 101, 0xFFF5E7CF);
        drawCentered(graphics, Component.literal("ou liberar os pets próximos."),
                this.width / 2, top + 113, 0xFFF5E7CF);
        drawCentered(graphics, Component.literal("ESC para fechar"),
                this.width / 2, top + 132, 0xFFB7A58A);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawCentered(GuiGraphics graphics, Component text, int centerX, int y, int color) {
        graphics.drawString(this.font, text, centerX - this.font.width(text) / 2, y, color, false);
    }
}
