package br.com.roubeumovo;

import br.com.roubeumovo.registry.ModBlocks;
import br.com.roubeumovo.registry.ModCreativeTabs;
import br.com.roubeumovo.registry.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RoubeUmOvoMod.MOD_ID)
public final class RoubeUmOvoMod {
    public static final String MOD_ID = "roubeumovo";

    public RoubeUmOvoMod() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
    }
}
