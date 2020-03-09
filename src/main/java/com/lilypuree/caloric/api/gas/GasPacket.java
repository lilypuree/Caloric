package com.lilypuree.caloric.api.gas;

import com.lilypuree.caloric.api.heat.HeatPacket;

public interface GasPacket {

    GasPacket addPacket(GasPacket packetToAdd);

    String getName();

}
