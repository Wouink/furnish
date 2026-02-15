package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.block.util.PlacementHelper;
import io.github.wouink.furnish.block.util.ShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Shutter extends HorizontalDirectionalBlock {
    private static final VoxelShape[] SHUTTER_CLOSED = ShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 2, 16, 16));
    private static final VoxelShape[] SHUTTER_HALF_OPENED = ShapeHelper.getRotatedShapes(Block.box(0, 0, 14, 16, 16, 16));
    private static final VoxelShape[] SHUTTER_HALF_OPENED_R = ShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 16, 16, 2));
    private static final VoxelShape[] SHUTTER_OPENED = ShapeHelper.getRotatedShapes(Block.box(0, 0, 14, 2, 16, 30));
    private static final VoxelShape[] SHUTTER_OPENED_R = ShapeHelper.getRotatedShapes(Block.box(0, 0, -14, 2, 16, 2));

    private static final VoxelShape[] INTERACT_HALF = ShapeHelper.getMergedShapes(SHUTTER_CLOSED, SHUTTER_HALF_OPENED);
    private static final VoxelShape[] INTERACT_HALF_R = ShapeHelper.getMergedShapes(SHUTTER_CLOSED, SHUTTER_HALF_OPENED_R);
    private static final VoxelShape[] INTERACT_OPEN = ShapeHelper.getMergedShapes(SHUTTER_CLOSED, SHUTTER_OPENED);
    private static final VoxelShape[] INTERACT_OPEN_R = ShapeHelper.getMergedShapes(SHUTTER_CLOSED, SHUTTER_OPENED_R);

    public enum State implements StringRepresentable {
        CLOSED("closed"),
        HALF_OPEN("half_open"),
        OPEN("open");

        private final String name;
        private State(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public static final BooleanProperty RIGHT = FurnishContents.Properties.RIGHT;
    public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);

    public Shutter(Properties properties) {
        super(properties);
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(STATE, State.CLOSED).setValue(RIGHT, false));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Shutter::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, STATE, RIGHT);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState()
                .setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
                .setValue(RIGHT, PlacementHelper.shouldPlaceRight(blockPlaceContext));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if(level.setBlock(blockPos, blockState.cycle(STATE), Block.UPDATE_ALL)) {
            level.playSound(player, blockPos, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS, 1.0f, 1.0f);

            // update shutters in the same column
            boolean rightProp = level.getBlockState(blockPos).getValue(RIGHT).booleanValue();
            BlockPos scan = blockPos.below();
            while(level.getBlockState(scan).getBlock() == this && level.getBlockState(scan).getValue(RIGHT) == rightProp) {
                level.setBlock(scan, level.getBlockState(blockPos), Block.UPDATE_ALL);
                scan = scan.below();
            }
            scan = blockPos.above();
            while(level.getBlockState(scan).getBlock() == this && level.getBlockState(scan).getValue(RIGHT) == rightProp) {
                level.setBlock(scan, level.getBlockState(blockPos), Block.UPDATE_ALL);
                scan = scan.above();
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return useWithoutItem(blockState, level, blockPos, player, blockHitResult);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        int index = blockState.getValue(FACING).ordinal() - 2;
        if(blockState.getValue(STATE) == State.HALF_OPEN) {
            return blockState.getValue(RIGHT) ? INTERACT_HALF_R[index] : INTERACT_HALF[index];
        } else if(blockState.getValue(STATE) == State.OPEN) {
            return blockState.getValue(RIGHT) ? INTERACT_OPEN_R[index] : INTERACT_OPEN[index];
        } else {
            return SHUTTER_CLOSED[index];
        }
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        int index = blockState.getValue(FACING).ordinal() - 2;
        if(blockState.getValue(STATE) == State.HALF_OPEN) {
            return blockState.getValue(RIGHT) ? SHUTTER_HALF_OPENED_R[index] : SHUTTER_HALF_OPENED[index];
        } else if(blockState.getValue(STATE) == State.OPEN) {
            return blockState.getValue(RIGHT) ? SHUTTER_OPENED_R[index] : SHUTTER_OPENED[index];
        } else {
            return SHUTTER_CLOSED[index];
        }
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }
}
