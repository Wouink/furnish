package io.github.wouink.furnish.container;

import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Optional;

/**
 * Literally a copy of StonecutterMenu but using FurnitureRecipe instead of FurnitureRecipe
 */
public class FurnitureWorkbenchMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = 29;
    private static final int USE_ROW_SLOT_START = 29;
    private static final int USE_ROW_SLOT_END = 38;
    private final ContainerLevelAccess access;
    final DataSlot selectedRecipeIndex;
    private final Level level;
    private SelectableRecipe.SingleInputSet<FurnitureRecipe> recipesForInput;
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
        super(MenuType.STONECUTTER, i);
        this.selectedRecipeIndex = DataSlot.standalone();
        this.recipesForInput = SelectableRecipe.SingleInputSet.empty();
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
                itemStack.onCraftedBy(player, itemStack.getCount());
                FurnitureWorkbenchMenu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());
                ItemStack itemStack2 = FurnitureWorkbenchMenu.this.inputSlot.remove(1);
                if (!itemStack2.isEmpty()) {
                    FurnitureWorkbenchMenu.this.setupResultSlot(FurnitureWorkbenchMenu.this.selectedRecipeIndex.get());
                }

                containerLevelAccess.execute((level, blockPos) -> {
                    long l = level.getGameTime();
                    if (FurnitureWorkbenchMenu.this.lastSoundTime != l) {
                        level.playSound((Entity)null, blockPos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        FurnitureWorkbenchMenu.this.lastSoundTime = l;
                    }

                });
                super.onTake(player, itemStack);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(FurnitureWorkbenchMenu.this.inputSlot.getItem());
            }
        });
        this.addStandardInventorySlots(inventory, 8, 84);
        this.addDataSlot(this.selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public SelectableRecipe.SingleInputSet<FurnitureRecipe> getVisibleRecipes() {
        return this.recipesForInput;
    }

    public int getNumberOfVisibleRecipes() {
        return this.recipesForInput.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.recipesForInput.isEmpty();
    }

    public boolean stillValid(Player player) {
        return stillValid(this.access, player, Blocks.STONECUTTER);
    }

    public boolean clickMenuButton(Player player, int i) {
        if (this.selectedRecipeIndex.get() == i) {
            return false;
        } else {
            if (this.isValidRecipeIndex(i)) {
                this.selectedRecipeIndex.set(i);
                this.setupResultSlot(i);
            }

            return true;
        }
    }

    private boolean isValidRecipeIndex(int i) {
        return i >= 0 && i < this.recipesForInput.size();
    }

    public void slotsChanged(Container container) {
        ItemStack itemStack = this.inputSlot.getItem();
        if (!itemStack.is(this.input.getItem())) {
            this.input = itemStack.copy();
            this.setupRecipeList(itemStack);
        }

    }

    private void setupRecipeList(ItemStack itemStack) {
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!itemStack.isEmpty()) {
            // TODO this.recipesForInput = this.level.recipeAccess().stonecutterRecipes();
        } else {
            this.recipesForInput = SelectableRecipe.SingleInputSet.empty();
        }

    }

    void setupResultSlot(int i) {
        Optional<RecipeHolder<FurnitureRecipe>> optional;
        if (!this.recipesForInput.isEmpty() && this.isValidRecipeIndex(i)) {
            SelectableRecipe.SingleInputEntry<FurnitureRecipe> singleInputEntry = (SelectableRecipe.SingleInputEntry)this.recipesForInput.entries().get(i);
            optional = singleInputEntry.recipe().recipe();
        } else {
            optional = Optional.empty();
        }

        optional.ifPresentOrElse((recipeHolder) -> {
            this.resultContainer.setRecipeUsed(recipeHolder);
            this.resultSlot.set(((FurnitureRecipe)recipeHolder.value()).assemble(new SingleRecipeInput(this.container.getItem(0)), this.level.registryAccess()));
        }, () -> {
            this.resultSlot.set(ItemStack.EMPTY);
            this.resultContainer.setRecipeUsed((RecipeHolder)null);
        });
        this.broadcastChanges();
    }

    public MenuType<?> getType() {
        return MenuType.STONECUTTER;
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
                item.onCraftedBy(itemStack2, player);
                if (!this.moveItemStackTo(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);
            } else if (i == 0) {
                if (!this.moveItemStackTo(itemStack2, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } /* TODO else if (this.level.recipeAccess().FurnitureRecipes().acceptsInput(itemStack2)) {
                if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } */ else if (i >= 2 && i < 29) {
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
            if (i == 1) {
                player.drop(itemStack2, false);
            }

            this.broadcastChanges();
        }

        return itemStack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.container));
    }
}
