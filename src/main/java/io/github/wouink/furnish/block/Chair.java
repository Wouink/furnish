package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.util.ShapeHelper;
import io.github.wouink.furnish.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Chair extends HorizontalDirectionalBlock {
    public static final VoxelShape[] STOOL = ShapeHelper.getRotatedShapes(Block.box(4, 0, 3, 14, 9, 13));
    public static final VoxelShape[] SMALL_SEAT = ShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 17, 13));
    public static final VoxelShape[] TALL_SEAT = ShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 22, 13));
    public static final VoxelShape[] THRONE_SEAT = ShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 30, 13));

    private VoxelShape[] shapes;

    public Chair(Properties properties) {
        super(properties);
        shapes = STOOL;
    }

    public void setShape(VoxelShape[] shapes) {
        this.shapes = shapes;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Direction dir = blockState.getValue(FACING);
        return shapes[dir.ordinal() - 2];
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Chair::new);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        return SeatEntity.create(level, blockPos, 0.55, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return useWithoutItem(blockState, level, blockPos, player, blockHitResult);
    }
}
