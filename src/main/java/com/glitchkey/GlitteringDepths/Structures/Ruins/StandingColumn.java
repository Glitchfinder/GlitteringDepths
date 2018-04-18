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

package com.glitchkey.glitteringdepths.structures.ruins;

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

public class StandingColumn extends StructureGenerator
{
	// Materials used in this class
	Material ice     = Material.ICE;
	Material leaves1 = Material.LEAVES;
	Material leaves2 = Material.LEAVES_2;
	Material quartz  = Material.QUARTZ_BLOCK;
	Material snow    = Material.SNOW;

	/**
	 * Generate an intact standing column
	 **/
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		// Get the base location of the column for tracking
		Location start = new Location(world, x, y, z);

		// Add the column base
		addPiece(start, world, x, y, z, 1);

		// Iterate through the column's height
		for (int cy = y + 4; cy > y; cy--)
		{
			// Place chiseled quartz as the top block
			if (cy == y + 4)
				addPiece(start, world, x, cy, z, 1);
			// Place vertical quartz columns as the middle blocks
			else
				addPiece(start, world, x, cy, z, 2);
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

		// Add a snow block if another block isn't already queued
		if (!isInBlockList(s, block))
			addBlock(s, block, snow);
	}
}
