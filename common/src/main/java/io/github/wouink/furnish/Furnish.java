package io.github.wouink.furnish;

import io.github.wouink.furnish.setup.FurnishClient;
import io.github.wouink.furnish.setup.FurnishConfig;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Furnish {
	public static final String MODID = "furnish";
	public static final Logger LOG = LogManager.getLogger("Furnish");
	public static final FurnishConfig CONFIG = new FurnishConfig();

	//TODO public static SimpleChannel networkChannel;
	public static final String MESSAGE_PROTOCOL_VERSION = "1.0";
	public static final ResourceLocation CHANNEL_LOC = new ResourceLocation(MODID, "net");

	public static void init() {
		FurnishRegistries.BLOCKS.register();
		FurnishRegistries.ITEMS.register();
		FurnishRegistries.RECIPE_TYPES.register();
		FurnishRegistries.RECIPE_SERIALIZERS.register();
		FurnishRegistries.SOUND_EVENTS.register();
		FurnishRegistries.CONTAINERS.register();
		FurnishRegistries.ENTITIES.register();
		FurnishRegistries.BLOCK_ENTITIES.register();
		FurnishRegistries.PAINTING_VARIANTS.register();
	}

	public Furnish() {
		//ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG.getSpec());
		//IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
/*TODO
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
 */
	}

	/*TODO
	private void setup(final FMLCommonSetupEvent event) {
		networkChannel = NetworkRegistry.newSimpleChannel(CHANNEL_LOC, () -> MESSAGE_PROTOCOL_VERSION,
				ClientMessageHandler::acceptsProtocol, ServerMessageHandler::acceptsProtocol
		);
		networkChannel.registerMessage(ItemStackUpdateMessage.MESSAGE_ID, ItemStackUpdateMessage.class,
				ItemStackUpdateMessage::encode, ItemStackUpdateMessage::decode,
				ServerMessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER)
		);
	}
	 */

	public static void initClient() {
		FurnishClient.registerBlockRenderTypes();
		FurnishClient.bindScreensToContainers();
		FurnishClient.registerEntityRenderers();
	}

	public static void debug(String msg) {
		// Toggle comment on the following line to enable/disable debug messages
		//System.out.println(msg);
	}
}
