package io.github.wouink.furnish.client.gui;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.container.FurnitureWorkbenchContainer;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

// copied from net.minecraft.client.gui.screens.inventory.StonecutterScreen
public class FurnitureWorkbenchScreen extends AbstractContainerScreen<FurnitureWorkbenchContainer> {
	private static final ResourceLocation BG_LOCATION = new ResourceLocation(Furnish.MODID, "textures/gui/furniture_workbench.png");
	private static final int SCROLLER_WIDTH = 12;
	private static final int SCROLLER_HEIGHT = 15;
	private static final int RECIPES_COLUMNS = 4;
	private static final int RECIPES_ROWS = 3;
	private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
	private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
	private static final int SCROLLER_FULL_HEIGHT = 54;
	private static final int RECIPES_X = 52;
	private static final int RECIPES_Y = 14;
	private float scrollOffs;
	private boolean scrolling;
	private int startIndex;
	private boolean displayRecipes;

	public FurnitureWorkbenchScreen(FurnitureWorkbenchContainer menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		--this.titleLabelY;
	}

	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		this.renderBackground(guiGraphics);
		int i = this.leftPos;
		int j = this.topPos;
		guiGraphics.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int k = (int)(41.0F * this.scrollOffs);
		guiGraphics.blit(BG_LOCATION, i + 119, j + 15 + k, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
		int l = this.leftPos + 52;
		int m = this.topPos + 14;
		int n = this.startIndex + 12;
		this.renderButtons(guiGraphics, mouseX, mouseY, l, m, n);
		this.renderRecipes(guiGraphics, l, m, n);
	}

	protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
		super.renderTooltip(guiGraphics, x, y);
		if (this.displayRecipes) {
			int i = this.leftPos + 52;
			int j = this.topPos + 14;
			int k = this.startIndex + 12;
			List<FurnitureRecipe> list = this.menu.getRecipeList();

			for(int l = this.startIndex; l < k && l < this.menu.getRecipeListSize(); ++l) {
				int m = l - this.startIndex;
				int n = i + m % 4 * 16;
				int o = j + m / 4 * 18 + 2;
				if (x >= n && x < n + 16 && y >= o && y < o + 18) {
					guiGraphics.renderTooltip(this.font, list.get(l).getResultItem(this.minecraft.level.registryAccess()), x, y);
				}
			}
		}

	}

	private void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int lastVisibleElementIndex) {
		for(int i = this.startIndex; i < lastVisibleElementIndex && i < this.menu.getRecipeListSize(); ++i) {
			int j = i - this.startIndex;
			int k = x + j % 4 * 16;
			int l = j / 4;
			int m = y + l * 18 + 2;
			int n = this.imageHeight;
			if (i == this.menu.getSelectedRecipe()) {
				n += 18;
			} else if (mouseX >= k && mouseY >= m && mouseX < k + 16 && mouseY < m + 18) {
				n += 36;
			}

			guiGraphics.blit(BG_LOCATION, k, m - 1, 0, n, 16, 18);
		}

	}

	private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int startIndex) {
		List<FurnitureRecipe> list = this.menu.getRecipeList();

		for(int i = this.startIndex; i < startIndex && i < this.menu.getRecipeListSize(); ++i) {
			int j = i - this.startIndex;
			int k = x + j % 4 * 16;
			int l = j / 4;
			int m = y + l * 18 + 2;
			guiGraphics.renderItem(list.get(i).getResultItem(this.minecraft.level.registryAccess()), k, m);
		}

	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		Furnish.debug("Mouse clicked in workbench");
		this.scrolling = false;
		if (this.displayRecipes) {
			Furnish.debug("displayRecipes");
			int i = this.leftPos + 52;
			int j = this.topPos + 14;
			int k = this.startIndex + 12;

			for(int l = this.startIndex; l < k; ++l) {
				Furnish.debug("l = " + l);
				int m = l - this.startIndex;
				double d = mouseX - (double)(i + m % 4 * 16);
				double e = mouseY - (double)(j + m / 4 * 18);
				if (d >= 0.0 && e >= 0.0 && d < 16.0 && e < 18.0 && this.menu.clickMenuButton(this.minecraft.player, l)) {
					Furnish.debug("Click in the area");
					Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.WOODEN_BUTTON_CLICK_ON, 1.0F));
					this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, l);
					return true;
				}
			}

			i = this.leftPos + 119;
			j = this.topPos + 9;
			if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
				this.scrolling = true;
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (this.scrolling && this.isScrollBarActive()) {
			int i = this.topPos + 14;
			int j = i + 54;
			this.scrollOffs = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
			this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5) * 4;
			return true;
		} else {
			return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
		}
	}

	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (this.isScrollBarActive()) {
			int i = this.getOffscreenRows();
			float f = (float)delta / (float)i;
			this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
			this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5) * 4;
		}

		return true;
	}

	private boolean isScrollBarActive() {
		return this.displayRecipes && this.menu.getRecipeListSize() > 12;
	}

	protected int getOffscreenRows() {
		return (this.menu.getRecipeListSize() + 4 - 1) / 4 - 3;
	}

	private void containerChanged() {
		this.displayRecipes = this.menu.hasItemsInInputSlot();
		if (!this.displayRecipes) {
			this.scrollOffs = 0.0F;
			this.startIndex = 0;
		}

	}
}
