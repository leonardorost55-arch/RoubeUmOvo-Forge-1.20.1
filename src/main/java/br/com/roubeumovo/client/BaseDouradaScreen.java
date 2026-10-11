package br.com.roubeumovo.client;

import br.com.roubeumovo.network.BaseDouradaNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.List;

/** Painel da Base Dourada com dados dos pets recebidos do servidor. */
public final class BaseDouradaScreen extends Screen {
    private static final int GOLD = 0xFFFFD45A;
    private static final int DARK_GOLD = 0xFF8A5A16;
    private static final int PANEL = 0xF0211910;
    private static final int SLOT = 0xFF30251A;

    private final BlockPos basePos;
    private List<BaseDouradaNetwork.PetSlot> slots = List.of();
    private int nearbyPets;
    private int ticksUntilRefresh;

    public BaseDouradaScreen(BlockPos basePos) {
        super(Component.literal("Base Dourada"));
        this.basePos = basePos.immutable();
    }

    @Override
    protected void init() {
        super.init();
        requestSnapshot();
    }

    @Override
    public void tick() {
        super.tick();
        if (--ticksUntilRefresh <= 0) {
            requestSnapshot();
        }
    }

    private void requestSnapshot() {
        ticksUntilRefresh = 20;
        if (minecraft != null && minecraft.player != null) {
            BaseDouradaNetwork.requestSnapshot(basePos);
        }
    }

    public void setSnapshot(List<BaseDouradaNetwork.PetSlot> slots, int nearbyPets) {
        this.slots = List.copyOf(slots);
        this.nearbyPets = Math.max(0, nearbyPets);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        int panelWidth = 270;
        int panelHeight = 174;
        int left = (this.width - panelWidth) / 2;
        int top = (this.height - panelHeight) / 2;

        graphics.fill(left - 3, top - 3, left + panelWidth + 3, top + panelHeight + 3, DARK_GOLD);
        graphics.fill(left, top, left + panelWidth, top + panelHeight, PANEL);
        graphics.fill(left + 4, top + 4, left + panelWidth - 4, top + 6, GOLD);

        drawCentered(graphics, Component.literal("✦ BASE DOURADA ✦").withStyle(ChatFormatting.GOLD),
                this.width / 2, top + 12, GOLD);
        drawCentered(graphics, Component.literal("ESTACIONAMENTO DE PETS"),
                this.width / 2, top + 27, 0xFFE8DCC8);

        int slotWidth = 52;
        int slotGap = 7;
        int slotsWidth = slotWidth * 4 + slotGap * 3;
        int slotStart = this.width / 2 - slotsWidth / 2;
        int slotTop = top + 44;
        for (int i = 0; i < 4; i++) {
            int x = slotStart + i * (slotWidth + slotGap);
            graphics.fill(x - 2, slotTop - 2, x + slotWidth + 2, slotTop + 57, DARK_GOLD);
            graphics.fill(x, slotTop, x + slotWidth, slotTop + 55, SLOT);
            drawCentered(graphics, Component.literal("SLOT " + (i + 1)),
                    x + slotWidth / 2, slotTop + 5, GOLD);

            if (i < slots.size()) {
                BaseDouradaNetwork.PetSlot pet = slots.get(i);
                drawCentered(graphics, Component.literal(shortRarity(pet.rarity())),
                        x + slotWidth / 2, slotTop + 20, rarityColor(pet.rarity()));
                drawCentered(graphics, Component.literal(pet.parked() ? "GUARDADO" : "ATIVO"),
                        x + slotWidth / 2, slotTop + 35, pet.parked() ? 0xFFFFD45A : 0xFF8FE39A);
                drawCentered(graphics, Component.literal("+1/min"),
                        x + slotWidth / 2, slotTop + 45, 0xFFE8DCC8);
            } else {
                drawCentered(graphics, Component.literal("VAZIO"),
                        x + slotWidth / 2, slotTop + 25, 0xFF9B8C78);
            }
        }

        drawCentered(graphics, Component.literal("Pets seus por perto: " + nearbyPets + "  |  Renda: +1 moeda/min por pet"),
                this.width / 2, top + 108, 0xFFF5E7CF);
        drawCentered(graphics, Component.literal("Clique com o botão direito na base para guardar/liberar"),
                this.width / 2, top + 123, 0xFFF5E7CF);
        drawCentered(graphics, Component.literal("Dados atualizados automaticamente"),
                this.width / 2, top + 139, 0xFFB7A58A);
        drawCentered(graphics, Component.literal("ESC para fechar"),
                this.width / 2, top + 155, 0xFFB7A58A);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private String shortRarity(String rarity) {
        return switch (rarity.toLowerCase(java.util.Locale.ROOT)) {
            case "lendário", "lendario" -> "LENDÁRIO";
            case "secreto" -> "SECRETO";
            case "mítico", "mitico" -> "MÍTICO";
            case "épico", "epico" -> "ÉPICO";
            default -> rarity.toUpperCase(java.util.Locale.ROOT);
        };
    }

    private int rarityColor(String rarity) {
        return switch (rarity.toLowerCase(java.util.Locale.ROOT)) {
            case "raro" -> 0xFF6EABFF;
            case "épico", "epico" -> 0xFFC58BFF;
            case "lendário", "lendario" -> 0xFFFFD45A;
            case "mítico", "mitico" -> 0xFFFF9FEA;
            case "secreto" -> 0xFF70E9F2;
            default -> 0xFFE8DCC8;
        };
    }

    private void drawCentered(GuiGraphics graphics, Component text, int centerX, int y, int color) {
        graphics.drawString(this.font, text, centerX - this.font.width(text) / 2, y, color, false);
    }
}
