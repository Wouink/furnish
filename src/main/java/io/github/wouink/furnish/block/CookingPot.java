package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishManager;
import io.github.wouink.furnish.block.tileentity.CookingPotTileEntity;
import io.github.wouink.furnish.item.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CookingPot extends Block {
	private static final VoxelShape POT = Block.box(3, 0, 2, 13, 10, 14);
	private static final VoxelShape POT_Z = Block.box(2, 0, 3, 14, 10, 13);

	public static final BooleanProperty Z_AXIS = BooleanProperty.create("z_axis");
	public CookingPot(Properties p, String registryName) {
		super(p.noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(Z_AXIS, false));
		FurnishManager.ModBlocks.register(registryName, this);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(Z_AXIS);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.defaultBlockState().setValue(Z_AXIS, ctx.getHorizontalDirection().getAxis() == Direction.Axis.Z);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return state.getValue(Z_AXIS).booleanValue() ? POT_Z : POT;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CookingPotTileEntity();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result) {
		if(world.isClientSide()) return ActionResultType.SUCCESS;
		else if(playerEntity.isSpectator()) return ActionResultType.CONSUME;
		else {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof CookingPotTileEntity) {
				playerEntity.openMenu((CookingPotTileEntity) tileEntity);
				return ActionResultType.CONSUME;
			} else return ActionResultType.PASS;
		}
	}

	@Override
	public void attack(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity) {
		// TODO sometimes the function is called twice...
		if(world.isClientSide()) return;
		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof CookingPotTileEntity) {
			boolean popped = false;
			if(playerEntity.getItemInHand(Hand.MAIN_HAND).isEdible()) {
				ItemStack result = ((CookingPotTileEntity) tileEntity).popSimilar(playerEntity.getItemInHand(Hand.MAIN_HAND));
				if(result.getCount() != playerEntity.getItemInHand(Hand.MAIN_HAND).getCount()) {
					playerEntity.setItemInHand(Hand.MAIN_HAND, result);
					popped = true;
				}
			} else if(playerEntity.getItemInHand(Hand.MAIN_HAND).isEmpty()) {
				ItemStack result = ((CookingPotTileEntity) tileEntity).pop();
				if(!result.isEmpty()) {
					playerEntity.setItemInHand(Hand.MAIN_HAND, result);
					popped = true;
				}
			}
			if(popped) world.playSound(playerEntity, pos, SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);
		TooltipHelper.appendInventoryContent(stack, tooltip);
	}

	@Override
	public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity playerEntity) {
		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof CookingPotTileEntity) {
			CookingPotTileEntity pot = (CookingPotTileEntity) tileEntity;
			if(!world.isClientSide() && playerEntity.isCreative() && !pot.isEmpty()) {
				ItemStack stack = new ItemStack(this);
				CompoundNBT nbt = pot.save(new CompoundNBT());
				if(!nbt.isEmpty()) stack.addTagElement("BlockEntityTag", nbt);
				if(pot.hasCustomName()) stack.setHoverName(pot.getCustomName());
				ItemEntity item = new ItemEntity(world, (double)pos.getX() + .5, (double)pos.getZ() + .5, (double)pos.getZ() + .5, stack);
				item.setDefaultPickUpDelay();
				world.addFreshEntity(item);
			} else {
				pot.unpackLootTable(playerEntity);
			}
		}
		super.playerWillDestroy(world, pos, state, playerEntity);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		TileEntity tileEntity = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
		if(tileEntity instanceof CookingPotTileEntity) {
			CookingPotTileEntity pot = (CookingPotTileEntity) tileEntity;
			builder = builder.withDynamicDrop(ShulkerBoxBlock.CONTENTS, (lootCtx, stackConsumer) -> {
				for(int i = 0; i < pot.getContainerSize(); i++) {
					stackConsumer.accept(pot.getItem(i));
				}
			});
		}
		return super.getDrops(state, builder);
	}
}
