package io.github.wouink.furnish.block.util;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;

public class InteractionHelper {

    public static ItemInteractionResult toItem(InteractionResult result) {
        ItemInteractionResult res = ItemInteractionResult.FAIL;
        switch(result) {
            case CONSUME -> res = ItemInteractionResult.CONSUME;
            case CONSUME_PARTIAL -> res = ItemInteractionResult.CONSUME_PARTIAL;
            case PASS -> res = ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            case SUCCESS, SUCCESS_NO_ITEM_USED -> res = ItemInteractionResult.SUCCESS;
            case FAIL -> res = ItemInteractionResult.FAIL;
        }
        return res;
    }

    public static InteractionResult toResult(ItemInteractionResult result) {
        InteractionResult res = InteractionResult.FAIL;
        switch(result) {
            case CONSUME -> res = InteractionResult.CONSUME;
            case CONSUME_PARTIAL -> res = InteractionResult.CONSUME_PARTIAL;
            case PASS_TO_DEFAULT_BLOCK_INTERACTION -> res = InteractionResult.PASS;
            case SUCCESS -> res = InteractionResult.SUCCESS;
            case FAIL, SKIP_DEFAULT_BLOCK_INTERACTION -> res = InteractionResult.FAIL;
        }
        return res;
    }
}
