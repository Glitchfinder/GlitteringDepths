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
	import org.bukkit.entity.EntityType;
	import org.bukkit.generator.BlockPopulator;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.listeners.GlacierMobListener;
	import com.glitchkey.glitteringdepths.structures.GlacierDungeon;
	import com.glitchkey.glitteringdepths.structures.GlacierOre;
	import com.glitchkey.glitteringdepths.structures.ruins.Circle;
	import com.glitchkey.glitteringdepths.structures.ruins.RandomColumn;
	import com.glitchkey.glitteringdepths.structures.ruins.RandomRuin;
	import com.glitchkey.glitteringdepths.structures.trees.FallenSpruce;
	import com.glitchkey.glitteringdepths.structures.trees.MegaRedwood;
	import com.glitchkey.glitteringdepths.structures.trees.MiniJungle;
	import com.glitchkey.glitteringdepths.structures.trees.Redwood;
	import com.glitchkey.glitteringdepths.structures.trees.TallRedwood;
	import com.glitchkey.glitteringdepths.structures.trees.WeepingBirch;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class GlacierPopulator extends BlockPopulator
{
	GlacierMobListener mobs;
	Circle circle;
	RandomColumn column;
	RandomRuin ruin;
	FallenSpruce fallenSpruce;
	MegaRedwood megaRedwood;
	MiniJungle miniJungle;
	Redwood redwood;
	TallRedwood tallRedwood;
	WeepingBirch weepingBirch;
	GlacierDungeon dungeon;
	GlacierOre coal, lapis, diamond, redstone, emerald, gold, iron, gravel;
	GlacierOre sand, granite, diorite, andesite;

	public GlacierPopulator(GlacierMobListener listener)
	{
		this.mobs     = listener;
		circle        = new Circle(false);
		column        = new RandomColumn(false);
		ruin          = new RandomRuin(false);
		fallenSpruce  = new FallenSpruce(false);
		redwood       = new Redwood(false);
		megaRedwood   = new MegaRedwood(false);
		miniJungle    = new MiniJungle(false);
		tallRedwood   = new TallRedwood(false);
		weepingBirch  = new WeepingBirch(false);
		dungeon  = new GlacierDungeon(false);
		coal     = new GlacierOre(false, Material.COAL_ORE, 3D, 5.5D, 1D, 2.3D);
		lapis    = new GlacierOre(false, Material.LAPIS_ORE, 2D, 3D, 1D, 1.5D);
		diamond  = new GlacierOre(false, Material.DIAMOND_ORE, 1.5D, 3D, 0.5D, 1.2D);
		redstone = new GlacierOre(false, Material.REDSTONE_ORE, 3D, 5D, 1D, 2D);
		emerald  = new GlacierOre(false, Material.EMERALD_ORE, 1.5D, 3.5D, 1D, 2D);
		iron     = new GlacierOre(false, Material.IRON_ORE, 3D, 3.5D, 1.2D, 2.5D);
		gold     = new GlacierOre(false, Material.GOLD_ORE, 2D, 3.5D, 1.2D, 2.1D);
		gravel   = new GlacierOre(false, Material.GRAVEL, 3.5D, 6D, 1D, 2.5D);
		sand     = new GlacierOre(false, Material.SAND, 4D, 5.5D, 1D, 2.8D);
		granite  = new GlacierOre(false, Material.STONE, 1, 4D, 5.5D, 1D, 2.8D);
		diorite  = new GlacierOre(false, Material.STONE, 3, 4D, 5.5D, 1D, 2.8D);
		andesite = new GlacierOre(false, Material.STONE, 5, 4D, 5.5D, 1D, 2.8D);
	}

	public void populate(World w, Random r, Chunk source) {
		int chunkX = source.getX();
		int chunkZ = source.getZ();

		int xPos = chunkX << 4;
		int zPos = chunkZ << 4;

		generateOre(w, r, xPos, zPos);

		Random rand = getRandom(w, xPos, zPos);

		if(rand.nextInt(150) < 1)
			ruin.place(w, r, xPos, 0, zPos);

		if(rand.nextInt(100) < 1)
			dungeon.place(w, r, xPos, 0, zPos);

		if(rand.nextInt(150) < 1)
			circle.place(w, r, xPos, 0, zPos);

		int x = xPos;
		int z = zPos;
		int soil = -1;
		int id = 0;
		int trees = rand.nextInt(rand.nextInt(31) + 1) + rand.nextInt(rand.nextInt(31) + 1);

		if (r.nextInt(8) < 1) {
			if (rand.nextBoolean()) {
				x = xPos + 16 - r.nextInt(16);
				z = zPos + 16 - r.nextInt(16);
			}
			else {
				x = xPos + r.nextInt(16);
				z = zPos + r.nextInt(16);
			}
			soil = this.getTopSoilY(w, x, z, chunkX, chunkZ);

			if (soil > 0)
				column.place(w, r, x, soil + 1, z);
		}

		for(int i = 0; i < trees; i++) {
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

		 if (r.nextInt(4) < 1) {
			x = xPos + 16 - r.nextInt(16);
			z = zPos + 16 - r.nextInt(16);
			soil = this.getTopSoilY(w, x, z, chunkX, chunkZ);

			if (soil > 0)
				megaRedwood.place(w, r, x, soil + 1, z);
		}

		if (r.nextInt(4) < 1) {
			if (rand.nextBoolean()) {
				x = xPos + 16 - r.nextInt(16);
				z = zPos + 16 - r.nextInt(16);
			}
			else {
				x = xPos + r.nextInt(16);
				z = zPos + r.nextInt(16);
			}
			soil = this.getTopSoilY(w, x, z, chunkX, chunkZ);

			if (soil > 0)
				fallenSpruce.place(w, r, x, soil + 1, z);
		}

		trees *= 6;

		for (int attempt = 0; attempt < trees; attempt++) {
			int cx = xPos + r.nextInt(16);
			int cz = zPos + r.nextInt(16);
			int cy = r.nextInt(140);

			id = w.getBlockTypeIdAt(cx, cy, cz);

			if (id != 0 && id != 78 && id != 79)
				continue;

			int id1 = w.getBlockTypeIdAt(cx + 1, cy, cz    );
			int id2 = w.getBlockTypeIdAt(cx - 1, cy, cz    );
			int id3 = w.getBlockTypeIdAt(cx    , cy, cz + 1);
			int id4 = w.getBlockTypeIdAt(cx    , cy, cz - 1);

			if (!checkIce(id1, id2, id3, id4))
				continue;

			miniJungle.place(w, r, cx, cy, cz);
		}

		for (int attempt = 0; attempt < trees; attempt++) {
			int cx = xPos + r.nextInt(16);
			int cz = zPos + r.nextInt(16);
			int cy = r.nextInt(140);

			id = w.getBlockTypeIdAt(cx, cy, cz);

			if (id != 0 && id != 78 && id != 79)
				continue;

			int id1 = w.getBlockTypeIdAt(cx + 1, cy, cz    );
			int id2 = w.getBlockTypeIdAt(cx - 1, cy, cz    );
			int id3 = w.getBlockTypeIdAt(cx    , cy, cz + 1);
			int id4 = w.getBlockTypeIdAt(cx    , cy, cz - 1);

			if (!checkIce(id1, id2, id3, id4))
				continue;

			weepingBirch.place(w, r, cx, cy, cz);
		}

		for (int attempt = 0; attempt < 24; attempt++) {
			int cx = xPos + r.nextInt(16);
			int cz = zPos + r.nextInt(16);
			int cy = 51;

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

			if (!checkWater(id1, id2, id3, id4))
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
			EntityType type;

			int dice = r.nextInt(100);

			if (dice < 30)
				type = EntityType.RABBIT;
			else if (dice < 55)
				type = EntityType.SHEEP;
			else if (dice < 65)
				type = EntityType.LLAMA;
			else if (dice < 75)
				type = EntityType.WOLF;
			else if (dice < 85)
				type = EntityType.HORSE;
			else if (dice < 95)
				type = EntityType.POLAR_BEAR;
			else
				type = EntityType.SNOWMAN;

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

				spawnPassive(l, type);
				spawned++;
			}
		}
	}

	private void spawnPassive(Location loc, EntityType type) {
		try {
			switch (type) {
				case RABBIT:     mobs.spawnRabbit(loc); break;
				case SHEEP:      mobs.spawnSheep(loc);  break;
				case LLAMA:      mobs.spawnLlama(loc);  break;
				case WOLF:       mobs.spawnWolf(loc);   break;
				case HORSE:      mobs.spawnHorse(loc);  break;
				case POLAR_BEAR: mobs.spawnBear(loc);   break;
				default:         mobs.spawnGolem(loc);  break;
			}
		} catch (Exception e) {}
	}

	private boolean checkIce(int id1, int id2, int id3, int id4) {
		if (id1 != 174 && id2 != 174 && id3 != 174 && id4 != 174) {
				return false;
		}

		return true;
	}

	private boolean checkWater(int id1, int id2, int id3, int id4) {
		if (id1 != 8 && id2 != 8 && id3 != 8 && id4 != 8) {
			if (id1 != 9 && id2 != 9 && id3 != 9 && id4 != 9) {
				return false;
			}
		}

		return true;
	}

	private void generateOre(World w, Random r, int x, int z) {
		granite.place(w, r, x + r.nextInt(16), r.nextInt(80) + 10, z + r.nextInt(16));
		granite.place(w, r, x + r.nextInt(16), r.nextInt(50) + 40, z + r.nextInt(16));
		granite.place(w, r, x + r.nextInt(16), r.nextInt(20) + 70, z + r.nextInt(16));
		diorite.place(w, r, x + r.nextInt(16), r.nextInt(80) + 10, z + r.nextInt(16));
		diorite.place(w, r, x + r.nextInt(16), r.nextInt(50) + 40, z + r.nextInt(16));
		diorite.place(w, r, x + r.nextInt(16), r.nextInt(20) + 70, z + r.nextInt(16));
		andesite.place(w, r, x + r.nextInt(16), r.nextInt(80) + 10, z + r.nextInt(16));
		andesite.place(w, r, x + r.nextInt(16), r.nextInt(50) + 40, z + r.nextInt(16));
		andesite.place(w, r, x + r.nextInt(16), r.nextInt(20) + 70, z + r.nextInt(16));
		coal.place(w, r, x + r.nextInt(16), r.nextInt(80) + 10, z + r.nextInt(16));
		coal.place(w, r, x + r.nextInt(16), r.nextInt(50) + 40, z + r.nextInt(16));
		coal.place(w, r, x + r.nextInt(16), r.nextInt(20) + 70, z + r.nextInt(16));
		lapis.place(w, r, x + r.nextInt(16), r.nextInt(72) + 10, z + r.nextInt(16));
		lapis.place(w, r, x + r.nextInt(16), r.nextInt(42) + 40, z + r.nextInt(16));
		lapis.place(w, r, x + r.nextInt(16), r.nextInt(12) + 70, z + r.nextInt(16));
		redstone.place(w, r, x + r.nextInt(16), r.nextInt(52) + 10, z + r.nextInt(16));
		redstone.place(w, r, x + r.nextInt(16), r.nextInt(32) + 30, z + r.nextInt(16));
		diamond.place(w, r, x + r.nextInt(16), r.nextInt(42) + 10, z + r.nextInt(16));
		diamond.place(w, r, x + r.nextInt(16), r.nextInt(32) + 20, z + r.nextInt(16));
		iron.place(w, r, x + r.nextInt(16), r.nextInt(62) + 10, z + r.nextInt(16));
		iron.place(w, r, x + r.nextInt(16), r.nextInt(42) + 30, z + r.nextInt(16));
		iron.place(w, r, x + r.nextInt(16), r.nextInt(22) + 50, z + r.nextInt(16));
		gold.place(w, r, x + r.nextInt(16), r.nextInt(52) + 10, z + r.nextInt(16));
		gold.place(w, r, x + r.nextInt(16), r.nextInt(42) + 20, z + r.nextInt(16));
		emerald.place(w, r, x + r.nextInt(16), r.nextInt(37) + 10, z + r.nextInt(16));

		if(r.nextBoolean())
			emerald.place(w, r, x + r.nextInt(16), r.nextInt(37) + 20, z + r.nextInt(16));

		gravel.place(w, r, x + r.nextInt(16), r.nextInt(62) + 10, z + r.nextInt(16));
		sand.place(w, r, x + r.nextInt(16), r.nextInt(62) + 10, z + r.nextInt(16));
	}

	public int getTopSoilY(World w, int x, int z, int chunkX, int chunkZ) {
		return this.getTopSoilY(w, x, 97, z, chunkX, chunkZ);
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
