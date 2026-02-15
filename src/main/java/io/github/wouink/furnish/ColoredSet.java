package io.github.wouink.furnish;

import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.reglib.RegLib;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Arrays;

public class ColoredSet {
    private static final BlockBehaviour.Properties PAPER_LAMP_PROPS = BlockBehaviour.Properties.of().strength(.5f).sound(SoundType.SCAFFOLDING).noOcclusion().lightLevel(state -> 15);

    public Block amphora, awning, sofa, showcase, plate, paperLamp,
            curtain, carpetOnStairs, carpetOnTrapdoor;
    public Block vanillaCarpet;
    public DyeColor dyeColor;

    public ColoredSet(DyeColor dyeColor) {
        this.dyeColor = dyeColor;

        String color = dyeColor.name().toLowerCase();
        Block terracotta = BuiltInRegistries.BLOCK.get(Identifier.withDefaultNamespace(color + "_terracotta")).get().value();
        Block wool = BuiltInRegistries.BLOCK.get(Identifier.withDefaultNamespace(color + "_wool")).get().value();
        vanillaCarpet = BuiltInRegistries.BLOCK.get(Identifier.withDefaultNamespace(color + "_carpet")).get().value();

        amphora = RegLib.registerBlock(color + "_amphora", Amphora::new, BlockBehaviour.Properties.ofFullCopy(terracotta), true);
        FurnishContents.amphorae.add(amphora);

        awning = RegLib.registerBlock(color + "_awning", Awning::new, BlockBehaviour.Properties.ofFullCopy(vanillaCarpet).noOcclusion().strength(.7f), true);
        sofa = RegLib.registerBlock(color + "_sofa", Sofa::new, BlockBehaviour.Properties.ofFullCopy(wool).noOcclusion(), true);

        showcase = RegLib.registerBlock(color + "_showcase", Showcase::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), true);
        FurnishContents.showcases.add(showcase);

        plate = RegLib.registerBlock(color + "_plate", Plate::new, BlockBehaviour.Properties.ofFullCopy(terracotta).strength(.5f), true, new Item.Properties().stacksTo(16));
        FurnishContents.plates.add(plate);

        paperLamp = RegLib.registerBlock(color + "_paper_lamp", PaperLamp::new, PAPER_LAMP_PROPS, true);

        curtain = RegLib.registerBlock(color + "_curtain", Curtain::new, BlockBehaviour.Properties.ofFullCopy(vanillaCarpet).noOcclusion().strength(.4f), true);

        carpetOnStairs = RegLib.registerBlock(color + "_carpet_on_stairs", CarpetOnStairs::new, BlockBehaviour.Properties.ofFullCopy(vanillaCarpet), false);
        ((CarpetOnStairs) carpetOnStairs).setClone(vanillaCarpet);

        carpetOnTrapdoor = RegLib.registerBlock(color + "_carpet_on_trapdoor", CarpetOnTrapdoor::new, BlockBehaviour.Properties.ofFullCopy(vanillaCarpet), false);
        ((CarpetOnTrapdoor) carpetOnTrapdoor).setClone(vanillaCarpet);
    }

    public Block[] getAllBlocks() {
        return new Block[]{amphora, awning, sofa, showcase, plate, paperLamp,
                curtain, carpetOnStairs, carpetOnTrapdoor};
    }

    public Item[] getAllItems() {
        return Arrays.stream(getAllBlocks())
                .filter(block -> block.asItem() != Items.AIR)
                .map(block -> block.asItem()).toList().toArray(new Item[]{});
    }
}
