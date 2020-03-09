package com.lilypuree.caloric.capability;

import com.lilypuree.caloric.api.gas.GasPacket;

public interface AirSupplier {

    GasPacket supplyAirFromDraft(float draft);
}
