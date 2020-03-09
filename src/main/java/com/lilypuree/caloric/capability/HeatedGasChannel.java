package com.lilypuree.caloric.capability;

import com.lilypuree.caloric.api.gas.GasPacket;

public interface HeatedGasChannel {

    float getDraft();

    void setDraft(float draft);

    void insertHeatedGas(GasPacket packet);

    GasPacket getGasToSend();
}
