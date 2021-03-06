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

package com.glitchkey.glitteringdepths.ruins;

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

public final class FallenColumn extends StructureGenerator
{
	// Materials used in this class
	private Material dirt    = Material.DIRT;
	private Material grass   = Material.GRASS;
	private Material ice     = Material.ICE;
	private Material leaves1 = Material.LEAVES;
	private Material leaves2 = Material.LEAVES_2;
	private Material quartz  = Material.QUARTZ_BLOCK;
	private Material snow    = Material.SNOW;

	/** 
	 * Generate a fallen column
	 **/
	public boolean generate(World w, Random random, int x, int y, int z)
	{
		// Get the base location of the column for tracking
		Location start = new Location(w, x, y, z);

		// Add the column base
		addPiece(start, w, x, y, z, 1);

		// Choose a direction at random
		int dir = random.nextInt(4);

		// Default the coordinate variables
		int xm = 0;
		int zm = 0;
		int data = 0;
		int count = 0;
		int xl = 1;
		int zl = 1;
		// Preallocate a material variable
		Material type;

		// If the column is east/west
		if (dir < 2)
		{
			// Set the x-based coordinate variables
			xm = 1;
			xl = 4;
			// Set the column to face east/west
			data = 3;
		}
		// If the column is north/south
		else
		{
			// Set the z-based coordinate variables
			zm = 1;
			zl = 4;
			// Set the column to face north/south
			data = 4;
		}

		// If the column is north/west
		if (dir % 2 == 0)
		{
			// Flip the directional coordinates
			xm *= -1;
			zm *= -1;
		}

		// Iterate through the length in the x direction
		for (int cx = x + (xm * 2); count < xl; cx += xm)
		{
			// Preset the z counter
			int zcount = 0;

			// Iterate through the length in the z direction
			for (int cz = z + (zm * 2); zcount < zl; cz += zm)
			{
				// Get the type of block below the current one
				type = w.getBlockAt(cx, y - 1, cz).getType();

				// Cut placement short if not grass or dirt
				if (type != dirt && type != grass)
					return placeBlocks(start, true);

				// Update relative counts
				count += 1;
				zcount += 1;

				// If an end piece, add a chiseled quartz
				if (count >= xl && zcount >= zl)
					addPiece(start, w, cx, y, cz, 1);
				// If not an end piece, add a normal column
				else
					addPiece(start, w, cx, y, cz, data);
			}
		}

		// Place the column
		return placeBlocks(start, true);
	}

	/**
	 * Add a piece to the column
	 **/
	private void addPiece(Location s, World w, int x, int y, int z, int d)
	{
		// Get the current block
		Block block = w.getBlockAt(x, y, z);

		// Skip if the block isn't whitelisted
		if (!isInBlacklist(block))
			return;

		// Add the block to the column
		addBlock(s, block, quartz, d);

		// Get the block above the current block
		block = w.getBlockAt(x, y + 1, z);

		// Skip if the block isn't whitelisted
		if (!isInBlacklist(block))
			return;

		// Get the block's material
		Material type = block.getType();

		// Skip if the block is ice or leaves
		if (type == ice || type == leaves1 || type == leaves2)
			return;

		// Add a snow block on top of the column
		addBlock(s, block, snow);
	}
}
