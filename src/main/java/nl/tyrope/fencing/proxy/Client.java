package nl.tyrope.fencing.proxy;

import nl.tyrope.fencing.renderer.FenceBlockRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class Client extends Common {

	@Override
	public void registerRenderers() {
		RenderingRegistry.registerBlockHandler(new FenceBlockRenderer());
	}
}