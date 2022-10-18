package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
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
