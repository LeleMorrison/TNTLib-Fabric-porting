package luckytntlib.client.renderer;

import luckytntlib.entity.PrimedLTNT;
import luckytntlib.util.IExplosiveEntity;
import luckytntlib.util.tnteffects.PrimedTNTEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.TntBlock;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.render.entity.state.TntEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

/**
 * The LTNTRenderer renders an {@link IExplosiveEntity} as a block.
 * The block can be a type of TNT, in which case it will also be animated, or any other block,
 * in which case it is rendered like a normal block.
 * The block is also scaled using the size of its {@link PrimedTNTEffect}.
 * 
 * Note: In 1.21.10, the rendering system was refactored to use EntityRenderState.
 * This renderer extends EntityRenderer directly and uses a custom render state
 * to support additional data needed for custom TNT effects (size, scaling).
 */
@Environment(value=EnvType.CLIENT)
public class LTNTRenderer extends EntityRenderer<PrimedLTNT, LTNTRenderer.LTNTRenderState> {
	
	public LTNTRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	
	/**
	 * Custom render state that extends TntEntityRenderState to include
	 * additional data for custom TNT effects (size multiplier, block state)
	 */
	public static class LTNTRenderState extends TntEntityRenderState {
		public float size = 1.0f;
		public float fuseProgress = 0.0f; // 0-1, used for scaling animation
		public BlockState customBlockState;
	}
	
	@Override
	public LTNTRenderState createRenderState() {
		return new LTNTRenderState();
	}
	
	@Override
	public void updateRenderState(PrimedLTNT entity, LTNTRenderState state, float tickDelta) {
		super.updateRenderState(entity, state, tickDelta);
		
		if (entity instanceof IExplosiveEntity explosiveEntity) {
			// Copy fuse data
			int fuse = explosiveEntity.getTNTFuse();
			state.fuse = (float) fuse;
			
			// Get custom block state from effect
			state.customBlockState = explosiveEntity.getEffect().getBlockState(explosiveEntity);
			state.blockState = state.customBlockState;
			
			// Get size from effect
			state.size = explosiveEntity.getEffect().getSize(explosiveEntity);
			
			// Calculate scaling animation progress for TNT blocks
			if (state.customBlockState != null && state.customBlockState.getBlock() instanceof TntBlock) {
				float fuseRemaining = (float) fuse - tickDelta + 1.0F;
				if (fuseRemaining < 10.0F) {
					float f = 1.0F - fuseRemaining / 10.0F;
					state.fuseProgress = MathHelper.clamp(f * f * f * f, 0.0F, 1.0F);
				}
			}
		}
	}
	
	@Override
	public void render(LTNTRenderState state, MatrixStack matrices, OrderedRenderCommandQueue commandQueue, CameraRenderState cameraState) {
		if (state.customBlockState != null) {
			matrices.push();
			
			// Apply scaling animation if TNT is about to explode
			if (state.fuseProgress > 0.0F && state.customBlockState.getBlock() instanceof TntBlock) {
				float scale = 1.0F + state.fuseProgress * 0.3F;
				matrices.scale(scale, scale, scale);
			}
			
			// Apply effect size
			matrices.scale(state.size, state.size, state.size);
			
			// Center the block
			matrices.translate(-0.5d, 0, -0.5d);
			
			// Render the block with flashing effect for TNT
			boolean flashing = state.customBlockState.getBlock() instanceof TntBlock 
				&& ((int) state.fuse) / 5 % 2 == 0;
			
			TntMinecartEntityRenderer.renderFlashingBlock(
				state.customBlockState, 
				matrices, 
				commandQueue, 
				state.light, 
				flashing, 
				(int) state.fuse
			);
			
			matrices.pop();
		}
		
		super.render(state, matrices, commandQueue, cameraState);
	}
}
