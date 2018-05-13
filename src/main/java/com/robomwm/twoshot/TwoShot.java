package com.robomwm.twoshot;

import com.robomwm.customitemrecipes.CustomItemRecipes;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created on 4/24/2018.
 *
 * What have I done.
 *
 * @author RoboMWM
 */
public class TwoShot extends JavaPlugin
{
    private CustomItemRecipes customItemRecipes;

    public void onEnable()
    {
        customItemRecipes = (CustomItemRecipes)getServer().getPluginManager().getPlugin("CustomItemRecipes");
        if (customItemRecipes == null || !customItemRecipes.isEnabled())
        {
            setEnabled(false);
            return;
        }
    }
}
