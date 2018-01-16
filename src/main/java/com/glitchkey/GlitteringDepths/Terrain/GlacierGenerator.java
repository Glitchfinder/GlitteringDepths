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
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Biome;
	import org.bukkit.block.Block;
	import org.bukkit.generator.BlockPopulator;
	import org.bukkit.generator.ChunkGenerator;
	import org.bukkit.generator.ChunkGenerator.BiomeGrid;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.util.noise.SimplexNoiseGenerator;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.datatypes.Column;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class GlacierGenerator extends ChunkGenerator
{
	private Map<World, SimplexNoiseGenerator> noises;
	private World world;
	private List<BlockPopulator> populators;
	

	public GlacierGenerator() {
		noises = new HashMap<World, SimplexNoiseGenerator>();
		populators = new ArrayList<BlockPopulator>();
		populators.add((BlockPopulator) (new GlacierPopulator()));
	}

	public List<BlockPopulator> getDefaultPopulators(World world) {
		return populators;
	}

	public boolean canSpawn(World world, int x, int z) {
		if (world == null)
			return false;

		Block b;
		boolean spawnable;

		switch (world.getEnvironment()) {
			case NETHER:
				return true;
			case THE_END:
				b = world.getBlockAt(x, world.getHighestBlockYAt(x, z), z);
				spawnable = true;
				spawnable = ((b.getType() != Material.AIR) ? spawnable : false);
				spawnable = ((b.getType() != Material.WATER) ? spawnable : false);
				spawnable = ((b.getType() != Material.LAVA) ? spawnable : false);
				return spawnable;
			case NORMAL:
			default:
				b = world.getBlockAt(x, world.getHighestBlockYAt(x, z), z);
				spawnable = false;
				spawnable = ((b.getType() != Material.SAND) ? spawnable : true);
				spawnable = ((b.getType() != Material.GRAVEL) ? spawnable : true);
				return spawnable;
		}
	}

	@Deprecated
	public byte[] generate(World world, Random rand, int x, int z) {
		return null;
	}

	public byte[][] generateBlockSections(World world, Random rand, int x,
		int z, BiomeGrid biomes)
	{
		return null;
	}

	public short[][] generateExtBlockSections(World world, Random rand,
		int x, int z, BiomeGrid biomes)
	{
		int height = world.getMaxHeight();
		short[][] chunk = new short[height / 16][];
		int xPos = x << 4;
		int zPos = z << 4;

		int baseX = xPos - (xPos % 10);
		int baseZ = zPos - (zPos % 10);

		int xMin = baseX - 80;
		int zMin = baseZ - 80;
		int xMax = baseX + 100;
		int ZMax = baseZ + 100;

		List<Column> columns = new ArrayList<Column>();

		for(int cx = xMin; cx < xMax; cx += 10)
		{
			for(int cz = zMin; cz < ZMax; cz += 10)
			{
				genColumn(cx, cz, columns);
			}
		}


		for (int cx = 0; cx < 16; cx++) {
			int cxPos = cx + xPos;

			for (int cz = 0; cz < 16; cz++) {
				int czPos = cz + zPos;

				short[] column = generateExtSections(world,
					rand, cxPos, czPos, biomes, columns);

				for (int cy = 0; cy < height; cy++) {
					short id = column[cy];
					setBlock(chunk, cx, cy, cz, id);
				}
			}
		}

		return chunk;
}

	public short[] generateExtSections(World w, Random random, int xPos,
		int zPos, BiomeGrid biomes, List<Column> columns)
	{
		short[] result = new short[256];

		int x = Math.abs(xPos % 16);
		int z = Math.abs(zPos % 16);
		biomes.setBiome(x, z, Biome.TAIGA_COLD);

		int ice = 0;
		int stone = 63;
		int bRock = 5;

		ice += (int) getNoise(w, xPos, zPos, 1D, 1D, 2, 16D, 0.03D);
		stone += ice / 2;
		ice += 127;

		double a = 50D;
		double b = 50D;

		a -= getNoiseD(w, xPos, zPos, 100D, 1D, 2, 1D, 0.0011D);
		b -= getNoiseD(w, zPos, xPos, 100D, 1D, 2, 1D, 0.0011D);

		double perc = 0D;
		double perc1 = 0D;
		double perc2 = 0D;
		int    type = 0;
		//int subtype = 0;

		if (a < -4D && b > 4D) {  // PLAINS
			perc1 = Math.max(0, (a + 6D) / 2D);
			perc2 = Math.max(0, 1D - ((b - 4D) / 2D));
			perc = Math.max(perc1, perc2);
			biomes.setBiome(x, z, Biome.ICE_FLATS);
			type = 1;
			//subtype = (perc1 == perc) ? 1 : 2;
		}
		else if (a > -4D && a < 50D && b > 4D) { // COVERED PLAINS
			perc = Math.max(0, Math.max((a - 48D) / 2D, 1D - ((b - 4D) / 2D)));
			biomes.setBiome(x, z, Biome.ICE_MOUNTAINS);
			type = 4;
		}
		else if (a < -4D && b < -4D) { // OCEAN
			perc = Math.max(0, Math.max((a + 6D) / 2D, (b + 6D) / 2D));
			biomes.setBiome(x, z, Biome.FROZEN_OCEAN);
			type = 2;
		}
		else if (a > 4D && b < -4D) { // MOUNTAINS
			perc = Math.max(0, Math.max((b + 6D) / 2D, 1D - ((a - 4D) / 2D)));
			biomes.setBiome(x, z, Biome.TAIGA_COLD_HILLS);
			type = 3;
		}

		double sh = 68D;
		double d  = 60D;
		double hm = 1D;
		double dm = 0D;
		int    i  = 60;

		if (type == 1) { // PLAINS
			int hmod = (int) lerp(15D, 0D,  perc);
			sh       =       lerp(98D, 68D, perc2);
			d        =       lerp(30D, 60D, perc2);
			dm       =       lerp(2D,  0D,  perc2);
			hm       =       lerp(6D, 1D,   perc2);
			stone   += (int) lerp(15D, 0D,  perc2);
			ice     -= hmod;
			i	-= hmod + (int) lerp(15D, 0D, perc2);
		}
		else if (type == 4) { // COVERED PLAINS
			int hmod = (int) lerp(15D, 0D,  perc);
			sh       =       lerp(98D, 68D, perc);
			d        =       lerp(30D, 60D, perc);
			dm       =       lerp(2D,  0D,  perc);
			stone   += hmod;
			i	-= hmod;
		}

		if (type == 2 || type == 4) { // COVERED PLAINS, OCEAN
			hm = lerp(6D, 1D, perc);
		}

		double mod = 0D;

		for(Column c : columns) {
			mod = generateColumn(mod, xPos, zPos, c);
		}

		double column;
		column = (double) getNoise(w, xPos, zPos, 1D, 1D, 2, 32D, 0.06);
		column = Math.abs(column / 16D) + 1D;
		mod = Math.max(0D, mod);
		boolean ground = false;

		for(int y = 143; y >= 0; y--) {
			if (!ground && y <= stone) {
				column = (column / 1.5D) + 1D;
				ground = true;
			}

			short id;
			id = this.getId(w, bRock, stone, ice, xPos, y, zPos,
				result, column, mod, type, sh, d, hm, dm, i, perc);

/*
			if (a < -4D && b < -4D) {
				id = (short) 22;
				if (a > -6D || b > -6D)
					id = (short) 174;
			}
			else if (a < -4D && b > 4D) {
				id = (short) 41;
				if (a > -6D || b < 6D)
					id = (short) 19;
			}
			else if (a > 4D && b < -4D) {
				id = (short) 42;
				if (a < 6D || b > -6D)
					id = (short) 82;
			}
			else
				id = (short) 57;
*/
			result[y] = id;
		}

		return result;
	}

	public short getId(World world, int bedrock, int stone, int ice, int x,
		int y, int z, short[] result, double column, double modifier,
		int type, double sh, double d, double hm, double dm, int iceMod,
		double perc)
	{
		short id = 0;

		if(y <= bedrock)
			id = 7;
		else if(y <= stone) {
			short temp;
			temp = result[y + 1];

			if(temp == 0) 
				id = 2;
			else if(temp == 2 || temp == 9)
				id = 3;
			else
				id = 1;
		}
		else if (y <= stone)
			id = 1;
		else if(y <= ice)
			id = 79;

		if (id == 79 && (y > (stone + iceMod) || y < stone + 15))
			id = checkFissures(world, id, x, y, z);

		id = checkColumn(world, id, x, y, z, column, modifier, ice,
			type, sh, d, hm, dm, perc);

		if(id == 0 && y <= 38)
			id = 9;

		if (id == 2 && result[y + 1] == 0)
			result[y + 1] = 78;

		return id;
	}

	public short checkColumn(World w, short id, int x, int y, int z,
		double column, double mod, int ice, int type,
		double startHeight, double depth, double heightMod,
		double distMod, double perc)
	{
		double baseHeight = Math.abs(((double) y) - startHeight);

		if(baseHeight >= depth && ((y > 158 || y <= 8) || (type == 1 || type == 4)))
			return id;

		baseHeight -= depth;
		baseHeight  = Math.abs(baseHeight);
		baseHeight /= depth + baseHeight;

		baseHeight *= heightMod;
		mod        *= heightMod;

		double noise = 0;
		double height = (baseHeight / column);

		if (type != 3) {
			noise = getNoise(w, x, y, z, 4, 0.15D, 120D) + 1D;
		}
		else if (perc <= 0D) {
			noise = getNoise(w, x, y, z, 4, 0.3D, 120D) + 1D;
		}
		else {
			double n1 = lerp(1D, getNoise(w, x, y, z, 4, 0.15D, 120D), perc);
			double n2 = lerp(getNoise(w, x, y, z, 4, 0.3D, 120D), 1D, perc);
			noise = Math.min(n1, n2) + 1D;
			//System.out.println("N: " + noise + ", N1: " + n1);
		}

		double distortion;
		distortion = (getNoise(w, x, y, z, 2, 0.06D, 10D) / 9D) + 0.5D;
		distortion += distMod;
		noise = (noise + (noise * distortion)) / 2D;

		if ((noise <= height) && (noise > baseHeight - mod) && y < ice)
			return 79;
		if (noise <= height) {
			return 0;
		}
		if (noise >= height + 0.05D && id == 79) {
			return 174;
		}

		return id;
	}

	double lerp(double start, double end, double percent) {
		return (start + (percent * (end - start)));
	}

	private short checkFissures(World w, short id, int x, int y,
		int z)
	{
		double noise = Math.abs(getNoise(w, x, y, z, 2, 0.06D, 50D));

		if (noise <= 0.2D)
			return 0;

		return id;
	}

	private Random getRandom(long x, long z) {
		long seed = (x * 341873128712L + z * 132897987541L) ^ 1575463L;
		return new Random(seed);
	}

	private void genColumn(int x, int z, List<Column> list) {
		Random random = getRandom(x, z);

		//int var1 = 5;
		//int var2 = 20;

		//if (type == 1 || type == 2) {// PLAINS, OCEAN
		//	var1 = (int) lerp(10D, 5D, perc);
		//	var2 = (int) lerp(25D, 20D, perc);
		//}

		if(random.nextInt(100) > 10 || random.nextInt(100) > 25)
			return;

		double tempModifier = (double) random.nextInt(100);
		tempModifier = ((tempModifier + 1D) / 150D) + 1D;
		int cx = (x + random.nextInt(25));
		int cz = (z + random.nextInt(25));

		list.add(new Column(cx, cz, tempModifier));
	}

	private double generateColumn(double modifier, int xPos, int zPos,
		Column c)
	{
		double xSq = Math.pow((xPos - c.x), 2);
		double zSq = Math.pow((zPos - c.z), 2);
		double distance = Math.sqrt(xSq + zSq);

		if (distance <= 0D)
			return Math.max(modifier, c.modifier);
		else
			return Math.max(modifier, (c.modifier / distance));
	}

	public Location getFixedSpawnLocation(World world, Random rand) {
		return null;
	}

	public final int getNoise(World w, int x, int z, double range,
		double scale, int octaves, double amp, double frequency)
	{
		return (int) getNoiseD(w, x, z, range, scale, octaves, amp, 
			frequency);
	}

	public final double getNoiseD(World w, int x, int z, double range,
		double scale, int octaves, double amp, double frequency)
	{
		SimplexNoiseGenerator noise = noises.get(w);

		if (noise == null) {
			noises.put(w, (new SimplexNoiseGenerator(w.getSeed())));
			noise = noises.get(w);
		}

		range /= 2D;
		int xs = (int) Math.round(((double) x) / scale);
		int zs = (int) Math.round(((double) z) / scale);
		double cnoise = noise.getNoise(xs, zs, octaves, frequency, amp);
		return ((range * cnoise) + range);
	}

	private double getNoise(World world, double x, double y, double z,
		int octaves, double frequency, double amplitude)
	{
		return getNoiseGenerator(world).noise(x, y, z, octaves,
			frequency, amplitude, true);
	}

	private void setBlock(short[][] result, int x, int y, int z, short id) {
		if (result[y >> 4] == null)
			result[y >> 4] = new short[4096];

		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = id;
	}

	public final SimplexNoiseGenerator getNoiseGenerator(World world) {
		return noises.get(world);
	}
}
