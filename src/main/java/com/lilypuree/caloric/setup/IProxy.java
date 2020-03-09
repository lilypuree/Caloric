package com.lilypuree.caloric.setup;

import net.minecraft.entity.player.PlayerEntity;

public interface IProxy {

    net.minecraft.world.World getClientWorld();

    PlayerEntity getClientPlayer();
}