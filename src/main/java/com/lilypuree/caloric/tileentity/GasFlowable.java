package com.lilypuree.caloric.tileentity;

import net.minecraft.util.math.BlockPos;

public interface GasFlowable {

    boolean isConnectedToSource();

    void connectSourceFrom(BlockPos sourcePos);

    void sendDraftInfo(float draft);

    void removeSourceFrom(BlockPos sourcePos);
}
