package nl.tyrope.fencing.tileEntity;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyConductor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ElectricFenceEntity extends TileEntity implements IEnergyConductor {

	private boolean initialized;

	public ElectricFenceEntity() {
		super();
		initialized = false;
	}

	public void initialize() {
		if (!initialized) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			initialized = true;
			System.out.println(String.format(
					"Electric fence entity initialized at [%s,%s,%s]", xCoord,
					yCoord, zCoord));
		}
	}

	public void uninitialize() {
		// MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
		initialized = false;
		System.out.println(String.format(
				"Electric fence entity UN-initialized at [%s,%s,%s]", xCoord,
				yCoord, zCoord));
	}

	@Override
	@SideOnly(Side.SERVER)
	public void updateEntity() {
		initialize();
	}

	@Override
	@SideOnly(Side.SERVER)
	public void invalidate() {
		uninitialize();
		super.invalidate();
	}

	@Override
	@SideOnly(Side.SERVER)
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
		return 0;
	}

	@Override
	public int getInsulationBreakdownEnergy() {
		return 64;
	}

	@Override
	public int getConductorBreakdownEnergy() {
		return 1024;
	}

	@Override
	public void removeInsulation() {
	}

	@Override
	public void removeConductor() {
	}
}
