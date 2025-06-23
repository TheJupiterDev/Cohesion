package io.github.thejupiterdev.cohesion.entity.client;

import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class BouncingArrowEntityRenderer extends ArrowEntityRenderer {
    private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/projectiles/arrow.png");

    public BouncingArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(net.minecraft.entity.projectile.ArrowEntity entity) {
        return TEXTURE;
    }
}
