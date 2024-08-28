package io.github.wouink.furnish.neoforge;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.client.gui.ConditionalSlotContainerScreen;
import io.github.wouink.furnish.client.gui.DiskRackScreen;
import io.github.wouink.furnish.client.gui.FurnitureWorkbenchScreen;
import io.github.wouink.furnish.client.renderer.*;
import io.github.wouink.furnish.event.AddArmsToArmorStand;
import io.github.wouink.furnish.event.CyclePainting;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(Furnish.MODID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class FurnishForge {

    public FurnishForge(IEventBus eventBus) {
        Furnish.LOG.info("Initializing Furnish on Forge.");

        eventBus.addListener(this::init);
        eventBus.addListener(this::initClient);

        Furnish.init();

        NeoForge.EVENT_BUS.addListener(FurnishForge::rightClickPainting);
        NeoForge.EVENT_BUS.addListener(FurnishForge::rightClickArmorStand);
    }

    private void init(final FMLCommonSetupEvent event) {
        // no op
    }

    private void initClient(final FMLClientSetupEvent event) {
        Furnish.LOG.info("Initialize Furnish client on Forge.");
        Furnish.initClient();
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        // Registering through Architectury does not work anymore in 1.21
        event.register(FurnishRegistries.Furniture_Workbench_Container.get(), FurnitureWorkbenchScreen::new);
        event.register(FurnishRegistries.Crate_Container.get(), ConditionalSlotContainerScreen::new);
        event.register(FurnishRegistries.Mailbox_Container.get(), ConditionalSlotContainerScreen::new);
        event.register(FurnishRegistries.Bookshelf_Chest_Container.get(), ConditionalSlotContainerScreen::new);
        event.register(FurnishRegistries.Disk_Rack_Container.get(), DiskRackScreen::new);
        Furnish.LOG.info("Bound Furnish containers to their screens (special Forge 1.21 workaround).");
    }

    @SubscribeEvent
    public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        // Registering through Architectury does not work anymore in 1.21
        event.registerEntityRenderer(FurnishRegistries.Seat_Entity.get(), SeatRenderer::new);
        event.registerBlockEntityRenderer(FurnishRegistries.Mailbox_BlockEntity.get(), MailboxRenderer::new);
        event.registerBlockEntityRenderer(FurnishRegistries.Plate_BlockEntity.get(), PlateRenderer::new);
        event.registerBlockEntityRenderer(FurnishRegistries.Shelf_BlockEntity.get(), ShelfRenderer::new);
        event.registerBlockEntityRenderer(FurnishRegistries.Showcase_BlockEntity.get(), ShowcaseRenderer::new);
        event.registerBlockEntityRenderer(FurnishRegistries.Disk_Rack_BlockEntity.get(), DiskRackRenderer::new);
        event.registerBlockEntityRenderer(FurnishRegistries.Recycle_Bin_BlockEntity.get(), RecycleBinRenderer::new);
        Furnish.LOG.info("Registered Furnish entity/block entity renderers (special Forge 1.21 workaround).");
    }

    public static void rightClickArmorStand(PlayerInteractEvent.EntityInteractSpecific event) {
        // Architectury InteractionEvent.INTERACT_ENTITY does not seem to work with Armor Stands and Paintings...
        AddArmsToArmorStand.rightClickArmorStand(event.getEntity(), event.getTarget(), event.getHand());
    }

    public static void rightClickPainting(PlayerInteractEvent.EntityInteract event) {
        // Architectury InteractionEvent.INTERACT_ENTITY does not seem to work with Armor Stands and Paintings...
        CyclePainting.onPaintingInteract(event.getEntity(), event.getTarget(), event.getHand());
    }
}
