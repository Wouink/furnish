package io.github.wouink.furnish.block.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ConditionalSlot extends Slot {
	private final Predicate<ItemStack> condition;

	public ConditionalSlot(Predicate<ItemStack> condition, Container inventory, int index, int xPos, int yPos) {
		super(inventory, index, xPos, yPos);
		this.condition = condition;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return condition.test(stack);
	}

	public static boolean never(ItemStack stack) {
		return false;
	}

	public static boolean always(ItemStack stack) {
		return true;
	}
}
