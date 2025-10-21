package luckytntlib;

import luckytntlib.client.ClientAccess;
import luckytntlib.registry.ClientNetworkRegistry;
import luckytntlib.registry.EventRegistry;
import luckytntlib.util.ClientProxyImpl;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.Text;

public class LuckyTNTLibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientProxyImpl.init();
		ClientNetworkRegistry.init();
		EventRegistry.init();
		
		LuckyTNTLib.RH.registerConfigScreenFactory(Text.literal("Lucky TNT Lib"), ClientAccess.getFactory());
	}
}
