package io.github.wouink.furnish.block.container;

import com.google.common.collect.Lists;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

public class FurnitureWorkbenchContainer extends Container {
	private final IWorldPosCallable worldPosCallable;
	private final IntReferenceHolder selectedRecipe = IntReferenceHolder.standalone();
	private final World world;
	private List<FurnitureRecipe> recipes = Lists.newArrayList();
	private ItemStack itemStackInput = ItemStack.EMPTY;
	private long lastOnTake;
	final Slot inputInventorySlot;
	final Slot outputInventorySlot;
	private Runnable inventoryUpdateListener = () -> {};
	public final IInventory inputInventory = new Inventory(1) {
		public void setChanged() {
			super.setChanged();
			FurnitureWorkbenchContainer.this.slotsChanged(this);
			FurnitureWorkbenchContainer.this.inventoryUpdateListener.run();
		}
	};
	private final CraftResultInventory inventory = new CraftResultInventory();

	public FurnitureWorkbenchContainer(int windowId, PlayerInventory playerInventory) {
		this(windowId, playerInventory, IWorldPosCallable.NULL);
	}

	public FurnitureWorkbenchContainer(int windowId, PlayerInventory playerInventory, final IWorldPosCallable worldPos) {
		super(FurnishData.Containers.Furniture_Workbench.get(), windowId);
		this.worldPosCallable = worldPos;
		this.world = playerInventory.player.level;
		this.inputInventorySlot = this.addSlot(new Slot(this.inputInventory, 0, 20, 33));
		this.outputInventorySlot = this.addSlot(new Slot(this.inventory, 1, 143, 33) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
			}

			@Override
			public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
				ItemStack itemStack = FurnitureWorkbenchContainer.this.inputInventorySlot.remove(1);
				if(!itemStack.isEmpty()) {
					FurnitureWorkbenchContainer.this.updateRecipeResultSlot();
				}

				stack.getItem().onCraftedBy(stack, thePlayer.level, thePlayer);
				worldPos.execute((world, pos) -> {
					long l = world.getGameTime();
					if(FurnitureWorkbenchContainer.this.lastOnTake != 1) {
						world.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
						FurnitureWorkbenchContainer.this.lastOnTake = 1;
					}
				});

				return super.onTake(thePlayer, stack);
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
		return this.inputInventorySlot.hasItem() && !this.recipes.isEmpty();
	}

	@Override
	public boolean stillValid(PlayerEntity playerIn) {
		return stillValid(this.worldPosCallable, playerIn, FurnishBlocks.Furniture_Workbench);
	}

	@Override
	public boolean clickMenuButton(PlayerEntity playerIn, int id) {
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
	public void slotsChanged(IInventory inv) {
		ItemStack itemStack = this.inputInventorySlot.getItem();
		if(itemStack.getItem() != this.itemStackInput.getItem()) {
			this.itemStackInput = itemStack.copy();
			this.updateAvailableRecipes(inv, itemStack);
		}
	}

	private void updateAvailableRecipes(IInventory inv, ItemStack stack) {
		this.recipes.clear();
		this.selectedRecipe.set(-1);
		this.outputInventorySlot.set(ItemStack.EMPTY);
		if(!stack.isEmpty()) {
			this.recipes = this.world.getRecipeManager().getRecipesFor(FurnishData.Furniture_Recipe, inv, this.world);
		}
	}

	public void updateRecipeResultSlot() {
		if(!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipe.get())) {
			FurnitureRecipe recipe = this.recipes.get(this.selectedRecipe.get());
			this.outputInventorySlot.set(recipe.assemble(this.inputInventory));
		} else {
			this.outputInventorySlot.set(ItemStack.EMPTY);
		}

		this.broadcastChanges();
	}

	@Override
	public ContainerType<?> getType() {
		return FurnishData.Containers.Furniture_Workbench.get();
	}

	public void setInventoryUpdateListener(Runnable listenerIn) {
		this.inventoryUpdateListener = listenerIn;
	}

	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return slot.container != this.inventory && super.canTakeItemForPickAll(stack, slot);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
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
			} else if(this.world.getRecipeManager().getRecipeFor(FurnishData.Furniture_Recipe, new Inventory(itemStack1), this.world).isPresent()) {
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

	public void removed(PlayerEntity playerIn) {
		super.removed(playerIn);
		this.inventory.removeItemNoUpdate(1);
		this.worldPosCallable.execute((world, pos) -> {
			this.clearContainer(playerIn, playerIn.level, this.inputInventory);
		});
	}
}
