package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.util.ShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
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

public class Ladder extends HorizontalDirectionalBlock {
    private static final VoxelShape[] SHAPES = ShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 3, 16, 16));

    public Ladder(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Ladder::new);
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
        return SHAPES[blockState.getValue(FACING).ordinal() - 2];
    }

    // extend the ladder when right-clicked with another ladder
    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.SUCCESS;

        if(!(itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof Ladder))
            return InteractionResult.FAIL;

        // first option = place down
        BlockPos search = blockPos.below();
        while(level.getBlockState(search).getBlock() instanceof Ladder) search = search.below();
        if(!level.isEmptyBlock(search)) {
            // second option = place up
            search = blockPos.above();
            while(level.getBlockState(search).getBlock() instanceof Ladder) search = search.above();
        }

        if(level.isEmptyBlock(search)) {
            level.setBlockAndUpdate(
                    search,
                    blockItem.getBlock().defaultBlockState().setValue(FACING, blockState.getValue(FACING))
            );
            level.playSound(null, search,
                    this.getSoundType(this.defaultBlockState()).getPlaceSound(),
                    SoundSource.BLOCKS, 1.0f, 1.0f
            );
            if(!player.isCreative()) {
                itemStack.shrink(1);
                player.setItemInHand(interactionHand, itemStack);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
