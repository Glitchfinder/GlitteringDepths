/*
 * Copyright (c) 2012-2018 Sean Porter <glitchkey@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.glitchkey.glitteringdepths.listeners;

//* IMPORTS: JDK/JRE
	//* NOT NEEDED
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.Bukkit;
	import org.bukkit.event.block.BlockBreakEvent;
	import org.bukkit.event.block.BlockFadeEvent;
	import org.bukkit.event.block.BlockFormEvent;
	import org.bukkit.event.EventHandler;
	import org.bukkit.event.EventPriority;
	import org.bukkit.event.HandlerList;
	import org.bukkit.event.Listener;
	import org.bukkit.Material;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.GlitteringDepthsPlugin;
//* IMPORTS: OTHER
	//* NOT NEEDED

public final class GlacierListener implements Listener {
	private final GlitteringDepthsPlugin plugin;

	public GlacierListener(final GlitteringDepthsPlugin plugin) {
		this.plugin = plugin;
		registerEvents();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockMelt(final BlockFadeEvent event) {
		final Block block = event.getBlock();

		if (block == null || block.getWorld() != plugin.world)
			return;

		Material mat = event.getNewState().getType();
		if (mat != Material.STATIONARY_WATER) {
			if (block.getType() != Material.SNOW) {
				return;
			}
		}

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onIceForm(final BlockFormEvent event) {
		final Block block = event.getBlock();

		if (block == null || block.getWorld() != plugin.world)
			return;

		Material mat = event.getNewState().getType();
		if (mat != Material.ICE)
			return;

		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event) {
		final Block block = event.getBlock();

		if (block == null || block.getWorld() != plugin.world)
			return;
		if (block.getType() != Material.ICE)
			return;

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
			new Runnable() {
				@Override
				public void run()
				{
					if (block.isLiquid())
						block.setType(Material.AIR);
				}
			});
	}

	public void registerEvents() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterEvents() {
		HandlerList.unregisterAll(this);
	}
}
