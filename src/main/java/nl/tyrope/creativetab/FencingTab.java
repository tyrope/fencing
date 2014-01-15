package nl.tyrope.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import nl.tyrope.fencing.Refs;

public class FencingTab extends CreativeTabs {

	public FencingTab() {
		super("Fencing");
	}

	@Override
	public int getTabIconItemIndex() {
		return Refs.FenceID;
	}
}
