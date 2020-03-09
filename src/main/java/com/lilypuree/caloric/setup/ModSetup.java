package com.lilypuree.caloric.setup;

import com.lilypuree.caloric.Caloric;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = Caloric.MODID, bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public void init(FMLCommonSetupEvent e) {
    }

    @SubscribeEvent
    public static void onServerSetUp(FMLServerStartingEvent event) {
    }

}
