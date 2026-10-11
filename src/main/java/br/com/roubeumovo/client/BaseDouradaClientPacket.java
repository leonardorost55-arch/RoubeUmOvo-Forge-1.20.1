package br.com.roubeumovo.client;

import br.com.roubeumovo.network.BaseDouradaNetwork;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/** Ponto de entrada do pacote de dados no lado cliente. */
@OnlyIn(Dist.CLIENT)
public final class BaseDouradaClientPacket {
    private BaseDouradaClientPacket() {}

    public static void handle(List<BaseDouradaNetwork.PetSlot> slots, int nearbyPets) {
        if (Minecraft.getInstance().screen instanceof BaseDouradaScreen screen) {
            screen.setSnapshot(slots, nearbyPets);
        }
    }
}
