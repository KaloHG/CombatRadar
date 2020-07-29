package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * @author Aleksey Terzi
 */
public class ResourceHelper {
    /**
     * gives us access to the protected getEntityTexture method
     */
    public static ResourceLocation getEntityTexture(EntityRenderer r, Entity e) {
        return r.getEntityTexture(e);
    }

	public static void blit(int x, int y, int z, float u, float v, int width, int height, int vScale, int uScale) {
		innerBlit(x, x + width, y, y + height, z, width, height, u, v, uScale, vScale);
	}

	public static void blit(int x, int y, int width, int height, float minU, float minV, int maxU, int maxV, int uScale, int vScale) {
		innerBlit(x, x + width, y, y + height, 0, maxU, maxV, minU, minV, uScale, vScale);
	}

	public static void blit(int x, int y, float minU, float minV, int width, int height, int uScale, int vScale) {
		blit(x, y, width, height, minU, minV, width, height, uScale, vScale);
	}
    
	private static void innerBlit(int x0, int x1, int y0, int y1, int z, int maxU, int maxV, float minU, float minV, int uScale, int vScale) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double) x0, (double) y0, (double) z).tex(minU, maxV).endVertex();
		bufferbuilder.pos((double) x1, (double) y0, (double) z).tex(maxU, maxV).endVertex();
		bufferbuilder.pos((double) x1, (double) y1, (double) z).tex(maxU, minV).endVertex();
		bufferbuilder.pos((double) x0, (double) y1, (double) z).tex(minU, minV).endVertex();
		tessellator.draw();
	}
}
