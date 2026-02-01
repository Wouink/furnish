package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Table extends Block {
    public static final BooleanProperty NW = BooleanProperty.create("nw");
    public static final BooleanProperty NE = BooleanProperty.create("ne");
    public static final BooleanProperty SW = BooleanProperty.create("sw");
    public static final BooleanProperty SE = BooleanProperty.create("se");

    protected static final VoxelShape BASE_SHAPE = Block.box(0d, 14d, 0d, 16d, 16d, 16d);
    protected static final VoxelShape NW_SHAPE = Block.box(1d, 0d, 1d, 4d, 14d, 4d);
    protected static final VoxelShape NE_SHAPE = Block.box(12d, 0d, 1d, 15d, 14d, 4d);
    protected static final VoxelShape SW_SHAPE = Block.box(1d, 0d, 12d, 4d, 14d, 15d);
    protected static final VoxelShape SE_SHAPE = Block.box(12d, 0d, 12d, 15d, 14d, 15d);

    public Table(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NW, NE, SW, SE);
    }

    private BlockState calculateState(LevelAccessor level, BlockPos pos) {
        BlockState N = level.getBlockState(pos.north());
        BlockState W = level.getBlockState(pos.west());
        BlockState S = level.getBlockState(pos.south());
        BlockState E = level.getBlockState(pos.east());
        boolean n = N.getBlock() instanceof Table;
        boolean w = W.getBlock() instanceof Table;
        boolean s = S.getBlock() instanceof Table;
        boolean e = E.getBlock() instanceof Table;

        return getStateDefinition().any()
                .setValue(NE, !n && !e)
                .setValue(NW, !n && !w)
                .setValue(SE, !s && !e)
                .setValue(SW, !s && !w);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return calculateState(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos());
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.or(BASE_SHAPE,
                blockState.getValue(NE).booleanValue() ? NE_SHAPE : Shapes.empty(),
                blockState.getValue(NW).booleanValue() ? NW_SHAPE : Shapes.empty(),
                blockState.getValue(SE).booleanValue() ? SE_SHAPE : Shapes.empty(),
                blockState.getValue(SW).booleanValue() ? SW_SHAPE : Shapes.empty()
        );
    }

    @Override
    protected BlockState updateShape(BlockState blockState, Direction direction, BlockState fromState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos fromPos) {
        return calculateState(levelAccessor, blockPos);
    }
}
