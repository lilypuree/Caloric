package com.lilypuree.caloric.capability;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class OvenChamberProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(HeatedGasChannel.class)

}
