package io.github.wouink.furnish.block;

import com.mojang.math.Vector3d;
import io.github.wouink.furnish.block.tileentity.FlowerPotTileEntity;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FlowerPot extends Block implements EntityBlock {
	public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
	public static final VoxelShape COLLISION = Block.box(4, 0, 4, 12, 6, 12);
	public static final VoxelShape HANGING_SHAPE = Shapes.or(
			COLLISION,
			Block.box(4, 6, 7, 12, 16, 9)
	);
	public final int plants;
	public FlowerPot(Properties properties, int plants) {
		super(properties);
		this.plants = plants;
		FurnishBlocks.Flower_Pots.add(this);
	}

	public int getPlants() {
		return plants;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(HANGING);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		if(state.getValue(HANGING).booleanValue()) return HANGING_SHAPE;
		else return COLLISION;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(HANGING, ctx.getClickedFace() == Direction.DOWN);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FlowerPotTileEntity(pos, state);
	}

	public Vector3d getRenderPos(int plantIndex) {
		return new Vector3d(0.5, 0.5, 0.5);
	}
}
