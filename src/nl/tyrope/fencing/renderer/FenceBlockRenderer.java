package nl.tyrope.fencing.renderer;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class FenceBlockRenderer implements ISimpleBlockRenderingHandler {

	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {

		int meta = world.getBlockMetadata(x, y, z);
		Icon c = block.getIcon(0, meta);
		float u = c.getMinU();
		float v = c.getMinV();
		float U = c.getMaxU();
		float V = c.getMaxV();

		// Delta-u and Delta-v is the size of a 'pixel' on the UV map, add a
		// multiplier of this to u or v to get a pixel count from origin.
		float du = (U - u) / Refs.textureSize;
		float dv = (V - v) / Refs.textureSize;

		Tessellator tess = Tessellator.instance;
		tess.addTranslation(x, y, z);

		int type = -1; // TODO Determine type by neighbor blocks

		switch (type) {
		case -1:
			// Debug, render ALL THE THINGS.
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.NORTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.SOUTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.EAST);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.WEST);
			renderStraightWires(tess, u, v, U, v + dv * 2, ForgeDirection.EAST);
			renderStraightWires(tess, u, v, U, v + dv * 2, ForgeDirection.NORTH);
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderStraightSpikes(tess, u, v, U, v + dv * 2,
						ForgeDirection.NORTH);
				renderStraightSpikes(tess, u, v, U, v + dv * 2,
						ForgeDirection.EAST);
			}
		case 0:
			// Straight N/S
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.NORTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.SOUTH);
			renderStraightWires(tess, u, v, U, v + dv * 2, ForgeDirection.NORTH);
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderStraightSpikes(tess, u, v, U, v + dv * 2,
						ForgeDirection.NORTH);
			}
			break;
		case 1:
			// Straight E/W
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.EAST);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.WEST);
			renderStraightWires(tess, u, v, U, v + dv * 2, ForgeDirection.EAST);
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderStraightSpikes(tess, u, v, U, v + dv * 2,
						ForgeDirection.EAST);
			}
			break;
		case 2:
			// corner N/E
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.NORTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.EAST);
			break;
		case 3:
			// corner N/W
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.NORTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.WEST);
			break;
		case 4:
			// corner S/E
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.SOUTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.EAST);
			break;
		case 5:
			// corner S/W
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.SOUTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.WEST);
			break;
		case 6:
			// T-section NEW
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.NORTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.EAST);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.WEST);
			renderStraightWires(tess, u, v, U, v + dv * 2, ForgeDirection.EAST);
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderStraightSpikes(tess, u, v, U, v + dv * 2,
						ForgeDirection.EAST);
			}
			break;
		case 7:
			// T-section NES
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.NORTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.EAST);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.SOUTH);
			renderStraightWires(tess, u, v, U, v + dv * 2, ForgeDirection.NORTH);
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderStraightSpikes(tess, u, v, U, v + dv * 2,
						ForgeDirection.NORTH);
			}
			break;
		case 8:
			// T-section ESW
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.EAST);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.SOUTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.WEST);
			renderStraightWires(tess, u, v, U, v + dv * 2, ForgeDirection.EAST);
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderStraightSpikes(tess, u, v, U, v + dv * 2,
						ForgeDirection.EAST);
			}
			break;
		case 9:
			// T-section NSW
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.NORTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.SOUTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.WEST);
			renderStraightWires(tess, u, v, U, v + dv * 2, ForgeDirection.NORTH);
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderStraightSpikes(tess, u, v, U, v + dv * 2,
						ForgeDirection.NORTH);
			}
			break;
		case 10:
			// X-section
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.NORTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.EAST);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.SOUTH);
			renderPost(tess, u, v + dv * 2, u + du * 8, v + dv * 14,
					ForgeDirection.WEST);
		default:
			System.out
					.println("[Fencing]EXCEPTION! Unknown rendering direction of fence on position ["
							+ x + "," + y + "," + z + "].");
			break;
		}
		tess.addTranslation(-x, -y, -z);
		return true;
	}

	private void renderPost(Tessellator t, float u, float v, float U, float V,
			ForgeDirection dir) {

		float postWidth = 0.125f, xMod, zMod;

		switch (dir) {
		case NORTH:
			xMod = (1 - postWidth) / 2f;
			zMod = 0f;
			break;
		case EAST:
			xMod = 0f;
			zMod = (1 - postWidth) / 2f;
			break;
		case SOUTH:
			xMod = (1 - postWidth) / 2f;
			zMod = 1 - postWidth;
			break;
		case WEST:
			xMod = 1 - postWidth;
			zMod = (1 - postWidth) / 2f;
			break;
		default:
			return;
		}
		// Top
		t.addVertexWithUV(xMod, 1, zMod + postWidth, u, v);
		t.addVertexWithUV(xMod + postWidth, 1, zMod + postWidth, u, V);
		t.addVertexWithUV(xMod + postWidth, 1, zMod, U, V);
		t.addVertexWithUV(xMod, 1, zMod, U, v);

		// Front
		t.addVertexWithUV(xMod + postWidth, 1, zMod + postWidth, u, v);
		t.addVertexWithUV(xMod + postWidth, 0, zMod + postWidth, u, V);
		t.addVertexWithUV(xMod + postWidth, 0, zMod, U, V);
		t.addVertexWithUV(xMod + postWidth, 1, zMod, U, v);

		// Back
		t.addVertexWithUV(xMod, 0, zMod + postWidth, u, v);
		t.addVertexWithUV(xMod, 1, zMod + postWidth, u, V);
		t.addVertexWithUV(xMod, 1, zMod, U, V);
		t.addVertexWithUV(xMod, 0, zMod, U, v);

		// Out
		t.addVertexWithUV(xMod, 0, zMod + postWidth, u, v);
		t.addVertexWithUV(xMod + postWidth, 0, zMod + postWidth, u, V);
		t.addVertexWithUV(xMod + postWidth, 1, zMod + postWidth, U, V);
		t.addVertexWithUV(xMod, 1, zMod + postWidth, U, v);

		// In
		t.addVertexWithUV(xMod, 1, zMod, u, v);
		t.addVertexWithUV(xMod + postWidth, 1, zMod, u, V);
		t.addVertexWithUV(xMod + postWidth, 0, zMod, U, V);
		t.addVertexWithUV(xMod, 0, zMod, U, v);

		// Bottom
		t.addVertexWithUV(xMod, 0, zMod, u, v);
		t.addVertexWithUV(xMod + postWidth, 0, zMod, u, V);
		t.addVertexWithUV(xMod + postWidth, 0, zMod + postWidth, U, V);
		t.addVertexWithUV(xMod, 0, zMod + postWidth, U, v);
	}

	private void renderStraightWires(Tessellator t, float u, float v, float U,
			float V, ForgeDirection dir) {

		float wireTop = 0.8125f, wireBottom = 0.75f, xMin = 0, xMax = 0, zMin = 0, zMax = 0;

		if (dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH) {
			xMin = 0.46875f;
			xMax = 0.53125f;
			zMin = 0.125f;
			zMax = 0.875f;
		} else if (dir == ForgeDirection.EAST || dir == ForgeDirection.WEST) {
			xMin = 0.125f;
			xMax = 0.875f;
			zMin = 0.46875f;
			zMax = 0.53125f;
		} else {
			// up and down are not supported... (yet??)
			return;
		}

		for (int i = 0; i < 3; i++) {
			// Top
			t.addVertexWithUV(xMin, wireTop, zMax, u, v);
			t.addVertexWithUV(xMax, wireTop, zMax, u, V);
			t.addVertexWithUV(xMax, wireTop, zMin, U, V);
			t.addVertexWithUV(xMin, wireTop, zMin, U, v);

			// Bottom
			t.addVertexWithUV(xMin, wireBottom, zMin, u, v);
			t.addVertexWithUV(xMax, wireBottom, zMin, u, V);
			t.addVertexWithUV(xMax, wireBottom, zMax, U, V);
			t.addVertexWithUV(xMin, wireBottom, zMax, U, v);

			// Front
			if (dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH) {
				t.addVertexWithUV(xMin, wireBottom, zMax, u, v);
				t.addVertexWithUV(xMin, wireTop, zMax, u, V);
				t.addVertexWithUV(xMin, wireTop, zMin, U, V);
				t.addVertexWithUV(xMin, wireBottom, zMin, U, v);
			} else if (dir == ForgeDirection.EAST || dir == ForgeDirection.WEST) {
				t.addVertexWithUV(xMin, wireBottom, zMin, u, v);
				t.addVertexWithUV(xMin, wireTop, zMin, u, V);
				t.addVertexWithUV(xMax, wireTop, zMin, U, V);
				t.addVertexWithUV(xMax, wireBottom, zMin, U, v);
			}

			// Back
			if (dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH) {
				t.addVertexWithUV(xMax, wireBottom, zMin, u, v);
				t.addVertexWithUV(xMax, wireTop, zMin, u, V);
				t.addVertexWithUV(xMax, wireTop, zMax, U, V);
				t.addVertexWithUV(xMax, wireBottom, zMax, U, v);
			} else if (dir == ForgeDirection.EAST || dir == ForgeDirection.WEST) {
				t.addVertexWithUV(xMax, wireBottom, zMax, u, v);
				t.addVertexWithUV(xMax, wireTop, zMax, u, V);
				t.addVertexWithUV(xMin, wireTop, zMax, U, V);
				t.addVertexWithUV(xMin, wireBottom, zMax, U, v);
			}

			wireTop = wireTop - 0.1875f;
			wireBottom = wireBottom - 0.1875f;
		}
	}

	private void renderStraightSpikes(Tessellator t, float u, float v, float U,
			float V, ForgeDirection dir) {

		/* Top Wire, Center Spike (top) */
		// Top
		t.addVertexWithUV(0.46875f, 0.875f, 0.53125f, u, v);
		t.addVertexWithUV(0.53125f, 0.875f, 0.53125f, u, V);
		t.addVertexWithUV(0.53125f, 0.875f, 0.46875f, U, V);
		t.addVertexWithUV(0.46875f, 0.875f, 0.46875f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.8125f, 0.53125f, u, v);
		t.addVertexWithUV(0.46875f, 0.875f, 0.53125f, u, V);
		t.addVertexWithUV(0.46875f, 0.875f, 0.46875f, U, V);
		t.addVertexWithUV(0.46875f, 0.8125f, 0.46875f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.8125f, 0.46875f, u, v);
		t.addVertexWithUV(0.53125f, 0.875f, 0.46875f, u, V);
		t.addVertexWithUV(0.53125f, 0.875f, 0.53125f, U, V);
		t.addVertexWithUV(0.53125f, 0.8125f, 0.53125f, U, v);

		// Left
		t.addVertexWithUV(0.46875f, 0.875f, 0.46875f, u, v);
		t.addVertexWithUV(0.53125f, 0.875f, 0.46875f, u, V);
		t.addVertexWithUV(0.53125f, 0.8125f, 0.46875f, U, V);
		t.addVertexWithUV(0.46875f, 0.8125f, 0.46875f, U, v);

		// Right
		t.addVertexWithUV(0.46875f, 0.8125f, 0.53125f, u, v);
		t.addVertexWithUV(0.53125f, 0.8125f, 0.53125f, u, V);
		t.addVertexWithUV(0.53125f, 0.875f, 0.53125f, U, V);
		t.addVertexWithUV(0.46875f, 0.875f, 0.53125f, U, v);

		/* Top Wire, Left Spike (bottom) */
		// Bottom
		t.addVertexWithUV(0.53125f, 0.6875f, 0.3125f, u, v);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.3125f, u, V);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.25f, U, V);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.25f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.75f, 0.25f, u, v);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.25f, u, V);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.3125f, U, V);
		t.addVertexWithUV(0.46875f, 0.75f, 0.3125f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.75f, 0.3125f, u, v);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.3125f, u, V);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.25f, U, V);
		t.addVertexWithUV(0.53125f, 0.75f, 0.25f, U, v);

		// Left
		t.addVertexWithUV(0.46875f, 0.6875f, 0.3125f, u, v);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.3125f, u, V);
		t.addVertexWithUV(0.53125f, 0.75f, 0.3125f, U, V);
		t.addVertexWithUV(0.46875f, 0.75f, 0.3125f, U, v);

		// Right
		t.addVertexWithUV(0.46875f, 0.75f, 0.25f, u, v);
		t.addVertexWithUV(0.53125f, 0.75f, 0.25f, u, V);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.25f, U, V);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.25f, U, v);

		/* Top Wire, Right Spike (bottom) */
		// Bottom
		t.addVertexWithUV(0.53125f, 0.6875f, 0.6875f, u, v);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.6875f, u, V);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.625f, U, V);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.625f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.75f, 0.625f, u, v);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.625f, u, V);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.6875f, U, V);
		t.addVertexWithUV(0.46875f, 0.75f, 0.6875f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.75f, 0.6875f, u, v);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.6875f, u, V);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.625f, U, V);
		t.addVertexWithUV(0.53125f, 0.75f, 0.625f, U, v);

		// Left
		t.addVertexWithUV(0.46875f, 0.6875f, 0.6875f, u, v);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.6875f, u, V);
		t.addVertexWithUV(0.53125f, 0.75f, 0.6875f, U, V);
		t.addVertexWithUV(0.46875f, 0.75f, 0.6875f, U, v);

		// Right
		t.addVertexWithUV(0.46875f, 0.75f, 0.625f, u, v);
		t.addVertexWithUV(0.53125f, 0.75f, 0.625f, u, V);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.625f, U, V);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.625f, U, v);

		/* Middle Wire, Center Spike (top) */
		// Top
		t.addVertexWithUV(0.46875f, 0.6875f, 0.53125f, u, v);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.53125f, u, V);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.46875f, U, V);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.46875f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.625f, 0.53125f, u, v);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.53125f, u, V);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.46875f, U, V);
		t.addVertexWithUV(0.46875f, 0.625f, 0.46875f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.625f, 0.46875f, u, v);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.46875f, u, V);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.53125f, U, V);
		t.addVertexWithUV(0.53125f, 0.625f, 0.53125f, U, v);

		// Left
		t.addVertexWithUV(0.46875f, 0.6875f, 0.46875f, u, v);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.46875f, u, V);
		t.addVertexWithUV(0.53125f, 0.625f, 0.46875f, U, V);
		t.addVertexWithUV(0.46875f, 0.625f, 0.46875f, U, v);

		// Right
		t.addVertexWithUV(0.46875f, 0.625f, 0.53125f, u, v);
		t.addVertexWithUV(0.53125f, 0.625f, 0.53125f, u, V);
		t.addVertexWithUV(0.53125f, 0.6875f, 0.53125f, U, V);
		t.addVertexWithUV(0.46875f, 0.6875f, 0.53125f, U, v);

		/* Middle Wire, Left Spike (bottom) */
		// Bottom
		t.addVertexWithUV(0.53125f, 0.5f, 0.3125f, u, v);
		t.addVertexWithUV(0.46875f, 0.5f, 0.3125f, u, V);
		t.addVertexWithUV(0.46875f, 0.5f, 0.25f, U, V);
		t.addVertexWithUV(0.53125f, 0.5f, 0.25f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.5625f, 0.25f, u, v);
		t.addVertexWithUV(0.46875f, 0.5f, 0.25f, u, V);
		t.addVertexWithUV(0.46875f, 0.5f, 0.3125f, U, V);
		t.addVertexWithUV(0.46875f, 0.5625f, 0.3125f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.5625f, 0.3125f, u, v);
		t.addVertexWithUV(0.53125f, 0.5f, 0.3125f, u, V);
		t.addVertexWithUV(0.53125f, 0.5f, 0.25f, U, V);
		t.addVertexWithUV(0.53125f, 0.5625f, 0.25f, U, v);

		// Left
		t.addVertexWithUV(0.46875f, 0.5f, 0.3125f, u, v);
		t.addVertexWithUV(0.53125f, 0.5f, 0.3125f, u, V);
		t.addVertexWithUV(0.53125f, 0.5625f, 0.3125f, U, V);
		t.addVertexWithUV(0.46875f, 0.5625f, 0.3125f, U, v);

		// Right
		t.addVertexWithUV(0.46875f, 0.5625f, 0.25f, u, v);
		t.addVertexWithUV(0.53125f, 0.5625f, 0.25f, u, V);
		t.addVertexWithUV(0.53125f, 0.5f, 0.25f, U, V);
		t.addVertexWithUV(0.46875f, 0.5f, 0.25f, U, v);

		/* Middle Wire, Right Spike (bottom) */
		// Bottom
		t.addVertexWithUV(0.53125f, 0.5f, 0.6875f, u, v);
		t.addVertexWithUV(0.46875f, 0.5f, 0.6875f, u, V);
		t.addVertexWithUV(0.46875f, 0.5f, 0.625f, U, V);
		t.addVertexWithUV(0.53125f, 0.5f, 0.625f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.5625f, 0.625f, u, v);
		t.addVertexWithUV(0.46875f, 0.5f, 0.625f, u, V);
		t.addVertexWithUV(0.46875f, 0.5f, 0.6875f, U, V);
		t.addVertexWithUV(0.46875f, 0.5625f, 0.6875f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.5625f, 0.6875f, u, v);
		t.addVertexWithUV(0.53125f, 0.5f, 0.6875f, u, V);
		t.addVertexWithUV(0.53125f, 0.5f, 0.625f, U, V);
		t.addVertexWithUV(0.53125f, 0.5625f, 0.625f, U, v);

		// Left
		t.addVertexWithUV(0.46875f, 0.5f, 0.6875f, u, v);
		t.addVertexWithUV(0.53125f, 0.5f, 0.6875f, u, V);
		t.addVertexWithUV(0.53125f, 0.5625f, 0.6875f, U, V);
		t.addVertexWithUV(0.46875f, 0.5625f, 0.6875f, U, v);

		// Right
		t.addVertexWithUV(0.46875f, 0.5625f, 0.625f, u, v);
		t.addVertexWithUV(0.53125f, 0.5625f, 0.625f, u, V);
		t.addVertexWithUV(0.53125f, 0.5f, 0.625f, U, V);
		t.addVertexWithUV(0.46875f, 0.5f, 0.625f, U, v);

		/* Bottom Wire, Center Spike (top) */
		// Top
		t.addVertexWithUV(0.46875f, 0.5f, 0.53125f, u, v);
		t.addVertexWithUV(0.53125f, 0.5f, 0.53125f, u, V);
		t.addVertexWithUV(0.53125f, 0.5f, 0.46875f, U, V);
		t.addVertexWithUV(0.46875f, 0.5f, 0.46875f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.4375f, 0.53125f, u, v);
		t.addVertexWithUV(0.46875f, 0.5f, 0.53125f, u, V);
		t.addVertexWithUV(0.46875f, 0.5f, 0.46875f, U, V);
		t.addVertexWithUV(0.46875f, 0.4375f, 0.46875f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.4375f, 0.46875f, u, v);
		t.addVertexWithUV(0.53125f, 0.5f, 0.46875f, u, V);
		t.addVertexWithUV(0.53125f, 0.5f, 0.53125f, U, V);
		t.addVertexWithUV(0.53125f, 0.4375f, 0.53125f, U, v);

		// Left
		t.addVertexWithUV(0.46875f, 0.5f, 0.46875f, u, v);
		t.addVertexWithUV(0.53125f, 0.5f, 0.46875f, u, V);
		t.addVertexWithUV(0.53125f, 0.4375f, 0.46875f, U, V);
		t.addVertexWithUV(0.46875f, 0.4375f, 0.46875f, U, v);

		// Right
		t.addVertexWithUV(0.46875f, 0.4375f, 0.53125f, u, v);
		t.addVertexWithUV(0.53125f, 0.4375f, 0.53125f, u, V);
		t.addVertexWithUV(0.53125f, 0.5f, 0.53125f, U, V);
		t.addVertexWithUV(0.46875f, 0.5f, 0.53125f, U, v);

		/* Bottom Wire, Left Spike (bottom) */
		// Bottom
		t.addVertexWithUV(0.53125f, 0.3125f, 0.3125f, u, v);
		t.addVertexWithUV(0.46875f, 0.3125f, 0.3125f, u, V);
		t.addVertexWithUV(0.46875f, 0.3125f, 0.25f, U, V);
		t.addVertexWithUV(0.53125f, 0.3125f, 0.25f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.375f, 0.25f, u, v);
		t.addVertexWithUV(0.46875f, 0.3125f, 0.25f, u, V);
		t.addVertexWithUV(0.46875f, 0.3125f, 0.3125f, U, V);
		t.addVertexWithUV(0.46875f, 0.375f, 0.3125f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.375f, 0.3125f, u, v);
		t.addVertexWithUV(0.53125f, 0.3125f, 0.3125f, u, V);
		t.addVertexWithUV(0.53125f, 0.3125f, 0.25f, U, V);
		t.addVertexWithUV(0.53125f, 0.375f, 0.25f, U, v);

		// Left
		t.addVertexWithUV(0.46875f, 0.3125f, 0.3125f, u, v);
		t.addVertexWithUV(0.53125f, 0.3125f, 0.3125f, u, V);
		t.addVertexWithUV(0.53125f, 0.375f, 0.3125f, U, V);
		t.addVertexWithUV(0.46875f, 0.375f, 0.3125f, U, v);

		// Right
		t.addVertexWithUV(0.46875f, 0.375f, 0.25f, u, v);
		t.addVertexWithUV(0.53125f, 0.375f, 0.25f, u, V);
		t.addVertexWithUV(0.53125f, 0.3125f, 0.25f, U, V);
		t.addVertexWithUV(0.46875f, 0.3125f, 0.25f, U, v);

		/* Bottom Wire, Right Spike (bottom) */
		// Bottom
		t.addVertexWithUV(0.53125f, 0.3125f, 0.6875f, u, v);
		t.addVertexWithUV(0.46875f, 0.3125f, 0.6875f, u, V);
		t.addVertexWithUV(0.46875f, 0.3125f, 0.625f, U, V);
		t.addVertexWithUV(0.53125f, 0.3125f, 0.625f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.375f, 0.625f, u, v);
		t.addVertexWithUV(0.46875f, 0.3125f, 0.625f, u, V);
		t.addVertexWithUV(0.46875f, 0.3125f, 0.6875f, U, V);
		t.addVertexWithUV(0.46875f, 0.375f, 0.6875f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.375f, 0.6875f, u, v);
		t.addVertexWithUV(0.53125f, 0.3125f, 0.6875f, u, V);
		t.addVertexWithUV(0.53125f, 0.3125f, 0.625f, U, V);
		t.addVertexWithUV(0.53125f, 0.375f, 0.625f, U, v);

		// Left
		t.addVertexWithUV(0.46875f, 0.3125f, 0.6875f, u, v);
		t.addVertexWithUV(0.53125f, 0.3125f, 0.6875f, u, V);
		t.addVertexWithUV(0.53125f, 0.375f, 0.6875f, U, V);
		t.addVertexWithUV(0.46875f, 0.375f, 0.6875f, U, v);

		// Right
		t.addVertexWithUV(0.46875f, 0.375f, 0.625f, u, v);
		t.addVertexWithUV(0.53125f, 0.375f, 0.625f, u, V);
		t.addVertexWithUV(0.53125f, 0.3125f, 0.625f, U, V);
		t.addVertexWithUV(0.46875f, 0.3125f, 0.625f, U, v);
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	@Override
	public int getRenderId() {
		return this.renderID;
	}
}
