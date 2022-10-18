package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.GravestoneTileEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class Gravestone extends VerticalSlab implements EntityBlock {
	public Gravestone(Properties p) {
		super(p);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GravestoneTileEntity(pos, state);
	}

	public InteractionResult use(BlockState p_56278_, Level p_56279_, BlockPos p_56280_, Player p_56281_, InteractionHand p_56282_, BlockHitResult p_56283_) {
		ItemStack itemstack = p_56281_.getItemInHand(p_56282_);
		Item item = itemstack.getItem();
		boolean flag = item instanceof DyeItem;
		boolean flag1 = itemstack.is(Items.GLOW_INK_SAC);
		boolean flag2 = itemstack.is(Items.INK_SAC);
		boolean flag3 = (flag1 || flag || flag2) && p_56281_.getAbilities().mayBuild;
		if (p_56279_.isClientSide) {
			return flag3 ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
		} else {
			BlockEntity blockentity = p_56279_.getBlockEntity(p_56280_);
			if (!(blockentity instanceof GravestoneTileEntity)) {
				return InteractionResult.PASS;
			} else {
				GravestoneTileEntity signblockentity = (GravestoneTileEntity) blockentity;
				boolean flag4 = signblockentity.hasGlowingText();
				if ((!flag1 || !flag4) && (!flag2 || flag4)) {
					if (flag3) {
						boolean flag5;
						if (flag1) {
							p_56279_.playSound((Player)null, p_56280_, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
							flag5 = signblockentity.setHasGlowingText(true);
							if (p_56281_ instanceof ServerPlayer) {
								CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)p_56281_, p_56280_, itemstack);
							}
						} else if (flag2) {
							p_56279_.playSound((Player)null, p_56280_, SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
							flag5 = signblockentity.setHasGlowingText(false);
						} else {
							p_56279_.playSound((Player)null, p_56280_, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
							flag5 = signblockentity.setColor(((DyeItem)item).getDyeColor());
						}

						if (flag5) {
							if (!p_56281_.isCreative()) {
								itemstack.shrink(1);
							}

							p_56281_.awardStat(Stats.ITEM_USED.get(item));
						}
					}

					return signblockentity.executeClickCommands((ServerPlayer)p_56281_) ? InteractionResult.SUCCESS : InteractionResult.PASS;
				} else {
					return InteractionResult.PASS;
				}
			}
		}
	}
}
