package io.github.wouink.furnish.blockentityrenderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.item.ItemStack;

public class RecycleBinRenderState extends BlockEntityRenderState {
    public ItemStack[] items = new ItemStack[9];
}
