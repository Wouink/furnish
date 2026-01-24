package io.github.wouink.furnish;

import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.reglib.RegLib;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ColoredSet {
    private static final BlockBehaviour.Properties PAPER_LAMP_PROPS = BlockBehaviour.Properties.of().strength(.5f).sound(SoundType.SCAFFOLDING).noOcclusion().lightLevel(state -> 15);

    public Block amphora, awning, sofa, showcase, plate, paperLamp,
            curtain, carpetOnStairs, carpetOnTrapdoor;
    public DyeColor dyeColor;

    public ColoredSet(DyeColor dyeColor) {
        this.dyeColor = dyeColor;

        String color = dyeColor.name().toLowerCase();
        Block terracotta = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(color + "_terracotta"));
        Block wool = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(color + "_wool"));
        Block carpet = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(color + "_carpet"));

        amphora = RegLib.registerBlock(color + "_amphora", Amphora::new, BlockBehaviour.Properties.ofFullCopy(terracotta), true);
        FurnishContents.amphorae.add(amphora);

        awning = RegLib.registerBlock(color + "_awning", Awning::new, BlockBehaviour.Properties.ofFullCopy(carpet).noOcclusion(), true);
        sofa = RegLib.registerBlock(color + "_sofa", Sofa::new, BlockBehaviour.Properties.ofFullCopy(wool).noOcclusion(), true);

        showcase = RegLib.registerBlock(color + "_showcase", Showcase::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), true);
        FurnishContents.showcases.add(showcase);

        // TODO item properties: stacksTo 16
        plate = RegLib.registerBlock(color + "_plate", Plate::new, BlockBehaviour.Properties.ofFullCopy(terracotta), true);
        FurnishContents.plates.add(plate);

        paperLamp = RegLib.registerBlock(color + "_paper_lamp", PaperLamp::new, PAPER_LAMP_PROPS, true);

        curtain = RegLib.registerBlock(color + "_curtain", Curtain::new, BlockBehaviour.Properties.ofFullCopy(carpet), true);

        carpetOnStairs = RegLib.registerBlock(color + "_carpet_on_stairs", CarpetOnStairs::new, BlockBehaviour.Properties.ofFullCopy(carpet).dropsLike(carpet), false);
        ((CarpetOnStairs) carpetOnStairs).setClone(carpet);

        carpetOnTrapdoor = RegLib.registerBlock(color + "_carpet_on_trapdoor", CarpetOnTrapdoor::new, BlockBehaviour.Properties.ofFullCopy(carpet).dropsLike(carpet), false);
        ((CarpetOnTrapdoor) carpetOnTrapdoor).setClone(carpet);
    }
}
