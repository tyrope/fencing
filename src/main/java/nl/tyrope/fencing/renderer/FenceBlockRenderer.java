package nl.tyrope.fencing.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.blocks.FenceBlock;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class FenceBlockRenderer implements ISimpleBlockRenderingHandler {

	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess iba, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {

		int meta = iba.getBlockMetadata(x, y, z);
		Icon c = block.getIcon(0, meta);
		float u = c.getMinU();
		float v = c.getMinV();

		// Delta-u and Delta-v is the size of a 'pixel' on the UV map, add a
		// multiplier of this to u or v to get a pixel count from origin.
		float du = (c.getMaxU() - u) / Refs.textureSize;
		float dv = (c.getMaxV() - v) / Refs.textureSize;

		Tessellator tess = Tessellator.instance;
		tess.addTranslation(x, y, z);
		tess.setNormal(0, 1, 0);
		tess.setColorRGBA(255, 255, 255, 255);
		tess.setBrightness(block.getMixedBrightnessForBlock(iba, x, y, z));

		int type = getRenderType(iba, x, y, z, (FenceBlock) block);

		switch (type) {
		case 0: // Straight N/S
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH });
			break;
		case 1: // Straight E/W
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.EAST, ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.EAST, ForgeDirection.WEST });
			break;
		case 2: // corner N/E
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.EAST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.EAST });
			break;
		case 3: // corner N/W
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.WEST });
			break;
		case 4: // corner S/E
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.EAST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.EAST });
			break;
		case 5: // corner S/W
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.WEST });
			break;
		case 6: // T-section NEW
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.EAST,
					ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[][] {
					new ForgeDirection[] { ForgeDirection.EAST,
							ForgeDirection.WEST },
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.EAST },
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.WEST } });
			break;
		case 7: // T-section NES
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH,
					ForgeDirection.EAST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[][] {
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.SOUTH },
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.EAST },
					new ForgeDirection[] { ForgeDirection.SOUTH,
							ForgeDirection.EAST } });
			break;
		case 8: // T-section ESW
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.EAST,
					ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[][] {
					new ForgeDirection[] { ForgeDirection.EAST,
							ForgeDirection.WEST },
					new ForgeDirection[] { ForgeDirection.SOUTH,
							ForgeDirection.EAST },
					new ForgeDirection[] { ForgeDirection.SOUTH,
							ForgeDirection.WEST } });
			break;
		case 9: // T-section NSW
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH,
					ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[][] {
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.SOUTH },
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.WEST },
					new ForgeDirection[] { ForgeDirection.SOUTH,
							ForgeDirection.WEST } });
			break;
		case 10: // X-section
			renderPosts(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH,
					ForgeDirection.EAST, ForgeDirection.WEST });
			renderWires(tess, u, v, du, dv, new ForgeDirection[][] {
					new ForgeDirection[] { ForgeDirection.NORTH,
							ForgeDirection.SOUTH },
					new ForgeDirection[] { ForgeDirection.EAST,
							ForgeDirection.WEST } });
			break;
		default:
			System.out
					.println(String
							.format("[Fencing]EXCEPTION! Unknown rendering direction %s of fence on position [%s,%s,%s].",
									type, x, y, z));
			break;
		}
		tess.addTranslation(-x, -y, -z);
		return true;
	}

	private int getRenderType(IBlockAccess iba, int x, int y, int z,
			FenceBlock block) {
		AxisAlignedBB bb = block.getHitBox(iba, x, y, z);
		double minX = bb.minX - x, maxX = bb.maxX - x, minZ = bb.minZ - z, maxZ = bb.maxZ
				- z;
		int cc = 0; // Connection Count

		// Count the amount of connections.
		if (minX == 0) {
			cc++;
		}
		if (minZ == 0) {
			cc++;
		}
		if (maxX == 1) {
			cc++;
		}
		if (maxZ == 1) {
			cc++;
		}

		if (cc == 2) {
			if (minZ == 0 && maxZ == 1) {
				// 0 Straight N/S
				return 0;
			} else if (minX == 0 && maxX == 1) {
				// 1 Straight E/W
				return 1;
			} else if (minX == 0 && minZ == 0) {
				// 2 corner N/E
				return 2;
			} else if (maxX == 1 && minZ == 0) {
				// 3 corner N/W
				return 3;
			} else if (minX == 0 && maxZ == 1) {
				// 4 corner S/E
				return 4;
			} else {
				// 5 corner S/W
				return 5;
			}
		} else if (cc == 3) {
			if (maxZ != 1) {
				// 6 T-section NEW
				return 6;
			} else if (maxX != 1) {
				// 7 T-section NES
				return 7;
			} else if (minZ != 0) {
				// 8 T-section ESW
				return 8;
			} else {
				// 9 T-section NSW
				return 9;
			}
		} else {
			// X section.
			return 10;
		}
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

		// Prepare texture for top/bottom.
		// Position
		u = u + du * 8;
		v = v + dv * 24;
		// Size
		float U = u + du * 8;
		float V = v + dv * 8;

		// Top
		t.addVertexWithUV(xMod, 1, zMod + postWidth, u, v);
		t.addVertexWithUV(xMod + postWidth, 1, zMod + postWidth, u, V);
		t.addVertexWithUV(xMod + postWidth, 1, zMod, U, V);
		t.addVertexWithUV(xMod, 1, zMod, U, v);

		// Bottom
		t.addVertexWithUV(xMod, 0, zMod, u, v);
		t.addVertexWithUV(xMod + postWidth, 0, zMod, u, V);
		t.addVertexWithUV(xMod + postWidth, 0, zMod + postWidth, U, V);
		t.addVertexWithUV(xMod, 0, zMod + postWidth, U, v);

		// Prepare texture for sides.
		// Position
		u = u - du * 8;
		v = v - dv * 20;
		// Size
		U = u + du * 8;
		V = v + dv * (Refs.textureSize - 4);

		// East
		t.addVertexWithUV(xMod + postWidth, 1, zMod + postWidth, u, v);
		t.addVertexWithUV(xMod + postWidth, 0, zMod + postWidth, u, V);
		t.addVertexWithUV(xMod + postWidth, 0, zMod, U, V);
		t.addVertexWithUV(xMod + postWidth, 1, zMod, U, v);

		// West
		t.addVertexWithUV(xMod, 0, zMod + postWidth, U, V);
		t.addVertexWithUV(xMod, 1, zMod + postWidth, u, v);
		t.addVertexWithUV(xMod, 1, zMod, u, v);
		t.addVertexWithUV(xMod, 0, zMod, u, V);

		// South
		t.addVertexWithUV(xMod, 0, zMod + postWidth, u, V);
		t.addVertexWithUV(xMod + postWidth, 0, zMod + postWidth, U, V);
		t.addVertexWithUV(xMod + postWidth, 1, zMod + postWidth, U, v);
		t.addVertexWithUV(xMod, 1, zMod + postWidth, u, v);

		// North
		t.addVertexWithUV(xMod, 1, zMod, U, v);
		t.addVertexWithUV(xMod + postWidth, 1, zMod, u, v);
		t.addVertexWithUV(xMod + postWidth, 0, zMod, u, V);
		t.addVertexWithUV(xMod, 0, zMod, U, V);
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

		/**
		 * { Start side: { x Min, x Max, z Min, z Max}, End side: {x Min, x Max,
		 * z Min, z Max} }
		 */
		float[][] loc = new float[2][4];

		ForgeDirection dir;
		for (int i = 0; i < 2; i++) {
			dir = dirs[i];
			switch (dir) {
			case NORTH:
				loc[i] = new float[] { 0.5f + wireWidth / 2.0f,
						0.5f - wireWidth / 2.0f, wireWidth, wireWidth };
				break;
			case EAST:
				loc[i] = new float[] { wireWidth, wireWidth,
						0.5f + wireWidth / 2.0f, 0.5f - wireWidth / 2.0f };
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
			t.addVertexWithUV(loc[1][1], wireTop, loc[1][2], u, V);
			t.addVertexWithUV(loc[0][0], wireTop, loc[0][3], U, V);
			t.addVertexWithUV(loc[0][1], wireTop, loc[0][2], U, v);
			t.addVertexWithUV(loc[1][0], wireTop, loc[1][3], u, v);

			// Bottom
			t.addVertexWithUV(loc[1][1], wireBottom, loc[1][2], U, V);
			t.addVertexWithUV(loc[1][0], wireBottom, loc[1][3], U, v);
			t.addVertexWithUV(loc[0][1], wireBottom, loc[0][2], u, v);
			t.addVertexWithUV(loc[0][0], wireBottom, loc[0][3], u, V);

			// Front
			t.addVertexWithUV(loc[1][1], wireBottom, loc[1][2], u, V);
			t.addVertexWithUV(loc[0][0], wireBottom, loc[0][3], U, V);
			t.addVertexWithUV(loc[0][0], wireTop, loc[0][3], U, v);
			t.addVertexWithUV(loc[1][1], wireTop, loc[1][2], u, v);

			// Back
			t.addVertexWithUV(loc[1][0], wireTop, loc[1][3], u, V);
			t.addVertexWithUV(loc[0][1], wireTop, loc[0][2], U, V);
			t.addVertexWithUV(loc[0][1], wireBottom, loc[0][2], U, v);
			t.addVertexWithUV(loc[1][0], wireBottom, loc[1][3], u, v);

			wireTop = wireTop - 0.1875f;
			wireBottom = wireBottom - 0.1875f;
		}
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
