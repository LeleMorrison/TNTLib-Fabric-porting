package luckytntlib.config.common;

import net.minecraft.client.gui.screen.Screen;

/**
 * Client-side implementation helper for ConfigScreenFactory.
 */
public interface ClientConfigScreenFactory extends ConfigScreenFactory {
	Screen createScreen();
	
	@Override
	default Object get() {
		return createScreen();
	}
}
