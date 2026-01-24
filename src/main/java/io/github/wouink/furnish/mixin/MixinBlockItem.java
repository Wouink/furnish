package io.github.wouink.furnish.mixin;

import io.github.wouink.furnish.event.PlaceBlockCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This is based on Architectury API's MixinBlockItem for BlockEvent.PLACE
 */

@Mixin(BlockItem.class)
public class MixinBlockItem {

    // call this method when calling placeBlock method of BlockItem class
    @Inject(method = "placeBlock", at = @At("HEAD"))
    private void place(BlockPlaceContext blockPlaceContext, BlockState blockState, CallbackInfoReturnable<Boolean> info) {
        InteractionResult result = PlaceBlockCallback.EVENT.invoker().onBlockPlaced(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos(), blockState, blockPlaceContext.getPlayer());
        // An attempt to get PlaceCarpet.onCarpetPlaced take priority over default/vanilla behaviour.
        // This "works", but: org.spongepowered.asm.mixin.injection.callback.CancellationException: The call placeBlock is not cancellable.
        if(result == InteractionResult.SUCCESS) info.cancel();
    }
}
