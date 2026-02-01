package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.block.util.ShapeHelper;
import io.github.wouink.furnish.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Sofa extends HorizontalDirectionalBlock {

    public enum SofaType implements StringRepresentable {
        ARMCHAIR("armchair"),
        LEFT("left"),
        RIGHT("right"),
        MIDDLE("middle"),
        CORNER_LEFT("corner_left"),
        CORNER_RIGHT("corner_right");

        private final String name;

        private SofaType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    private static final VoxelShape[] SEAT = ShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 4, 16, 16));
    private static final VoxelShape[] SITTING = ShapeHelper.getRotatedShapes(Block.box(4, 0, 0, 13, 6, 16));
    private static final VoxelShape[] REST_L = ShapeHelper.getRotatedShapes(Block.box(4, 0, 0, 15, 10, 3));
    private static final VoxelShape[] REST_R = ShapeHelper.getRotatedShapes(Block.box(4, 0, 13, 15, 10, 16));
    private static final VoxelShape[] SEAT_L = ShapeHelper.getRotatedShapes(Block.box(4, 0, 0, 16, 16, 4));
    private static final VoxelShape[] SEAT_R = ShapeHelper.getRotatedShapes(Block.box(4, 0, 12, 16, 16, 16));

    private static final VoxelShape[] MIDDLE_SHAPE = ShapeHelper.getMergedShapes(SEAT, SITTING);
    private static final VoxelShape[] ARMCHAIR_SHAPE = ShapeHelper.getMergedShapes(SEAT, SITTING, REST_L, REST_R);
    private static final VoxelShape[] RIGHT_SHAPE = ShapeHelper.getMergedShapes(SEAT, SITTING, REST_L);
    private static final VoxelShape[] LEFT_SHAPE = ShapeHelper.getMergedShapes(SEAT, SITTING, REST_R);
    private static final VoxelShape[] RIGHT_CORNER_SHAPE = ShapeHelper.getMergedShapes(SEAT, SITTING, SEAT_L);
    private static final VoxelShape[] LEFT_CORNER_SHAPE = ShapeHelper.getMergedShapes(SEAT, SITTING, SEAT_R);

    public static final EnumProperty<SofaType> SOFA_TYPE = EnumProperty.create("type", SofaType.class);

    public Sofa(Properties properties) {
        super(properties);
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(SOFA_TYPE, SofaType.ARMCHAIR));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Sofa::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, SOFA_TYPE);
    }

    private BlockState calculateState(BlockState state, Direction dir, BlockPos pos, LevelAccessor level) {
        BlockState leftState = level.getBlockState(pos.relative(dir.getCounterClockWise()));
        BlockState rightState = level.getBlockState(pos.relative(dir.getClockWise()));
        boolean left = (leftState.getBlock() instanceof Sofa) &&
                (leftState.getValue(FACING) == state.getValue(FACING) || leftState.getValue(SOFA_TYPE) == SofaType.CORNER_RIGHT);
        boolean right = (rightState.getBlock() instanceof Sofa) &&
                (rightState.getValue(FACING) == state.getValue(FACING) || rightState.getValue(SOFA_TYPE) == SofaType.CORNER_LEFT);

        if(left && right) state = state.setValue(SOFA_TYPE, SofaType.MIDDLE);
        else if(left) {
            BlockState front = level.getBlockState(pos.relative(state.getValue(FACING)));
            if((front.getBlock() instanceof Sofa) && front.getValue(FACING) == state.getValue(FACING).getClockWise()) {
                state = state.setValue(SOFA_TYPE, SofaType.CORNER_RIGHT);
            } else state = state.setValue(SOFA_TYPE, SofaType.RIGHT);
        }
        else if(right) {
            BlockState front = level.getBlockState(pos.relative(state.getValue(FACING)));
            if((front.getBlock() instanceof Sofa)  && front.getValue(FACING) == state.getValue(FACING).getCounterClockWise()) {
                state = state.setValue(SOFA_TYPE, SofaType.CORNER_LEFT);
            } else state = state.setValue(SOFA_TYPE, SofaType.LEFT);
        }
        else state = state.setValue(SOFA_TYPE, SofaType.ARMCHAIR);

        return state;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState state = defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
        return calculateState(state, blockPlaceContext.getHorizontalDirection(), blockPlaceContext.getClickedPos(), blockPlaceContext.getLevel());
    }

    @Override
    protected BlockState updateShape(BlockState blockState, Direction direction, BlockState fromState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos fromPos) {
        return calculateState(blockState, blockState.getValue(FACING).getOpposite(), blockPos, levelAccessor);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        int index = blockState.getValue(FACING).ordinal() - 2;
        VoxelShape ret = ARMCHAIR_SHAPE[index];
        switch(blockState.getValue(SOFA_TYPE).ordinal()) {
            case 1:
                ret = LEFT_SHAPE[index];
                break;
            case 2:
                ret = RIGHT_SHAPE[index];
                break;
            case 3:
                ret = MIDDLE_SHAPE[index];
                break;
            case 4:
                ret = LEFT_CORNER_SHAPE[index];
                break;
            case 5:
                ret = RIGHT_CORNER_SHAPE[index];
                break;
            default:
                break;
        }
        return ret;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        return SeatEntity.create(level, blockPos, 0.45, player);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float dist) {
        super.fallOn(level, blockState, blockPos, entity, dist * .5f);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter blockGetter, Entity entity) {
        if(entity.isSuppressingBounce())
            super.updateEntityAfterFallOn(blockGetter, entity);
        else
            bounceUp(entity);
    }

    // copied from bed
    private static void bounceUp(Entity entity) {
        Vec3 vector3d = entity.getDeltaMovement();
        if (vector3d.y < 0.0D) {
            double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
            entity.setDeltaMovement(vector3d.x, -vector3d.y * (double) 0.66F * d0, vector3d.z);
        }
    }
}
