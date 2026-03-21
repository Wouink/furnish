package io.github.wouink.furnish.blockentityrenderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

public class RecycleBinRenderState extends BlockEntityRenderState {
    public ItemStackRenderState[] items = new ItemStackRenderState[9];

    public RecycleBinRenderState() {
        for(int i = 0; i < 9; i++) items[i] = new ItemStackRenderState();
    }
}
