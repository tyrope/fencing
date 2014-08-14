/*
 Fencing - A minecraft mod that adds pretty fences to your world!
Copyright (C)2013-2014 Dimitri "Tyrope" Molenaars

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>. 
*/

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
