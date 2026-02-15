package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LanternBunting extends Bunting {
    public static final VoxelShape LANTERN = Shapes.or(Block.box(5.0D, 1.0D, 5.0D, 11.0D, 8.0D, 11.0D), Block.box(6.0D, 8.0D, 6.0D, 10.0D, 10.0D, 10.0D));
    public static final VoxelShape X_SHAPE = Shapes.or(LANTERN, BUNTING_X);
    public static final VoxelShape Z_SHAPE = Shapes.or(LANTERN, BUNTING_Z);

    public LanternBunting(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(Z_AXIS) ? Z_SHAPE : X_SHAPE;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        return new ItemStack(this == FurnishContents.SOUL_LANTERN_BUNTING ? Blocks.SOUL_LANTERN : Blocks.LANTERN);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        // prevent from replacing a lantern bunting with another lantern bunting
        return InteractionResult.SUCCESS;
    }
}
