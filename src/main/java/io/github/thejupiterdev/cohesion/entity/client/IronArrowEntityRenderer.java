package io.github.thejupiterdev.cohesion.entity.client;

import io.github.thejupiterdev.cohesion.entity.custom.IronArrowEntity;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class IronArrowEntityRenderer extends ArrowEntityRenderer {
    private static final Identifier TEXTURE = Identifier.of("minecraft", "textures/entity/projectiles/arrow.png");

    public IronArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(net.minecraft.entity.projectile.ArrowEntity entity) {
        return TEXTURE;
    }
}
