package com.lilypuree.caloric.tileentity;

import com.lilypuree.caloric.api.gas.GasPacket;
import com.lilypuree.caloric.capability.HeatedGasChannel;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FlueTile extends GasSenderTile implements ITickableTileEntity, GasFlowable {

    private LazyOptional<HeatedGasChannel> gasChannel = LazyOptional.of();
    HeatedGasChannel flueChannel;
    private Direction inputDir;
    private Direction outputDir;

    private boolean fireboxConnected = false;
    private LazyOptional<HeatedGasChannel> outputChannel = LazyOptional.empty();
    private LazyOptional<HeatedGasChannel> inputChannel = LazyOptional.empty();

    @CapabilityInject(HeatedGasChannel.class)
    public Capability<HeatedGasChannel> HEATED_GAS_CHANNEL_CAPABILITY;

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("input")) {
            inputDir = Direction.byIndex(compound.getInt("input"));
        }
        if (compound.contains("output")) {
            outputDir = Direction.byIndex(compound.getInt("output"));
        }
        fireboxConnected = compound.getBoolean("fireboxConnected");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("input", IntNBT.valueOf(inputDir.getIndex()));
        compound.put("output", IntNBT.valueOf(outputDir.getIndex()));
        compound.put("fireboxConnected", ByteNBT.valueOf(fireboxConnected));
        return super.write(compound);
    }

    @Override
    public void tick() {
        gasChannel.ifPresent(channel -> {
            GasPacket packet = channel.getGasToSend();
            outputChannel.ifPresent(output -> {
                output.insertHeatedGas(packet);
            });
        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (HEATED_GAS_CHANNEL_CAPABILITY == cap) {
            if (inputDir == side || outputDir == side) {
                return gasChannel.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void validate() {
        super.validate();
        cacheCapabilityForSide(inputDir);
        cacheCapabilityForSide(outputDir);
    }

    @Override
    public void remove() {
        removeSourceFrom();
        super.remove();
    }

    @Override
    public boolean isConnectedToSource() {
        return fireboxConnected;
    }

    @Override
    public void connectSourceFrom(BlockPos sourcePos) {
        fireboxConnected = true;
        if (!pos.offset(inputDir).equals(sourcePos)) {
            Direction temp = outputDir;
            outputDir = inputDir;
            inputDir = temp;
        }
        cacheCapabilityForSide(inputDir);
        updateForSide(outputDir);
    }

    @Override
    public void removeSourceFrom(BlockPos sourcePos) {
        fireboxConnected = false;
        outputChannel.ifPresent(channel -> {
            TileEntity output = world.getTileEntity(pos.offset(outputDir));
            ((GasFlowable) output).removeSourceFrom(pos);
        });
    }

    @Override
    public void sendDraftInfo(float draft) {
        gasChannel.ifPresent(channel -> channel.setDraft(draft));
        TileEntity input = world.getTileEntity(pos.offset(inputDir));
        if (inputChannel.isPresent() || input instanceof FireboxTile) {
            ((GasFlowable) input).sendDraftInfo(draft);
        }
    }

    public void updateForSide(Direction side) {
        TileEntity tileEntity = world.getTileEntity(pos.offset(side));
        if (fireboxConnected) {
            if (side == outputDir) {
                if (isAirOnSide(side)) {
                    createAirTileEntity(side);
                }
                cacheCapabilityForSide(side);
                if (outputChannel.isPresent()) {
                    ((GasFlowable) tileEntity).connectSourceFrom(pos);
                } else {
                    sendDraftInfo(-1);
                }
            }
        }
    }

    public void cacheCapabilityForSide(Direction side) {
        if (side == outputDir) {
            outputChannel = getCapabilityForSide(HEATED_GAS_CHANNEL_CAPABILITY, side);
        }
        if (side == inputDir) {
            inputChannel = getCapabilityForSide(HEATED_GAS_CHANNEL_CAPABILITY, side);
        }
    }
}
