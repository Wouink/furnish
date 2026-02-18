package io.github.wouink.furnish.blockentityrenderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class StackHoldingRenderState extends BlockEntityRenderState {
    public ItemStack heldItem = ItemStack.EMPTY;
    public Direction facing = Direction.NORTH;
    public final ItemStackRenderState item = new ItemStackRenderState();
}
