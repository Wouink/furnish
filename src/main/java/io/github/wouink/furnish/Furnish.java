package io.github.wouink.furnish;

import io.github.wouink.furnish.client.gui.FurnitureWorkbenchScreen;
import io.github.wouink.furnish.client.renderer.SeatRenderer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Furnish.MODID)
@Mod.EventBusSubscriber(modid = Furnish.MODID)
public class Furnish {
	public static final String MODID = "furnish";

	public Furnish() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
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
	}

	private void clientSetup(FMLClientSetupEvent event) {
		ScreenManager.register(FurnishManager.Containers.Furniture_Workbench.get(), FurnitureWorkbenchScreen::new);
		FurnishManager.Furnish_Logger.info("Registered Furnish Screens.");
		RenderingRegistry.registerEntityRenderingHandler(FurnishManager.Entities.Seat_Entity.get(), SeatRenderer::new);
		FurnishManager.Furnish_Logger.info("Registered Furnish Entity Renderers.");
	}
}
