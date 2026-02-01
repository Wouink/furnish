package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Bunting extends Block {
    public static final BooleanProperty Z_AXIS = BooleanProperty.create("z_axis");
    public static final VoxelShape BUNTING_X = Block.box(0, 7, 7, 16, 12, 9);
    public static final VoxelShape BUNTING_Z = Block.box(7, 7, 0, 9, 12, 16);

    public Bunting(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(Z_AXIS);
    }

    // place the bunting in the continuation of its neighbor, unless sneaking
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        if(!blockPlaceContext.getPlayer().isShiftKeyDown()) {
            BlockState neighbor = blockPlaceContext.getLevel().getBlockState(
                    blockPlaceContext.getClickedPos().relative(blockPlaceContext.getClickedFace().getOpposite()));
            if (neighbor.getBlock() instanceof Bunting)
                return this.defaultBlockState().setValue(Z_AXIS, neighbor.getValue(Z_AXIS));
        }
        return this.defaultBlockState()
                   .setValue(Z_AXIS, blockPlaceContext.getHorizontalDirection().getAxis() == Direction.Axis.Z);

    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(Z_AXIS).booleanValue() ? BUNTING_Z : BUNTING_X;
    }

    // transform bunting into a lantern bunting on right click with a lantern
    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return ItemInteractionResult.SUCCESS;
        if(itemStack.isEmpty()) return ItemInteractionResult.FAIL;

        Item item = itemStack.getItem();
        Block block;
        if(item == Items.SOUL_LANTERN) block = FurnishContents.SOUL_LANTERN_BUNTING;
        else if(item == Items.LANTERN) block = FurnishContents.LANTERN_BUNTING;
        else return ItemInteractionResult.FAIL;

        level.destroyBlock(blockPos, true);
        level.setBlock(blockPos, block.defaultBlockState().setValue(Bunting.Z_AXIS, blockState.getValue(Z_AXIS)), Block.UPDATE_ALL);
        if(!player.isCreative()) itemStack.shrink(1);
        player.setItemInHand(interactionHand, itemStack);

        return ItemInteractionResult.SUCCESS;
    }
}
