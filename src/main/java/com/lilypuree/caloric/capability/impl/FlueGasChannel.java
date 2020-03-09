package com.lilypuree.caloric.capability.impl;

import com.lilypuree.caloric.api.gas.GasPacket;
import com.lilypuree.caloric.capability.HeatedGasChannel;

public class FlueGasChannel implements HeatedGasChannel {

    public FlueGasChannel(){

    }

    @Override
    public float getDraft() {
        return 0;
    }

    @Override
    public void insertHeatedGas(GasPacket packet) {

    }
}
