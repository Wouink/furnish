package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Awning extends HorizontalDirectionalBlock {
    public static final VoxelShape AWNING_SHAPE = Block.box(0, 0, 0, 16, 2, 16);
    public static BooleanProperty LEFT = FurnishContents.Properties.LEFT;
    public static BooleanProperty RIGHT = FurnishContents.Properties.RIGHT;

    public Awning(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, LEFT, RIGHT);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction dir = blockPlaceContext.getHorizontalDirection();
        Level world = blockPlaceContext.getLevel();
        BlockState state = this.defaultBlockState().setValue(FACING, dir.getOpposite());

        // right awning check
        BlockState scanState = world.getBlockState(blockPlaceContext.getClickedPos().relative(dir.getClockWise()));
        if(scanState.is(this) && scanState.getValue(FACING) == state.getValue(FACING)) {
            state = state.setValue(RIGHT, true);
        }

        // left awning check
        scanState = world.getBlockState(blockPlaceContext.getClickedPos().relative(dir.getCounterClockWise()));
        if(scanState.is(this) && scanState.getValue(FACING) == state.getValue(FACING)) {
            state = state.setValue(LEFT, true);
        }

        return state;
    }

    @Override
    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos fromPos, BlockState fromState, RandomSource randomSource) {
        if(!direction.getAxis().isVertical()) {
            // left awning check
            BlockState scan = levelReader.getBlockState(blockPos.relative(blockState.getValue(FACING).getClockWise()));
            blockState = blockState.setValue(LEFT, (scan.is(this) && scan.getValue(FACING) == blockState.getValue(FACING)));

            // right awning check
            scan = levelReader.getBlockState(blockPos.relative(blockState.getValue(FACING).getCounterClockWise()));
            blockState = blockState.setValue(RIGHT, (scan.is(this) && scan.getValue(FACING) == blockState.getValue(FACING)));
        }
        return blockState;
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return AWNING_SHAPE;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Awning::new);
    }

    // TODO bouncing ok?
    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, double dist) {
        super.fallOn(level, blockState, blockPos, entity, dist * .5);
    }

    @Override
    public void updateEntityMovementAfterFallOn(BlockGetter blockGetter, Entity entity) {
        if(entity.isSuppressingBounce())
            super.updateEntityMovementAfterFallOn(blockGetter, entity);
        else
            bounceUp(entity);
    }

    // copied from bed
    private static void bounceUp(Entity entity) {
        Furnish.LOGGER.debug("Awning bounceUp");
        Vec3 vector3d = entity.getDeltaMovement();
        if (vector3d.y < 0.0D) {
            double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
            entity.setDeltaMovement(vector3d.x, -vector3d.y * (double) 0.66F * d0, vector3d.z);
        }
    }
}
