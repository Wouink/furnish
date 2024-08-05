package io.github.wouink.furnish.block.container;

import com.google.common.collect.Lists;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

// based on net.minecraft.world.inventory.StonecutterMenu
public class FurnitureWorkbenchMenu extends AbstractContainerMenu {
	public static final int INPUT_SLOT = 0;
	public static final int RESULT_SLOT = 1;
	private static final int INV_SLOT_START = 2;
	private static final int INV_SLOT_END = 29;
	private static final int USE_ROW_SLOT_START = 29;
	private static final int USE_ROW_SLOT_END = 38;
	private final ContainerLevelAccess access;
	private final DataSlot selectedRecipeIndex;
	private final Level level;
	private List<RecipeHolder<FurnitureRecipe>> recipes;
	private ItemStack input;
	long lastSoundTime;
	final Slot inputSlot;
	final Slot resultSlot;
	Runnable slotUpdateListener;
	public final Container container;
	final ResultContainer resultContainer;

	public FurnitureWorkbenchMenu(int i, Inventory inventory) {
		this(i, inventory, ContainerLevelAccess.NULL);
	}

	public FurnitureWorkbenchMenu(int i, Inventory inventory, final ContainerLevelAccess containerLevelAccess) {
		super(FurnishRegistries.Furniture_Workbench_Container.get(), i);
		this.selectedRecipeIndex = DataSlot.standalone();
		this.recipes = Lists.newArrayList();
		this.input = ItemStack.EMPTY;
		this.slotUpdateListener = () -> {
		};
		this.container = new SimpleContainer(1) {
			public void setChanged() {
				super.setChanged();
				FurnitureWorkbenchMenu.this.slotsChanged(this);
				FurnitureWorkbenchMenu.this.slotUpdateListener.run();
			}
		};
		this.resultContainer = new ResultContainer();
		this.access = containerLevelAccess;
		this.level = inventory.player.level();
		this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
		this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
			public boolean mayPlace(ItemStack itemStack) {
				return false;
			}

			public void onTake(Player player, ItemStack itemStack) {
				itemStack.onCraftedBy(player.level(), player, itemStack.getCount());
				FurnitureWorkbenchMenu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());
				ItemStack itemStack2 = FurnitureWorkbenchMenu.this.inputSlot.remove(1);
				if (!itemStack2.isEmpty()) {
					FurnitureWorkbenchMenu.this.setupResultSlot();
				}

				containerLevelAccess.execute((level, blockPos) -> {
					long l = level.getGameTime();
					if (FurnitureWorkbenchMenu.this.lastSoundTime != l) {
						level.playSound(null, blockPos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
						FurnitureWorkbenchMenu.this.lastSoundTime = l;
					}

				});
				super.onTake(player, itemStack);
			}

			private List<ItemStack> getRelevantItems() {
				return List.of(FurnitureWorkbenchMenu.this.inputSlot.getItem());
			}
		});

		int j;
		for(j = 0; j < 3; ++j) {
			for(int k = 0; k < 9; ++k) {
				this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
			}
		}

		for(j = 0; j < 9; ++j) {
			this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
		}

		this.addDataSlot(this.selectedRecipeIndex);
	}

	public int getSelectedRecipeIndex() {
		return this.selectedRecipeIndex.get();
	}

	public List<RecipeHolder<FurnitureRecipe>> getRecipes() {
		return this.recipes;
	}

	public int getNumRecipes() {
		return this.recipes.size();
	}

	public boolean hasInputItem() {
		return this.inputSlot.hasItem() && !this.recipes.isEmpty();
	}

	public boolean stillValid(Player player) {
		return stillValid(this.access, player, Blocks.STONECUTTER);
	}

	public boolean clickMenuButton(Player player, int i) {
		if (this.isValidRecipeIndex(i)) {
			this.selectedRecipeIndex.set(i);
			this.setupResultSlot();
		}

		return true;
	}

	private boolean isValidRecipeIndex(int i) {
		return i >= 0 && i < this.recipes.size();
	}

	public void slotsChanged(Container container) {
		ItemStack itemStack = this.inputSlot.getItem();
		if (!itemStack.is(this.input.getItem())) {
			this.input = itemStack.copy();
			this.setupRecipeList(container, itemStack);
		}

	}

	private static SingleRecipeInput createRecipeInput(Container container) {
		return new SingleRecipeInput(container.getItem(0));
	}

	private void setupRecipeList(Container container, ItemStack itemStack) {
		this.recipes.clear();
		this.selectedRecipeIndex.set(-1);
		this.resultSlot.set(ItemStack.EMPTY);
		if (!itemStack.isEmpty()) {
			this.recipes = this.level.getRecipeManager().getRecipesFor(FurnishRegistries.Furniture_Recipe.get(), createRecipeInput(container), this.level);
		}

	}

	void setupResultSlot() {
		if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
			RecipeHolder<StonecutterRecipe> recipeHolder = (RecipeHolder)this.recipes.get(this.selectedRecipeIndex.get());
			ItemStack itemStack = ((StonecutterRecipe)recipeHolder.value()).assemble(createRecipeInput(this.container), this.level.registryAccess());
			if (itemStack.isItemEnabled(this.level.enabledFeatures())) {
				this.resultContainer.setRecipeUsed(recipeHolder);
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

	public void registerUpdateListener(Runnable runnable) {
		this.slotUpdateListener = runnable;
	}

	public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
		return slot.container != this.resultContainer && super.canTakeItemForPickAll(itemStack, slot);
	}

	public ItemStack quickMoveStack(Player player, int i) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.slots.get(i);
		if (slot != null && slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			Item item = itemStack2.getItem();
			itemStack = itemStack2.copy();
			if (i == 1) {
				item.onCraftedBy(itemStack2, player.level(), player);
				if (!this.moveItemStackTo(itemStack2, 2, 38, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemStack2, itemStack);
			} else if (i == 0) {
				if (!this.moveItemStackTo(itemStack2, 2, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.level.getRecipeManager().getRecipeFor(FurnishRegistries.Furniture_Recipe.get(), new SingleRecipeInput(itemStack2), this.level).isPresent()) {
				if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (i >= 2 && i < 29) {
				if (!this.moveItemStackTo(itemStack2, 29, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (i >= 29 && i < 38 && !this.moveItemStackTo(itemStack2, 2, 29, false)) {
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
