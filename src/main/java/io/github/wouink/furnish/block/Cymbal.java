package io.github.wouink.furnish.block;

import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class Cymbal extends Block {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final IntegerProperty NOTE = FurnishBlocks.CustomProperties.NOTE;
	public static final BooleanProperty HIHAT = BooleanProperty.create("hihat");

	private static final VoxelShape CYMBAL_SHAPE = VoxelShapes.or(
			Block.box(1, 4, 1, 15, 5, 15),
			Block.box(7, 0, 7, 9, 8, 9)
	);
	private static final VoxelShape HIHAT_SHAPE = VoxelShapes.or(
			Block.box(1, 6, 1, 15, 10, 15),
			Block.box(7, 0, 7, 9, 16, 9)
	);

	public Cymbal(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(HIHAT, false).setValue(POWERED, false).setValue(NOTE, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(NOTE, POWERED, HIHAT);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return VoxelShapes.block();
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return state.getValue(HIHAT).booleanValue() ? HIHAT_SHAPE : CYMBAL_SHAPE;
	}

	private void playSound(BlockState state, World world, BlockPos pos) {
		float pitch = (float)Math.pow(2.0D, (double)(state.getValue(NOTE).intValue() - 12) / 12.0D);
		world.playSound(null, pos, state.getValue(HIHAT).booleanValue() ? FurnishData.Sounds.Cymbal_Hihat.get() : FurnishData.Sounds.Cymbal.get(), SoundCategory.RECORDS, 3.0f, pitch);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result) {
		ActionResultType res = ActionResultType.FAIL;

		if(!world.isClientSide()) {
			ItemStack stack = playerEntity.getItemInHand(hand);
			if(stack.getItem() == this.asItem()) {
				if(!state.getValue(HIHAT).booleanValue()) {
					world.setBlock(pos, state.setValue(HIHAT, true), 3);
					stack.shrink(1);
					playerEntity.setItemInHand(hand, stack);
					res = ActionResultType.SUCCESS;
				} else res = ActionResultType.FAIL;
			} else {
				int newNote = ForgeHooks.onNoteChange(world, pos, state, state.getValue(NOTE), state.cycle(NOTE).getValue(NOTE));
				if (newNote == -1) res = ActionResultType.FAIL;
				else {
					state = state.setValue(NOTE, newNote);
					world.setBlock(pos, state, 3);
					this.playSound(state, world, pos);
					playerEntity.awardStat(Stats.TUNE_NOTEBLOCK);
					res = ActionResultType.SUCCESS;
				}
			}
		}
		return res == ActionResultType.SUCCESS ? ActionResultType.sidedSuccess(world.isClientSide()) : res;
	}

	@Override
	public void attack(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity) {
		if(!world.isClientSide() && !playerEntity.isCreative()) {
			playSound(state, world, pos);
			playerEntity.awardStat(Stats.PLAY_NOTEBLOCK);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighbor, BlockPos neighborPos, boolean moving) {
		boolean flag = world.hasNeighborSignal(pos);
		if (flag != state.getValue(POWERED)) {
			if (flag) playSound(state, world, pos);
			world.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(flag)), 3);
		}
	}
}
