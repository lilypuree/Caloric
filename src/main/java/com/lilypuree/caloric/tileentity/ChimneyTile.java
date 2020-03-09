package com.lilypuree.caloric.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class ChimneyTile extends TileEntity implements ITickableTileEntity {

    public ChimneyTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void validate() {
        super.validate();
    }

    @Override
    public void tick() {
        //pull draft through connected flues or chambers
        //chimneys are a multiblock structure. Higher chimneys generate more draft, wider chimneys generate more draft
        //each oven multiblock has only one chimney -
        //activate the multiblock by the topmost chimney block.
        //it will begin a search. search priority: chimneys(all the way down)->optional flues(if there is more than one flue the draft generated will be less
    }
}
