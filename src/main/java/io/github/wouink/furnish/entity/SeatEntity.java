package io.github.wouink.furnish.entity;

import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SeatEntity extends Entity {
    private BlockPos seatPos;

    public SeatEntity(Level level) {
        super(FurnishContents.SEAT_ENTITY, level);
        this.noPhysics = true;
    }

    private SeatEntity(Level level, BlockPos pos, double yOffset) {
        this(level);
        seatPos = pos;
        setPos(seatPos.getX() + 0.5, seatPos.getY() + yOffset, seatPos.getZ() + 0.5);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        super.tick();
        if(seatPos == null) seatPos = blockPosition();
        if(!level().isClientSide()) {
            if(getPassengers().isEmpty() || level().isEmptyBlock(seatPos)) remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {

    }

    @Override
    protected boolean canRide(Entity entity) {
        return entity instanceof LivingEntity;
    }

    public static InteractionResult create(Level level, BlockPos pos, double yOffset, LivingEntity user) {
        if(!level.isClientSide()) {
            List<SeatEntity> seatsInThisBlock = level.getEntitiesOfClass(
                    SeatEntity.class,
                    new AABB(pos.getX(), pos.getY(), pos.getZ(),
                            pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0)
            );
            if(seatsInThisBlock.isEmpty()) {
                SeatEntity seat = new SeatEntity(level, pos, yOffset);
                level.addFreshEntity(seat);
                user.startRiding(seat);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
