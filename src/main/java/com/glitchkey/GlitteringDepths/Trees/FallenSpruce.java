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

package com.glitchkey.glitteringdepths.trees;

//* IMPORTS: JDK/JRE
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.StructureGenerator;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class FallenSpruce extends StructureGenerator
{
	Material air     = Material.AIR;
	Material dirt    = Material.DIRT;
	Material grass   = Material.GRASS;
	Material ice     = Material.ICE;
	Material leaves1 = Material.LEAVES;
	Material leaves2 = Material.LEAVES_2;
	Material log     = Material.LOG;
	Material snow    = Material.SNOW;

	public FallenSpruce()
	{
		addToBlacklist(air);
		addToBlacklist(ice);
		addToBlacklist(snow);

		for (int i = 0; i < 16; i++)
		{
			addToBlacklist(leaves1, i);
			addToBlacklist(leaves2, i);
		}
	}

	public boolean generate(World w, Random random, int x, int y, int z)
	{
		Location start = new Location(w, x, y, z);

		if (!isChunkValid(w, x, z))
			return fail(start);

		addTrunk(start, w, x, y, z, 1);

		int direction = random.nextInt(4);
		int length = random.nextInt(6) + 3;

		int xm = 0;
		int zm = 0;
		int data = 0;
		int count = 0;
		int xMax = 1;
		int zMax = 1;
		Material type;

		if (direction < 2)
		{
			xm = 1;
			xMax = length;
			data = 5;
		}
		else
		{
			zm = 1;
			zMax = length;
			data = 9;
		}

		if (direction % 2 == 0)
		{
			xm *= -1;
			zm *= -1;
		}

		for (int cx = x + (xm * 2); count < xMax; cx += xm)
		{
			int zcount = 0;

			for (int cz = z + (zm * 2); zcount < zMax; cz += zm)
			{
				if (!isChunkValid(w, cx, cz))
					return placeBlocks(start, true);

				type = w.getBlockAt(cx, y - 1, cz).getType();

				if (type != dirt && type != grass)
					return placeBlocks(start, true);

				addTrunk(start, w, cx, y, cz, data);
				count += 1;
				zcount += 1;
			}
		}

		return placeBlocks(start, true);
	}

	private void addTrunk(Location s, World w, int x, int y, int z, int d)
	{
		if (!isChunkValid(w, x, z))
			return;

		Block block = w.getBlockAt(x, y, z);

		if (!isInBlacklist(block))
			return;

		addBlock(s, block, log, d);

		block = w.getBlockAt(x, y + 1, z);

		if (!isInBlacklist(block))
			return;

		Material type = block.getType();

		if (type == ice || type == leaves1 || type == leaves2)
			return;

		if (!isInBlockList(s, block))
			addBlock(s, block, snow);
	}

	public boolean isChunkValid(World w, int x, int z)
	{
		x = x >> 4; // Chunk X
		z = z >> 4; // Chunk Z

		// If the chunk is not loaded, and does not exist
		if (!w.isChunkLoaded(x, z) && !w.loadChunk(x, z, false))
			return false;

		return true;
	}
}
