package nl.tyrope.fencing.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.blocks.FenceBlock;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class FenceBlockRenderer implements ISimpleBlockRenderingHandler {

	// posts
	private final int N_POST = 1;
	private final int S_POST = 2;
	private final int E_POST = 4;
	private final int W_POST = 8;
	// strings
	private final int NS_STRING = 16;
	private final int EW_STRING = 32;
	private final int NE_STRING = 64;
	private final int NW_STRING = 128;
	private final int SE_STRING = 256;
	private final int SW_STRING = 512;

	public static int renderID = RenderingRegistry.getNextAvailableRenderId();

	private float[] getTexCoords(IIcon c) {
		float u = c.getMinU();
		float v = c.getMinV();

		// Delta-u and Delta-v is the size of a 'pixel' on the UV map, add a
		// multiplier of this to u or v to get a pixel count from origin.
		float du = (c.getMaxU() - u) / Refs.textureSize;
		float dv = (c.getMaxV() - v) / Refs.textureSize;
		return new float[] { u, v, du, dv };
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess iba, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {

		IIcon c = block.getIcon(0, iba.getBlockMetadata(x, y, z));
		float[] tex = getTexCoords(c);
		float u = tex[0], v = tex[1], du = tex[2], dv = tex[3];

		boolean[] poleConnections = ((FenceBlock) block).getPoleConnections(
				iba, x, y, z);// NWSE
		int type = getType(((FenceBlock) block).getConnections(iba, x, y, z));

		Tessellator tess = Tessellator.instance;
		tess.addTranslation(x, y, z);
		tess.setNormal(0, 1, 0);
		tess.setColorRGBA(255, 255, 255, 255);
		tess.setBrightness(block.getMixedBrightnessForBlock(iba, x, y, z));

		if ((type & N_POST) != 0)
			renderPost(tess, u, v, du, dv, ForgeDirection.NORTH,
					poleConnections[0]);
		if ((type & S_POST) != 0)
			renderPost(tess, u, v, du, dv, ForgeDirection.SOUTH,
					poleConnections[2]);
		if ((type & E_POST) != 0)
			renderPost(tess, u, v, du, dv, ForgeDirection.EAST,
					poleConnections[3]);
		if ((type & W_POST) != 0)
			renderPost(tess, u, v, du, dv, ForgeDirection.WEST,
					poleConnections[1]);

		if ((type & NS_STRING) != 0)
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.SOUTH });
		if ((type & EW_STRING) != 0)
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.EAST, ForgeDirection.WEST });
		if ((type & NE_STRING) != 0)
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.EAST });
		if ((type & NW_STRING) != 0)
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.NORTH, ForgeDirection.WEST });
		if ((type & SE_STRING) != 0)
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.EAST });
		if ((type & SW_STRING) != 0)
			renderWires(tess, u, v, du, dv, new ForgeDirection[] {
					ForgeDirection.SOUTH, ForgeDirection.WEST });

		// Rendering Integration
		// Cable caps
		// North
		TileEntity te = iba.getTileEntity(x, y, z - 1);
		if (te != null) {
			renderCableCap(tess, u, v, du, dv, ForgeDirection.NORTH);
		}

		// East
		te = iba.getTileEntity(x - 1, y, z);
		if (te != null) {
			renderCableCap(tess, u, v, du, dv, ForgeDirection.EAST);
		}

		// South
		te = iba.getTileEntity(x + 1, y, z);
		if (te != null) {
			renderCableCap(tess, u, v, du, dv, ForgeDirection.SOUTH);
		}

		// East
		te = iba.getTileEntity(x, y, z + 1);
		if (te != null) {
			renderCableCap(tess, u, v, du, dv, ForgeDirection.WEST);
		}

		tess.addTranslation(-x, -y, -z);
		return true;
	}

	private int getType(boolean[] connections) {
		int type = 0;

		int cc = 0;
		for (boolean b : connections) {
			if (b) {
				cc++;
			}
		}
		// add posts
		if (cc >= 2) {
			// Straight... or corner.
			if (connections[0]) {
				type = type | N_POST;
			}
			if (connections[1]) {
				type = type | W_POST;
			}
			if (connections[2]) {
				type = type | S_POST;
			}
			if (connections[3]) {
				type = type | E_POST;
			}
		} else if (cc == 1) {
			// Straight.
			if (connections[0] || connections[2]) {
				type = type | N_POST | S_POST;
			} else {
				type = type | E_POST | W_POST;
			}
		} else {
			type = type | N_POST | E_POST | S_POST | W_POST;
			cc = 4;
		}
		// connect posts
		if (cc == 4) {
			type = type | NS_STRING | EW_STRING;
		} else {
			if ((type & (N_POST | S_POST)) == (N_POST | S_POST))
				type = type | NS_STRING;
			if ((type & (N_POST | E_POST)) == (N_POST | E_POST))
				type = type | NE_STRING;
			if ((type & (N_POST | W_POST)) == (N_POST | W_POST))
				type = type | NW_STRING;
			if ((type & (E_POST | W_POST)) == (E_POST | W_POST))
				type = type | EW_STRING;
			if ((type & (S_POST | E_POST)) == (S_POST | E_POST))
				type = type | SE_STRING;
			if ((type & (S_POST | W_POST)) == (S_POST | W_POST))
				type = type | SW_STRING;
		}

		return type;
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
			float dv, ForgeDirection dir, boolean connected) {

		float postWidth = 0.125f, xMod, zMod, wMod = 0f, eMod = 0f, nMod = 0f, sMod = 0f;

		switch (dir) {
		case NORTH:
			xMod = (1 - postWidth) / 2f;
			zMod = 0f;
			sMod = connected ? (postWidth / 2f) : 0;
			break;
		case EAST:
			xMod = 0f;
			zMod = (1 - postWidth) / 2f;
			wMod = connected ? (postWidth / 2f) : 0;
			break;
		case SOUTH:
			xMod = (1 - postWidth) / 2f;
			zMod = 1 - postWidth;
			nMod = connected ? (postWidth / 2f) : 0;
			break;
		case WEST:
			xMod = 1 - postWidth;
			zMod = (1 - postWidth) / 2f;
			eMod = connected ? (postWidth / 2f) : 0;
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
		t.addVertexWithUV(xMod + eMod, 1, zMod + postWidth - sMod, u, v);
		t.addVertexWithUV(xMod + postWidth - wMod, 1, zMod + postWidth - sMod,
				u, V);
		t.addVertexWithUV(xMod + postWidth - wMod, 1, zMod + nMod, U, V);
		t.addVertexWithUV(xMod + eMod, 1, zMod + nMod, U, v);

		// Bottom
		t.addVertexWithUV(xMod + eMod, 0, zMod + nMod, u, v);
		t.addVertexWithUV(xMod + postWidth - wMod, 0, zMod + nMod, u, V);
		t.addVertexWithUV(xMod + postWidth - wMod, 0, zMod + postWidth - sMod,
				U, V);
		t.addVertexWithUV(xMod + eMod, 0, zMod + postWidth - sMod, U, v);

		// Prepare texture for sides.
		// Position
		u = u - du * 8;
		v = v - dv * 20;
		// Size
		U = u + du * 8;
		V = v + dv * (Refs.textureSize - 4);

		// East
		t.addVertexWithUV(xMod + postWidth - wMod, 1, zMod + postWidth - sMod,
				u, v);
		t.addVertexWithUV(xMod + postWidth - wMod, 0, zMod + postWidth - sMod,
				u, V);
		t.addVertexWithUV(xMod + postWidth - wMod, 0, zMod + nMod, U, V);
		t.addVertexWithUV(xMod + postWidth - wMod, 1, zMod + nMod, U, v);

		// West
		t.addVertexWithUV(xMod + eMod, 0, zMod + postWidth - sMod, U, V);
		t.addVertexWithUV(xMod + eMod, 1, zMod + postWidth - sMod, u, v);
		t.addVertexWithUV(xMod + eMod, 1, zMod + nMod, u, v);
		t.addVertexWithUV(xMod + eMod, 0, zMod + nMod, u, V);

		// South
		t.addVertexWithUV(xMod + eMod, 0, zMod + postWidth - sMod, u, V);
		t.addVertexWithUV(xMod + postWidth - wMod, 0, zMod + postWidth - sMod,
				U, V);
		t.addVertexWithUV(xMod + postWidth - wMod, 1, zMod + postWidth - sMod,
				U, v);
		t.addVertexWithUV(xMod + eMod, 1, zMod + postWidth - sMod, u, v);

		// North
		t.addVertexWithUV(xMod + eMod, 1, zMod + nMod, U, v);
		t.addVertexWithUV(xMod + postWidth - wMod, 1, zMod + nMod, u, v);
		t.addVertexWithUV(xMod + postWidth - wMod, 0, zMod + nMod, u, V);
		t.addVertexWithUV(xMod + eMod, 0, zMod + nMod, U, V);

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

	/**
	 * Render the caps on adjacent IC2 wires.
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
	 * @param insulated
	 *            Whether or not the cable connected to is insulated
	 */
	private void renderCableCap(Tessellator t, float u, float v, float du,
			float dv, ForgeDirection dir) {
		// Prepare texture.
		// Position
		u = u + du * 8;
		v = v + dv * 24;
		// Size
		float U = u + du * 8;
		float V = v + dv * 8;

		// set cap size.
		float tMin = 6.5f / 16f, tMax = 9.5f / 16f;

		switch (dir) {
		case NORTH:
			t.addVertexWithUV(tMin, tMin, 0, u, V);
			t.addVertexWithUV(tMax, tMin, 0, U, V);
			t.addVertexWithUV(tMax, tMax, 0, U, v);
			t.addVertexWithUV(tMin, tMax, 0, u, v);
			break;
		case SOUTH:
			t.addVertexWithUV(1, tMin, tMax, U, V);
			t.addVertexWithUV(1, tMax, tMax, u, v);
			t.addVertexWithUV(1, tMax, tMin, u, v);
			t.addVertexWithUV(1, tMin, tMin, u, V);
			break;
		case EAST:
			t.addVertexWithUV(0, tMax, tMax, U, V);
			t.addVertexWithUV(0, tMin, tMax, u, v);
			t.addVertexWithUV(0, tMin, tMin, u, v);
			t.addVertexWithUV(0, tMax, tMin, u, V);
			break;
		case WEST:
			t.addVertexWithUV(tMin, tMax, 1, u, V);
			t.addVertexWithUV(tMax, tMax, 1, U, V);
			t.addVertexWithUV(tMax, tMin, 1, U, v);
			t.addVertexWithUV(tMin, tMin, 1, u, v);
			break;
		default:
			return;
		}
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return FenceBlockRenderer.renderID;
	}
}
