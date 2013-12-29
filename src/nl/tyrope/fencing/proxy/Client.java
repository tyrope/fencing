package nl.tyrope.fencing.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;
import nl.tyrope.fencing.render.*;
import nl.tyrope.fencing.tileEntities.*;

public class Client extends Common {

	@Override
	public void registerRenderers() {
		//TODO: Add proper renderer for all different fences...
		ClientRegistry.bindTileEntitySpecialRenderer(FenceEntity.class,
				new FenceRenderer("String"));
	}
}