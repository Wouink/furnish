package io.github.wouink.furnish.screen;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.container.DiskRackMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class DiskRackScreen extends AbstractContainerScreen<DiskRackMenu> {
    private static final Identifier CONTAINER_BACKGROUND = Identifier.fromNamespaceAndPath(Furnish.MOD_ID, "textures/gui/disk_rack.png");
    private final int containerRows = 1;

    public DiskRackScreen(DiskRackMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, i, j, 0.0f, 0.0f, this.imageWidth, this.containerRows * 18 + 17, 256, 256);
        // TODO guiGraphics.blit(CONTAINER_BACKGROUND, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}
