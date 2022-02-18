package io.github.wouink.furnish.block;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.tileentity.PlateTileEntity;
import io.github.wouink.furnish.block.util.ISpecialItemProperties;
import io.github.wouink.furnish.setup.FurnishItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class Plate extends HorizontalDirectionalBlock implements EntityBlock, ISpecialItemProperties {
	public static final VoxelShape PLATE_SHAPE = Block.box(1, 0, 1, 15, 1, 15);
	private static final ResourceLocation WHITELIST = new ResourceLocation(Furnish.MODID, "food");

	public Plate(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return PLATE_SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		InteractionResult resultType = InteractionResult.FAIL;
		if(!world.isClientSide()) {
			if (playerEntity.getItemInHand(hand).isEmpty() || playerEntity.getItemInHand(hand).isEdible() || playerEntity.getItemInHand(hand).getItem().getTags().contains(WHITELIST)) {
				BlockEntity tileEntity = world.getBlockEntity(pos);
				if (tileEntity instanceof PlateTileEntity) {
					playerEntity.setItemInHand(hand, ((PlateTileEntity) tileEntity).swap(playerEntity.getItemInHand(hand)));
					resultType = InteractionResult.SUCCESS;
				}
			}
		}
		return resultType == InteractionResult.SUCCESS ? InteractionResult.sidedSuccess(world.isClientSide()) : resultType;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PlateTileEntity(pos, state);
	}

	@Override
	public Item.Properties getProperties() {
		Rarity rarity = getRegistryName().getPath().startsWith("rare_") ? Rarity.RARE : Rarity.COMMON;
		return new Item.Properties().tab(FurnishItems.Furnish_ItemGroup).stacksTo(16).rarity(rarity);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		BlockEntity tileEntity = world.getBlockEntity(pos);
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
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		super.onRemove(state, world, pos, newState, moving);
		BlockEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof PlateTileEntity) {
			ItemStack stack = ((PlateTileEntity) tileEntity).getHeldItem();
			if(!stack.isEmpty()) {
				world.addFreshEntity(new ItemEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack));
			}
		}
	}
}
