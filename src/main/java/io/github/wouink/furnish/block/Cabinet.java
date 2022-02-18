package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.PlacementHelper;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.registries.RegistryObject;

public class Cabinet extends InventoryFurniture {
	public static final BooleanProperty RIGHT = FurnishBlocks.CustomProperties.RIGHT;
	public Cabinet(Properties p, final RegistryObject<SoundEvent> openSound, final RegistryObject<SoundEvent> closeSound) {
		super(p, openSound, closeSound);
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(RIGHT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(RIGHT);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx).setValue(RIGHT, PlacementHelper.placeRight(ctx));
	}
}
