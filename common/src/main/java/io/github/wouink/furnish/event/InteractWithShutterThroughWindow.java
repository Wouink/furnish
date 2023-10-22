package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.Furnish;
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
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class InteractWithShutterThroughWindow {

    public static EventResult onRightClickWindow(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        Level level = player.level();
        if(level.isClientSide()) return EventResult.pass();
        Furnish.debug("Right click on block");
        if(face == Direction.DOWN || face == Direction.UP) return EventResult.pass();
        Furnish.debug("-> correct face");
        Block potentiallyWindow = level.getBlockState(pos).getBlock();
        if(potentiallyWindow instanceof IronBarsBlock || potentiallyWindow instanceof StainedGlassPaneBlock || potentiallyWindow instanceof FenceBlock) {
            Furnish.debug("-> it's a glass pane or a fence");
            BlockPos afterWindow = pos.relative(face.getOpposite());
            BlockState potentiallyShutter = level.getBlockState(afterWindow);
            if (potentiallyShutter.getBlock() instanceof Shutter shutter) {
                Furnish.debug("-> it has a shutter attached");
                BlockHitResult blockHitResult = new BlockHitResult(afterWindow.getCenter(), face, afterWindow, true);
                InteractionResult res = shutter.use(potentiallyShutter, level, afterWindow, player, hand, blockHitResult);
                if(res == InteractionResult.SUCCESS || res == InteractionResult.CONSUME) {
                    Furnish.debug("-> interaction with the shutter went ok");
                    return EventResult.interruptTrue();
                }
                Furnish.debug("-> interacted with the shutter went wrong");
                return EventResult.pass();
            }
        }
        return EventResult.pass();
    }
}
