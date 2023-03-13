package io.github.wouink.furnish.entity;

import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SeatEntity extends Entity {
	private BlockPos seatBlock;

	public SeatEntity(Level world) {
		super(FurnishData.Entities.Seat_Entity.get(), world);
		this.noPhysics = true;
	}

	private SeatEntity(Level world, BlockPos pos, double yOffset) {
		this(world);
		this.seatBlock = pos;
		this.setPos(seatBlock.getX() + 0.5, seatBlock.getY() + yOffset, seatBlock.getZ() + 0.5);
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public void tick() {
		super.tick();
		if(seatBlock == null) {
			seatBlock = blockPosition();
		}
		if(!level.isClientSide()) {
			if(getPassengers().isEmpty() || level.isEmptyBlock(seatBlock)) {
				remove(RemovalReason.DISCARDED);
			}
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {

	}

	@Override
	protected boolean canRide(Entity e) {
		return true;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return null;
	}

	@Override
	public double getMyRidingOffset() {
		return 0.0;
	}

	public static InteractionResult create(Level world, BlockPos pos, double yOffset, Player playerEntity) {
		if(!world.isClientSide()) {
			List<SeatEntity> seatsInThisBlock = world.getEntitiesOfClass(SeatEntity.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));
			if(seatsInThisBlock.isEmpty()) {
				SeatEntity seat = new SeatEntity(world, pos, yOffset);
				world.addFreshEntity(seat);
				playerEntity.startRiding(seat);
			}
		}
		return InteractionResult.SUCCESS;
	}
}
