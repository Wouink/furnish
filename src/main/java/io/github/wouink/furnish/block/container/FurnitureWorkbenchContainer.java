package io.github.wouink.furnish.block.container;

import com.google.common.collect.Lists;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class FurnitureWorkbenchContainer extends AbstractContainerMenu {
	private final ContainerLevelAccess access;
	private final Level level;
	private final DataSlot selectedRecipe = DataSlot.standalone();
	private List<FurnitureRecipe> recipes = Lists.newArrayList();
	private ItemStack itemStackInput = ItemStack.EMPTY;
	private long lastOnTake;
	final Slot inputSlot;
	final Slot outputSlot;
	private Runnable inventoryUpdateListener = () -> {};
	public final Container inputContainer = new SimpleContainer(1) {
		public void setChanged() {
			super.setChanged();
			FurnitureWorkbenchContainer.this.slotsChanged(this);
			FurnitureWorkbenchContainer.this.inventoryUpdateListener.run();
		}
	};
	private final ResultContainer resultContainer = new ResultContainer();

	public FurnitureWorkbenchContainer(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, ContainerLevelAccess.NULL);
	}

	public FurnitureWorkbenchContainer(int syncId, Inventory playerInventory, final ContainerLevelAccess posCallable) {
		super(FurnishData.Containers.Furniture_Workbench.get(), syncId);
		this.access = posCallable;
		this.level = playerInventory.player.level;
		this.inputSlot = this.addSlot(new Slot(this.inputContainer, 0, 20, 33));
		this.outputSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}

			@Override
			public void onTake(Player thePlayer, ItemStack stack) {
				ItemStack itemStack = FurnitureWorkbenchContainer.this.inputSlot.remove(1);
				if(!itemStack.isEmpty()) {
					FurnitureWorkbenchContainer.this.updateRecipeResultSlot();
				}

				stack.getItem().onCraftedBy(stack, thePlayer.level, thePlayer);
				access.execute((world, pos) -> {
					long l = world.getGameTime();
					if(FurnitureWorkbenchContainer.this.lastOnTake != l) {
						world.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
						FurnitureWorkbenchContainer.this.lastOnTake = l;
					}
				});

				super.onTake(thePlayer, stack);
			}
		});

		// player's inventory slots
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		// player's hotbar
		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}

		this.addDataSlot(this.selectedRecipe);
	}

	public int getSelectedRecipe() {
		return this.selectedRecipe.get();
	}

	public List<FurnitureRecipe> getRecipeList() {
		return this.recipes;
	}

	public int getRecipeListSize() {
		return this.recipes.size();
	}

	public boolean hasItemsInInputSlot() {
		return this.inputSlot.hasItem() && !this.recipes.isEmpty();
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return stillValid(this.access, playerIn, FurnishBlocks.Furniture_Workbench);
	}

	@Override
	public boolean clickMenuButton(Player playerIn, int id) {
		if(this.isValidRecipeIndex(id)) {
			this.selectedRecipe.set(id);
			this.updateRecipeResultSlot();
		}
		return true;
	}

	public boolean isValidRecipeIndex(int n) {
		return n >= 0 && n < this.getRecipeListSize();
	}

	@Override
	public void slotsChanged(Container inv) {
		ItemStack itemStack = this.inputSlot.getItem();
		if(itemStack.getItem() != this.itemStackInput.getItem()) {
			this.itemStackInput = itemStack.copy();
			this.updateAvailableRecipes(inv, itemStack);
		}
	}

	private void updateAvailableRecipes(Container inv, ItemStack stack) {
		this.recipes.clear();
		this.selectedRecipe.set(-1);
		this.outputSlot.set(ItemStack.EMPTY);
		if(!stack.isEmpty()) {
			this.recipes = this.level.getRecipeManager().getRecipesFor(FurnishData.Furniture_Recipe, inv, this.level);
		}
	}

	public void updateRecipeResultSlot() {
		if(!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipe.get())) {
			FurnitureRecipe recipe = this.recipes.get(this.selectedRecipe.get());
			this.outputSlot.set(recipe.assemble(this.inputContainer));
		} else {
			this.outputSlot.set(ItemStack.EMPTY);
		}
		this.broadcastChanges();
	}

	@Override
	public MenuType<?> getType() {
		return FurnishData.Containers.Furniture_Workbench.get();
	}

	public void setInventoryUpdateListener(Runnable listenerIn) {
		this.inventoryUpdateListener = listenerIn;
	}

	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if(slot != null && slot.hasItem()) {
			ItemStack itemStack1 = slot.getItem();
			Item item = itemStack1.getItem();
			itemStack = itemStack1.copy();
			if(index == 1) {
				item.onCraftedBy(itemStack1, playerIn.level, playerIn);
				if(!this.moveItemStackTo(itemStack1, 2, 38, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemStack1, itemStack);
			} else if(index == 0) {
				if(!this.moveItemStackTo(itemStack1, 2, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if(this.level.getRecipeManager().getRecipeFor(FurnishData.Furniture_Recipe, new SimpleContainer(itemStack1), this.level).isPresent()) {
				if (!this.moveItemStackTo(itemStack1, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if(index >= 2 && index < 29) {
				if(!this.moveItemStackTo(itemStack1, 29, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if(index >= 29 && index < 38 && !this.moveItemStackTo(itemStack1, 2, 29, false)) {
				return ItemStack.EMPTY;
			}

			if(itemStack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			}

			slot.setChanged();
			if(itemStack1.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemStack1);
			this.broadcastChanges();
		}

		return itemStack;
	}

	public void removed(Player playerIn) {
		super.removed(playerIn);
		this.resultContainer.removeItemNoUpdate(1);
		this.access.execute((world, pos) -> {
			this.clearContainer(playerIn, this.inputContainer);
		});
	}
}
