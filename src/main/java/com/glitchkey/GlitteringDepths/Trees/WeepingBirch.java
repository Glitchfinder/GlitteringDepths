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

public class WeepingBirch extends StructureGenerator
{
	Material air     = Material.AIR;
	Material dirt    = Material.DIRT;
	Material grass   = Material.GRASS;
	Material ice     = Material.ICE;
	Material leaves1 = Material.LEAVES;
	Material leaves2 = Material.LEAVES_2;
	Material log     = Material.LOG;
	Material snow    = Material.SNOW;

	public WeepingBirch()
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
		Location start = new Location(world, x, y, z);

		if (!isChunkValid(world, x, z))
			return fail(start);

		int rad  = random.nextInt(5) + 2;
		int diff = random.nextInt(3) + 2;
		int xDiff = random.nextInt(5);
		int zDiff = random.nextInt(5);

		if (random.nextBoolean())
			xDiff *= -1;
		if (random.nextBoolean())
			zDiff *= -1;

		int x2 = x + xDiff;
		int z2 = z + zDiff;

		int xMin = Math.min(x, x2);
		int zMin = Math.min(z, z2);
		int xMax = Math.max(x, x2);
		int zMax = Math.max(z, z2);

		double dist = rad;
		Location b1 = new Location(world, x, y - rad, z);
		Location b2 = new Location(world, x2, y - (rad + diff), z2);
		Location c1 = new Location(world, x, y, z);

		for (int cy = y; cy >= y - (rad * 2); cy--)
		{
			c1.setY(cy);

			for (int cx = xMin; cx <= xMax; cx++)
			{

				c1.setX(cx);
				for (int cz = zMin; cz <= zMax; cz++)
				{
					if (!isChunkValid(world, cx, cz))
						return fail(start);

					c1.setZ(cz);

					if (dist < c1.distance(b1))
						continue;
					if (dist > c1.distance(b2))
						continue;

					addLeaf(start, world, cx, cy, cz);
				}
			}
		}

		return placeBlocks(start, true);
	}

	private void addLeaf(Location s, World w, int x, int y, int z)
	{
		if (!isChunkValid(w, x, z))
			return;

		Block block = w.getBlockAt(x, y, z);

		if (!isInBlacklist(block))
			return;

		addBlock(s, block, leaves1, 2);

		block = w.getBlockAt(x, y + 1, z);

		if (!isInBlacklist(block))
			return;

		Material type = block.getType();

		if (type == ice || type == leaves1 || type == leaves2)
			return;

		if (!isInBlockList(s, block))
			addBlock(s, block, snow);
	}
}
