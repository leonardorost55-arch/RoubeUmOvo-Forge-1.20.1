package br.com.roubeumovo.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Raridade: " + rarity).withStyle(color));
        tooltip.add(Component.literal("Ovo misterioso — novas mecânicas em desenvolvimento.")
                .withStyle(ChatFormatting.GRAY));
    }
}
