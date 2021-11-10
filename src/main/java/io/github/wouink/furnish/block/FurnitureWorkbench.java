package io.github.wouink.furnish.block;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.container.FurnitureWorkbenchContainer;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FurnitureWorkbench extends HorizontalBlock {
	private static final TranslationTextComponent Container_Name = new TranslationTextComponent("container.furniture_workbench");

	public FurnitureWorkbench() {
		super(AbstractBlock.Properties.of(Material.WOOD).strength(1.0f).sound(SoundType.WOOD).noOcclusion());
		setRegistryName(Furnish.MODID, "furniture_workbench");
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(worldIn.isClientSide()) return ActionResultType.SUCCESS;
		else {
			player.openMenu(state.getMenuProvider(worldIn, pos));
			player.awardStat(Stats.INTERACT_WITH_STONECUTTER);
			return ActionResultType.CONSUME;
		}
	}

	@Nullable
	@Override
	public INamedContainerProvider getMenuProvider(BlockState state, World worldIn, BlockPos pos) {
		return new SimpleNamedContainerProvider((winId, playerIn, idk) -> new FurnitureWorkbenchContainer(winId, playerIn, IWorldPosCallable.create(worldIn, pos)), Container_Name);
	}
}
