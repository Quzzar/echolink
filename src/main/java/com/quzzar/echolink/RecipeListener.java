package com.quzzar.echolink;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

public class RecipeListener implements Listener{

    @EventHandler
    public void onItemCraft(CraftItemEvent e) {

        if(e.getInventory().getResult()!=null) {

            if(Utils.isFairlySimilar(e.getInventory().getResult(), Utils.echolinkFrame)) {
                if(e.getAction().equals(InventoryAction.PICKUP_ALL)) {
                    ItemStack registeredRadio = Utils.getEcholinkItem(e.getInventory().getResult(), e.getInventory().getMatrix());
                    e.setCurrentItem(registeredRadio);
                } else {
                    e.setCancelled(true);
                }
            }

        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(Utils.isFairlySimilar(e.getItemInHand(), Utils.echolinkFrame)) {
            e.setCancelled(true);
        }
    }

}
