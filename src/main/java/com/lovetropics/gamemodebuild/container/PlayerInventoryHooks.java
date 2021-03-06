package com.lovetropics.gamemodebuild.container;

import com.lovetropics.gamemodebuild.GamemodeBuild;
import com.lovetropics.gamemodebuild.message.GBNetwork;
import com.lovetropics.gamemodebuild.message.OpenBuildInventoryMessage;
import com.lovetropics.gamemodebuild.state.GBClientState;
import com.lovetropics.gamemodebuild.state.GBPlayerStore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = GamemodeBuild.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public final class PlayerInventoryHooks {
	@SubscribeEvent
	public static void onOpenScreen(GuiOpenEvent event) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player == null) return;
		
		if (!GBClientState.isActive()) {
			return;
		}
		
		if (event.getGui() instanceof InventoryScreen) {
			GBNetwork.CHANNEL.sendToServer(new OpenBuildInventoryMessage());
			
			BuildContainer container = new BuildContainer(0, player.inventory, player, null);
			event.setGui(new BuildScreen(container, player.inventory, BuildContainer.title()));
		}
	}
	
	@SubscribeEvent
	public static void onToss(ItemTossEvent event) {
		if (GBStackMarker.isMarked(event.getEntityItem().getItem())) {
			event.setCanceled(true);
		}
	}
}
