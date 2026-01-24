package io.github.wouink.furnish.event;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

/**
 * This is the hard way of handling the placement of carpet on stairs/trapdoor, and it doesn't even work!
 * The hard way because Fabric does not have an event/callback for placing a block, so I had to create mine.
 * And it doesn't work because:
 *  1. Unless the mixin cancels the code that runs next, the carpet placed in `onCarpetPlaced` is immediately replaced
 *     by the vanilla carped (default behaviour of the BlockItem class).
 *  2. The mixin cannot cleanly cancel the default behaviour: org.spongepowered.asm.mixin.injection.callback.CancellationException: The call placeBlock is not cancellable.
 *
 *  I'm pushing these changes anyway for documentation purposes.
 */
public class PlaceCarpet {

    public static InteractionResult onCarpetPlaced(LevelAccessor level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if(level.isClientSide()) return InteractionResult.PASS;
        if(blockState.getBlock() instanceof WoolCarpetBlock carpetBlock) {
            if(BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).getNamespace().equals("minecraft")) {
                DyeColor color = carpetBlock.getColor();
                BlockState stateBelow = level.getBlockState(blockPos.below());
                Furnish.LOGGER.debug("block is " + blockState);
                Furnish.LOGGER.debug("placeable on stairs = " + blockState.is(FurnishContents.PLACE_ON_STAIRS));
                Furnish.LOGGER.debug("placeable on trapdoor = " + blockState.is(FurnishContents.PLACE_ON_TRAPDOOR));

                if(stateBelow.getBlock() instanceof StairBlock && blockState.is(FurnishContents.PLACE_ON_STAIRS) && !entity.isShiftKeyDown()) {
                    if(stateBelow.getValue(StairBlock.HALF) == Half.BOTTOM && stateBelow.getValue(StairBlock.SHAPE) == StairsShape.STRAIGHT) {
                        BlockState toPlace = FurnishContents.COLORED_SETS.get(color).carpetOnStairs.defaultBlockState();
                        toPlace.setValue(BlockStateProperties.HORIZONTAL_FACING, stateBelow.getValue(BlockStateProperties.HORIZONTAL_FACING));
                        Furnish.LOGGER.debug("toPlace = " + toPlace);
                        level.setBlock(blockPos, toPlace, Block.UPDATE_ALL);
                        System.out.println("placed carpet on stairs");
                        return InteractionResult.SUCCESS;
                    }
                } else if(stateBelow.getBlock() instanceof TrapDoorBlock && blockState.is(FurnishContents.PLACE_ON_TRAPDOOR)) {
                    BlockState toPlace = FurnishContents.COLORED_SETS.get(color).carpetOnTrapdoor.defaultBlockState();
                    toPlace = toPlace.setValue(BlockStateProperties.HORIZONTAL_FACING, stateBelow.getValue(BlockStateProperties.HORIZONTAL_FACING)).setValue(BlockStateProperties.OPEN, stateBelow.getValue(BlockStateProperties.OPEN));
                    Furnish.LOGGER.debug("toPlace = " + toPlace);
                    level.setBlock(blockPos, toPlace, Block.UPDATE_ALL);
                    System.out.println("placed carpet on trapdoor");
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
