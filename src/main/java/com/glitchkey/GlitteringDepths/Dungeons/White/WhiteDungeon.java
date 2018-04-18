/*
 * Copyright (c) 2018 Sean Porter <glitchkey@gmail.com>
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

package com.glitchkey.glitteringdepths.dungeons.white;

//* IMPORTS: JDK/JRE
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.BlockState;
	import org.bukkit.block.Chest;
	import org.bukkit.block.CreatureSpawner;
	import org.bukkit.DyeColor;
	import org.bukkit.entity.EntityType;
	import org.bukkit.inventory.Inventory;
	import org.bukkit.inventory.ItemStack;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.material.Dye;
	import org.bukkit.material.MaterialData;
	import org.bukkit.material.Sapling;
	import org.bukkit.TreeSpecies;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	//* NOT NEEDED
//* IMPORTS: OTHER
	//* NOT NEEDED

public class WhiteDungeon
{
	// Dungeon Parts
	private Base1 base1;
	private Cap1  cap1;

	/**
	 * Constructor
	 **/
	public WhiteDungeon()
	{
		// Instanciate dungeon parts
		base1 = new Base1();
		cap1 = new Cap1();
	}

	/**
	 * Adds materials to the blacklist (actually a material whitelist)
	 **/
	public WhiteDungeon addToBlacklist(Material type)
	{
		// Forward settings to the dungeon classes
		base1.addToBlacklist(type);
		cap1.addToBlacklist(type);

		// Return this class for chaining
		return this;
	}

	/**
	 * Wrapper for the generate function
	 **/
	public boolean place(World world, Random random, int x, int y, int z)
	{
		return generate(world, random, x, y, z);
	}

	/**
	 * Generates a random dungeon
	 **/
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		// Zero coordinates to the lowest value chunk edge
		x = (x >> 4) << 4;
		z = (z >> 4) << 4;

		int baseY = getBaseY(world, x, z, false) + 11;

		if (baseY <= 10)
			return false;

		int bottomY = baseY;

		for(int cx = x; cx < (x + 16); cx++) {
			for(int cz = z; cz < (z + 16); cz++) {
				int checkY = getBaseY(world, cx, cz, true);
				bottomY = Math.min(checkY, bottomY);

				if (bottomY < 0)
					return false;
			}
		}

		for (int cy = (baseY - 1); cy > bottomY; cy -= 3)
		{
			base1.place(world, random, x, cy - 2, z);
		}

		cap1.place(world, random, x, baseY, z);

		Location l1 = new Location(world, x + 7, baseY + 3, z + 7);
		Location l2 = new Location(world, x + 7, baseY + 3, z + 8);
		Location l3 = new Location(world, x + 8, baseY + 3, z + 8);
		Location l4 = new Location(world, x + 8, baseY + 3, z + 7);
		setSpawner(l1, random);
		setSpawner(l2, random);
		setSpawner(l3, random);
		setSpawner(l4, random);
		l1 = new Location(world, x + 6, baseY + 2, z + 7);
		l2 = new Location(world, x + 7, baseY + 2, z + 9);
		l3 = new Location(world, x + 8, baseY + 2, z + 6);
		l4 = new Location(world, x + 9, baseY + 2, z + 8);
		setLoot(l1, random);
		setLoot(l2, random);
		setLoot(l3, random);
		setLoot(l4, random);

		return true;
	}

	private void setSpawner(Location location, Random r) {
		EntityType type = EntityType.SHEEP;

		if (r.nextInt(10) < 8) {
			int rand = r.nextInt(100);

			if (rand < 28)
				type = EntityType.PIG;
			else if (rand < 56)
				type = EntityType.COW;
			else if (rand < 84)
				type = EntityType.CHICKEN;
			else if (rand < 94)
				type = EntityType.OCELOT;
			else
				type = EntityType.MUSHROOM_COW;
		}
		else {
			int rand = r.nextInt(100);

			if (rand < 28)
				type = EntityType.CAVE_SPIDER;
			else if (rand < 56)
				type = EntityType.SPIDER;
			else if (rand < 84)
				type = EntityType.ENDERMAN;
			else if (rand < 94)
				type = EntityType.SLIME;
			else
				type = EntityType.CREEPER;
		}

		BlockState b = location.getBlock().getState();

		if (!(b instanceof CreatureSpawner))
			return;

		((CreatureSpawner) b).setSpawnedType(type);
		b.update(true, false);
	}

	private void setLoot(Location location, Random r) {
		BlockState b = location.getBlock().getState();

		if (!(b instanceof Chest))
			return;

		Chest c = (Chest) b;

		addTreasure(c.getBlockInventory(), r);
		addPlants(c.getBlockInventory(), r);
	}

	private void addTreasure(Inventory i, Random r) {
		ItemStack item = new ItemStack(Material.AIR);

		if (r.nextInt(100) < 11) {
			item = new ItemStack(Material.SPONGE);
			item.setAmount(r.nextInt(11) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 3) {
			item = new ItemStack(Material.LAPIS_BLOCK);
			item.setAmount(r.nextInt(5) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 33) {
			item = new ItemStack(Material.WEB);
			item.setAmount(r.nextInt(12) + 3);
			i.addItem(item);
		}
		if (r.nextInt(100) < 3) {
			item = new ItemStack(Material.GOLD_BLOCK);
			item.setAmount(r.nextInt(3) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.IRON_BLOCK);
			item.setAmount(r.nextInt(4) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 16) {
			item = new ItemStack(Material.MOSSY_COBBLESTONE);
			item.setAmount(r.nextInt(20) + 5);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.OBSIDIAN);
			item.setAmount(r.nextInt(6) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 10) {
			item = new ItemStack(Material.CHEST);
			item.setAmount(r.nextInt(2) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 2) {
			item = new ItemStack(Material.DIAMOND_BLOCK);
			item.setAmount(r.nextInt(3) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 17) {
			item = new ItemStack(Material.CLAY);
			item.setAmount(r.nextInt(11) + 3);
			i.addItem(item);
		}
		if (r.nextInt(100) < 6) {
			item = new ItemStack(Material.MYCEL);
			item.setAmount(r.nextInt(5) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 2) {
			item = new ItemStack(Material.EMERALD_BLOCK);
			item.setAmount(r.nextInt(3) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 17) {
			item = new ItemStack(Material.COAL);
			item.setAmount(r.nextInt(15) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 10) {
			item = new ItemStack(Material.DIAMOND);
			item.setAmount(r.nextInt(6) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 13) {
			item = new ItemStack(Material.IRON_INGOT);
			item.setAmount(r.nextInt(10) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 12) {
			item = new ItemStack(Material.GOLD_INGOT);
			item.setAmount(r.nextInt(8) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 16) {
			item = new ItemStack(Material.SADDLE);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 21) {
			item = new ItemStack(Material.CLAY_BALL);
			item.setAmount(r.nextInt(30) + 5);
			i.addItem(item);
		}
		if (r.nextInt(100) < 19) {
			item = new ItemStack(Material.PAPER);
			item.setAmount(r.nextInt(12) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 11) {
			item = new ItemStack(Material.SLIME_BALL);
			item.setAmount(r.nextInt(6) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 15) {
			item = new ItemStack(Material.EGG);
			item.setAmount(r.nextInt(7) + 6);
			i.addItem(item);
		}
		if (r.nextInt(100) < 36) {
			item = new ItemStack(Material.GLOWSTONE_DUST);
			item.setAmount(r.nextInt(50) + 10);
			i.addItem(item);
		}
		if (r.nextInt(100) < 14) {
			item = new ItemStack(Material.ENDER_PEARL);
			item.setAmount(r.nextInt(4) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 18) {
			item = new ItemStack(Material.GOLD_NUGGET);
			item.setAmount(r.nextInt(22) + 4);
			i.addItem(item);
		}
		if (r.nextInt(100) < 11) {
			item = new ItemStack(Material.EMERALD);
			item.setAmount(r.nextInt(6) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 1) {
			item = new ItemStack(Material.NETHER_STAR);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 11) {
			item = new ItemStack(Material.IRON_BARDING);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 8) {
			item = new ItemStack(Material.GOLD_BARDING);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.DIAMOND_BARDING);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 28) {
			item = new ItemStack(Material.NAME_TAG);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 5) {
			item = new ItemStack(Material.ELYTRA);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.GREEN_RECORD);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.GOLD_RECORD);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.GREEN_RECORD);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.RECORD_3);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.RECORD_4);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.RECORD_5);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.RECORD_6);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.RECORD_7);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.RECORD_8);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.RECORD_9);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.RECORD_10);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.RECORD_11);
			item.setAmount(1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 4) {
			item = new ItemStack(Material.RECORD_12);
			item.setAmount(1);
			i.addItem(item);
		}
	}

	private void addPlants(Inventory i, Random r) {
		ItemStack item = new ItemStack(Material.AIR);

		if (r.nextInt(100) < 10) {
			item = new ItemStack(Material.SEEDS);
			item.setAmount(r.nextInt(3) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 13) {
			item = new ItemStack(Material.SUGAR_CANE);
			item.setAmount(r.nextInt(2) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 11) {
			item = new ItemStack(Material.PUMPKIN_SEEDS);
			item.setAmount(r.nextInt(3) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 13) {
			item = new ItemStack(Material.MELON_SEEDS);
			item.setAmount(r.nextInt(3) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 35) {
			item = new ItemStack(Material.CARROT_ITEM);
			item.setAmount(r.nextInt(5) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 31) {
			item = new ItemStack(Material.POTATO_ITEM);
			item.setAmount(r.nextInt(4) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 23) {
			item = new ItemStack(Material.BEETROOT_SEEDS);
			item.setAmount(r.nextInt(4) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 18) {
			Sapling s = new Sapling(TreeSpecies.GENERIC);
			item = s.toItemStack(1);
			item.setAmount(r.nextInt(2) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 9) {
			Sapling s = new Sapling(TreeSpecies.ACACIA);
			item = s.toItemStack(1);
			item.setAmount(r.nextInt(2) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 17) {
			Sapling s = new Sapling(TreeSpecies.BIRCH);
			item = s.toItemStack(1);
			item.setAmount(r.nextInt(2) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 11) {
			Sapling s = new Sapling(TreeSpecies.DARK_OAK);
			item = s.toItemStack(1);
			item.setAmount(r.nextInt(2) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 13) {
			Sapling s = new Sapling(TreeSpecies.JUNGLE);
			item = s.toItemStack(1);
			item.setAmount(r.nextInt(2) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 17) {
			item = new ItemStack(Material.RED_MUSHROOM);
			item.setAmount(r.nextInt(4) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 17) {
			item = new ItemStack(Material.BROWN_MUSHROOM);
			item.setAmount(r.nextInt(4) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 11) {
			item = new ItemStack(Material.CACTUS);
			item.setAmount(r.nextInt(3) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 9) {
			item = new ItemStack(Material.VINE);
			item.setAmount(r.nextInt(2) + 1);
			i.addItem(item);
		}
		if (r.nextInt(100) < 23) {
			item = new ItemStack(Material.WATER_LILY);
			item.setAmount(r.nextInt(16) + 2);
			i.addItem(item);
		}
		if (r.nextInt(100) < 14) {
			item = new ItemStack(Material.INK_SACK);
			item.setData((MaterialData) new Dye(DyeColor.BROWN));
			item.setAmount(r.nextInt(5) + 1);
			i.addItem(item);
		}
	}

	private int getBaseY(World world, int x, int z, boolean ignoreExtras) {
		for (int y = 97; y > 0; y--) {
			int id = world.getBlockTypeIdAt(x, y, z);

			if (!ignoreExtras && (id == 8 || id == 9))
				return y;
			else if (!ignoreExtras && (id == 2 || id == 3))
				return y;
			else if (id == 1 || id == 16 || id == 21 || id == 56)
				return y;
			else if (id == 73 || id == 74 || id == 129)
				return y;
		}

		return -1;
	}
}
