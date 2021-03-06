/*
 * Copyright (c) 2015, 2016, 2017, 2018, 2019, 2020 Adrian Siekierka
 *
 * This file is part of Charset.
 *
 * Charset is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Charset is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Charset.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.asie.charset.module.tools.engineering;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import pl.asie.charset.lib.stagingapi.ISignalMeterData;
import pl.asie.charset.lib.utils.RayTraceUtils;

import javax.annotation.Nullable;

public class SignalMeterTracker implements ISignalMeterTracker {
	private ISignalMeterData signalMeterDataClient;

	@Nullable
	public ISignalMeterData getNewDataToSend(EntityLivingBase entity) {
		Vec3d start = RayTraceUtils.getStart(entity);
		Vec3d end = RayTraceUtils.getEnd(entity);

		RayTraceResult result = entity.getEntityWorld().rayTraceBlocks(start, end, true);
		if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
			return SignalMeterProviderHandler.INSTANCE.getSignalMeterData(entity.getEntityWorld(), result.getBlockPos(), result).orElse(null);
		} else {
			return null;
		}
	}

	@Override
	public ISignalMeterData getClientData() {
		return signalMeterDataClient;
	}

	@Override
	public void setClientData(ISignalMeterData data) {
		signalMeterDataClient = data;
	}
}
