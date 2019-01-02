package com.lbg.ib.api.sales.dao.device;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.device.DeviceDTO;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface DeviceDAO {
    DAOResponse<DeviceDTO> getDevice(ChannelBrandDTO channelBrandDto);
}
