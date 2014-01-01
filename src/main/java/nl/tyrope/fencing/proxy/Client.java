package nl.tyrope.fencing.proxy;

import nl.tyrope.fencing.blocks.FenceBlock;
import nl.tyrope.fencing.renderer.FenceBlockRenderer;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class Client extends Common {

	@Override
	public void registerRenderers(FenceBlock fenceBlock) {
		ISimpleBlockRenderingHandler fenceBlockRenderer = new FenceBlockRenderer();
		fenceBlock.renderId = fenceBlockRenderer.getRenderId();
		RenderingRegistry.registerBlockHandler(fenceBlock.getRenderType(),
				fenceBlockRenderer);
	}
}