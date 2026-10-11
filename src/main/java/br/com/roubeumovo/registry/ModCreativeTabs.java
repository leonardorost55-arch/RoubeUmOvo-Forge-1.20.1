package br.com.roubeumovo.registry;

import br.com.roubeumovo.RoubeUmOvoMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(ForgeRegistries.CREATIVE_MODE_TABS, RoubeUmOvoMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.roubeumovo.main"))
                    .icon(() -> new ItemStack(ModItems.OVO_LENDARIO.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.OVO_COMUM.get());
                        output.accept(ModItems.OVO_RARO.get());
                        output.accept(ModItems.OVO_EPICO.get());
                        output.accept(ModItems.OVO_LENDARIO.get());
                        output.accept(ModItems.OVO_MITICO.get());
                        output.accept(ModItems.OVO_SECRETO.get());
                    })
                    .build());

    private ModCreativeTabs() {}
}
