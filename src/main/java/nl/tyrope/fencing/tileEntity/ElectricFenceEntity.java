package nl.tyrope.fencing.tileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import nl.tyrope.fencing.Refs;

public class ElectricFenceEntity extends TileEntity {

	private boolean initialized;
	private double buffer;

	public ElectricFenceEntity() {
		super();
	}

	public double zap() {
		// TODO: Actual maths involving like, damage and stuff.
		int ret = 1;
		if (blockMetadata == Refs.MetaValues.FenceElectricTin) {
			return ret;
		} else {
			return ret * 2;
		}
	}
}
