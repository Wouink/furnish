package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.PotionShelfTileEntity;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PotionShelf extends HorizontalBlock {
	private static final VoxelShape[] SHAPES = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 8, 16, 16));
	public static final BooleanProperty TOP_LEFT = BooleanProperty.create("top_left");
	public static final BooleanProperty TOP_RIGHT = BooleanProperty.create("top_right");
	public static final BooleanProperty BOTTOM_LEFT = BooleanProperty.create("bottom_left");
	public static final BooleanProperty BOTTOM_RIGHT = BooleanProperty.create("bottom_right");

	public PotionShelf(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(TOP_LEFT, false).setValue(TOP_RIGHT, false).setValue(BOTTOM_LEFT, false).setValue(BOTTOM_RIGHT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPES[state.getValue(FACING).ordinal() - 2];
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new PotionShelfTileEntity();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		if(world.isClientSide()) return ActionResultType.SUCCESS;
		else {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof PotionShelfTileEntity) {
				playerEntity.openMenu((INamedContainerProvider) tileEntity);
			}
			return ActionResultType.CONSUME;
		}
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof IInventory) {
				InventoryHelper.dropContents(world, pos, (IInventory) tileEntity);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}
}
