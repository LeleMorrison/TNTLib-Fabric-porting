package luckytntlib.entity;

import org.jetbrains.annotations.Nullable;

import luckytntlib.util.IExplosiveEntity;
import luckytntlib.util.tnteffects.PrimedTNTEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * The LExplosiveProjectile is an extension of Minecraft's {@link PersistentProjectileEntity} 
 * and represents a projectile that holds a {@link PrimedTNTEffect}.
 * Unlike a {@link PrimedLTNT} a LExplosiveProjectile has access to other types of logic specifically designed
 * for entities that travel through the world with high speeds and hit blocks or entities, while still retaining the abilities of a TNT
 * through its {@link PrimedTNTEffect}.
 * It implements {@link IExplosiveEntity} and {@link FlyingItemEntity}.
 */
public class LExplosiveProjectile extends PersistentProjectileEntity implements IExplosiveEntity, FlyingItemEntity{
	
	private static final TrackedData<Integer> DATA_FUSE_ID = DataTracker.registerData(LExplosiveProjectile.class, TrackedDataHandlerRegistry.INTEGER);
	private NbtCompound persistentData = new NbtCompound();
	@Nullable
	private LivingEntity thrower;
	private boolean hitEntity = false;
	private PrimedTNTEffect effect;
	
	public LExplosiveProjectile(EntityType<LExplosiveProjectile> type, World level, PrimedTNTEffect effect) {
		super(type, 0, 0, 0, level, new ItemStack(Items.CARROT), null);
		setTNTFuse(effect.getDefaultFuse(this));
		pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
		this.effect = effect;
		setStack(getDefaultItemStack());
	}
	
	@Override
	public void onBlockHit(BlockHitResult hitResult) {
		Vec3d pos = hitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
		setVelocity(pos);
		Vec3d pos2 = pos.normalize().multiply((double) 0.05F);
		setPos(this.getX() - pos2.x, this.getY() - pos2.y, this.getZ() - pos2.z);
	    setInGround(true);
	}
	
	@Override
	public void onEntityHit(EntityHitResult hitResult) {
		if(hitResult.getEntity() instanceof PlayerEntity player) {
			if(!(player.isCreative() || player.isSpectator())) {
				hitEntity = true;
			}
		}
		else {
			hitEntity = true;
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		effect.baseTick(this);
	}
	
	@Override
	public void initDataTracker(DataTracker.Builder builder) {
		builder.add(DATA_FUSE_ID, -1);
		super.initDataTracker(builder);
	}
	
	public void writeCustomDataToNbt(NbtCompound tag) {
		if(thrower != null) {
			tag.putInt("throwerID", thrower.getId());
		}
		tag.putShort("Fuse", (short)getTNTFuse());
		tag.put("PersistentData", getPersistentData());
	}
	
	public void readCustomDataFromNbt(NbtCompound tag) {
		tag.getInt("throwerID").ifPresent(id -> {
			if(getEntityWorld().getEntityById(id) instanceof LivingEntity lEnt) {
				thrower = lEnt;
			}
		});
		tag.getShort("Fuse").ifPresent(fuse -> setTNTFuse(fuse));
		tag.getCompound("PersistentData").ifPresent(data -> setPersistentData(data));
	}
	
	public PrimedTNTEffect getEffect() {
		return effect;
	}
	
	public boolean inGround() {
		return this.isInGround();
	}
	
	public boolean hitEntity() {
		return hitEntity;
	}
	
	@Override
	public void setTNTFuse(int fuse) {
		dataTracker.set(DATA_FUSE_ID, fuse);
	}
	
	public void setOwner(@Nullable LivingEntity thrower) {
		this.thrower = thrower;
	}
	
	@Override
	public void setOwner(Entity entity) {
		thrower = entity instanceof LivingEntity ? (LivingEntity) entity : thrower;
	}
	
	@Override
	@Nullable
	public LivingEntity getOwner() {
		return thrower;
	}
	
	@Override
	public ItemStack asItemStack() {
		return getStack();
	}
	
	@Override
	public int getTNTFuse() {
		return dataTracker.get(DATA_FUSE_ID);
	}
	
	@Override
	public Vec3d getPos() {
		return getLerpedPos(1);
	}

	@Override
	public void destroy() {
		discard();
	}
	
	@Override
	public World getLevel() {
		return getEntityWorld();
	}
	
	@Override
	public double x() {
		return getX();
	}

	@Override
	public double y() {
		return getY();
	}

	@Override
	public double z() {
		return getZ();
	}
	
	@Override
	public ItemStack getStack() {
		return effect == null ? new ItemStack(Items.CARROT) : effect.getItemStack();
	}
	
	@Override
	public LivingEntity owner() {
		return getOwner();
	}
	
	@Override
	public NbtCompound getPersistentData() {
		return persistentData;
	}

	@Override
	public void setPersistentData(NbtCompound tag) {
		this.persistentData = tag;
	}

	@Override
	protected ItemStack getDefaultItemStack() {
		return new ItemStack(Items.CARROT);
	}
}
