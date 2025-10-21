package luckytntlib.client.renderer;

import luckytntlib.util.IExplosiveEntity;
import luckytntlib.util.tnteffects.PrimedTNTEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntEntityRenderer;

/**
 * The LTNTRenderer renders an {@link IExplosiveEntity} as a block.
 * The block can be a type of TNT, in which case it will also be animated, or any other block,
 * in which case it is rendered like a normal block.
 * The block is also scaled using the size of its {@link PrimedTNTEffect}.
 * 
 * Note: In 1.21.10, the rendering system was refactored to use EntityRenderState.
 * This simplified version extends TntEntityRenderer. Custom rendering logic may need
 * to be re-implemented using the new rendering architecture.
 */
@Environment(value=EnvType.CLIENT)
public class LTNTRenderer extends TntEntityRenderer {
	
	public LTNTRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	
	// TODO: Re-implement custom scaling and rendering logic for 1.21.10
	// The rendering system now uses EntityRenderState instead of direct entity access
	// Override the appropriate methods to add custom TNT rendering behavior
}
