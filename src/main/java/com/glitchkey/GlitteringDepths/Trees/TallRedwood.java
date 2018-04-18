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

public class TallRedwood extends StructureGenerator
{
	Material air     = Material.AIR;
	Material dirt    = Material.DIRT;
	Material grass   = Material.GRASS;
	Material ice     = Material.ICE;
	Material leaves1 = Material.LEAVES;
	Material leaves2 = Material.LEAVES_2;
	Material log     = Material.LOG;
	Material snow    = Material.SNOW;

	public TallRedwood()
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
		int maxHeight = random.nextInt(5) + 7;
		int leafHeight = maxHeight - random.nextInt(2) - 3;
		int baseLeafWidth = maxHeight - leafHeight;
		int maxLeafWidth = 1 + random.nextInt(baseLeafWidth + 1);
		Location start = new Location(world, x, y, z);

		if (!isChunkValid(world, x, z))
			return fail(start);

		if (y < 1 || (y + maxHeight + 1) > 256)
			return fail(start);

		Material type = world.getBlockAt(x, y - 1, z).getType();

		if ((type != grass && type != dirt) || y >= (255 - maxHeight))
			return fail(start);

		addBlock(start, world.getBlockAt(x, y - 1, z), dirt, 0);

		int width = 0;

		for (int cy = y + maxHeight; cy >= y + leafHeight; --cy)
		{
			for (int cx = x - width; cx <= x + width; ++cx)
			{
				int cw = Math.abs(cx - x);

				for (int cz = z - width; cz <= z + width; ++cz)
				{
					if (!isChunkValid(world, cx, cz))
						return fail(start);

					int l = Math.abs(cz - z);
					Block b = world.getBlockAt(cx, cy, cz);

					if (!isInBlacklist(b))
						continue;

					if (cw == l && cw == width && width > 0)
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

			if (width >= 1 && cy == (y + leafHeight + 1))
			{
				--width;
			}
			else if (width < maxLeafWidth)
			{
				++width;
			}
		}

		for (int cy = 0; cy < maxHeight - 1; ++cy)
		{
			Block block = world.getBlockAt(x, y + cy, z);

			if (!isInBlacklist(block))
				continue;

			addBlock(start, block, log, 1);
		}

		return placeBlocks(start, true);
	}
}
