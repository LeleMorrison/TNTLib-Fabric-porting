package luckytntlib.util;

import net.minecraft.network.packet.CustomPayload;

/**
 * Interface for client-side operations that can be called from common code.
 * Implementation is in the client source set.
 */
public interface ClientProxy {
	void openConfigScreenListScreen();
	void sendC2SPacket(CustomPayload packet);
	
	class Holder {
		public static ClientProxy INSTANCE = new ClientProxy() {
			@Override
			public void openConfigScreenListScreen() {
				// No-op on server
			}
			
			@Override
			public void sendC2SPacket(CustomPayload packet) {
				// No-op on server
			}
		};
	}
}
