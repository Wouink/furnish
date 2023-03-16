package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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

import javax.annotation.Nullable;

public class Curtain extends HorizontalDirectionalBlock {
	private static final VoxelShape[] CURTAIN = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 1, 16, 16));
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public Curtain(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, OPEN);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return CURTAIN[state.getValue(FACING).ordinal() - 2];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return Shapes.empty();
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult result) {
		if(!world.isClientSide()) world.setBlockAndUpdate(pos, state.cycle(OPEN));
		world.playSound(playerEntity, pos, FurnishRegistries.Curtain_Sound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
		return InteractionResult.sidedSuccess(world.isClientSide());
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
		return Shapes.empty();
	}
}
