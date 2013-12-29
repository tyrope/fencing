package nl.tyrope.fencing.proxy;

import nl.tyrope.fencing.render.FenceRenderer;
import nl.tyrope.fencing.tileEntities.FenceEntity;
import cpw.mods.fml.client.registry.ClientRegistry;

public class Client extends Common {

	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(FenceEntity.class,
				new FenceRenderer());
	}
}