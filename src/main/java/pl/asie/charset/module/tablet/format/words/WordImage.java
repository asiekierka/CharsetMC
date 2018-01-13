package pl.asie.charset.module.tablet.format.words;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import pl.asie.charset.module.tablet.format.api.Word;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class WordImage extends Word {
    public final ResourceLocation resource;
    public int width = 12, height = 12;

    public WordImage(ResourceLocation resource) {
        this.resource = resource;
        autosize();
    }

    public WordImage(ResourceLocation resource, int width, int height) {
        this(resource);
        this.width = width;
        this.height = height;
    }

    public void scale(double scale) {
        width *= scale;
        height *= scale;
    }

    private static final HashMap<ResourceLocation, Pair<Integer, Integer>> size_cache = new HashMap<ResourceLocation, Pair<Integer, Integer>>();

    private void autosize() {
        Pair<Integer, Integer> cached = size_cache.get(resource);
        if (cached != null) {
            width = cached.getLeft();
            height = cached.getRight();
            return;
        }

        IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        try {
            IResource iresource = resourceManager.getResource(resource);
            BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
            this.width = bufferedimage.getWidth();
            this.height = bufferedimage.getHeight();
            size_cache.put(resource, Pair.of(width, height));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fitToPage(int pageWidth, int pageHeight) {
        double s = 1.0;
        if (width > pageWidth) {
            s = pageWidth / (double) width;
        }
        if (height > pageHeight) {
            double h = pageHeight / (double) height;
            if (h < s) {
                s = h;
            }
        }
        scale(s);
    }
}