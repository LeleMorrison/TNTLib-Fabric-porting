package luckytntlib.client.renderer;

import luckytntlib.util.tnteffects.PrimedTNTEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;

/**
 * The LTNTMinecartRenderer renders a Minecart with a TNT inside of it.
 * The TNT is scaled using the size parameter of its {@link PrimedTNTEffect}.
 * 
 * Note: In 1.21.10, MinecartEntityRenderer no longer takes type parameters
 * and the rendering system was refactored. Custom rendering logic may need
 * to be re-implemented.
 */
@Environment(value=EnvType.CLIENT)
public class LTNTMinecartRenderer extends MinecartEntityRenderer {
	
	public LTNTMinecartRenderer(EntityRendererFactory.Context context) {
		super(context, EntityModelLayers.TNT_MINECART);
	}
	
	// TODO: Re-implement custom TNT scaling logic for 1.21.10
	// The renderBlock method signature may have changed
	// Override the appropriate methods to add custom TNT rendering behavior
}
