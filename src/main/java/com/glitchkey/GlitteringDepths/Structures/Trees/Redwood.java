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

package com.glitchkey.glitteringdepths.structures.trees;

//* IMPORTS: JDK/JRE
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.structures.StructureGenerator;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class Redwood extends StructureGenerator
{
	Material air     = Material.AIR;
	Material dirt    = Material.DIRT;
	Material grass   = Material.GRASS;
	Material ice     = Material.ICE;
	Material leaves1 = Material.LEAVES;
	Material leaves2 = Material.LEAVES_2;
	Material log     = Material.LOG;
	Material snow    = Material.SNOW;

	public Redwood()
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

	public boolean generate(World world, Random random, int x, int y, int z)
	{
		int maxHeight = random.nextInt(4) + 7;
		int leafHeight = maxHeight - (2 + random.nextInt(2));
		int leafWidth = 2 + random.nextInt(2);
		Location start = new Location(world, x, y, z);

		if (!isChunkValid(world, x, z))
			return fail(start);

		if ((y < 1) || (y + maxHeight + 1 > 256))
			return fail(start);

		Material type = world.getBlockAt(x, y - 1, z).getType();

		if ((type != grass && type != dirt) || y >= (255 - maxHeight))
			return fail(start);

		addBlock(start, world.getBlockAt(x, y - 1, z), dirt, 0);

		int rad = random.nextInt(2);
		int width = 1;
		int canopySpawned = 0;

		for (int depth = 0; depth <= leafHeight; depth++)
		{
			int cy = y + maxHeight - depth;

			for (int cx = x - rad; cx <= x + rad; cx++)
			{
				int xRad = cx - x;

				for (int cz = z - rad; cz <= z + rad; cz++)
				{
					if (!isChunkValid(world, cx, cz))
						return fail(start);

					int zRad = cz - z;

					Block b = world.getBlockAt(cx, cy, cz);

					if (!isInBlacklist(b))
						continue;

					boolean c1 = (Math.abs(xRad) != rad);
					boolean c2 = (Math.abs(zRad) != rad);
					boolean c3 = (c1 || c2 || (rad <= 0));

					if (!c3)
						continue;

					addBlock(start, b, leaves1, 1);

					b = world.getBlockAt(cx, cy + 1, cz);

					if (!isInBlacklist(b))
						continue;

					Material t = b.getType();

					if (t == ice)
						continue;
					else if (t == leaves1 || t == leaves2)
						continue;

					if (!isInBlockList(start, b))
						addBlock(start, b, snow);
				}
			}

			if (rad >= width)
			{
				rad = canopySpawned;
				canopySpawned = 1;
				width++;
				if (width > leafWidth)
					width = leafWidth;
			}
			else {
				rad++;
			}
		}

		int depth = random.nextInt(3);

		for (int cDepth = 0; cDepth < maxHeight - depth; cDepth++)
		{
			Block block = world.getBlockAt(x, y + cDepth, z);

			if (!isInBlacklist(block))
				continue;

			addBlock(start, block, log, 1);
		}

		return placeBlocks(start, true);
	}
}
