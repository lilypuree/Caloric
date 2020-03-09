package com.lilypuree.caloric.setup;

import net.minecraft.entity.player.PlayerEntity;

public class ServerProxy implements IProxy {

    @Override
    public net.minecraft.world.World getClientWorld() {
        throw new IllegalStateException("Only run this on the client!");
    }

    @Override
    public PlayerEntity getClientPlayer() {
        throw new IllegalStateException("Only run this on the client!");
    }
}