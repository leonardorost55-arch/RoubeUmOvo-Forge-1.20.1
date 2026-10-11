package br.com.roubeumovo;

import br.com.roubeumovo.registry.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RoubeUmOvoMod.MOD_ID)
public final class RoubeUmOvoMod {
    public static final String MOD_ID = "roubeumovo";

    public RoubeUmOvoMod() {
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
