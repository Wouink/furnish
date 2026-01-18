package io.github.wouink.furnish.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ConditionalSlot extends Slot {
    private Predicate<ItemStack> predicate;

    public ConditionalSlot(Predicate<ItemStack> predicate, Container container, int i, int j, int k) {
        super(container, i, j, k);
        this.predicate = predicate;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return predicate.test(itemStack);
    }
}
