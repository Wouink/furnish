package io.github.wouink.furnish.entity.ai;

import io.github.wouink.furnish.Furnish;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;

import java.util.EnumSet;

public class LieInBasketGoal extends MoveToBlockGoal {
	private static final TagKey<Block> BASKETS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Furnish.MODID, "animal_baskets"));
	private TamableAnimal pet;

	public LieInBasketGoal(TamableAnimal pet, double speed, int searchRange, int verticalSearchRange) {
		super(pet, speed, searchRange, verticalSearchRange);
		this.pet = pet;
		this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return pet.isTame() && !pet.isOrderedToSit() && super.canUse();
	}

	@Override
	public void start() {
		System.out.println("start");
		super.start();
		pet.setInSittingPose(false);
	}

	@Override
	public void stop() {
		System.out.println("stop");
		super.stop();
		pet.setInSittingPose(false);
	}

	public void tick() {
		super.tick();
		pet.setInSittingPose(isReachedTarget());
	}

	@Override
	protected boolean isValidTarget(LevelReader world, BlockPos pos) {
		return world.isEmptyBlock(pos.above()) && world.getBlockState(pos).is(BASKETS);
	}

	public static class CatGoal extends LieInBasketGoal {
		private Cat cat;
		public CatGoal(Cat cat, double speed, int searchRange, int verticalSearchRange) {
			super(cat, speed, searchRange, verticalSearchRange);
			this.cat = cat;
		}

		@Override
		public boolean canUse() {
			return super.canUse() && !cat.isLying();
		}

		@Override
		public void stop() {
			super.stop();
			cat.setLying(false);
		}

		@Override
		public void tick() {
			super.tick();
			this.cat.setInSittingPose(false);
			if (!this.isReachedTarget()) {
				this.cat.setLying(false);
			} else if (!this.cat.isLying()) {
				this.cat.setLying(true);
			}
		}
	}
}
