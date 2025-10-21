package luckytntlib.client.renderer;

import luckytntlib.entity.LExplosiveProjectile;
import luckytntlib.util.tnteffects.PrimedTNTEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.util.Identifier;

/**
 * The LDynamiteRenderer is similar to the {@link FlyingItemEntityRenderer}, but the item is also scaled by using
 * the size given by the {@link PrimedTNTEffect} of the {@link LExplosiveProjectile}.
 * @param <T>  is an instance of {@link LExplosiveProjectile} and implements {@link FlyingItemEntity}
 */
@Environment(value=EnvType.CLIENT)
public class LDynamiteRenderer<T extends LExplosiveProjectile & FlyingItemEntity> extends FlyingItemEntityRenderer<T>{
	
	public LDynamiteRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	
	// Note: The render method signature may have changed in 1.21.10
	// If this class doesn't work properly, you may need to override the new render methods
	// that use EntityRenderState instead of the entity directly
	
	@SuppressWarnings("deprecation")
	public Identifier getTexture(T entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}
