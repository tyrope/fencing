package nl.tyrope.fencing.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;
import nl.tyrope.fencing.render.*;
import nl.tyrope.fencing.tileEntities.*;

public class Client extends Common {

	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(StringFenceEntity.class,
				new BasicFenceRenderer("String"));

		ClientRegistry.bindTileEntitySpecialRenderer(IronFenceEntity.class,
				new BasicFenceRenderer("Iron"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(BarbedFenceEntity.class,
				new BasicFenceRenderer("Barbed"));

		ClientRegistry.bindTileEntitySpecialRenderer(SillyFenceEntity.class,
				new BasicFenceRenderer("Silly"));
	}
}