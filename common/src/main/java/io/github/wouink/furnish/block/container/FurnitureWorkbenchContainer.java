package io.github.wouink.furnish.block.container;

import com.google.common.collect.Lists;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

// based on net.minecraft.world.inventory.StonecutterMenu
public class FurnitureWorkbenchContainer extends AbstractContainerMenu {
	public static final int INPUT_SLOT = 0;
	public static final int RESULT_SLOT = 1;
	private static final int INV_SLOT_START = 2;
	private static final int INV_SLOT_END = 29;
	private static final int USE_ROW_SLOT_START = 29;
	private static final int USE_ROW_SLOT_END = 38;
	private final ContainerLevelAccess access;
	private final DataSlot selectedRecipeIndex;
	private final Level level;
	private List<FurnitureRecipe> recipes;
	private ItemStack input;
	long lastSoundTime;
	final Slot inputSlot;
	final Slot resultSlot;
	Runnable slotUpdateListener;
	public final Container container;
	final ResultContainer resultContainer;

	public FurnitureWorkbenchContainer(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, ContainerLevelAccess.NULL);
	}

	public FurnitureWorkbenchContainer(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
		super(FurnishRegistries.Furniture_Workbench_Container.get(), containerId);
		this.selectedRecipeIndex = DataSlot.standalone();
		this.recipes = Lists.newArrayList();
		this.input = ItemStack.EMPTY;
		this.slotUpdateListener = () -> {
		};
		this.container = new SimpleContainer(1) {
			public void setChanged() {
				super.setChanged();
				FurnitureWorkbenchContainer.this.slotsChanged(this);
				FurnitureWorkbenchContainer.this.slotUpdateListener.run();
			}
		};
		this.resultContainer = new ResultContainer();
		this.access = access;
		this.level = playerInventory.player.level();
		this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
		this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
			public boolean mayPlace(ItemStack stack) {
				return false;
			}

			public void onTake(Player player, ItemStack stack) {
				stack.onCraftedBy(player.level(), player, stack.getCount());
				FurnitureWorkbenchContainer.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());
				ItemStack itemStack = FurnitureWorkbenchContainer.this.inputSlot.remove(1);
				if (!itemStack.isEmpty()) {
					FurnitureWorkbenchContainer.this.setupResultSlot();
				}

				access.execute((level, blockPos) -> {
					long l = level.getGameTime();
					if (FurnitureWorkbenchContainer.this.lastSoundTime != l) {
						SoundEvent sound = stack.getItem() instanceof BlockItem blockItem ? blockItem.getBlock().getSoundType(blockItem.getBlock().defaultBlockState()).getBreakSound() : SoundEvents.WOOD_BREAK;
						level.playSound((Player)null, blockPos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
						FurnitureWorkbenchContainer.this.lastSoundTime = l;
					}

				});
				super.onTake(player, stack);
			}

			private List<ItemStack> getRelevantItems() {
				return List.of(FurnitureWorkbenchContainer.this.inputSlot.getItem());
			}
		});

		int i;
		for(i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}

		this.addDataSlot(this.selectedRecipeIndex);
	}

	public int getSelectedRecipeIndex() {
		return this.selectedRecipeIndex.get();
	}

	public List<FurnitureRecipe> getRecipes() {
		return this.recipes;
	}

	public int getNumRecipes() {
		return this.recipes.size();
	}

	public boolean hasInputItem() {
		return this.inputSlot.hasItem() && !this.recipes.isEmpty();
	}

	public boolean stillValid(Player player) {
		return stillValid(this.access, player, FurnishBlocks.Furniture_Workbench.get());
	}

	public boolean clickMenuButton(Player player, int id) {
		if (this.isValidRecipeIndex(id)) {
			this.selectedRecipeIndex.set(id);
			this.setupResultSlot();
		}

		return true;
	}

	private boolean isValidRecipeIndex(int recipeIndex) {
		return recipeIndex >= 0 && recipeIndex < this.recipes.size();
	}

	public void slotsChanged(Container container) {
		ItemStack itemStack = this.inputSlot.getItem();
		if (!itemStack.is(this.input.getItem())) {
			this.input = itemStack.copy();
			this.setupRecipeList(container, itemStack);
		}

	}

	private void setupRecipeList(Container container, ItemStack stack) {
		this.recipes.clear();
		this.selectedRecipeIndex.set(-1);
		this.resultSlot.set(ItemStack.EMPTY);
		if (!stack.isEmpty()) {
			// todo
			this.recipes = this.level.getRecipeManager().getRecipesFor(FurnishRegistries.Furniture_Recipe.get(), container, this.level);
		}

	}

	void setupResultSlot() {
		if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
			FurnitureRecipe recipe = (FurnitureRecipe)this.recipes.get(this.selectedRecipeIndex.get());
			ItemStack itemStack = recipe.assemble(this.container, this.level.registryAccess());
			if (itemStack.isItemEnabled(this.level.enabledFeatures())) {
				this.resultContainer.setRecipeUsed(recipe);
				this.resultSlot.set(itemStack);
			} else {
				this.resultSlot.set(ItemStack.EMPTY);
			}
		} else {
			this.resultSlot.set(ItemStack.EMPTY);
		}

		this.broadcastChanges();
	}

	public MenuType<?> getType() {
		return FurnishRegistries.Furniture_Workbench_Container.get();
	}

	public void registerUpdateListener(Runnable listener) {
		this.slotUpdateListener = listener;
	}

	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
	}

	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			Item item = itemStack2.getItem();
			itemStack = itemStack2.copy();
			if (index == 1) {
				item.onCraftedBy(itemStack2, player.level(), player);
				if (!this.moveItemStackTo(itemStack2, 2, 38, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemStack2, itemStack);
			} else if (index == 0) {
				if (!this.moveItemStackTo(itemStack2, 2, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.level.getRecipeManager().getRecipeFor(FurnishRegistries.Furniture_Recipe.get(), new SimpleContainer(new ItemStack[]{itemStack2}), this.level).isPresent()) {
				if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 2 && index < 29) {
				if (!this.moveItemStackTo(itemStack2, 29, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 29 && index < 38 && !this.moveItemStackTo(itemStack2, 2, 29, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			}

			slot.setChanged();
			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemStack2);
			this.broadcastChanges();
		}

		return itemStack;
	}

	public void removed(Player player) {
		super.removed(player);
		this.resultContainer.removeItemNoUpdate(1);
		this.access.execute((level, blockPos) -> {
			this.clearContainer(player, this.container);
		});
	}
}
