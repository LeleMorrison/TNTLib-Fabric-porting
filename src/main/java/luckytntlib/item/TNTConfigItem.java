package luckytntlib.item;

import luckytntlib.util.ClientProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TNTConfigItem extends Item {

	public TNTConfigItem(RegistryKey<Item> key) {
		super(new Item.Settings().registryKey(key).maxCount(1));
	}

	@Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if(user.isSneaking()) {
			return ActionResult.FAIL;
		}
		
		if(world.isClient()) {
			ClientProxy.Holder.INSTANCE.openConfigScreenListScreen();
		}
		
		return ActionResult.SUCCESS;
	}
}
