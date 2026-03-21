package io.github.wouink.furnish.blockentityrenderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class DiskRackRenderState extends BlockEntityRenderState {
    public ItemStackRenderState[] disks = new ItemStackRenderState[8];
    public Direction facing = Direction.NORTH;

    public DiskRackRenderState() {
        for(int i = 0; i < 8; i++) disks[i] = new ItemStackRenderState();
    }
}
