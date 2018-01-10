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

package com.glitchkey.glitteringdepths.structures.trees;

//* IMPORTS: JDK/JRE
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.Location;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.structures.StructureGenerator;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class Redwood extends StructureGenerator
{
	public Redwood(boolean notifyOnBlockChanges) {
		super(notifyOnBlockChanges, true);

		addToBlacklist(0);

		for (int i = 0; i < 16; i++) {
			addToBlacklist(18, i);
		}
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		int maxHeight = random.nextInt(4) + 7;
		int leafHeight = maxHeight - (2 + random.nextInt(2));
		int leafWidth = 2 + random.nextInt(2);
		Location start = new Location(world, x, y, z);

		if ((y < 1) || (y + maxHeight + 1 > 256))
			return false;

		int baseId = world.getBlockTypeIdAt(x, y - 1, z);

		if (((baseId != 2) && (baseId != 3)) || (y >= 256 - maxHeight - 1))
			return false;

		addToWhitelist(start, world.getBlockAt(x, y - 1, z));
		addBlock(start, world.getBlockAt(x, y - 1, z), 3, 0);

		int radius = random.nextInt(2);
		int width = 1;
		int canopySpawned = 0;

		for (int depth = 0; depth <= leafHeight; depth++)
		{
			int cy = y + maxHeight - depth;

			for (int cx = x - radius; cx <= x + radius; cx++)
			{
				int xRadius = cx - x;

				for (int cz = z - radius; cz <= z + radius; cz++)
				{
					// Cancel if the block is invalid for some reason
					if (!isChunkValid(world, cx, cz))
						return false;

					int zRadius = cz - z;

					Block block = world.getBlockAt(cx, cy, cz);

					if (!isInBlacklist(block))
						continue;

					boolean cond1 = (Math.abs(xRadius) != radius);
					boolean cond2 = (Math.abs(zRadius) != radius);
					boolean cond3 = (cond1 || cond2 || (radius <= 0));

					if (!cond3)
						continue;

					addBlock(start, block, 18, 1);

					block = world.getBlockAt(cx, cy + 1, cz);

					if (!isInBlacklist(block))
						continue;

					if (!isInBlockList(start, block))
						addBlock(start, block, 78);
				}
			}

			if (radius >= width) {
				radius = canopySpawned;
				canopySpawned = 1;
				width++;
				if (width > leafWidth)
					width = leafWidth;
			}
			else {
				radius++;
			}
		}

		int depth = random.nextInt(3);

		for (int cDepth = 0; cDepth < maxHeight - depth; cDepth++) {
			Block block = world.getBlockAt(x, y + cDepth, z);

			if (!isInBlacklist(block))
				continue;

			addBlock(start, block, 17, 1);
		}

		return placeBlocks(start, true);
	}

	public boolean isChunkValid(World world, int x, int z) {
		x = x >> 4; // Chunk X
		z = z >> 4; // Chunk Z

		// If the chunk is not loaded, and does not exist
		if (!world.isChunkLoaded(x, z) && !world.loadChunk(x, z, false))
			return false;

		return true;
	}
}
