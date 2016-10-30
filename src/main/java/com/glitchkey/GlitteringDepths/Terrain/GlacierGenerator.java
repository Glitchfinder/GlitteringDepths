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
	import org.bukkit.block.Biome;
	import org.bukkit.generator.ChunkGenerator.BiomeGrid;
	import org.bukkit.World;
//* IMPORTS: PANDORA
	import org.pandora.EmptyBiomeGrid;
	import org.pandora.PandoraGenerator;
	import org.pandora.PandoraBiome;
//* IMPORTS: GLITTERING DEPTHS
	//* NOT NEEDED
//* IMPORTS: OTHER
	//* NOT NEEDED

public class GlacierGenerator extends PandoraBiome
{
	public GlacierGenerator()
	{
		this.minTemperature = 0.0F;
		this.maxTemperature = 50.0F;
		this.minHumidity = 0.0F;
		this.maxHumidity = 100.0F;
	}

	public short[] generateExtSections(World w, Random random, int xPos,
		int zPos, BiomeGrid biomes)
	{
		short[] result = new short[256];

		int x = Math.abs(xPos % 16);
		int z = Math.abs(zPos % 16);

		int ice = 127;
		int stone = 63;
		int bRock = 5;

		ice += (int) getNoise(w, xPos, zPos, 1D, 1D, 2, 16D, 0.015D);
		stone += (int) getNoise(w, xPos, zPos, 1D, 1D, 2, 8D, 0.03D);
		bRock += (int) getNoise(w, xPos, zPos, 1D, 1D, 2, 4D, 0.06D);

		biomes.setBiome(x, z, Biome.TAIGA);

		int chunkX = xPos >> 4;
		int chunkZ = zPos >> 4;

		int baseX = (chunkX << 4) - ((chunkX << 4) % 10);
		int baseZ = (chunkZ << 4) - ((chunkZ << 4) % 10);

		int xMin = baseX - 80;
		int zMin = baseZ - 80;
		int xMax = baseX + 100;
		int ZMax = baseZ + 100;

		double mod = 0D;

		for(int cx = xMin; cx < xMax; cx += 10)
		{
			for(int cz = zMin; cz < ZMax; cz += 10)
			{
				mod = generateColumn(mod, cx, cz, xPos, zPos);
			}
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
				result, column, mod);

			result[y] = id;
		}

		return result;
	}

	public short getId(World world, int bedrock, int stone, int ice, int x,
		int y, int z, short[] result, double column, double modifier)
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

		if (id == 79 && (y > (stone + 60) || y < (stone + 15)))
			id = checkFissures(world, id, x, y, z);

		id = checkColumn(world, id, x, y, z, column, modifier, ice);

		if(id == 0 && y <= 38)
			id = 9;

		return id;
	}

	public short checkColumn(World w, short id, int x, int y, int z,
		double column, double mod, int ice)
	{
		double baseHeight = Math.abs(((double) y) - 68D);

		if(baseHeight >= 60D && (y > 143 || y <= 8))
			return id;

		baseHeight -= 60D;
		baseHeight = Math.abs(baseHeight);
		baseHeight /= 60D + baseHeight;

		double noise = getNoise(w, x, y, z, 4, 0.15D, 120D) + 1D;
		double distortion;
		distortion = (getNoise(w, x, y, z, 2, 0.06D, 10D) / 9D) + 0.5D;

		noise = (noise + (noise * distortion)) / 2D;
		double height = (baseHeight / column);

		if ((noise <= height) && (noise > baseHeight - mod) && y < ice)
			return 79;
		if (noise <= height) {
			return 0;
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

	private double generateColumn(double modifier, int x, int z, int xPos,
		int zPos)
	{
		Random random = getRandom(x, z);

		if(random.nextInt(100) > 5 || random.nextInt(100) > 20)
			return modifier;

		double tempModifier = (double) random.nextInt(100);
		tempModifier = ((tempModifier + 1D) / 150D) + 1D;
		int cx = (x + random.nextInt(25));
		int cz = (z + random.nextInt(25));

		double xSq = Math.pow((xPos - cx), 2);
		double zSq = Math.pow((zPos - cz), 2);
		double distance = Math.sqrt(xSq + zSq);

		if (distance <= 0D)
			return Math.max(modifier, tempModifier);
		else
			return Math.max(modifier, (tempModifier / distance));
	}

	private double getNoise(World world, double x, double y, double z,
		int octaves, double frequency, double amplitude)
	{
		return getNoiseGenerator(world).noise(x, y, z, octaves,
			frequency, amplitude, true);
	}

}