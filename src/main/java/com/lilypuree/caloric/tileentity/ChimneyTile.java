package com.lilypuree.caloric.tileentity;

import com.lilypuree.caloric.capability.HeatedGasChannel;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;

public class ChimneyTile extends TileEntity implements ITickableTileEntity, GasFlowable {

//    private LazyOptional<HeatedGasChannel> chimneyChannel = LazyOptional.of();


    public ChimneyTile(TileEntityType<?> tileEntityTypeIn, boolean fireboxConnected) {
        super(tileEntityTypeIn);
        this.fireboxConnected = fireboxConnected;
    }

    boolean fireboxConnected = false;

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }

    @Override
    public void tick() {
        //pull draft through connected flues or chambers
        //chimneys are a multiblock structure. Higher chimneys generate more draft, wider chimneys generate more draft
        //each oven multiblock has only one chimney -
        //activate the multiblock by the topmost chimney block.
        //it will begin a search. search priority: chimneys(all the way down)->optional flues(if there is more than one flue the draft generated will be less
    }


    @Override
    public void validate() {
        super.validate();
    }

    @Override
    public void remove() {

        super.remove();
    }

    @Override
    public boolean isConnectedToSource() {
        return fireboxConnected;
    }

    @Override
    public void connectSourceFrom(BlockPos sourcePos) {

    }

    @Override
    public void sendDraftInfo(float draft) {
    }

    @Override
    public void removeSourceFrom(BlockPos sourcePos) {

    }
}
