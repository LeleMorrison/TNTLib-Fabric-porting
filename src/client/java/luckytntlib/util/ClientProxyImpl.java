package luckytntlib.util;

import luckytntlib.client.ClientAccess;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;

/**
 * Client-side implementation of ClientProxy.
 */
public class ClientProxyImpl implements ClientProxy {
	
	public static void init() {
		ClientProxy.Holder.INSTANCE = new ClientProxyImpl();
	}
	
	@Override
	public void openConfigScreenListScreen() {
		ClientAccess.openConfigScreenListScreen();
	}
	
	@Override
	public void sendC2SPacket(CustomPayload packet) {
		ClientPlayNetworking.send(packet);
	}
}
