package nl.tyrope.fencing.tileEntity;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

public class ElectricFenceEntity extends TileEntity implements IEnergySink,
		IEnergySource {

	private boolean initialized;
	private double buffer, charge;

	public ElectricFenceEntity() {
		super();
		initialized = false;
		charge = 0;
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
		return Math.min(buffer, getVoltage());
	}

	@Override
	public void drawEnergy(double amount) {
		buffer = buffer - amount;
	}

	@Override
	public double demandedEnergyUnits() {
		return Math.max(0, getVoltage() - buffer);
	}

	@Override
	public double injectEnergyUnits(ForgeDirection directionFrom, double amount) {
		if (charge != getVoltage()) {
			// Charge the fence.
			double need = getVoltage() - charge;
			charge = Math.max(charge + amount, getVoltage());
			amount = Math.max(amount - need, 0);
		}
		buffer = buffer + amount;
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
}
