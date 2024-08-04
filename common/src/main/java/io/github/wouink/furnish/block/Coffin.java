package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.InteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Coffin extends BedBlock {
	public Coffin(Properties p) {
		super(DyeColor.BLACK, p.noOcclusion());
	}

	@Override
	public void fallOn(Level p_152169_, BlockState p_152170_, BlockPos p_152171_, Entity p_152172_, float p_152173_) {
		Blocks.OAK_PLANKS.fallOn(p_152169_, p_152170_, p_152171_, p_152172_, p_152173_);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		if(player.isCrouching()) {
			if (level.isClientSide()) return InteractionResult.CONSUME;
			boolean occupied = blockState.getValue(OCCUPIED);
			setOccupied(level, blockPos, !occupied);
			if (occupied) level.playSound(null, blockPos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 1.0f, 1.0f);
			else level.playSound(null, blockPos, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 1.0f, 1.0f);
			return InteractionResult.SUCCESS;
		} else return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
	}

	public void setOccupied(Level level, BlockPos pos, boolean occupied) {
		BlockState state = level.getBlockState(pos);
		if(state.is(this)) {
			BlockPos otherPart = pos.relative(state.getValue(PART) == BedPart.FOOT ? state.getValue(FACING) : state.getValue(FACING).getOpposite());
			BlockState otherState = level.getBlockState(otherPart);
			level.setBlockAndUpdate(pos, state.setValue(OCCUPIED, occupied));
			level.setBlockAndUpdate(otherPart, otherState.setValue(OCCUPIED, occupied));
		}
	}

	@Override
	public VoxelShape getShape(BlockState p_49547_, BlockGetter p_49548_, BlockPos p_49549_, CollisionContext p_49550_) {
		return Shapes.block();
	}

	@Override
	public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
		return Shapes.block();
	}

	@Override
	public RenderShape getRenderShape(BlockState p_49545_) {
		return RenderShape.MODEL;
	}
}
