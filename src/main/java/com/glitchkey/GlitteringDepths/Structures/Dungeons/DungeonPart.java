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

package com.glitchkey.glitteringdepths.structures.dungeons;

//* IMPORTS: JDK/JRE
	import java.util.List;
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

public abstract class DungeonPart extends StructureGenerator
{
	protected List<Integer> blocksN, blocksS, blocksE, blocksW;
	protected int height = 0;

	public DungeonPart()
	{
		fillNorth();
		fillSouth();
		fillEast();
		fillWest();
	}

	// Abstract the template filling methods
	protected abstract void fillNorth();
	protected abstract void fillSouth();
	protected abstract void fillEast();
	protected abstract void fillWest();

	protected abstract Material getType(int index);
	protected abstract int getData(int index, Random random);

	public boolean generate(World world, Random random, int x, int y, int z)
	{
		Location start = new Location(world, x, y, z);
		List<Integer> blocks;

		switch (random.nextInt(4))
		{
			case 0:  blocks = blocksN; break;
			case 1:  blocks = blocksS; break;
			case 2:  blocks = blocksE; break;
			default: blocks = blocksW; break;
		}

		for (int cx = 0; cx < 16; cx++)
		{
			for (int cz = 0; cz < 16; cz++)
			{
				for (int cy = 0; cy < height; cy++)
				{
					int i = blocks.get((cy << 8) + (cz << 4) + cx);

					if (i == 0)
						continue;

					Block b = world.getBlockAt(cx + x, y + 1 + cy, cz + z);
					addBlock(start, b, getType(i), getData(i, random));
				}
			}
		}

		return placeBlocks(start, true);
	}
}
