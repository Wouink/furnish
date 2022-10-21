package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SkullTorch extends Block {
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
	protected final ParticleOptions flameParticle;

	public SkullTorch(Properties p, ParticleOptions flame) {
		super(p);
		this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, Integer.valueOf(0)));
		this.flameParticle = flame;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ROTATION);
	}

	public VoxelShape getShape(BlockState p_56331_, BlockGetter p_56332_, BlockPos p_56333_, CollisionContext p_56334_) {
		return SHAPE;
	}

	public VoxelShape getOcclusionShape(BlockState p_56336_, BlockGetter p_56337_, BlockPos p_56338_) {
		return Shapes.empty();
	}

	public BlockState getStateForPlacement(BlockPlaceContext p_56321_) {
		return this.defaultBlockState().setValue(ROTATION, Integer.valueOf(Mth.floor((double)(p_56321_.getRotation() * 16.0F / 360.0F) + 0.5D) & 15));
	}

	public BlockState rotate(BlockState p_56326_, Rotation p_56327_) {
		return p_56326_.setValue(ROTATION, Integer.valueOf(p_56327_.rotate(p_56326_.getValue(ROTATION), 16)));
	}

	public BlockState mirror(BlockState p_56323_, Mirror p_56324_) {
		return p_56323_.setValue(ROTATION, Integer.valueOf(p_56324_.mirror(p_56323_.getValue(ROTATION), 16)));
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource randomSource) {
		double x = pos.getX() + .5d;
		double y = pos.getY() + .25d;
		double z = pos.getZ() + .5d;
		world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
		world.addParticle(this.flameParticle, x, y, z, 0.0D, 0.0D, 0.0D);
	}
}
