package nl.tyrope.fencing.tileEntity;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import nl.tyrope.fencing.Refs;

public class ElectricFenceEntity extends TileEntity implements IEnergySink,
		IEnergySource {

	private boolean initialized;
	private double buffer;

	public ElectricFenceEntity() {
		super();
		initialized = false;
	}

	private void initialize() {
		if (!initialized && !this.worldObj.isRemote) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			initialized = true;
		}
	}

	private void uninitialize() {
		if (initialized && !this.worldObj.isRemote) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			initialized = false;
		}
	}

	@Override
	public void updateEntity() {
		initialize();
	}

	@Override
	public void invalidate() {
		uninitialize();
		super.invalidate();
	}

	@Override
	public void onChunkUnload() {
		uninitialize();
		super.onChunkUnload();
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter,
			ForgeDirection direction) {
		switch (direction) {
		case UNKNOWN:
		case UP:
		case DOWN:
			return false;
		default:
			return true;
		}
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		switch (direction) {
		case UNKNOWN:
		case UP:
		case DOWN:
			return false;
		default:
			return true;
		}
	}

	@Override
	public double getOfferedEnergy() {
		return Math.max(0, buffer - getVoltage());
	}

	@Override
	public void drawEnergy(double amount) {
		buffer -= amount;
	}

	@Override
	public double demandedEnergyUnits() {
		return Math.max(0, getVoltage() * 2 - buffer);
	}

	@Override
	public double injectEnergyUnits(ForgeDirection directionFrom, double amount) {
		buffer += amount;
		return 0;
	}

	@Override
	public int getMaxSafeInput() {
		return getVoltage();
	}

	private int getVoltage() {
		try {
			return EnergyNet.instance.getPowerFromTier(blockMetadata + 1);
		} catch (NullPointerException ArrayIndexOutOfBoundsException) {
			// Can't get power from tier, probably an invalid block...
			return 0;
		}
	}

	public double zap() {
		double ret = Math.min(getVoltage(), buffer);
		drawEnergy(ret);
		if (blockMetadata == Refs.MetaValues.FenceElectricTin) {
			return ret / 16.0;
		} else {
			return ret / 32.0;
		}
	}
}
