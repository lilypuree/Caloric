package com.lilypuree.caloric.api.heat;

public interface HeatContainer {

    float getTemperature();

    void insertHeatPacket(HeatPacket packet);

    HeatPacket loseHeat(float ambient);
}
