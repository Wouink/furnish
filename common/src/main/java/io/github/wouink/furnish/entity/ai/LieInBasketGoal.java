package io.github.wouink.furnish.entity.ai;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.AnimalBasket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.Level;
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
		Furnish.debug("start, move to " + getMoveToTarget());
		pet.setInSittingPose(false);
		super.start();
	}

	@Override
	public void stop() {
		Furnish.debug("stop");
		super.stop();
		// pet.setInSittingPose(false);
	}

	public void tick() {
		super.tick();
		pet.setInSittingPose(isReachedTarget());
	}

	@Override
	protected boolean isValidTarget(LevelReader world, BlockPos pos) {
		// the target is inside the basket
		BlockPos check = pos.above();
		return world.isEmptyBlock(check.above())
				&& world.getBlockState(check).is(BASKETS)
				&& AnimalBasket.isOccupied((Level) world, check);
	}

	public static class CatLieInBasketGoal extends LieInBasketGoal {
		private Cat cat;
		public CatLieInBasketGoal(Cat cat, double speed, int searchRange, int verticalSearchRange) {
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
			cat.setLying(true);
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
