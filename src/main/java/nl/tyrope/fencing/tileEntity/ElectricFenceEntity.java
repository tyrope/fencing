package nl.tyrope.fencing.tileEntity;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyConductor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import nl.tyrope.fencing.Refs;

public class ElectricFenceEntity extends TileEntity implements IEnergyConductor {

	private boolean initialized;
	private long totalSunk;

	public ElectricFenceEntity() {
		super();
		initialized = false;
		totalSunk = 0;
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
			return false;
		default:
			return true;
		}
	}

	@Override
	public double getConductionLoss() {
		return 0.1;
	}

	@Override
	public int getInsulationEnergyAbsorption() {
		switch (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) {
		case Refs.MetaValues.FenceElectricTin:
			return 16;
		case Refs.MetaValues.FenceElectricCopper:
			return 64;
		default:
			return 0;
		}
	}

	@Override
	public int getInsulationBreakdownEnergy() {
		return getInsulationEnergyAbsorption() + 64;
	}

	@Override
	public int getConductorBreakdownEnergy() {
		switch (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) {
		case Refs.MetaValues.FenceElectricTin:
			return 32;
		case Refs.MetaValues.FenceElectricCopper:
			return 128;
		default:
			return 0;
		}
	}

	@Override
	public void removeInsulation() {
	}

	@Override
	public void removeConductor() {
		worldObj.destroyBlock(xCoord, yCoord, zCoord, false);
	}

	public float hasPower() {
		long oldSunk = totalSunk;
		try {
			totalSunk = ic2.api.energy.EnergyNet.instance
					.getTotalEnergySunken(this);
		} catch (NullPointerException e) {
			totalSunk = 0;
		}
		return totalSunk - oldSunk;
	}
}
