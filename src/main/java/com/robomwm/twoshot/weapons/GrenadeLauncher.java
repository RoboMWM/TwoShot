package com.robomwm.twoshot.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

/**
 * Created on 4/24/2018.
 *
 * @author RoboMWM
 */
public class GrenadeLauncher
{
    private JavaPlugin plugin;

    public GrenadeLauncher(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    public boolean fire(PlayerInteractEvent event)
    {
        if (!event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasDisplayName())
            return false;

//        ItemMeta itemMeta = event.getItem().getItemMeta();
//        String[] name = itemMeta.getDisplayName().split(" · ");
//
//        int ammo = Integer.parseInt(name[1]);
//        name[1] = Integer.toString(--ammo);
//        itemMeta.setDisplayName(String.join(" ", name));
        //blahblah

        Location location = event.getPlayer().getLocation().add(event.getPlayer().getLocation().getDirection());
        Item grenade = location.getWorld().dropItem(location, new ItemStack(Material.TNT));
        grenade.setCanMobPickup(false);
        grenade.setPickupDelay(Integer.MAX_VALUE);
        grenade.setVelocity(location.getDirection());

        new BukkitRunnable()
        {
            int duration = 60;

            @Override
            public void run()
            {
                if (--duration <= 0)
                {
                    grenade.remove();
                    grenade.getWorld().createExplosion(grenade.getLocation(), 1f, false, false);
                    cancel();
                }
                Vector vector = grenade.getVelocity();
                int length = Math.max(2, (int)Math.round(vector.length()));
                BlockIterator blockIterator = new BlockIterator(grenade.getWorld(), vector, vector, 0, length);
                Block previousBlock = null;
                Block collidingBlock = null;
                while ((collidingBlock == null || collidingBlock.getType() != Material.AIR) && blockIterator.hasNext())
                {
                    previousBlock = collidingBlock;
                    collidingBlock = blockIterator.next();
                }

                if (collidingBlock == null || collidingBlock.getType() == Material.AIR)
                    return;

                switch (collidingBlock.getFace(previousBlock))
                {
                    case UP:
                    case DOWN:
                        vector.setY(-vector.getY());
                        break;
                    default:
                        vector.setX(-vector.getX());
                }
                grenade.setVelocity(vector);
            }
        }.runTaskTimer(plugin, 1L, 1L);
        return true;
    }
}
