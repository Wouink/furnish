package io.github.wouink.furnish.event;

import io.github.wouink.furnish.block.Shutter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class OpenShutter {

    public static InteractionResult rightClickOnWindow(Player player, Level level, InteractionHand hand, BlockHitResult blockHitResult) {
        // if(level.isClientSide()) PASS // no filtering here as the filtering is done in Shutter#use
        if(player.isSpectator()) return InteractionResult.PASS;
        Direction dir = blockHitResult.getDirection();
        if(dir == Direction.DOWN || dir == Direction.UP) return InteractionResult.PASS;
        Block maybeWindow = level.getBlockState(blockHitResult.getBlockPos()).getBlock();
        if(!(maybeWindow instanceof IronBarsBlock || maybeWindow instanceof StainedGlassBlock || maybeWindow instanceof FenceBlock)) return InteractionResult.PASS;

        BlockPos shutterPos = blockHitResult.getBlockPos().relative(dir.getOpposite());
        BlockState maybeShutter = level.getBlockState(shutterPos);
        if(!(maybeShutter.getBlock() instanceof Shutter)) return InteractionResult.PASS;
        BlockHitResult hitShutter = new BlockHitResult(shutterPos.getCenter(), dir, shutterPos, true);
        return maybeShutter.useWithoutItem(level, player, hitShutter);
    }
}
