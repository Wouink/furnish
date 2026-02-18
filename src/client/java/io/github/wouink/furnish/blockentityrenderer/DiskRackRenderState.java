package io.github.wouink.furnish.blockentityrenderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class DiskRackRenderState extends BlockEntityRenderState {
    public ItemStack[] disks = new ItemStack[8];
    public Direction facing = Direction.NORTH;
}
