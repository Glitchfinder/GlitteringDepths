/*
 * Copyright (c) 2012-2016 Sean Porter <glitchkey@gmail.com>
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
 
package com.glitchkey.glitteringdepths.terrain;

//* IMPORTS: JDK/JRE
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.block.BlockFace;
	import org.bukkit.Chunk;
	import org.bukkit.DyeColor;
	import org.bukkit.entity.EntityType;
	import org.bukkit.entity.LivingEntity;
	import org.bukkit.entity.Sheep;
	import org.bukkit.Location;
	import org.bukkit.plugin.Plugin;
	import org.bukkit.World;
//* IMPORTS: PANDORA
	import org.pandora.PandoraBiomePopulator;
	import org.pandora.trees.Redwood;
	import org.pandora.trees.TallRedwood;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.GlitteringDepthsPlugin;
	import com.glitchkey.glitteringdepths.structures.GlacierDungeon;
	import com.glitchkey.glitteringdepths.structures.GlacierOre;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class GlacierPopulator extends PandoraBiomePopulator
{
	Redwood redwood;
	TallRedwood tallRedwood;
	GlacierDungeon dungeon;
	GlacierOre coal, lapis, diamond, redstone, emerald, gold, iron, gravel;
	GlacierOre sand, lava;

	public GlacierPopulator(GlitteringDepthsPlugin plugin)
	{
		Plugin p = (Plugin) plugin;
		minTemperature = 0.0F;
		maxTemperature = 50.0F;
		minHumidity = 0.0F;
		maxHumidity = 100.0F;
		redwood     = new Redwood(p, false);
		tallRedwood = new TallRedwood(p, false);
		redwood.addToBlacklist(78);
		tallRedwood.addToBlacklist(78);
		redwood.addToBlacklist(79);
		tallRedwood.addToBlacklist(79);
		dungeon  = new GlacierDungeon(p, false);
		coal     = new GlacierOre(p, false, 16, 3D, 5.5D, 1D, 2.3D);
		lapis    = new GlacierOre(p, false, 21, 2D, 3D, 1D, 1.5D);
		diamond  = new GlacierOre(p, false, 56, 1.5D, 3D, 0.5D, 1.2D);
		redstone = new GlacierOre(p, false, 73, 3D, 5D, 1D, 2D);
		emerald  = new GlacierOre(p, false, 129, 1.5D, 3.5D, 1D, 2D);
		iron     = new GlacierOre(p, false, 15, 3D, 3.5D, 1.2D, 2.5D);
		gold     = new GlacierOre(p, false, 14, 2D, 3.5D, 1.2D, 2.1D);
		gravel   = new GlacierOre(p, false, 13, 3.5D, 6D, 1D, 2.5D);
		sand     = new GlacierOre(p, false, 12, 4D, 5.5D, 1D, 2.8D);
		lava     = new GlacierOre(p, false, 11, 5D, 8D, 1.2D, 3.1D);
	}

	public void populate(World w, Random r, Chunk source, int x,
		int z)
	{
		int chunkX = source.getX();
		int chunkZ = source.getZ();
		int lastId = 0;
		int maxHeight = w.getMaxHeight();

		for (int cy = (maxHeight - 1); lastId != -1 && cy >= 0; cy--) {
			lastId = generateSoil(w, x, cy, z, lastId);
		}

		int soil = this.getTopSoilY(w, x, z, chunkX, chunkZ);
		int base = soil + 1;
		boolean space = (soil != -1);
		boolean snow = space;
		snow = snow && (w.getBlockTypeIdAt(x, base, z) == 78);
		boolean tree = false;

		generateOre(w, r, x, z);

		if ((chunkX << 4) == x && (chunkZ << 4) == z) {
			Random rand = getRandom(w, x, z);

			if(rand.nextInt(100) <= 5 && rand.nextInt(100) <= 20) {
				if (dungeon.place(w, r, x, 0, z))
					return;
			}
		}

		int id = w.getBlockTypeIdAt(x, base, z);

		if (space && r.nextInt(25) == 0) {
			if (r.nextInt(3) == 0)
				tree = tallRedwood.place(w, r, x, base, z);
			else
				tree = redwood.place(w, r, x, base, z);
		}

		if (r.nextInt(28672) == 0) {
			for (int attempt = 0; attempt < 24; attempt++) {
				int cx = x + r.nextInt(8) - r.nextInt(8);
				int cz = z + r.nextInt(8) - r.nextInt(8);
				int chX = cx >> 4;
				int chZ = cz >> 4;

				int cy = getTopSoilY(w, cx, cz, chX, chZ) + 1;

				if (cy <= 0)
					continue;

				id = w.getBlockTypeIdAt(cx, cy, cz);

				if (id != 0 && id != 78)
					continue;

				id = w.getBlockTypeIdAt(cx, cy - 1, cz);

				if (id != 3 && id != 2)
					continue;

				Block block = w.getBlockAt(cx, cy, cz);
				block.setTypeIdAndData(86, (byte) 4, true);
			}
		}

		if (r.nextInt(32768) == 0) {
			for (int attempt = 0; attempt < 6; attempt++) {
				int cx = x + r.nextInt(8) - r.nextInt(8);
				int cz = z + r.nextInt(8) - r.nextInt(8);
				int chX = cx >> 4;
				int chZ = cz >> 4;

				int cy = getTopSoilY(w, cx, cz, chX, chZ) + 1;

				if (cy <= 0)
					continue;

				id = w.getBlockTypeIdAt(cx, cy, cz);

				if (id != 0 && id != 78)
					continue;

				id = w.getBlockTypeIdAt(cx, cy - 1, cz);

				if (id != 3 && id != 2)
					continue;

				Block block = w.getBlockAt(cx, cy, cz);
				block.setTypeIdAndData(103, (byte) 0, true);
			}
		}

		if (r.nextInt(256) == 0) {
			for (int attempt = 0; attempt < 24; attempt++) {
				int cx = x + r.nextInt(8) - r.nextInt(8);
				int cz = z + r.nextInt(8) - r.nextInt(8);
				int chX = cx >> 4;
				int chZ = cz >> 4;
				int cy = 39;

				boolean con1 = w.isChunkLoaded(chX, chZ);
				boolean con2 = w.loadChunk(chX, chZ, false);

				if (!con1 && !con2)
					continue;

				id = w.getBlockTypeIdAt(cx, cy, cz);

				if (id != 0 && id != 78)
					continue;

				id = w.getBlockTypeIdAt(cx, cy - 1, cz);

				if (id != 3 && id != 2 && id != 12)
					continue;
				
				int y = cy - 1;

				int id1 = w.getBlockTypeIdAt(cx + 1, y, cz    );
				int id2 = w.getBlockTypeIdAt(cx - 1, y, cz    );
				int id3 = w.getBlockTypeIdAt(cx    , y, cz + 1);
				int id4 = w.getBlockTypeIdAt(cx    , y, cz - 1);

				if (checkWater(id1, id2, id3, id4))
					continue;

				Block b = w.getBlockAt(cx, cy, cz);

				for (int i = 0; i < 7; i++) {
					b.setTypeIdAndData(83, (byte) 0, true);
					b = b.getRelative(0, 1, 0);

					if (r.nextBoolean())
						break;
				}
			}
		}

		if (space && r.nextInt(800) == 0) {
			EntityType type = EntityType.SHEEP;

			if (r.nextInt(8) == 0)
				type = EntityType.WOLF;

			int spawned = 0;
			int pack = ((type == EntityType.WOLF) ? 8 : 4);

			for (int att = 0; att < 12 && spawned < pack; att++) {
				int cx = x + r.nextInt(8) - r.nextInt(8);
				int cz = z + r.nextInt(8) - r.nextInt(8);
				int chX = cx >> 4;
				int chZ = cz >> 4;

				int cy = getTopSoilY(w, cx, cz, chX, chZ) + 1;

				if (cy <= 0)
					continue;

				id = w.getBlockTypeIdAt(cx, cy, cz);

				if(id == 8 || id == 9)
					continue;

				Location l = new Location(w, cx, cy, cz);
				l.add(0.5D, 0D, 0.5D);

				LivingEntity entity;
				entity = (LivingEntity) w.spawnEntity(l, type);

				if(entity == null)
					continue;

				spawned++;

				if (type == EntityType.SHEEP) {
					handleSheep(r, (Sheep) entity);
				}
				else {
					entity.setCustomName("Winter Wolf");
					entity.setCustomNameVisible(false);
					entity.setRemoveWhenFarAway(false);
					entity.setMaxHealth(16);
					entity.setHealth(16);
				}
			}
		}


		if (!space || (snow && !tree))
			return;

		int maxX = (x + 16);
		int maxZ = (z + 16);

		for (int cx = (x - 8); cx <= maxX; cx++) {
			int chX = cx >> 4;

			for (int cz = (z - 8); cz <= maxZ; cz++) {
				int chZ = cz >> 4;

				handleSnow(w, cx, cz, chX, chZ);
			}
		}
	}

	private boolean checkWater(int id1, int id2, int id3, int id4) {
		if (id1 != 8 && id2 != 8 && id3 != 8 && id4 != 8) {
			if (id1 != 9 && id2 != 9 && id3 != 9 && id4 != 9) {
				return false;
			}
		}

		return true;
	}

	private void handleSheep(Random r, Sheep sheep) {
		sheep.setCustomName("Grey Troender");
		sheep.setCustomNameVisible(false);
		sheep.setRemoveWhenFarAway(false);
		sheep.setMaxHealth(16);
		sheep.setHealth(16);

		int rand = r.nextInt(100);

		if (rand < 30)
			sheep.setColor(DyeColor.WHITE);
		else if (rand < 90)
			sheep.setColor(DyeColor.SILVER);
		else if (rand < 97)
			sheep.setColor(DyeColor.GRAY);
		else
			sheep.setColor(DyeColor.BLACK);
	}

	private void handleSnow(World w, int cx, int cz, int chX, int chZ) {
		boolean cond1 = (!w.isChunkLoaded(chX, chZ));
		boolean cond2 = (!w.loadChunk(chX, chZ, false));

		if (cond1 && cond2) {
			cz = ((cz >> 4) << 4) + 15;
			return;
		}

		for (int cy = 96; cy > 38; cy--) {
			int id1 = w.getBlockTypeIdAt(cx, cy, cz);
			int id2 = w.getBlockTypeIdAt(cx, cy - 1, cz);

			if (id1 == 0 && (id2 == 2 || id2 == 3 || id2 == 18)) {
				Block block = w.getBlockAt(cx, cy, cz);
				block.setTypeIdAndData(78, (byte) 0, true);
				continue;
			}
		}
	}

	private int generateSoil(World w, int x, int y, int z, int lastId) {
		

		if (!isValidTop(lastId))
			return -1;

		int id = w.getBlockTypeIdAt(x, y, z);

		if (id != 1 || lastId == 3 || lastId == 1)
			return id;
		else if (id == 2 || id == 3 || id == 7)
			return -1;

		if (lastId == 8 || lastId == 9 || lastId == 2) {
			Block block = w.getBlockAt(x, y, z);
			block.setTypeIdAndData(3, (byte) 0, true);
			return 3;
		}

		int mY = y - 2;

		for (int cy = y - 1; cy >= mY; cy--) {
			int tId = w.getBlockTypeIdAt(x, cy, z);

			if (isValidSupport(tId))
				continue;

			return id;
		}

		int mX = x + 1;
		int mZ = z + 1;
		mY += 1;

		for (int cx = (x - 1); cx <= mX; cx++) {
			boolean xCorner = (x != cx);
			int chX = cx >> 4;

			for (int cz = (z - 1); cz <= mZ; cz++) {
				if (xCorner && (z != cz))
					continue;

				int chZ = cz >> 4;
				boolean cond1 = (!w.isChunkLoaded(chX, chZ));
				boolean cond2 = (!w.loadChunk(chX, chZ, false));

				if (cond1 && cond2)
					continue;

				int tId = w.getBlockTypeIdAt(cx, y, cz);

				if (isValidAdjSupport(tId))
					continue;

				boolean valid = false;

				for (int cy = y - 1; cy >= mY; cy--) {
					tId = w.getBlockTypeIdAt(cx, cy, cz);

					if (!isValidAdjSupport(tId))
						continue;

					valid = true;
					break;
				}

				if (!valid)
					return id;
			}
		}

		Block block = w.getBlockAt(x, y, z);

		if (lastId == 2) {
			block.setTypeIdAndData(3, (byte) 0, true);
			return 3;
		}

		block.setTypeIdAndData(2, (byte) 0, true);
		return 2;
	}

	private boolean isValidSupport(int id) {
		if (id == 1 || id == 7 || id == 16 || id == 21 || id == 56)
			return true;
		else if (id == 73 || id == 74 || id == 129)
			return true;

		return false;
	}

	private boolean isValidAdjSupport(int id) {
		if (id == 2 || id == 3)
			return true;

		return isValidSupport(id);
	}

	private boolean isValidTop(int id) {
		if (id == 2 || id == 3 || id == 0 || id == 8 || id == 9)
			return true;
		else if (id == 78 || id == 79 || id == 18)
			return true;

		return false;
	}

	private void generateOre(World w, Random rand, int x, int z) {
		if(rand.nextInt(100) < 2) {
			coal.place(w, rand, x, rand.nextInt(55) + 10, z);
		}

		if(rand.nextInt(100) < 1) {
			lapis.place(w, rand, x, rand.nextInt(50) + 10, z);
		}

		if(rand.nextInt(10000) < 78) {
			redstone.place(w, rand, x, rand.nextInt(30) + 10, z);
		}

		if(rand.nextInt(10000) < 39) {
			diamond.place(w, rand, x, rand.nextInt(30) + 10, z);
		}

		if(rand.nextInt(100000) < 78) {
			emerald.place(w, rand, x, rand.nextInt(25) + 10, z);
		}

		if(rand.nextInt(5000) < 78) {
			iron.place(w, rand, x, rand.nextInt(30) + 10, z);
		}

		if(rand.nextInt(7000) < 78) {
			gold.place(w, rand, x, rand.nextInt(30) + 10, z);
		}

		if(rand.nextInt(100) < 1) {
			gravel.place(w, rand, x, rand.nextInt(50) + 10, z);
		}

		if(rand.nextInt(100) < 1) {
			sand.place(w, rand, x, rand.nextInt(50) + 10, z);
		}

		if(rand.nextInt(11000) < 78) {
			lava.place(w, rand, x, rand.nextInt(10) + 10, z);
		}
	}

	public int getTopSoilY(World w, int x, int z, int chunkX, int chunkZ) {
		return this.getTopSoilY(w, x, 85, z, chunkX, chunkZ);
	}

	public int getTopSoilY(World w, int x, int maxY, int z, int chunkX,
		int chunkZ)
	{
		if (!w.isChunkLoaded(chunkX, chunkZ)) {
			if (!w.loadChunk(chunkX, chunkZ, false)) {
				return -1;
			}
		}

		for (int y = maxY; y >= 0; y--) {
			Block b = w.getBlockAt(x, y, z);
			int id = b.getTypeId();

			if (id == 8 || id == 9)
				return -1;

			boolean solid;
			solid = b.getRelative(BlockFace.UP).getType().isSolid();
			if ((id == 2 || id == 3) && !solid)
				return y;
		}

		return -1;
	}

	public int getTopLeafY(World w, int x, int z, int chunkX, int chunkZ) {
		return this.getTopLeafY(w, x, 96, z, chunkX, chunkZ);
	}

	public int getTopLeafY(World w, int x, int maxY, int z, int chunkX,
		int chunkZ)
	{
		if (!w.isChunkLoaded(chunkX, chunkZ)) {
			if (!w.loadChunk(chunkX, chunkZ, false)) {
				return -1;
			}
		}

		for (int y = maxY; y >= 0; y--) {
			int id = w.getBlockTypeIdAt(x, y, z);
			boolean top = (w.getBlockTypeIdAt(x, y + 1, z) == 0);

			if (id == 18 && top)
				return y;
		}

		return -1;
	}

	private Random getRandom(World w, long x, long z) {
		long seed = (x * 341873128712L + z * 132897987541L);
		seed = seed ^ w.getSeed();
		return new Random(seed);
	}
}
