package com.lilypuree.caloric.tileentity;

import com.lilypuree.caloric.api.gas.GasPacket;
import com.lilypuree.caloric.capability.AirSupplier;
import com.lilypuree.caloric.capability.HeatedGasChannel;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AirTile extends TileEntity implements ITickableTileEntity,GasFlowable {

    private LazyOptional<AirSupplier> defaultSupplier = LazyOptional.of();
    private LazyOptional<HeatedGasChannel> defaultChimney = LazyOptional.of();

    private Set<Direction> connectedSides = new HashSet<>();
    private float defaultDraft;

    @CapabilityInject(HeatedGasChannel.class)
    public Capability<HeatedGasChannel> HEATED_GAS_CHANNEL_CAPABILITY;
    @CapabilityInject(AirSupplier.class)
    public Capability<AirSupplier> AIR_SUPPLIER_CAPABILITY;

    @Override
    public void tick() {
        defaultChimney.ifPresent(channel ->{
            GasPacket packet = channel.getGasToSend();
            releaseGas(packet);
        });
    }

    private void releaseGas(GasPacket output){

    }

    @Override
    public void validate() {
        super.validate();
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == HEATED_GAS_CHANNEL_CAPABILITY){
            return defaultChimney.cast();
        }else if(cap == AIR_SUPPLIER_CAPABILITY){
            return defaultSupplier.cast();
        }
        return super.getCapability(cap,side);
    }

    @Override
    public boolean isConnectedToSource() {
        return true;
    }

    @Override
    public void connectSourceFrom(BlockPos sourcePos) {
        Direction sourceDir = getDirectionFromPos(sourcePos);
        if(sourceDir != null){
            connectedSides.add(sourceDir);
            TileEntity input = world.getTileEntity(sourcePos);
            ((GasFlowable)input).sendDraftInfo(defaultDraft);
        }
    }

    @Override
    public void sendDraftInfo(float draft) {
    }

    @Override
    public void removeSourceFrom(BlockPos sourcePos) {
        connectedSides.remove(getDirectionFromPos(sourcePos));
        checkConnectionRemains();
    }

    public void updateSide(Direction side){
        if (connectedSides.contains(side)){
            if(world.isAirBlock(pos.offset(side))){
                connectedSides.remove(side);
            }
        }
        checkConnectionRemains();
        //TODO
        //send gas to a 'collector' flue above
    }

    private void checkConnectionRemains(){
        if(connectedSides.isEmpty()){
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    private Direction getDirectionFromPos(BlockPos source){
        for (Direction dir : Direction.values()){
            if(pos.offset(dir).equals(source)){
                return dir;
            }
        }
        return null;
    }
}
