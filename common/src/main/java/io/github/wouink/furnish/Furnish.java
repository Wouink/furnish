package io.github.wouink.furnish;

import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.utils.Env;
import io.github.wouink.furnish.event.*;
import io.github.wouink.furnish.network.C2S_UpdateItemStack;
import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishClient;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Furnish {
	public static final String MODID = "furnish";
	public static final Logger LOG = LogManager.getLogger("Furnish");
	public static final SimpleNetworkManager NET = SimpleNetworkManager.create(MODID);
	public static final MessageType CL_UPDATE_ITEMSTACK = NET.registerC2S("cl_update_itemstack", C2S_UpdateItemStack::new);
	public static final CreativeModeTab CREATIVE_TAB = CreativeTabRegistry.create(new ResourceLocation(Furnish.MODID, "furnish"), () -> new ItemStack(FurnishBlocks.Furniture_Workbench.get()));

	public static void init() {
		FurnishBlocks.BLOCKS.register();
		LOG.info("Registered Furnish Blocks.");
		FurnishBlocks.ITEMS.register();
		LOG.info("Registered Furnish Items.");
		FurnishRegistries.RECIPE_TYPES.register();
		LOG.info("Registered Furnish Recipe Types.");
		FurnishRegistries.RECIPE_SERIALIZERS.register();
		LOG.info("Registered Furnish Recipe Serializers.");
		FurnishRegistries.SOUND_EVENTS.register();
		LOG.info("Registered Furnish Sound Events.");
		FurnishRegistries.CONTAINERS.register();
		LOG.info("Registered Furnish Containers.");
		FurnishRegistries.ENTITIES.register();
		LOG.info("Registered Furnish Entities.");
		FurnishRegistries.BLOCK_ENTITIES.register();
		LOG.info("Registered Furnish Block Entities.");
		FurnishRegistries.PAINTING_VARIANTS.register();
		LOG.info("Registered Furnish Painting Variants.");

		BlockEvent.PLACE.register(PlaceCarpet::onCarpetPlaced);
		InteractionEvent.RIGHT_CLICK_BLOCK.register(PlaceSnow::onSnowLayerUsedOnBlock);
		InteractionEvent.LEFT_CLICK_BLOCK.register(PopLecternBook::onLecternLeftClick);
		InteractionEvent.LEFT_CLICK_BLOCK.register(KnockOnDoor::onDoorHit);
		EntityEvent.ADD.register(GivePlateToEnderman::onEndermanSpawn);

		// These InteractionEvents do not fire with Armor Stand or Painting (but fires with Slimes...), the event registration is therefore moved to Forge/Fabric code
		// See FurnishForge @SubscribeEvent-s and FurnishFabric.onInitialize for details
		// InteractionEvent.INTERACT_ENTITY.register(AddArmsToArmorStand::rightClickArmorStand);
		// InteractionEvent.INTERACT_ENTITY.register(CyclePainting::onPaintingInteract);

		LOG.info("Registered Furnish Events.");
	}

	public static void initClient() {
		if(Platform.getEnvironment() == Env.CLIENT) {
			FurnishClient.registerBlockRenderTypes();
			FurnishClient.bindScreensToContainers();
			FurnishClient.registerEntityRenderers();
			FurnishClient.copyDoorKnockSilencerPack();
		} else LOG.error("Attempt to call initClient elsewhere than on client.");
	}

	public static void debug(String msg) {
		// Toggle comment on the following line to enable/disable debug messages
		//System.out.println(msg);
	}
}
