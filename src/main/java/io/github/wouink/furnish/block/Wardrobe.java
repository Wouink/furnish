package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.PlacementHelper;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.registries.RegistryObject;

public class Wardrobe extends TallInventoryFurniture {
	public static final BooleanProperty RIGHT = FurnishBlocks.CustomProperties.RIGHT;
	public Wardrobe(BlockBehaviour.Properties p, final RegistryObject<SoundEvent> openSound, final RegistryObject<SoundEvent> closeSound) {
		super(p, openSound, closeSound);
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TOP, false).setValue(RIGHT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(RIGHT);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = super.getStateForPlacement(ctx);
		return state != null ? state.setValue(RIGHT, PlacementHelper.placeRight(ctx)) : null;
	}
}
