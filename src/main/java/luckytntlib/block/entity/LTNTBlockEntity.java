package luckytntlib.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class LTNTBlockEntity extends BlockEntity {
	
	protected NbtCompound persistentData = new NbtCompound();
	private String PERSISTENT_DATA_TAG = "PersistentData";

	public LTNTBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		nbt.put(PERSISTENT_DATA_TAG, persistentData);
	}
	
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		if (nbt.contains(PERSISTENT_DATA_TAG)) {
			nbt.getCompound(PERSISTENT_DATA_TAG).ifPresent(compound -> persistentData = compound);
		}
	}
	
	public NbtCompound getPersistentData() {
		return persistentData;
	}
}
