package br.com.roubeumovo.registry;

import br.com.roubeumovo.RoubeUmOvoMod;
import br.com.roubeumovo.item.EggItem;
import br.com.roubeumovo.registry.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RoubeUmOvoMod.MOD_ID);

    public static final RegistryObject<Item> BASE_DOURADA = ITEMS.register("base_dourada",
            () -> new BlockItem(ModBlocks.BASE_DOURADA.get(), new Item.Properties()));

    public static final RegistryObject<Item> OVO_COMUM = ITEMS.register("ovo_comum",
            () -> new EggItem(new Item.Properties(), "Comum", ChatFormatting.WHITE));
    public static final RegistryObject<Item> OVO_RARO = ITEMS.register("ovo_raro",
            () -> new EggItem(new Item.Properties(), "Raro", ChatFormatting.BLUE));
    public static final RegistryObject<Item> OVO_EPICO = ITEMS.register("ovo_epico",
            () -> new EggItem(new Item.Properties(), "Épico", ChatFormatting.DARK_PURPLE));
    public static final RegistryObject<Item> OVO_LENDARIO = ITEMS.register("ovo_lendario",
            () -> new EggItem(new Item.Properties(), "Lendário", ChatFormatting.GOLD));
    public static final RegistryObject<Item> OVO_MITICO = ITEMS.register("ovo_mitico",
            () -> new EggItem(new Item.Properties(), "Mítico", ChatFormatting.LIGHT_PURPLE));
    public static final RegistryObject<Item> OVO_SECRETO = ITEMS.register("ovo_secreto",
            () -> new EggItem(new Item.Properties().fireResistant(), "Secreto", ChatFormatting.AQUA));

    private ModItems() {}
}
