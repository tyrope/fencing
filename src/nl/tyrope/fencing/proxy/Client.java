package nl.tyrope.fencing.proxy;

import nl.tyrope.fencing.blocks.FenceBlock;
import nl.tyrope.fencing.renderer.FenceBlockRenderer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class Client extends Common {

	@Override
	public void registerRenderers(FenceBlock fenceBlock) {
		int id = RenderingRegistry.getNextAvailableRenderId();
		ISimpleBlockRenderingHandler fenceBlockRenderer = new FenceBlockRenderer(
				id);
		fenceBlock.renderId = id;
		RenderingRegistry.registerBlockHandler(fenceBlock.getRenderType(),
				fenceBlockRenderer);
	}
}