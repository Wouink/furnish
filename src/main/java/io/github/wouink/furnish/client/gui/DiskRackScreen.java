package io.github.wouink.furnish.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.container.DiskRackContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DiskRackScreen extends AbstractContainerScreen<DiskRackContainer> {
	private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Furnish.MODID, "textures/gui/disk_rack.png");
	public static final int containerRows = 1;

	public DiskRackScreen(DiskRackContainer p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_) {
		super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
		this.passEvents = false;
		int i = 222;
		int j = 114;
		this.imageHeight = 114 + containerRows * 18;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	public void render(PoseStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(p_230430_1_);
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
	}

	protected void renderBg(PoseStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
		this.renderBackground(p_230450_1_);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, containerRows * 18 + 17);
		this.blit(p_230450_1_, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
	}
}
