package io.github.wouink.furnish;

import io.github.wouink.furnish.client.gui.ConditionalSlotContainerScreen;
import io.github.wouink.furnish.client.gui.FurnitureWorkbenchScreen;
import io.github.wouink.furnish.client.renderer.SeatRenderer;
import io.github.wouink.furnish.network.ClientMessageHandler;
import io.github.wouink.furnish.network.ItemStackUpdateMessage;
import io.github.wouink.furnish.network.ServerMessageHandler;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

@Mod(Furnish.MODID)
@Mod.EventBusSubscriber(modid = Furnish.MODID)
public class Furnish {
	public static final String MODID = "furnish";

	public static SimpleChannel networkChannel;
	public static final String MESSAGE_PROTOCOL_VERSION = "1.0";
	public static final ResourceLocation CHANNEL_LOC = new ResourceLocation(MODID, "net");

	public static final FurnishConfig Furnish_Config = new FurnishConfig();

	public Furnish() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Furnish_Config.getSpec());
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		FurnishManager.init();
		FurnishManager.ModBlocks.Blocks.register(bus);
		FurnishManager.Containers.Container_Types.register(bus);
		FurnishManager.Furnish_Logger.info("Registered Furnish Containers.");
		FurnishManager.Serializer.Recipe_Serializers.register(bus);
		FurnishManager.Furnish_Logger.info("Registered Furnish Recipes Serializers.");
		FurnishManager.Entities.Furnish_Entities.register(bus);
		FurnishManager.Furnish_Logger.info("Registered Furnish Entities.");
		FurnishManager.TileEntities.Furnish_Tile_Entities.register(bus);
		FurnishManager.Furnish_Logger.info("Registered Furnish Tile Entities.");
		FurnishManager.Sounds.Furnish_Sounds.register(bus);
		FurnishManager.Furnish_Logger.info("Registered Furnish Sounds.");

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
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

	private void clientSetup(FMLClientSetupEvent event) {
		ScreenManager.register(FurnishManager.Containers.Furniture_Workbench.get(), FurnitureWorkbenchScreen::new);
		ScreenManager.register(FurnishManager.Containers.Crate.get(), ConditionalSlotContainerScreen::new);
		ScreenManager.register(FurnishManager.Containers.Mailbox.get(), ConditionalSlotContainerScreen::new);
		ScreenManager.register(FurnishManager.Containers.Cooking_Pot.get(), ConditionalSlotContainerScreen::new);
		FurnishManager.Furnish_Logger.info("Registered Furnish Screens.");
		RenderingRegistry.registerEntityRenderingHandler(FurnishManager.Entities.Seat_Entity.get(), SeatRenderer::new);
		FurnishManager.Furnish_Logger.info("Registered Furnish Entity Renderers.");
		FurnishManager.registerTransparency();
	}
}
