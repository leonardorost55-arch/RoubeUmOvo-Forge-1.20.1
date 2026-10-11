package br.com.roubeumovo.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

/** Comunicação entre a Base Dourada no servidor e seu painel no cliente. */
public final class BaseDouradaNetwork {
    private static final String PROTOCOL = "1";
    private static final String PET_KEY = "roubeumovo_pet";
    private static final String OWNER_KEY = "roubeumovo_pet_owner";
    private static final String PARKED_KEY = "roubeumovo_pet_parked";
    private static final String RARITY_KEY = "roubeumovo_pet_rarity";
    private static final double RANGE = 8.0D;

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("roubeumovo", "base_dourada"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    private BaseDouradaNetwork() {}

    public static void register() {
        CHANNEL.registerMessage(0, RequestSnapshot.class,
                RequestSnapshot::encode, RequestSnapshot::decode, RequestSnapshot::handle);
        CHANNEL.registerMessage(1, BaseSnapshot.class,
                BaseSnapshot::encode, BaseSnapshot::decode, BaseSnapshot::handle);
    }

    public static void requestSnapshot(BlockPos pos) {
        CHANNEL.sendToServer(new RequestSnapshot(pos));
    }

    public static final class RequestSnapshot {
        private final BlockPos pos;

        public RequestSnapshot(BlockPos pos) {
            this.pos = pos.immutable();
        }

        private static void encode(RequestSnapshot message, FriendlyByteBuf buffer) {
            buffer.writeBlockPos(message.pos);
        }

        private static RequestSnapshot decode(FriendlyByteBuf buffer) {
            return new RequestSnapshot(buffer.readBlockPos());
        }

        private static void handle(RequestSnapshot message, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null || !(player.level() instanceof ServerLevel level)
                        || !level.getBlockState(message.pos).is(
                        br.com.roubeumovo.registry.ModBlocks.BASE_DOURADA.get())) {
                    return;
                }

                List<Chicken> pets = level.getEntitiesOfClass(Chicken.class,
                        new AABB(message.pos).inflate(RANGE),
                        chicken -> chicken.getPersistentData().getBoolean(PET_KEY)
                                && chicken.getPersistentData().hasUUID(OWNER_KEY)
                                && player.getUUID().equals(chicken.getPersistentData().getUUID(OWNER_KEY)))
                        .stream()
                        .sorted(Comparator.comparingDouble(chicken -> chicken.distanceToSqr(
                                message.pos.getX() + 0.5D, message.pos.getY() + 0.5D,
                                message.pos.getZ() + 0.5D)))
                        .toList();

                List<PetSlot> slots = new ArrayList<>();
                for (Chicken pet : pets.stream().limit(4).toList()) {
                    String rarity = pet.getPersistentData().getString(RARITY_KEY);
                    if (rarity.isBlank()) rarity = "Comum";
                    slots.add(new PetSlot(rarity,
                            pet.getPersistentData().getBoolean(PARKED_KEY)));
                }
                CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                        new BaseSnapshot(slots, pets.size()));
            });
            context.setPacketHandled(true);
        }
    }

    public record PetSlot(String rarity, boolean parked) {}

    public static final class BaseSnapshot {
        private final List<PetSlot> slots;
        private final int nearbyPets;

        public BaseSnapshot(List<PetSlot> slots, int nearbyPets) {
            this.slots = List.copyOf(slots);
            this.nearbyPets = nearbyPets;
        }

        private static void encode(BaseSnapshot message, FriendlyByteBuf buffer) {
            buffer.writeVarInt(message.slots.size());
            for (PetSlot slot : message.slots) {
                buffer.writeUtf(slot.rarity(), 32);
                buffer.writeBoolean(slot.parked());
            }
            buffer.writeVarInt(message.nearbyPets);
        }

        private static BaseSnapshot decode(FriendlyByteBuf buffer) {
            int count = Math.min(4, buffer.readVarInt());
            List<PetSlot> slots = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                slots.add(new PetSlot(buffer.readUtf(32), buffer.readBoolean()));
            }
            return new BaseSnapshot(slots, buffer.readVarInt());
        }

        private static void handle(BaseSnapshot message, Supplier<NetworkEvent.Context> supplier) {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> br.com.roubeumovo.client.BaseDouradaClientPacket.handle(
                            message.slots, message.nearbyPets)));
            context.setPacketHandled(true);
        }
    }
}
