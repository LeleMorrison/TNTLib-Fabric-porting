package luckytntlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import luckytntlib.util.LuckyTNTEntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

/**
 * This mixin basically exists to replace the missing {@code NbtCompound Entity.persistentData} that was provided by default by Forge 
 * and was commonly used by the LuckyTNTMod to store addditional data
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements LuckyTNTEntityExtension {

	@Unique
	private NbtCompound luckytntlib$persistentData = new NbtCompound();
	
	@Inject(method = "writeNbt", at = @At("RETURN"))
	private void injectionWriteNbt(NbtCompound tag, CallbackInfoReturnable<NbtCompound> cir) {
		tag.put("AdditionalData", luckytntlib$persistentData);
	}
	
	@Inject(method = "readNbt", at = @At("RETURN"))
	private void injectionReadNbt(NbtCompound tag, CallbackInfo info) {
		if (tag.contains("AdditionalData")) {
			tag.getCompound("AdditionalData").ifPresent(compound -> luckytntlib$persistentData = compound);
		}
	}

	@Unique
	public NbtCompound getAdditionalPersistentData() {
		return luckytntlib$persistentData;
	}

	@Unique
	public void setAdditionalPersistentData(NbtCompound nbt) {
		luckytntlib$persistentData = nbt;
	}
}
