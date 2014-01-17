package nl.tyrope.fencing.proxy;

import nl.tyrope.fencing.blocks.FenceBlock;
import nl.tyrope.fencing.renderer.FenceBlockRenderer;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class Client extends Common {

	@Override
	public void registerRenderers(FenceBlock[] blocks) {
		ISimpleBlockRenderingHandler fenceBlockRenderer = new FenceBlockRenderer();
		for (FenceBlock block : blocks) {
			block.renderId = fenceBlockRenderer.getRenderId();
			RenderingRegistry.registerBlockHandler(block.getRenderType(),
					fenceBlockRenderer);
		}
	}
}