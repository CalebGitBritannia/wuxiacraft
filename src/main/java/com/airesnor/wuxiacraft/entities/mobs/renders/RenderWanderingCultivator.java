package com.airesnor.wuxiacraft.entities.mobs.renders;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderWanderingCultivator<T extends WanderingCultivator> extends RenderBiped<T> {

	private static final ResourceLocation WANDERING_CULTIVATOR_TEXTURE = new ResourceLocation(WuxiaCraft.MODID, "textures/entities/wandering_cultivator.png");

	public RenderWanderingCultivator(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelWanderingCultivator(), 0.5f);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return WANDERING_CULTIVATOR_TEXTURE;
	}
}