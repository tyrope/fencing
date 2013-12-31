package nl.tyrope.fencing.renderer;

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

		// Delta-u and Delta-v is the size of a 'pixel' on the UV map, add a
		// multiplier of this to u or v to get a pixel count from origin.
		float du = (c.getMaxU() - u) / Refs.textureSize;
		float dv = (c.getMaxV() - v) / Refs.textureSize;

		Tessellator tess = Tessellator.instance;
		tess.addTranslation(x, y, z);

		int type = -1; // TODO Determine type by neighbor blocks

		switch (type) {
		case -1: // DEBUG, render ALL THE THINGS.
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH,
					ForgeDirection.EAST, ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[][] {
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.SOUTH },
					new ForgeDirection[] { ForgeDirection.EAST,
							ForgeDirection.WEST },
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.EAST },
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.WEST },
					new ForgeDirection[] { ForgeDirection.SOUTH,
							ForgeDirection.EAST },
					new ForgeDirection[] { ForgeDirection.SOUTH,
							ForgeDirection.WEST } });
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderSpikes(tess, u, v, du, dv, new ForgeDirection[][] {
						new ForgeDirection[] { ForgeDirection.NORTH,
								ForgeDirection.SOUTH },
						new ForgeDirection[] { ForgeDirection.EAST,
								ForgeDirection.WEST },
						new ForgeDirection[] { ForgeDirection.NORTH,
								ForgeDirection.EAST },
						new ForgeDirection[] { ForgeDirection.NORTH,
								ForgeDirection.WEST },
						new ForgeDirection[] { ForgeDirection.SOUTH,
								ForgeDirection.EAST },
						new ForgeDirection[] { ForgeDirection.SOUTH,
								ForgeDirection.WEST } });
			}
			break;
		case 0:
			// Straight N/S
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH });
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderSpikes(tess, u, v, du, dv, new ForgeDirection[] {
						ForgeDirection.NORTH, ForgeDirection.SOUTH });
			}
			break;
		case 1:
			// Straight E/W
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.EAST, ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.EAST, ForgeDirection.WEST });
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderSpikes(tess, u, v, du, dv, new ForgeDirection[] {
						ForgeDirection.EAST, ForgeDirection.WEST });
			}
			break;
		case 2:
			// corner N/E
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.EAST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.EAST });
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderSpikes(tess, u, v, du, dv, new ForgeDirection[] {
						ForgeDirection.NORTH, ForgeDirection.EAST });
			}
			break;
		case 3:
			// corner N/W
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.WEST });
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderSpikes(tess, u, v, du, dv, new ForgeDirection[] {
						ForgeDirection.NORTH, ForgeDirection.WEST });
			}
			break;
		case 4:
			// corner S/E
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.EAST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.EAST });
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderSpikes(tess, u, v, du, dv, new ForgeDirection[] {
						ForgeDirection.SOUTH, ForgeDirection.EAST });
			}
			break;
		case 5:
			// corner S/W
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.WEST });
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderSpikes(tess, u, v, du, dv, new ForgeDirection[] {
						ForgeDirection.SOUTH, ForgeDirection.WEST });
			}
			break;
		case 6:
			// T-section NEW
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.EAST,
					ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[][] {
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.EAST },
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.WEST } });
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderSpikes(tess, u, v, du, dv, new ForgeDirection[][] {
						new ForgeDirection[] { ForgeDirection.NORTH,
								ForgeDirection.EAST },
						new ForgeDirection[] { ForgeDirection.NORTH,
								ForgeDirection.WEST } });
			}
			break;
		case 7:
			// T-section NES
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH,
					ForgeDirection.EAST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[][] {
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.EAST },
					new ForgeDirection[] { ForgeDirection.SOUTH,
							ForgeDirection.EAST } });
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderSpikes(tess, u, v, du, dv, new ForgeDirection[][] {
						new ForgeDirection[] { ForgeDirection.NORTH,
								ForgeDirection.EAST },
						new ForgeDirection[] { ForgeDirection.SOUTH,
								ForgeDirection.EAST } });
			}
			break;
		case 8:
			// T-section ESW
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.EAST,
					ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[][] {
					new ForgeDirection[] { ForgeDirection.SOUTH,
							ForgeDirection.EAST },
					new ForgeDirection[] { ForgeDirection.SOUTH,
							ForgeDirection.WEST } });
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderSpikes(tess, u, v, du, dv, new ForgeDirection[][] {
						new ForgeDirection[] { ForgeDirection.SOUTH,
								ForgeDirection.EAST },
						new ForgeDirection[] { ForgeDirection.SOUTH,
								ForgeDirection.WEST } });
			}
			break;
		case 9:
			// T-section NSW
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH,
					ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[][] {
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.WEST },
					new ForgeDirection[] { ForgeDirection.SOUTH,
							ForgeDirection.WEST } });
			if (meta == Refs.MetaValues.FenceBarbed) {
			}
			break;
		case 10:
			// X-section
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH,
					ForgeDirection.EAST, ForgeDirection.WEST });
		default:
			System.out
					.println("[Fencing]EXCEPTION! Unknown rendering direction of fence on position ["
							+ x + "," + y + "," + z + "].");
			break;
		}
		tess.addTranslation(-x, -y, -z);
		return true;
	}

	/**
	 * Render more than 1 fence post.
	 * 
	 * @param t
	 *            an instance of the minecraft tessellator
	 * @param u
	 *            lower U bound of the UV map's.
	 * @param v
	 *            lower V bound of the UV map.
	 * @param du
	 *            U size of 1 pixel on the texture.
	 * @param dv
	 *            V size of 1 pixel on the texture.
	 * @param dirs
	 *            The directions the posts should face. (Valid:
	 *            NORTH/EAST/SOUTH/WEST)
	 */
	private void renderPosts(Tessellator t, float u, float v, float du,
			float dv, ForgeDirection[] dirs) {
		for (ForgeDirection d : dirs) {
			renderPost(t, u, v, du, dv, d);
		}
	}

	/**
	 * Render the wires between more than 2 posts.
	 * 
	 * @param t
	 *            an instance of the minecraft tessellator
	 * @param u
	 *            lower U bound of the UV map's.
	 * @param v
	 *            lower V bound of the UV map.
	 * @param du
	 *            U size of 1 pixel on the texture.
	 * @param dv
	 *            V size of 1 pixel on the texture.
	 * @param dirs
	 *            The directions the wires should run. (Valid: any combination
	 *            of 2 NORTH/EAST/SOUTH/WEST)
	 */
	private void renderWires(Tessellator t, float u, float v, float du,
			float dv, ForgeDirection[][] dirs) {
		for (ForgeDirection[] dir : dirs) {
			renderWires(t, u, v, du, dv, dir);
		}
	}

	/**
	 * Render the wires between more than 2 posts.
	 * 
	 * @param t
	 *            an instance of the minecraft tessellator
	 * @param u
	 *            lower U bound of the UV map's.
	 * @param v
	 *            lower V bound of the UV map.
	 * @param du
	 *            U size of 1 pixel on the texture.
	 * @param dv
	 *            V size of 1 pixel on the texture.
	 * @param dirs
	 *            The directions the wires should run. (Valid: any combination
	 *            of 2 NORTH/EAST/SOUTH/WEST)
	 */
	private void renderSpikes(Tessellator t, float u, float v, float du,
			float dv, ForgeDirection[][] dirs) {
		for (ForgeDirection[] dir : dirs) {
			renderSpikes(t, u, v, du, dv, dir);
		}
	}

	/**
	 * Render a fence post.
	 * 
	 * @param t
	 *            an instance of the minecraft tessellator
	 * @param u
	 *            lower U bound of the UV map's.
	 * @param v
	 *            lower V bound of the UV map.
	 * @param du
	 *            U size of 1 pixel on the texture.
	 * @param dv
	 *            V size of 1 pixel on the texture.
	 * @param dir
	 *            The direction the posts should face. (Valid:
	 *            NORTH/EAST/SOUTH/WEST)
	 */
	private void renderPost(Tessellator t, float u, float v, float du,
			float dv, ForgeDirection dir) {

		float postWidth = 0.125f, xMod, zMod;

		// Prepare texture.
		v = v + dv * 2;
		float U = u + du * 8;
		float V = v + dv * 14;

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

	/**
	 * Render the wires between 2 fence posts.
	 * 
	 * @param t
	 *            an instance of the minecraft tessellator
	 * @param u
	 *            lower U bound of the UV map's.
	 * @param v
	 *            lower V bound of the UV map.
	 * @param du
	 *            U size of 1 pixel on the texture.
	 * @param dv
	 *            V size of 1 pixel on the texture.
	 * @param dir
	 *            The directions the wire should run. (Valid values:
	 *            NORTH/SOUTH/EAST/WEST.)
	 */
	private void renderWires(Tessellator t, float u, float v, float du,
			float dv, ForgeDirection[] dirs) {

		float wireWidth = 0.0625f, wireTop = 0.8125f, wireBottom = 0.75f;

		// Prepare texture.
		float U = u + du * Refs.textureSize;
		float V = v + dv * 2;

		float[][] loc = new float[2][4];
		ForgeDirection dir;
		for (int i = 0; i < 2; i++) {
			dir = dirs[i];
			switch (dir) {
			case NORTH:
				loc[i] = new float[] { 0.5f - wireWidth / 2.0f,
						0.5f + wireWidth / 2.0f, wireWidth, wireWidth };
				break;
			case EAST:
				loc[i] = new float[] { wireWidth, wireWidth,
						0.5f - wireWidth / 2.0f, 0.5f + wireWidth / 2.0f };
				break;
			case SOUTH:
				loc[i] = new float[] { 0.5f - wireWidth / 2.0f,
						0.5f + wireWidth / 2.0f, 1 - wireWidth, 1 - wireWidth };
				break;
			case WEST:
				loc[i] = new float[] { 1 - wireWidth, 1 - wireWidth,
						0.5f - wireWidth / 2.0f, 0.5f + wireWidth / 2.0f };
				break;
			default:
				return;
			}
		}

		for (int i = 0; i < 3; i++) {
			// Top
			t.addVertexWithUV(loc[1][0], wireTop, loc[1][2], u, v);
			t.addVertexWithUV(loc[0][0], wireTop, loc[0][2], u, V);
			t.addVertexWithUV(loc[0][1], wireTop, loc[0][3], U, V);
			t.addVertexWithUV(loc[1][1], wireTop, loc[1][3], U, v);

			wireTop = wireTop - 0.1875f;
			wireBottom = wireBottom - 0.1875f;
		}
	}

	/**
	 * Render the spikes on the wires between 2 fence posts opposite (NS or EW).
	 * Used for Barbed Wire Fences.
	 * 
	 * @param t
	 *            an instance of the minecraft tessellator
	 * @param u
	 *            lower U bound of the UV map's.
	 * @param v
	 *            lower V bound of the UV map.
	 * @param du
	 *            U size of 1 pixel on the texture.
	 * @param dv
	 *            V size of 1 pixel on the texture.
	 * @param dir
	 *            The direction the wire should run. (Valid values:
	 *            NORTH/SOUTH/EAST/WEST.)
	 */
	private void renderSpikes(Tessellator t, float u, float v, float du,
			float dv, ForgeDirection[] dirs) {

		// Prepare texture.
		float U = u + dv * Refs.textureSize;
		float V = v + dv * 2;

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
