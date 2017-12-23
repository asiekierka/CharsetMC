package pl.asie.charset.module.experiments.projector.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;
import pl.asie.charset.lib.utils.Orientation;
import pl.asie.charset.lib.utils.Quaternion;
import pl.asie.charset.module.experiments.projector.IProjectorHandler;
import pl.asie.charset.module.experiments.projector.IProjectorSurface;
import pl.asie.charset.module.experiments.projector.ProjectorHelper;

import java.util.List;

public class ProjectorHandlerBook implements IProjectorHandler<ItemStack> {
	@Override
	public boolean matches(ItemStack target) {
		return target.getItem() instanceof ItemWrittenBook;
	}

	@Override
	public float getAspectRatio(ItemStack target) {
		return 146f/180f;
	}

	@Override
	public void render(ItemStack stack, IProjectorSurface surface) {
		float[] uvValues = surface.createUvArray(20, 166, 1, 1+180);

		// oh boy! text!
		if (stack.hasTagCompound()) {
			GlStateManager.pushMatrix();
			double[] data = {
					surface.getCornerStart().y,
					surface.getCornerEnd().y,
					surface.getCornerStart().z,
					surface.getCornerEnd().z,
					surface.getCornerStart().x,
					surface.getCornerEnd().x
			};

			GlStateManager.translate(((surface.getCornerStart().x + surface.getCornerEnd().x) / 2) + surface.getScreenFacing().getFrontOffsetX() * 0.001f,
					((surface.getCornerStart().y + surface.getCornerEnd().y) / 2) + surface.getScreenFacing().getFrontOffsetY() * 0.001f,
					((surface.getCornerStart().z + surface.getCornerEnd().z) / 2) + surface.getScreenFacing().getFrontOffsetZ() * 0.001f);

			Orientation orientation = ProjectorHelper.INSTANCE.getOrientation(surface);

			Quaternion.fromOrientation(orientation).glRotate();
			GlStateManager.rotate(270.0f, 0, 0, 1);
			GlStateManager.rotate(270.0f, 0, 1, 0);
			GlStateManager.translate(0, 0, -ProjectorHelper.OFFSET);

			float scaleVal = 2f * surface.getWidth() / 146f;
			GlStateManager.scale(scaleVal, scaleVal, scaleVal);
			FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;

			NBTTagList pages = stack.getTagCompound().getTagList("pages", Constants.NBT.TAG_STRING);
			if (pages.tagCount() >= 1) {
				String pageCount = I18n.format("book.pageIndicator", 1, pages.tagCount());
				renderer.drawString(pageCount,-73 + 129 - renderer.getStringWidth(pageCount), -90 + 15, 0xFF000000);

				String page = ((NBTTagString) pages.get(0)).getString();
				ITextComponent fullComponent = ITextComponent.Serializer.jsonToComponent(page);
				if (fullComponent != null) {
					List<ITextComponent> components = GuiUtilRenderComponents.splitText(fullComponent, 116, renderer, true, true);
					for (int i = 0; i < components.size(); i++) {
						renderer.drawString(components.get(i).getUnformattedText(), -73 + 16, -90 + 30 + i * renderer.FONT_HEIGHT, 0xFF000000);
					}
				}
			}
			GlStateManager.popMatrix();
			surface.restoreGLColor();
		}

		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/book.png"));
		ProjectorHelper.INSTANCE.renderTexture(surface, 20, 20+146, 1, 1+180);
	}
}
