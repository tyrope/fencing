package nl.tyrope.fencing.tileEntity;

import net.minecraft.tileentity.TileEntity;
import nl.tyrope.fencing.Refs;

public class ElectricFenceEntity extends TileEntity {

	public ElectricFenceEntity() {
		super();
	}

	public double zap() {
		// TODO: Actual maths involving like, damage and stuff.
		int ret = 1;
		if (blockMetadata == Refs.MetaValues.FenceElectricTin) {
			return ret;
		} else if (blockMetadata == Refs.MetaValues.FenceElectricNickel) {
			return ret * 1.5;
		} else {
			return ret * 2;
		}
	}
}
