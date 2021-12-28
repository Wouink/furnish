package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.container.CookingPotContainer;
import io.github.wouink.furnish.block.tileentity.PlateTileEntity;
import io.github.wouink.furnish.block.util.ISpecialItemProperties;
import io.github.wouink.furnish.setup.FurnishItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Plate extends HorizontalBlock implements ISpecialItemProperties {
	public static final VoxelShape PLATE_SHAPE = Block.box(1, 0, 1, 15, 1, 15);
	private static final ResourceLocation WHITELIST = CookingPotContainer.COOKING_POT_TAG;

	public Plate(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return PLATE_SHAPE;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result) {
		ActionResultType resultType = ActionResultType.FAIL;
		if(!world.isClientSide()) {
			if (playerEntity.getItemInHand(hand).isEmpty() || playerEntity.getItemInHand(hand).isEdible() || playerEntity.getItemInHand(hand).getItem().getTags().contains(WHITELIST)) {
				TileEntity tileEntity = world.getBlockEntity(pos);
				if (tileEntity instanceof PlateTileEntity) {
					playerEntity.setItemInHand(hand, ((PlateTileEntity) tileEntity).swap(playerEntity.getItemInHand(hand)));
					resultType = ActionResultType.SUCCESS;
				}
			}
		}
		return resultType == ActionResultType.SUCCESS ? ActionResultType.sidedSuccess(world.isClientSide()) : resultType;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new PlateTileEntity();
	}

	@Override
	public Item.Properties getProperties() {
		Rarity rarity = getRegistryName().getPath().startsWith("rare_") ? Rarity.RARE : Rarity.COMMON;
		return new Item.Properties().tab(FurnishItems.Furnish_ItemGroup).stacksTo(16).rarity(rarity);
	}

	@Override
	public ItemStack getCloneItemStack(IBlockReader world, BlockPos pos, BlockState state) {
		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof PlateTileEntity) {
			ItemStack stack = ((PlateTileEntity) tileEntity).getHeldItem().copy();
			if(!stack.isEmpty()) {
				stack.setCount(1);
				return stack;
			}
		}
		return super.getCloneItemStack(world, pos, state);
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving) {
		super.onRemove(state, world, pos, newState, moving);
		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof PlateTileEntity) {
			ItemStack stack = ((PlateTileEntity) tileEntity).getHeldItem();
			if(!stack.isEmpty()) {
				world.addFreshEntity(new ItemEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack));
			}
		}
	}
}
