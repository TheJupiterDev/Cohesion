package io.github.thejupiterdev.cohesion.entity.client;

import io.github.thejupiterdev.cohesion.Cohesion;
import io.github.thejupiterdev.cohesion.entity.custom.IronArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class IronArrowEntityRenderer extends ProjectileEntityRenderer<IronArrowEntity> {
    public static final Identifier TEXTURE = Identifier.of(Cohesion.MOD_ID, "textures/entity/iron_arrow/iron_arrow.png");

    public IronArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(IronArrowEntity arrowEntity) {
        return TEXTURE;
    }
}
