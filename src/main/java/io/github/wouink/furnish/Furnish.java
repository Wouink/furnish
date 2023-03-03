package io.github.wouink.furnish;

import io.github.wouink.furnish.block.FlowerPot;
import io.github.wouink.furnish.event.*;
import io.github.wouink.furnish.network.ClientMessageHandler;
import io.github.wouink.furnish.network.ItemStackUpdateMessage;
import io.github.wouink.furnish.network.ServerMessageHandler;
import io.github.wouink.furnish.setup.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Mod(Furnish.MODID)
@Mod.EventBusSubscriber(modid = Furnish.MODID)
public class Furnish {
	public static final String MODID = "furnish";
	public static final Logger LOG = LogManager.getLogger("Furnish");
	public static final FurnishConfig CONFIG = new FurnishConfig();

	public static SimpleChannel networkChannel;
	public static final String MESSAGE_PROTOCOL_VERSION = "1.0";
	public static final ResourceLocation CHANNEL_LOC = new ResourceLocation(MODID, "net");

	public Furnish() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG.getSpec());
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		FurnishBlocks.Registry.register(bus);
		FurnishItems.Registry.register(bus);
		FurnishData.setup(bus);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

		MinecraftForge.EVENT_BUS.register(PlaceCarpet.class);
		MinecraftForge.EVENT_BUS.register(AddArmsToArmorStand.class);
		MinecraftForge.EVENT_BUS.register(CyclePainting.class);
		MinecraftForge.EVENT_BUS.register(KnockOnDoor.class);
		MinecraftForge.EVENT_BUS.register(GivePlateToEnderman.class);
		//MinecraftForge.EVENT_BUS.register(AddGoalsToPets.class);
		MinecraftForge.EVENT_BUS.register(SleepInCoffin.class);
		MinecraftForge.EVENT_BUS.register(PlaceSnow.class);
		//MinecraftForge.EVENT_BUS.register(PopLecternBook.class);

		// Now, right-clicks on blocks have to be registered as events
		MinecraftForge.EVENT_BUS.register(FlowerPot.class);
	}

	private void setup(final FMLCommonSetupEvent event) {
		networkChannel = NetworkRegistry.newSimpleChannel(CHANNEL_LOC, () -> MESSAGE_PROTOCOL_VERSION,
				ClientMessageHandler::acceptsProtocol, ServerMessageHandler::acceptsProtocol
		);
		networkChannel.registerMessage(ItemStackUpdateMessage.MESSAGE_ID, ItemStackUpdateMessage.class,
				ItemStackUpdateMessage::encode, ItemStackUpdateMessage::decode,
				ServerMessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER)
		);
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		FurnishClient.clientSetup();
		FurnishData.clientSetup();
	}

	public static void debug(String msg) {
		// Toggle comment on the following line to enable/disable debug messages
		//System.out.println(msg);
	}
}
