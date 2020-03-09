package com.lilypuree.caloric.tileentity;

import com.lilypuree.caloric.api.gas.GasPacket;
import com.lilypuree.caloric.capability.AirSupplier;
import com.lilypuree.caloric.capability.HeatedGasChannel;
import com.lilypuree.caloric.capability.HeatedGasProducer;
import net.minecraft.block.BlockState;
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

public class FireboxTile extends GasSenderTile implements ITickableTileEntity, GasFlowable{

    private HeatedGasProducer gasProducer;

    private Direction outputDir = Direction.UP;
    private Direction primaryAirInputDir = Direction.DOWN;
    private Direction secondaryAirInputDir = null;
    float secondaryAirRatio = 0;

    float naturalDraft = -1;
    float primaryDraft = 0;
    float secondaryDraft = 0;

    LazyOptional<HeatedGasChannel> outputChannel = LazyOptional.empty();
    LazyOptional<AirSupplier> primaryAirSupplier = LazyOptional.empty();
    LazyOptional<AirSupplier> secondaryAirSupplier = LazyOptional.empty();


    @CapabilityInject(HeatedGasChannel.class)
    public Capability<HeatedGasChannel> HEATED_GAS_CHANNEL_CAPABILITY;
    @CapabilityInject(AirSupplier.class)
    public Capability<AirSupplier> AIR_SUPPLIER_CAPABILITY;


    @Override
    public void read(CompoundNBT compound) {
        CompoundNBT producerTag = compound.getCompound("producer");
        if (gasProducer != null) {
            gasProducer.deserializeNBT(producerTag);
        }
        outputDir = Direction.byIndex(compound.getInt("output"));
        if(compound.contains("primary")){
            primaryAirInputDir = Direction.byIndex(compound.getInt("primary"));
        }
        if(compound.contains("secondary")){
            secondaryAirInputDir = Direction.byIndex(compound.getInt("secondary"));
        }
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (gasProducer != null) {
            CompoundNBT producerTag = gasProducer.serializeNBT();
            compound.put("producer", producerTag);
        }
        compound.put("output", IntNBT.valueOf(outputDir.getIndex()));
        if(primaryAirInputDir != null){
            compound.put("primary", IntNBT.valueOf(primaryAirInputDir.getIndex()));
        }
        if(secondaryAirInputDir != null){
            compound.put("secondary", IntNBT.valueOf(secondaryAirInputDir.getIndex()));
        }
        return super.write(compound);
    }

    @Override
    public void tick() {
        if (naturalDraft < 0) {

        } else {
            insertGasInputs();
            GasPacket outputAir = gasProducer.produceGas();
            sendOutputGas(outputAir);
        }
    }

    public void insertGasInputs() {
        if (primaryAirInputDir != null) {
            gasProducer.insertAir(primaryAirSupplier.map(supplier -> supplier.supplyAirFromDraft(primaryDraft)).orElse());
        }
        if (secondaryAirInputDir != null && secondaryAirSupplier.isPresent()) {
            gasProducer.insertSecondaryAir(secondaryAirSupplier.map(supplier -> supplier.supplyAirFromDraft(secondaryDraft)).orElse());
        }
    }

    public void sendOutputGas(GasPacket outputGas) {
        outputChannel.ifPresent(channel -> channel.insertHeatedGas(outputGas));
    }

    @Override
    public boolean isConnectedToSource() {
        return true;
    }

    @Override
    public void connectSourceFrom(BlockPos sourcePos) { }

    @Override
    public void removeSourceFrom(BlockPos sourcePos) { }

    @Override
    public void sendDraftInfo(float draft) {
        this.naturalDraft = draft;
        if(draft >= 0){
            this.secondaryDraft = draft * secondaryAirRatio;
            this.primaryDraft = draft - secondaryDraft;
        }
    }

    @Override
    public void validate() {
        updateForSide(outputDir);
        initInput();
        super.validate();
    }

    @Override
    public void remove() {
        if(outputChannel.isPresent()){
           TileEntity output = world.getTileEntity(pos.offset(outputDir));
            ((GasFlowable)output).removeSourceFrom(pos);
        }
        super.remove();
    }


    public void initInput() {
        if (primaryAirInputDir != null) {
            updateForSide(primaryAirInputDir);
        }
        if(secondaryAirInputDir != null){
            updateForSide(secondaryAirInputDir);
        }
    }


    public void updateForSide(Direction side) {
        BlockPos offsetPos = pos.offset(side);
        if(!world.isBlockLoaded(offsetPos)){
            return;
        }
        if (side == outputDir) {
            setupSide(side);
            if(outputChannel.isPresent()){
                TileEntity tileEntity = world.getTileEntity(offsetPos);
                ((GasFlowable)tileEntity).connectSourceFrom(pos);
            }else {
                sendDraftInfo(-1);
            }
        }
        if (side == primaryAirInputDir || side == secondaryAirInputDir) {
            setupSide(side);
        }
    }

    private void setupSide(Direction side){
        if (isAirOnSide(side)) {
            createAirTileEntity(side);
        }
        cacheCapabilityForSide(side);
    }

    public void cacheCapabilityForSide(Direction side) {
        if (side == outputDir) {
            outputChannel = getCapabilityForSide(HEATED_GAS_CHANNEL_CAPABILITY, side);
        }
        if (side == primaryAirInputDir) {
            primaryAirSupplier = getCapabilityForSide(AIR_SUPPLIER_CAPABILITY, side);
        }
        if (side == secondaryAirInputDir) {
            secondaryAirSupplier = getCapabilityForSide(AIR_SUPPLIER_CAPABILITY, side);
        }
    }
}
