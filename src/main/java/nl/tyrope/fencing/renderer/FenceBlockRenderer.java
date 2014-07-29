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
import nl.tyrope.fencing.blocks.FenceTopBlock;
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

	private float[] getTextureCoords(IIcon icon) {
		float u = icon.getMinU();
		float v = icon.getMinV();

		// Delta-u and Delta-v is the size of a 'pixel' on the UV map, add a
		// multiplier of this to u or v to get a pixel count from origin.
		float du = (icon.getMaxU() - u) / Refs.textureSize;
		float dv = (icon.getMaxV() - v) / Refs.textureSize;
		return new float[] { u, v, du, dv };
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		if (block instanceof FenceBlock) {
			return renderWorldBlock(blockAccess, x, y, z, (FenceBlock) block, modelId, renderer);
		} else if (block instanceof FenceTopBlock) {
			return true;
		}

		return false;
	}

	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z,
			FenceBlock fenceBlock, int modelId, RenderBlocks renderer) {
		IIcon blockIcon = fenceBlock.getIcon(0, blockAccess.getBlockMetadata(x, y, z));
		float[] textureCoords = getTextureCoords(blockIcon);
		float u = textureCoords[0], v = textureCoords[1], du = textureCoords[2], dv = textureCoords[3];

		boolean[] poleConnections = fenceBlock.getPoleConnections(blockAccess, x, y, z);// NWSET
		int nbConnection = 0;
		for (boolean connection : poleConnections) {
			if (connection) {
				nbConnection++;
			}
		}

		boolean smoothEnd = nbConnection == 1 && !poleConnections[4];
		int type = getType(fenceBlock.getConnections(blockAccess, x, y, z));

		Tessellator tessellator = Tessellator.instance;
		tessellator.addTranslation(x, y, z);
		tessellator.setNormal(0, 1, 0);
		tessellator.setColorRGBA(255, 255, 255, 255);
		tessellator.setBrightness(fenceBlock.getMixedBrightnessForBlock(blockAccess, x, y, z));

		if ((type & N_POST) != 0)
			renderPost(tessellator, u, v, du, dv, ForgeDirection.NORTH,
					poleConnections[0], smoothEnd && poleConnections[2]);
		if ((type & S_POST) != 0)
			renderPost(tessellator, u, v, du, dv, ForgeDirection.SOUTH,
					poleConnections[2], smoothEnd && poleConnections[0]);
		if ((type & E_POST) != 0)
			renderPost(tessellator, u, v, du, dv, ForgeDirection.EAST,
					poleConnections[3], smoothEnd && poleConnections[1]);
		if ((type & W_POST) != 0)
			renderPost(tessellator, u, v, du, dv, ForgeDirection.WEST,
					poleConnections[1], smoothEnd && poleConnections[3]);

		if ((type & NS_STRING) != 0)
			renderWires(tessellator, u, v, du, dv, ForgeDirection.NORTH, ForgeDirection.SOUTH,
					!smoothEnd ? ForgeDirection.UNKNOWN : poleConnections[0] ? ForgeDirection.NORTH : ForgeDirection.SOUTH);
		if ((type & EW_STRING) != 0)
			renderWires(tessellator, u, v, du, dv, ForgeDirection.EAST, ForgeDirection.WEST,
					!smoothEnd ? ForgeDirection.UNKNOWN : poleConnections[1] ? ForgeDirection.EAST : ForgeDirection.WEST);
		if ((type & NE_STRING) != 0)
			renderWires(tessellator, u, v, du, dv, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.UNKNOWN);
		if ((type & NW_STRING) != 0)
			renderWires(tessellator, u, v, du, dv, ForgeDirection.NORTH, ForgeDirection.WEST, ForgeDirection.UNKNOWN);
		if ((type & SE_STRING) != 0)
			renderWires(tessellator, u, v, du, dv, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.UNKNOWN);
		if ((type & SW_STRING) != 0)
			renderWires(tessellator, u, v, du, dv, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.UNKNOWN);

		// Rendering Integration
		// Cable caps
		// North
		TileEntity tileEntity = blockAccess.getTileEntity(x, y, z - 1);
		if (tileEntity != null) {
			renderCableCap(tessellator, u, v, du, dv, ForgeDirection.NORTH);
		}

		// East
		tileEntity = blockAccess.getTileEntity(x - 1, y, z);
		if (tileEntity != null) {
			renderCableCap(tessellator, u, v, du, dv, ForgeDirection.EAST);
		}

		// South
		tileEntity = blockAccess.getTileEntity(x + 1, y, z);
		if (tileEntity != null) {
			renderCableCap(tessellator, u, v, du, dv, ForgeDirection.SOUTH);
		}

		// East
		tileEntity = blockAccess.getTileEntity(x, y, z + 1);
		if (tileEntity != null) {
			renderCableCap(tessellator, u, v, du, dv, ForgeDirection.WEST);
		}

		tessellator.addTranslation(-x, -y, -z);
		return true;
	}

	private int getType(boolean[] connections) {
		int type = 0;

		int nbConnection = 0;
		for (boolean connection : connections) {
			if (connection) {
				nbConnection++;
			}
		}

		// add posts
		if (nbConnection >= 2) {
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
		} else if (nbConnection == 1) {
			// Straight.
			if (connections[0] || connections[2]) {
				type = type | N_POST | S_POST;
			} else {
				type = type | E_POST | W_POST;
			}
		} else {
			type = type | N_POST | E_POST | S_POST | W_POST;
			nbConnection = 4;
		}

		// connect posts
		if (nbConnection == 4) {
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
	 * @param tessellator The tessellator instance.
	 * @param u		The lower U bound of the UV map.
	 * @param v		The lower V bound of the UV map.
	 * @param du	The U size of 1 pixel on the texture.
	 * @param dv	The V size of 1 pixel on the texture.
	 * @param direction	The direction the posts should face. (Valid: NORTH/EAST/SOUTH/WEST)
	 * @param connected	Is the fence connected in that direction.
	 * @param smooth	Render a 'smooth' pole.
	 */
	private void renderPost(Tessellator tessellator, float u, float v, float du,
			float dv, ForgeDirection dir, boolean connected, boolean smooth) {

		float postWidth = 1/8.0f;
		float xMod = 0f, zMod = 0f;
		float wMod = 0f, eMod = 0f, nMod = 0f, sMod = 0f, bMod = 0f;
		float tMod = smooth ? 1/16.0f * 10 : 1f;

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
		float uMax = u + du * 8;
		float vMax = v + dv * 8;

		// Top
		tessellator.addVertexWithUV(xMod + eMod, tMod, zMod + postWidth - sMod, u, v);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, tMod, zMod + postWidth - sMod, u, vMax);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, tMod, zMod + nMod, uMax, vMax);
		tessellator.addVertexWithUV(xMod + eMod, tMod, zMod + nMod, uMax, v);

		// Bottom
		tessellator.addVertexWithUV(xMod + eMod, bMod, zMod + nMod, u, v);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, bMod, zMod + nMod, u, vMax);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, bMod, zMod + postWidth - sMod, uMax, vMax);
		tessellator.addVertexWithUV(xMod + eMod, bMod, zMod + postWidth - sMod, uMax, v);

		// Prepare texture for sides.
		// Position
		u = u - du * 8;
		v = v - dv * 20;
		// Size
		uMax = u + du * 8;
		vMax = v + dv * (Refs.textureSize - 4);

		// East
		tessellator.addVertexWithUV(xMod + postWidth - wMod, tMod, zMod + postWidth - sMod, u, v);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, bMod, zMod + postWidth - sMod, u, vMax);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, bMod, zMod + nMod, uMax, vMax);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, tMod, zMod + nMod, uMax, v);

		// West
		tessellator.addVertexWithUV(xMod + eMod, bMod, zMod + postWidth - sMod, uMax, vMax);
		tessellator.addVertexWithUV(xMod + eMod, tMod, zMod + postWidth - sMod, u, v);
		tessellator.addVertexWithUV(xMod + eMod, tMod, zMod + nMod, u, v);
		tessellator.addVertexWithUV(xMod + eMod, bMod, zMod + nMod, u, vMax);

		// South
		tessellator.addVertexWithUV(xMod + eMod, bMod, zMod + postWidth - sMod, u, vMax);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, bMod, zMod + postWidth - sMod, uMax, vMax);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, tMod, zMod + postWidth - sMod, uMax, v);
		tessellator.addVertexWithUV(xMod + eMod, tMod, zMod + postWidth - sMod, u, v);

		// North
		tessellator.addVertexWithUV(xMod + eMod, tMod, zMod + nMod, uMax, v);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, tMod, zMod + nMod, u, v);
		tessellator.addVertexWithUV(xMod + postWidth - wMod, bMod, zMod + nMod, u, vMax);
		tessellator.addVertexWithUV(xMod + eMod, bMod, zMod + nMod, uMax, vMax);
	}

	/**
	 * Render the wires between 2 fence posts.
	 * 
	 * @param tessellator	The tessellator instance.
	 * @param u		The lower U bound of the UV map.
	 * @param v		The lower V bound of the UV map.
	 * @param du	The U size of 1 pixel on the texture.
	 * @param dv	The V size of 1 pixel on the texture.
	 * @param startSide	The wire starting position. (Valid values: NORTH/SOUTH/EAST/WEST.)
	 * @param endSide	The wire ending position. (Valid values: NORTH/SOUTH/EAST/WEST.)
	 * @param smoothSide	The side of the fence to smooth.
	 */
	private void renderWires(Tessellator tessellator, float u, float v, float du,
			float dv, ForgeDirection startSide, ForgeDirection endSide,
			ForgeDirection smoothSide) {

		float wireWidth = 1/16.0f;
		float wireTop = 1 - (1/32.0f * 5);
		float wireBottom = wireTop - wireWidth;

		// Prepare texture.
		float U = u + du * Refs.textureSize;
		float V = v + dv * 2;

		/**
		 * { Start side : { x Min, x Max, z Min, z Max, y Min, y Max },
		 *   End side :   { x Min, x Max, z Min, z Max, y Min, y Max } }
		 */
		float[][] loc = new float[2][6];

		for (int i = 0; i < 2; i++) {
			switch (i == 0 ? startSide : endSide) {
			case NORTH:
				loc[i] = new float[] { 0.5f + wireWidth / 2.0f,
						0.5f - wireWidth / 2.0f, wireWidth, wireWidth,
						smoothSide == ForgeDirection.SOUTH ? wireTop - 0.3f : wireTop,
						smoothSide == ForgeDirection.SOUTH ? wireBottom - 0.3f : wireBottom};
				break;
			case EAST:
				loc[i] = new float[] { wireWidth, wireWidth,
						0.5f + wireWidth / 2.0f, 0.5f - wireWidth / 2.0f,
						smoothSide == ForgeDirection.EAST ? wireTop - 0.3f : wireTop,
						smoothSide == ForgeDirection.EAST ? wireBottom - 0.3f : wireBottom};
				break;
			case SOUTH:
				loc[i] = new float[] { 0.5f - wireWidth / 2.0f,
						0.5f + wireWidth / 2.0f, 1 - wireWidth, 1 - wireWidth,
						smoothSide == ForgeDirection.NORTH ? wireTop - 0.3f : wireTop,
						smoothSide == ForgeDirection.NORTH ? wireBottom - 0.3f : wireBottom};
				break;
			case WEST:
				loc[i] = new float[] { 1 - wireWidth, 1 - wireWidth,
						0.5f - wireWidth / 2.0f, 0.5f + wireWidth / 2.0f,
						smoothSide == ForgeDirection.WEST ? wireTop - 0.3f : wireTop,
						smoothSide == ForgeDirection.WEST ? wireBottom - 0.3f : wireBottom};
				break;
			default:
				return;
			}
		}

		for (int i = 0; i < 3; i++) {
			// Top
			tessellator.addVertexWithUV(loc[1][1], loc[1][4], loc[1][2], u, V);
			tessellator.addVertexWithUV(loc[0][0], loc[0][4], loc[0][3], U, V);
			tessellator.addVertexWithUV(loc[0][1], loc[0][4], loc[0][2], U, v);
			tessellator.addVertexWithUV(loc[1][0], loc[1][4], loc[1][3], u, v);

			// Bottom
			tessellator.addVertexWithUV(loc[1][1], loc[1][5], loc[1][2], U, V);
			tessellator.addVertexWithUV(loc[1][0], loc[1][5], loc[1][3], U, v);
			tessellator.addVertexWithUV(loc[0][1], loc[0][5], loc[0][2], u, v);
			tessellator.addVertexWithUV(loc[0][0], loc[0][5], loc[0][3], u, V);

			// Front
			tessellator.addVertexWithUV(loc[1][1], loc[1][5], loc[1][2], u, V);
			tessellator.addVertexWithUV(loc[0][0], loc[0][5], loc[0][3], U, V);
			tessellator.addVertexWithUV(loc[0][0], loc[0][4], loc[0][3], U, v);
			tessellator.addVertexWithUV(loc[1][1], loc[1][4], loc[1][2], u, v);

			// Back
			tessellator.addVertexWithUV(loc[1][0], loc[1][4], loc[1][3], u, V);
			tessellator.addVertexWithUV(loc[0][1], loc[0][4], loc[0][2], U, V);
			tessellator.addVertexWithUV(loc[0][1], loc[0][5], loc[0][2], U, v);
			tessellator.addVertexWithUV(loc[1][0], loc[1][5], loc[1][3], u, v);

			loc[0][4] -= wireWidth * 3;
			loc[0][5] = loc[0][4] - wireWidth;
			loc[1][4] -= wireWidth * 3;
			loc[1][5] = loc[1][4] - wireWidth;
		}
	}

	/**
	 * Render the caps on adjacent IC2 wires.
	 * 
	 * @param tessellator	The tessellator instance.
	 * @param u		The lower U bound of the UV map's.
	 * @param v		The lower V bound of the UV map.
	 * @param du	The U size of 1 pixel on the texture.
	 * @param dv	The V size of 1 pixel on the texture.
	 * @param direction	The directions the wire should run. (Valid values: NORTH/SOUTH/EAST/WEST.)
	 */
	private void renderCableCap(Tessellator tessellator, float u, float v, float du,
			float dv, ForgeDirection direction) {
		// Prepare texture.
		// Position
		u = u + du * 8;
		v = v + dv * 24;
		// Size
		float uMax = u + du * 8;
		float vMax = v + dv * 8;

		// set cap size.
		float tMin = 6.5f / 16f, tMax = 9.5f / 16f;

		switch (direction) {
		case NORTH:
			tessellator.addVertexWithUV(tMin, tMin, 0, u, vMax);
			tessellator.addVertexWithUV(tMax, tMin, 0, uMax, vMax);
			tessellator.addVertexWithUV(tMax, tMax, 0, uMax, v);
			tessellator.addVertexWithUV(tMin, tMax, 0, u, v);
			break;
		case SOUTH:
			tessellator.addVertexWithUV(1, tMin, tMax, uMax, vMax);
			tessellator.addVertexWithUV(1, tMax, tMax, u, v);
			tessellator.addVertexWithUV(1, tMax, tMin, u, v);
			tessellator.addVertexWithUV(1, tMin, tMin, u, vMax);
			break;
		case EAST:
			tessellator.addVertexWithUV(0, tMax, tMax, uMax, vMax);
			tessellator.addVertexWithUV(0, tMin, tMax, u, v);
			tessellator.addVertexWithUV(0, tMin, tMin, u, v);
			tessellator.addVertexWithUV(0, tMax, tMin, u, vMax);
			break;
		case WEST:
			tessellator.addVertexWithUV(tMin, tMax, 1, u, vMax);
			tessellator.addVertexWithUV(tMax, tMax, 1, uMax, vMax);
			tessellator.addVertexWithUV(tMax, tMin, 1, uMax, v);
			tessellator.addVertexWithUV(tMin, tMin, 1, u, v);
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
