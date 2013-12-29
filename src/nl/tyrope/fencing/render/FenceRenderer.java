package nl.tyrope.fencing.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.models.FenceCornerModel;
import nl.tyrope.fencing.models.FenceStraightModel;
import nl.tyrope.fencing.models.FenceTsectionModel;
import nl.tyrope.fencing.models.FenceXsectionModel;
import nl.tyrope.fencing.tileEntities.FenceEntity;

import org.lwjgl.opengl.GL11;

public class FenceRenderer extends TileEntitySpecialRenderer {

	private final ModelBase[] models;

	public FenceRenderer() {
		this.models = new ModelBase[] { new FenceStraightModel(),
				new FenceCornerModel(), new FenceTsectionModel(),
				new FenceXsectionModel() };
	}

	private void adjustRotatePivotViaMeta(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		GL11.glPushMatrix();
		GL11.glRotatef(meta * (-90), 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z,
			float scale) {
		int model = ((FenceEntity) te).getModel();
		float[] rot = ((FenceEntity) te).getRotation();

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		ResourceLocation textures = (new ResourceLocation(Refs.MODID
				+ ":textures/blocks/fence" + te.getBlockMetadata() + ".png"));
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		GL11.glPushMatrix();
		GL11.glRotatef(180F, rot[0], rot[1], rot[2]);
		this.models[model].render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F,
				0.0625F);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	private void adjustLightFixture(World world, int i, int j, int k,
			Block block) {
		Tessellator tess = Tessellator.instance;
		float brightness = block.getBlockBrightness(world, i, j, k);
		int skyLight = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
		int modulousModifier = skyLight % 65536;
		int divModifier = skyLight / 65536;
		tess.setColorOpaque_F(brightness, brightness, brightness);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
				(float) modulousModifier, divModifier);
	}
}