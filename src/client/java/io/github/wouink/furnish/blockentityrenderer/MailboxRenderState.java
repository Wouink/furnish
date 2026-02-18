package io.github.wouink.furnish.blockentityrenderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.network.chat.Component;

public class MailboxRenderState extends BlockEntityRenderState {
    public Component ownerDisplayName;
    public boolean shouldRender;
}
