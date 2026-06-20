package io.github.wouink.furnish.container;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishContents;
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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.*;

/**
 * Literally a copy of StonecutterMenu but using FurnitureRecipe instead of StonecutterRecipe
 * The only difference is in `setupRecipeList`, which uses my own `getRecipes` method
 */

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
    private SelectableRecipe.SingleInputSet<FurnitureRecipe> recipesForInput;
    private ItemStack input;
    private long lastSoundTime;
    private final Slot inputSlot;
    private final Slot resultSlot;
    private Runnable slotUpdateListener;
    public final Container container;
    private final ResultContainer resultContainer;

    public FurnitureWorkbenchMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public FurnitureWorkbenchMenu(final int containerId, final Inventory inventory, final ContainerLevelAccess access) {
        super(MenuType.STONECUTTER, containerId);
        this.selectedRecipeIndex = DataSlot.standalone();
        this.recipesForInput = SelectableRecipe.SingleInputSet.empty();
        this.input = ItemStack.EMPTY;
        this.slotUpdateListener = () -> {
        };
        this.container = new SimpleContainer(1) {
            {
                Objects.requireNonNull(FurnitureWorkbenchMenu.this);
            }

            public void setChanged() {
                super.setChanged();
                FurnitureWorkbenchMenu.this.slotsChanged(this);
                FurnitureWorkbenchMenu.this.slotUpdateListener.run();
            }
        };
        this.resultContainer = new ResultContainer();
        this.access = access;
        this.level = inventory.player.level();
        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
            {
                Objects.requireNonNull(FurnitureWorkbenchMenu.this);
            }

            public boolean mayPlace(final ItemStack itemStack) {
                return false;
            }

            public void onTake(final Player player, final ItemStack carried) {
                carried.onCraftedBy(player, carried.getCount());
                FurnitureWorkbenchMenu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());
                ItemStack remaining = FurnitureWorkbenchMenu.this.inputSlot.remove(1);
                if (!remaining.isEmpty()) {
                    FurnitureWorkbenchMenu.this.setupResultSlot(FurnitureWorkbenchMenu.this.selectedRecipeIndex.get());
                }

                access.execute((level, pos) -> {
                    long gameTime = level.getGameTime();
                    if (FurnitureWorkbenchMenu.this.lastSoundTime != gameTime) {
                        level.playSound((Entity)null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        FurnitureWorkbenchMenu.this.lastSoundTime = gameTime;
                    }

                });
                super.onTake(player, carried);
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

    public boolean stillValid(final Player player) {
        return stillValid(this.access, player, FurnishContents.FURNITURE_WORKBENCH);
    }

    public boolean clickMenuButton(final Player player, final int buttonId) {
        if (this.selectedRecipeIndex.get() == buttonId) {
            return false;
        } else {
            if (this.isValidRecipeIndex(buttonId)) {
                this.selectedRecipeIndex.set(buttonId);
                this.setupResultSlot(buttonId);
            }

            return true;
        }
    }

    private boolean isValidRecipeIndex(final int buttonId) {
        return buttonId >= 0 && buttonId < this.recipesForInput.size();
    }

    public void slotsChanged(final Container container) {
        ItemStack input = this.inputSlot.getItem();
        if (!input.is(this.input.getItem())) {
            this.input = input.copy();
            this.setupRecipeList(input);
        }
    }

    private SelectableRecipe.SingleInputSet<FurnitureRecipe> getRecipes(RecipeAccess recipeAccess, ItemStack input) {
        List<SelectableRecipe.SingleInputEntry<FurnitureRecipe>> furnitureRecipes = new ArrayList();

        // to get recipes on client, we need to synchronizeRecipeSerializer (see FurnishContents init)
        Collection<RecipeHolder<FurnitureRecipe>> recipes = recipeAccess.getSynchronizedRecipes().getAllOfType(FurnishContents.FURNITURE_RECIPE);
        for(RecipeHolder holder : recipes) {
            if(holder.value() instanceof FurnitureRecipe furnitureRecipe) {
                if(furnitureRecipe.input().test(input))
                    furnitureRecipes.add(new SelectableRecipe.SingleInputEntry(furnitureRecipe.input(), new SelectableRecipe(furnitureRecipe.resultDisplay(), Optional.of(holder))));
            }
        }

        return new SelectableRecipe.SingleInputSet<>(furnitureRecipes);
    }

    private void setupRecipeList(final ItemStack item) {
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!item.isEmpty()) {
            this.recipesForInput = getRecipes(level.recipeAccess(), item);
        } else {
            this.recipesForInput = SelectableRecipe.SingleInputSet.empty();
        }
    }

    private void setupResultSlot(final int index) {
        Optional<RecipeHolder<FurnitureRecipe>> usedRecipe;
        if (!this.recipesForInput.isEmpty() && this.isValidRecipeIndex(index)) {
            SelectableRecipe.SingleInputEntry<FurnitureRecipe> entry = this.recipesForInput.entries().get(index);
            usedRecipe = entry.recipe().recipe();
        } else {
            usedRecipe = Optional.empty();
        }

        usedRecipe.ifPresentOrElse((recipe) -> {
            this.resultContainer.setRecipeUsed(recipe);
            this.resultSlot.set(((FurnitureRecipe)recipe.value()).assemble(new SingleRecipeInput(this.container.getItem(0))));
        }, () -> {
            this.resultSlot.set(ItemStack.EMPTY);
            this.resultContainer.setRecipeUsed((RecipeHolder)null);
        });
        this.broadcastChanges();
    }

    public MenuType<?> getType() {
        return FurnishContents.WORKBENCH_MENU;
    }

    public void registerUpdateListener(final Runnable slotUpdateListener) {
        this.slotUpdateListener = slotUpdateListener;
    }

    public boolean canTakeItemForPickAll(final ItemStack carried, final Slot target) {
        return target.container != this.resultContainer && super.canTakeItemForPickAll(carried, target);
    }

    public ItemStack quickMoveStack(final Player player, final int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            Item item = stack.getItem();
            clicked = stack.copy();
            if (slotIndex == 1) {
                item.onCraftedBy(stack, player);
                if (!this.moveItemStackTo(stack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, clicked);
            } else if (slotIndex == 0) {
                if (!this.moveItemStackTo(stack, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (getRecipes(level.recipeAccess(), stack).size() > 0) {
                if (!this.moveItemStackTo(stack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 2 && slotIndex < 29) {
                if (!this.moveItemStackTo(stack, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 29 && slotIndex < 38 && !this.moveItemStackTo(stack, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (stack.getCount() == clicked.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
            if (slotIndex == 1) {
                player.drop(stack, false);
            }

            this.broadcastChanges();
        }

        return clicked;
    }

    public void removed(final Player player) {
        super.removed(player);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((level, pos) -> this.clearContainer(player, this.container));
    }
}

