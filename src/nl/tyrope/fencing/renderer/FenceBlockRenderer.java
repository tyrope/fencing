package nl.tyrope.fencing.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class FenceBlockRenderer implements ISimpleBlockRenderingHandler {

	int renderID;

	public FenceBlockRenderer(int renderId) {
		this.renderID = renderId;
	}

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

		// 0 = straight N/S
		// 1 = Straight E/S
		// 2 = corner N/E
		// 3 = corner N/W
		// 4 = corner S/E
		// 5 = corner S/W
		// 6 = T-section NEW
		// 7 = T-section NSE
		// 8 = T-section SEW
		// 9 = T-section NSW
		// 10 = X-section
		int type = 0; // TODO Determine type by neighbor blocks

		if (type < 2) {
			renderStraightPosts(tess, u, v + dv * 2, u + du * 8, v + dv * 14);
			renderStraightWires(tess, u, v, U, v + dv * 2);
			if (meta == Refs.MetaValues.FenceBarbed) {
				renderStraightSpikes(tess, u, v, U, v + dv * 2);
			}
		} else {
			System.out
					.println("[Fencing]EXCEPTION! Unknown rendering direction of fence on position ["
							+ x + "," + y + "," + z + "].");
		}

		tess.addTranslation(-x, -y, -z);
		return true;
	}

	private void renderStraightPosts(Tessellator t, float u, float v, float U,
			float V) {
		/* Left pole */
		// Top
		t.addVertexWithUV(0.4375f, 1, 1, u, v);
		t.addVertexWithUV(0.5625f, 1, 1, u, V);
		t.addVertexWithUV(0.5625f, 1, 0.875f, U, V);
		t.addVertexWithUV(0.4375f, 1, 0.875f, U, v);

		// Front
		t.addVertexWithUV(0.5625f, 1, 1, u, v);
		t.addVertexWithUV(0.5625f, 0, 1, u, V);
		t.addVertexWithUV(0.5625f, 0, 0.875f, U, V);
		t.addVertexWithUV(0.5625f, 1, 0.875f, U, v);

		// Back
		t.addVertexWithUV(0.4375f, 0, 1, u, v);
		t.addVertexWithUV(0.4375f, 1, 1, u, V);
		t.addVertexWithUV(0.4375f, 1, 0.875f, U, V);
		t.addVertexWithUV(0.4375f, 0, 0.875f, U, v);

		// Out
		t.addVertexWithUV(0.4375f, 0, 1, u, v);
		t.addVertexWithUV(0.5625f, 0, 1, u, V);
		t.addVertexWithUV(0.5625f, 1, 1, U, V);
		t.addVertexWithUV(0.4375f, 1, 1, U, v);

		// In
		t.addVertexWithUV(0.4375f, 1, 0.875f, u, v);
		t.addVertexWithUV(0.5625f, 1, 0.875f, u, V);
		t.addVertexWithUV(0.5625f, 0, 0.875f, U, V);
		t.addVertexWithUV(0.4375f, 0, 0.875f, U, v);

		// Bottom
		t.addVertexWithUV(0.4375f, 0, 0.875f, u, v);
		t.addVertexWithUV(0.5625f, 0, 0.875f, u, V);
		t.addVertexWithUV(0.5625f, 0, 1, U, V);
		t.addVertexWithUV(0.4375f, 0, 1, U, v);

		/* Right pole */
		// Top
		t.addVertexWithUV(0.4375f, 1, 0.125f, u, v);
		t.addVertexWithUV(0.5625f, 1, 0.125f, u, V);
		t.addVertexWithUV(0.5625f, 1, 0, U, V);
		t.addVertexWithUV(0.4375f, 1, 0, U, v);

		// Front
		t.addVertexWithUV(0.5625f, 1, 0.125f, u, v);
		t.addVertexWithUV(0.5625f, 0, 0.125f, u, V);
		t.addVertexWithUV(0.5625f, 0, 0, U, V);
		t.addVertexWithUV(0.5625f, 1, 0, U, v);

		// Back
		t.addVertexWithUV(0.4375f, 0, 0.125f, u, v);
		t.addVertexWithUV(0.4375f, 1, 0.125f, u, V);
		t.addVertexWithUV(0.4375f, 1, 0, U, V);
		t.addVertexWithUV(0.4375f, 0, 0, U, v);

		// Out
		t.addVertexWithUV(0.4375f, 0, 0.125f, u, v);
		t.addVertexWithUV(0.5625f, 0, 0.125f, u, V);
		t.addVertexWithUV(0.5625f, 1, 0.125f, U, V);
		t.addVertexWithUV(0.4375f, 1, 0.125f, U, v);

		// In
		t.addVertexWithUV(0.4375f, 1, 0, u, v);
		t.addVertexWithUV(0.5625f, 1, 0, u, V);
		t.addVertexWithUV(0.5625f, 0, 0, U, V);
		t.addVertexWithUV(0.4375f, 0, 0, U, v);

		// Bottom
		t.addVertexWithUV(0.4375f, 0, 0, u, v);
		t.addVertexWithUV(0.5625f, 0, 0, u, V);
		t.addVertexWithUV(0.5625f, 0, 0.125f, U, V);
		t.addVertexWithUV(0.4375f, 0, 0.125f, U, v);
	}

	private void renderStraightWires(Tessellator t, float u, float v, float U,
			float V) {
		/* Top Wire */
		// Top
		t.addVertexWithUV(0.46875f, 0.8125f, 0.875f, u, v);
		t.addVertexWithUV(0.53125f, 0.8125f, 0.875f, u, V);
		t.addVertexWithUV(0.53125f, 0.8125f, 0.125f, U, V);
		t.addVertexWithUV(0.46875f, 0.8125f, 0.125f, U, v);

		// Bottom
		t.addVertexWithUV(0.46875f, 0.75f, 0.125f, u, v);
		t.addVertexWithUV(0.53125f, 0.75f, 0.125f, u, V);
		t.addVertexWithUV(0.53125f, 0.75f, 0.875f, U, V);
		t.addVertexWithUV(0.46875f, 0.75f, 0.875f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.75f, 0.875f, u, v);
		t.addVertexWithUV(0.46875f, 0.8125f, 0.875f, u, V);
		t.addVertexWithUV(0.46875f, 0.8125f, 0.125f, U, V);
		t.addVertexWithUV(0.46875f, 0.75f, 0.125f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.75f, 0.125f, u, v);
		t.addVertexWithUV(0.53125f, 0.8125f, 0.125f, u, V);
		t.addVertexWithUV(0.53125f, 0.8125f, 0.875f, U, V);
		t.addVertexWithUV(0.53125f, 0.75f, 0.875f, U, v);

		/* Middle Wire */
		// Top
		t.addVertexWithUV(0.46875f, 0.625f, 0.875f, u, v);
		t.addVertexWithUV(0.53125f, 0.625f, 0.875f, u, V);
		t.addVertexWithUV(0.53125f, 0.625f, 0.125f, U, V);
		t.addVertexWithUV(0.46875f, 0.625f, 0.125f, U, v);

		// Bottom
		t.addVertexWithUV(0.46875f, 0.5625f, 0.125f, u, v);
		t.addVertexWithUV(0.53125f, 0.5625f, 0.125f, u, V);
		t.addVertexWithUV(0.53125f, 0.5625f, 0.875f, U, V);
		t.addVertexWithUV(0.46875f, 0.5625f, 0.875f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.5625f, 0.875f, u, v);
		t.addVertexWithUV(0.46875f, 0.625f, 0.875f, u, V);
		t.addVertexWithUV(0.46875f, 0.625f, 0.125f, U, V);
		t.addVertexWithUV(0.46875f, 0.5625f, 0.125f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.5625f, 0.125f, u, v);
		t.addVertexWithUV(0.53125f, 0.625f, 0.125f, u, V);
		t.addVertexWithUV(0.53125f, 0.625f, 0.875f, U, V);
		t.addVertexWithUV(0.53125f, 0.5625f, 0.875f, U, v);

		/* Bottom Wire */
		// Top
		t.addVertexWithUV(0.46875f, 0.4375f, 0.875f, u, v);
		t.addVertexWithUV(0.53125f, 0.4375f, 0.875f, u, V);
		t.addVertexWithUV(0.53125f, 0.4375f, 0.125f, U, V);
		t.addVertexWithUV(0.46875f, 0.4375f, 0.125f, U, v);

		// Bottom
		t.addVertexWithUV(0.46875f, 0.375f, 0.125f, u, v);
		t.addVertexWithUV(0.53125f, 0.375f, 0.125f, u, V);
		t.addVertexWithUV(0.53125f, 0.375f, 0.875f, U, V);
		t.addVertexWithUV(0.46875f, 0.375f, 0.875f, U, v);

		// Front
		t.addVertexWithUV(0.46875f, 0.375f, 0.875f, u, v);
		t.addVertexWithUV(0.46875f, 0.4375f, 0.875f, u, V);
		t.addVertexWithUV(0.46875f, 0.4375f, 0.125f, U, V);
		t.addVertexWithUV(0.46875f, 0.375f, 0.125f, U, v);

		// Back
		t.addVertexWithUV(0.53125f, 0.375f, 0.125f, u, v);
		t.addVertexWithUV(0.53125f, 0.4375f, 0.125f, u, V);
		t.addVertexWithUV(0.53125f, 0.4375f, 0.875f, U, V);
		t.addVertexWithUV(0.53125f, 0.375f, 0.875f, U, v);
	}

	private void renderStraightSpikes(Tessellator t, float u, float v, float U,
			float V) {
		// TODO (Maybe) semi-random location for the spikes?

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
