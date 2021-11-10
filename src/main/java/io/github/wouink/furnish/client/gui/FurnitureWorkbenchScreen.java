package io.github.wouink.furnish.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.container.FurnitureWorkbenchContainer;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class FurnitureWorkbenchScreen extends ContainerScreen<FurnitureWorkbenchContainer> {
	private static final ResourceLocation Background_Texture = new ResourceLocation(Furnish.MODID, "textures/gui/furniture_workbench.png");
	private float sliderProgress;
	private boolean clickedOnScroll;
	private int recipeIndexOffset;
	private boolean hasItemsInInputSlot;

	public FurnitureWorkbenchScreen(FurnitureWorkbenchContainer container, PlayerInventory inv, ITextComponent title) {
		super(container, inv, title);
		container.setInventoryUpdateListener(this::onInventoryUpdate);
		--this.titleLabelY;
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderBg(MatrixStack ms, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
		this.renderBackground(ms);
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.minecraft.getTextureManager().bind(Background_Texture);
		int i = this.leftPos;
		int j = this.topPos;
		this.blit(ms, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int k = (int) (41.0f * this.sliderProgress);
		int l = this.leftPos + 52;
		int i1 = this.topPos + 14;
		int j1 = this.recipeIndexOffset + 12;
		this.renderButtons(ms, p_230450_3_, p_230450_4_, l, i1, j1);
		this.drawRecipesItems(l, i1, j1);
	}

	@Override
	protected void renderTooltip(MatrixStack ms, int mouseX, int mouseY) {
		super.renderTooltip(ms, mouseX, mouseY);
		if(this.hasItemsInInputSlot) {
			int i = this.leftPos + 52;
			int j = this.topPos + 14;
			int k = this.recipeIndexOffset + 12;
			List<FurnitureRecipe> list = this.menu.getRecipeList();

			for(int l = this.recipeIndexOffset; l < k && l < this.menu.getRecipeListSize(); ++l) {
				int i1 = l - this.recipeIndexOffset;
				int j1 = i + i1 % 4 * 16;
				int k1 = j + i1 / 4 * 18 + 2;
				if (mouseX >= j1 && mouseX < j1 + 16 && mouseY >= k1 && mouseY < k1 + 18) {
					this.renderTooltip(ms, list.get(l).getResultItem(), mouseX, mouseY);
				}
			}
		}
	}

	private void renderButtons(MatrixStack p_238853_1_, int p_238853_2_, int p_238853_3_, int p_238853_4_, int p_238853_5_, int p_238853_6_) {
		for (int i = this.recipeIndexOffset; i < p_238853_6_ && i < this.menu.getRecipeListSize(); ++i) {
			int j = i - this.recipeIndexOffset;
			int k = p_238853_4_ + j % 4 * 16;
			int l = j / 4;
			int i1 = p_238853_5_ + l * 18 + 2;
			int j1 = this.imageHeight;
			if (i == this.menu.getSelectedRecipe()) {
				j1 += 18;
			} else if (p_238853_2_ >= k && p_238853_3_ >= i1 && p_238853_2_ < k + 16 && p_238853_3_ < i1 + 18) {
				j1 += 36;
			}

			this.blit(p_238853_1_, k, i1 - 1, 0, j1, 16, 18);
		}

	}

	private void drawRecipesItems(int left, int top, int recipeIndexOffsetMax) {
		List<FurnitureRecipe> list = this.menu.getRecipeList();

		for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.menu.getRecipeListSize(); ++i) {
			int j = i - this.recipeIndexOffset;
			int k = left + j % 4 * 16;
			int l = j / 4;
			int i1 = top + l * 18 + 2;
			this.minecraft.getItemRenderer().renderAndDecorateItem(list.get(i).getResultItem(), k, i1);
		}

	}

	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		this.clickedOnScroll = false;
		if (this.hasItemsInInputSlot) {
			int i = this.leftPos + 52;
			int j = this.topPos + 14;
			int k = this.recipeIndexOffset + 12;

			for (int l = this.recipeIndexOffset; l < k; ++l) {
				int i1 = l - this.recipeIndexOffset;
				double d0 = p_231044_1_ - (double) (i + i1 % 4 * 16);
				double d1 = p_231044_3_ - (double) (j + i1 / 4 * 18);
				if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, l)) {
					Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.WOODEN_BUTTON_CLICK_ON, 1.0F));
					this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
					return true;
				}
			}

			i = this.leftPos + 119;
			j = this.topPos + 9;
			if (p_231044_1_ >= (double) i && p_231044_1_ < (double) (i + 12) && p_231044_3_ >= (double) j && p_231044_3_ < (double) (j + 54)) {
				this.clickedOnScroll = true;
			}
		}

		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
	}

	@Override
	public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
		if (this.clickedOnScroll && this.canScroll()) {
			int i = this.topPos + 14;
			int j = i + 54;
			this.sliderProgress = ((float) p_231045_3_ - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
			this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
			this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) this.getHiddenRows()) + 0.5D) * 4;
			return true;
		} else {
			return super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
		}
	}

	@Override
	public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
		if (this.canScroll()) {
			int i = this.getHiddenRows();
			this.sliderProgress = (float) ((double) this.sliderProgress - p_231043_5_ / (double) i);
			this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
			this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) i) + 0.5D) * 4;
		}

		return true;
	}

	private boolean canScroll() {
		return this.hasItemsInInputSlot && this.menu.getRecipeListSize() > 12;
	}

	protected int getHiddenRows() {
		return (this.menu.getRecipeListSize() + 4 - 1) / 4 - 3;
	}

	private void onInventoryUpdate() {
		this.hasItemsInInputSlot = this.menu.hasItemsInInputSlot();
		if (!this.hasItemsInInputSlot) {
			this.sliderProgress = 0.0F;
			this.recipeIndexOffset = 0;
		}

	}
}
