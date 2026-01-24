package io.github.wouink.furnish.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Callback for placing a block.
 * Weirdly enough, Fabric API does not provide an event for this purpose.
 * https://wiki.fabricmc.net/tutorial:event_index
 *
 * https://docs.fabricmc.net/develop/events#custom-events
 * This requires a mixin to function, see MixinBlockItem.
 */
public interface PlaceBlockCallback {
    Event<PlaceBlockCallback> EVENT = EventFactory.createArrayBacked(PlaceBlockCallback.class,
        listeners -> (level, blockPos, blockState, entity) -> {
            for(PlaceBlockCallback listener : listeners) {
                InteractionResult result = listener.onBlockPlaced(level, blockPos, blockState, entity);
                if(result != InteractionResult.PASS) return result;
            }
            return InteractionResult.PASS;
    });

    /**
     * Triggered when a block is placed in the world.
     * @param level the world
     * @param blockPos the position
     * @param blockState the block that was just place
     * @param entity the entity that placed this block
     * @return the result
     */
    InteractionResult onBlockPlaced(LevelAccessor level, BlockPos blockPos, BlockState blockState, Entity entity);
}
