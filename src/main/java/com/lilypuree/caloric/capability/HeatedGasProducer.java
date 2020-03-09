package com.lilypuree.caloric.capability;

import com.lilypuree.caloric.api.gas.GasPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface HeatedGasProducer extends INBTSerializable<CompoundNBT> {

    void ignite();

    void extinguish();

    void insertAir(GasPacket primary);

    void insertSecondaryAir(GasPacket secondary);

    void insertFuel();

    GasPacket produceGas();
}
