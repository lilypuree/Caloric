package com.lilypuree.caloric.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class GasSenderTile extends TileEntity {

    public GasSenderTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }



    public boolean isAirOnSide(Direction side) {
        BlockPos offsetPos = pos.offset(side);
        BlockState state = world.getBlockState(offsetPos);
        return state.isAir(world, offsetPos);
    }

    public void createAirTileEntity(Direction side){

    }

    public <T> LazyOptional<T> getCapabilityForSide(Capability<T> capability, @Nonnull Direction capDirection) {
        TileEntity tile = world.getTileEntity(pos.offset(capDirection));
        if (tile != null) {
            return tile.getCapability(capability, capDirection.getOpposite());
        }
        return LazyOptional.empty();
    }
}
