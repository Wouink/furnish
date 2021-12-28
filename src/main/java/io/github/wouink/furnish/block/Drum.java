package io.github.wouink.furnish.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.RegistryObject;

public class Drum extends Block {
	private final RegistryObject<SoundEvent> instrumentSound;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final IntegerProperty NOTE = IntegerProperty.create("note", 0, 12);

	public Drum(Properties p, final RegistryObject<SoundEvent> instrumentSound) {
		super(p);
		this.instrumentSound = instrumentSound;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(NOTE, POWERED);
	}

	private void playSound(BlockState state, World world, BlockPos pos) {
		float pitch = (float)Math.pow(2.0D, (double)(state.getValue(NOTE).intValue() - 12) / 12.0D);
		world.playSound(null, pos, instrumentSound.get(), SoundCategory.RECORDS, 3.0f, pitch);
	}

	private void playSound(World world, BlockPos pos, float volume, float pitch) {
		world.playSound(null, pos, instrumentSound.get(), SoundCategory.RECORDS, 3.0f * volume, pitch);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result) {
		if(world.isClientSide()) return ActionResultType.SUCCESS;
		else {
			int newNote = ForgeHooks.onNoteChange(world, pos, state, state.getValue(NOTE), state.cycle(NOTE).getValue(NOTE));
			if (newNote == -1) return ActionResultType.FAIL;
			state = state.setValue(NOTE, newNote);
			world.setBlock(pos, state, 3);
			this.playSound(state, world, pos);
			playerEntity.awardStat(Stats.TUNE_NOTEBLOCK);
			return ActionResultType.CONSUME;
		}
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

	@Override
	public void fallOn(World world, BlockPos pos, Entity entity, float distance) {
		float entitySize = entity.getBbWidth() * entity.getBbHeight();
		playSound(world, pos, Math.min(entitySize, 3.0f), distance);
	}

	@Override
	public void updateEntityAfterFallOn(IBlockReader reader, Entity entity) {
		if(entity.isSuppressingBounce()) {
			super.updateEntityAfterFallOn(reader, entity);
		} else {
			bounceUp(entity);
		}
	}

	// copied from BedBlock
	private static void bounceUp(Entity entity) {
		Vector3d vector3d = entity.getDeltaMovement();
		if (vector3d.y < 0.0D) {
			double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
			entity.setDeltaMovement(vector3d.x, -vector3d.y * (double) 0.66F * d0, vector3d.z);
		}
	}
}
