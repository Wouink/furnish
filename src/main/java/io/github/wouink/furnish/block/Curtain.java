package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.block.util.ShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Curtain extends HorizontalDirectionalBlock {
    private static final VoxelShape[] CURTAIN = ShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 1, 16, 16));
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty LEFT = FurnishContents.Properties.LEFT;
    public static final BooleanProperty RIGHT = FurnishContents.Properties.RIGHT;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public Curtain(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Curtain::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, OPEN, POWERED, LEFT, RIGHT, UP, DOWN);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState state = defaultBlockState();
        Level level = blockPlaceContext.getLevel();
        BlockPos pos = blockPlaceContext.getClickedPos();

        // which direction are we facing?
        state = state.setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());

        state = calculateState(state, level, pos);

        return state;
    }

    private static BlockState calculateState(BlockState initialState, LevelAccessor level, BlockPos pos) {
        BlockState state = initialState;
        Direction facing = initialState.getValue(FACING);

        // is there a curtain on the left?
        state = state.setValue(LEFT, level.getBlockState(pos.relative(facing.getClockWise())).getBlock() instanceof Curtain);
        // is there a curtain on the right?
        state = state.setValue(RIGHT, level.getBlockState(pos.relative(facing.getCounterClockWise())).getBlock() instanceof Curtain);
        // is there a curtain above?
        state = state.setValue(UP, level.getBlockState(pos.above()).getBlock() instanceof Curtain);
        // is there a curtain below?
        state = state.setValue(DOWN, level.getBlockState(pos.below()).getBlock() instanceof Curtain);

        return state;
    }

    @Override
    protected BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return calculateState(blockState, levelAccessor, blockPos);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return CURTAIN[blockState.getValue(FACING).ordinal() - 2];
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    private static void setCurtainsInLine(LevelAccessor level, BlockPos pos, boolean open) {
        Furnish.LOGGER.debug("line " + pos + " to " + open);
        Direction facing = level.getBlockState(pos).getValue(FACING);
        BlockPos scan = pos;

        // look left
        while(level.getBlockState(scan).getBlock() instanceof Curtain) {
            setCurtainsInColumn(level, scan, open);
            scan = scan.relative(facing.getClockWise());
        }

        // look right
        scan = pos.relative(facing.getCounterClockWise());
        while(level.getBlockState(scan).getBlock() instanceof Curtain) {
            setCurtainsInColumn(level, scan, open);
            scan = scan.relative(facing.getCounterClockWise());
        }
    }

    private static void setCurtainsInColumn(LevelAccessor level, BlockPos pos, boolean open) {
        Furnish.LOGGER.debug("column " + pos);
        BlockState state;
        BlockPos scan = pos;

        // look up
        while((state = level.getBlockState(scan)).getBlock() instanceof Curtain) {
            level.setBlock(scan, state.setValue(OPEN, open), Block.UPDATE_ALL);
            scan = scan.above();
        }

        // look down
        scan = pos.below();
        while((state = level.getBlockState(scan)).getBlock() instanceof Curtain) {
            level.setBlock(scan, state.setValue(OPEN, open), Block.UPDATE_ALL);
            scan = scan.below();
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if(!level.isClientSide()) {
            setCurtainsInLine(level, blockPos, !blockState.getValue(OPEN));
            level.playSound(null, blockPos, FurnishContents.CURTAIN_TOGGLE, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if(!level.isClientSide()) {
            boolean powered = level.hasNeighborSignal(blockPos);
            if (blockState.getValue(POWERED) != powered) {
                level.setBlock(blockPos, blockState.setValue(POWERED, Boolean.valueOf(powered)).setValue(OPEN, Boolean.valueOf(powered)), Block.UPDATE_CLIENTS);
                setCurtainsInLine(level, blockPos, Boolean.valueOf(powered));
                if (blockState.getValue(OPEN) != powered) {
                    level.playSound(null, blockPos, FurnishContents.CURTAIN_TOGGLE, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
    }
}
