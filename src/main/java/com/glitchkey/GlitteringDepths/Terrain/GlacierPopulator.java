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
	import org.bukkit.generator.BlockPopulator;
	import org.bukkit.Location;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.structures.GlacierDungeon;
	import com.glitchkey.glitteringdepths.structures.GlacierOre;
	import com.glitchkey.glitteringdepths.structures.trees.Redwood;
	import com.glitchkey.glitteringdepths.structures.trees.TallRedwood;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class GlacierPopulator extends BlockPopulator
{
	Redwood redwood;
	TallRedwood tallRedwood;
	GlacierDungeon dungeon;
	//GlacierLamp lamp;
	GlacierOre coal, lapis, diamond, redstone, emerald, gold, iron, gravel;
	GlacierOre sand, lava;

	public GlacierPopulator()
	{
		redwood     = new Redwood(false);
		tallRedwood = new TallRedwood(false);
		redwood.addToBlacklist(78);
		tallRedwood.addToBlacklist(78);
		redwood.addToBlacklist(79);
		tallRedwood.addToBlacklist(79);
		dungeon  = new GlacierDungeon(false);
		coal     = new GlacierOre(false, 16, 3D, 5.5D, 1D, 2.3D);
		lapis    = new GlacierOre(false, 21, 2D, 3D, 1D, 1.5D);
		diamond  = new GlacierOre(false, 56, 1.5D, 3D, 0.5D, 1.2D);
		redstone = new GlacierOre(false, 73, 3D, 5D, 1D, 2D);
		emerald  = new GlacierOre(false, 129, 1.5D, 3.5D, 1D, 2D);
		iron     = new GlacierOre(false, 15, 3D, 3.5D, 1.2D, 2.5D);
		gold     = new GlacierOre(false, 14, 2D, 3.5D, 1.2D, 2.1D);
		gravel   = new GlacierOre(false, 13, 3.5D, 6D, 1D, 2.5D);
		sand     = new GlacierOre(false, 12, 4D, 5.5D, 1D, 2.8D);
		lava     = new GlacierOre(false, 11, 5D, 8D, 1.2D, 3.1D);
	}

	public void populate(World w, Random r, Chunk source) {
		int chunkX = source.getX();
		int chunkZ = source.getZ();

		int xPos = chunkX << 4;
		int zPos = chunkZ << 4;

		generateOre(w, r, xPos, zPos);

		Random rand = getRandom(w, xPos, zPos);

		if(rand.nextInt(100) < 1)
			dungeon.place(w, r, xPos, 0, zPos);

		int x = xPos;
		int z = zPos;
		int soil = -1;
		int id = 0;

		for(int i = 0; i < 64; i++) {
			if (i % 2 == 0) {
				x = xPos + 16 - r.nextInt(16);
				z = zPos + 16 - r.nextInt(16);
			}
			else {
				x = xPos + r.nextInt(16);
				z = zPos + r.nextInt(16);
			}
			soil = this.getTopSoilY(w, x, z, chunkX, chunkZ);

			if (soil < 0)
				continue;

			if (r.nextInt(3) == 0)
				tallRedwood.place(w, r, x, soil + 1, z);
			else
				redwood.place(w, r, x, soil + 1, z);
		}

		for (int attempt = 0; attempt < 24; attempt++) {
			int cx = xPos + r.nextInt(16);
			int cz = zPos + r.nextInt(16);
			int cy = 39;

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

		if (r.nextInt(112) < 1) {
			for (int attempt = 0; attempt < 24; attempt++) {
				int cx = xPos + r.nextInt(16);
				int cz = zPos + r.nextInt(16);

				int cy = getTopSoilY(w, cx, cz, chunkX, chunkZ) + 1;

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

				block = w.getBlockAt(cx, cy + 1, cz);

				if (block.getTypeId() == 0)
					block.setTypeIdAndData(78, (byte) 0, true);
			}
		}

		if (r.nextInt(128) < 1) {
			for (int attempt = 0; attempt < 6; attempt++) {
				int cx = xPos + r.nextInt(16);
				int cz = zPos + r.nextInt(16);

				int cy = getTopSoilY(w, cx, cz, chunkX, chunkZ) + 1;

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

				block = w.getBlockAt(cx, cy + 1, cz);

				if (block.getTypeId() == 0)
					block.setTypeIdAndData(78, (byte) 0, true);
			}
		}

		if (r.nextInt(3) < 1) {
			EntityType type = EntityType.SHEEP;

			if (r.nextInt(8) == 0)
				type = EntityType.WOLF;

			int spawned = 0;
			int pack = ((type == EntityType.WOLF) ? 8 : 4);

			for (int att = 0; att < 12 && spawned < pack; att++) {
				int cx = xPos + r.nextInt(16);
				int cz = zPos + r.nextInt(16);

				int cy = getTopSoilY(w, cx, cz, chunkX, chunkZ) + 1;

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

	private void generateOre(World w, Random r, int x, int z) {
		coal.place(w, r, x + r.nextInt(16), r.nextInt(55) + 10, z + r.nextInt(16));
		coal.place(w, r, x + r.nextInt(16), r.nextInt(55) + 10, z + r.nextInt(16));
		lapis.place(w, r, x + r.nextInt(16), r.nextInt(50) + 10, z + r.nextInt(16));
		lapis.place(w, r, x + r.nextInt(16), r.nextInt(50) + 10, z + r.nextInt(16));
		redstone.place(w, r, x + r.nextInt(16), r.nextInt(30) + 10, z + r.nextInt(16));
		diamond.place(w, r, x + r.nextInt(16), r.nextInt(30) + 10, z + r.nextInt(16));
		iron.place(w, r, x + r.nextInt(16), r.nextInt(30) + 10, z + r.nextInt(16));
		iron.place(w, r, x + r.nextInt(16), r.nextInt(30) + 10, z + r.nextInt(16));
		gold.place(w, r, x + r.nextInt(16), r.nextInt(30) + 10, z + r.nextInt(16));

		if(r.nextBoolean())
			emerald.place(w, r, x + r.nextInt(16), r.nextInt(25) + 10, z + r.nextInt(16));

		gravel.place(w, r, x + r.nextInt(16), r.nextInt(50) + 10, z + r.nextInt(16));
		sand.place(w, r, x + r.nextInt(16), r.nextInt(50) + 10, z + r.nextInt(16));

		if(r.nextInt(5) < 1) {
			lava.place(w, r, x + r.nextInt(16), r.nextInt(10) + 10, z + r.nextInt(16));
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

	private Random getRandom(World w, long x, long z) {
		long seed = (x * 341873128712L + z * 132897987541L);
		seed = seed ^ w.getSeed();
		return new Random(seed);
	}
}
