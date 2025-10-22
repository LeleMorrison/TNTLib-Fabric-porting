package luckytntlib.client.renderer;

import luckytntlib.entity.LTNTMinecart;
import luckytntlib.util.tnteffects.PrimedTNTEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.AbstractMinecartEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.TntMinecartEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

/**
 * The LTNTMinecartRenderer renders a Minecart with a TNT inside of it.
 * The TNT is scaled using the size parameter of its {@link PrimedTNTEffect}.
 * 
 * Note: In 1.21.10, the rendering system was refactored using render states.
 * This renderer extends AbstractMinecartEntityRenderer directly with custom type parameters
 * to support TntMinecartEntityRenderState for proper TNT flashing rendering.
 */
@Environment(value=EnvType.CLIENT)
public class LTNTMinecartRenderer extends AbstractMinecartEntityRenderer<LTNTMinecart, TntMinecartEntityRenderState> {
	
	public LTNTMinecartRenderer(EntityRendererFactory.Context context) {
		super(context, EntityModelLayers.TNT_MINECART);
	}
	
	@Override
	public TntMinecartEntityRenderState createRenderState() {
		return new TntMinecartEntityRenderState();
	}
	
	@Override
	public void updateRenderState(LTNTMinecart entity, TntMinecartEntityRenderState state, float tickDelta) {
		super.updateRenderState(entity, state, tickDelta);
		// Copy fuse data from entity to render state
		state.fuseTicks = (float) entity.getTNTFuse();
	}
	
	@Override
	protected void renderBlock(TntMinecartEntityRenderState state, BlockState blockState, MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light) {
		// Calculate flashing based on fuse ticks (matches vanilla TNT minecart behavior)
		int fuseTicks = Math.max(0, (int) state.fuseTicks);
		boolean flashing = fuseTicks > 0 && fuseTicks / 5 % 2 == 0;
		TntMinecartEntityRenderer.renderFlashingBlock(blockState, matrices, orderedRenderCommandQueue, light, flashing, fuseTicks);
	}
}
