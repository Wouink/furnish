package io.github.wouink.furnish.entity;

import io.github.wouink.furnish.FurnishManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class SeatEntity extends Entity {
	private BlockPos seatBlock;

	public SeatEntity(World world) {
		super(FurnishManager.Entities.Seat_Entity.get(), world);
		this.noPhysics = true;
	}

	private SeatEntity(World world, BlockPos pos, double yOffset) {
		this(world);
		this.seatBlock = pos;
		this.setPos(seatBlock.getX() + 0.5, seatBlock.getY() + yOffset, seatBlock.getZ() + 0.5);
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT nbt) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt) {

	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick() {
		super.tick();
		if(seatBlock == null) {
			seatBlock = blockPosition();
		}
		if(!level.isClientSide()) {
			if(getPassengers().isEmpty() || level.isEmptyBlock(seatBlock)) {
				System.out.println("Removed entity");
				remove();
			}
		}
	}

	@Override
	protected boolean canRide(Entity e) {
		return true;
	}

	@Override
	public double getMyRidingOffset() {
		return 0.0;
	}

	public static ActionResultType create(World world, BlockPos pos, double yOffset, PlayerEntity playerEntity) {
		if(!world.isClientSide()) {
			List<SeatEntity> seatsInThisBlock = world.getEntitiesOfClass(SeatEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));
			if(seatsInThisBlock.isEmpty()) {
				SeatEntity seat = new SeatEntity(world, pos, yOffset);
				world.addFreshEntity(seat);
				playerEntity.startRiding(seat);
			}
		}
		return ActionResultType.SUCCESS;
	}
}
