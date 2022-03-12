package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.INoBlockItem;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LanternBunting extends Bunting implements INoBlockItem {
	// HANGING_AABB copied from LanternBlock
	public static final VoxelShape HANGING_AABB = Shapes.or(Block.box(5.0D, 1.0D, 5.0D, 11.0D, 8.0D, 11.0D), Block.box(6.0D, 8.0D, 6.0D, 10.0D, 10.0D, 10.0D));
	public static final VoxelShape X_SHAPE = Shapes.or(HANGING_AABB, BUNTING_X);
	public static final VoxelShape Z_SHAPE = Shapes.or(HANGING_AABB, BUNTING_Z);

	public LanternBunting(Properties p) {
		super(p);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
		return state.getValue(Z_AXIS) ? Z_SHAPE : X_SHAPE;
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
		return new ItemStack(this == FurnishBlocks.Soul_Lantern_Bunting.get() ? Blocks.SOUL_LANTERN : Blocks.LANTERN);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		// prevent super.use() from replacing a LanternBunting with another LanternBunting
		return InteractionResult.FAIL;
	}
}
