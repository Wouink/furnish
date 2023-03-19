package io.github.wouink.furnish.integration.rei.client;

import io.github.wouink.furnish.integration.rei.FurnishREIPlugin;
import io.github.wouink.furnish.integration.rei.FurnitureRecipeDisplay;
import io.github.wouink.furnish.setup.FurnishBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class FurnitureRecipeCategory implements DisplayCategory<FurnitureRecipeDisplay> {
    @Override
    public CategoryIdentifier<? extends FurnitureRecipeDisplay> getCategoryIdentifier() {
        return FurnishREIPlugin.FURNITURE_MAKING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("furnish.furniture_making");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(FurnishBlocks.Furniture_Workbench.get());
    }

    @Override
    public List<Widget> setupDisplay(FurnitureRecipeDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getInputEntries().get(0)).markInput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }
}
