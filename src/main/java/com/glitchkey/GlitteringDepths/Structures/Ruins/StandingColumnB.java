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
	import org.bukkit.block.BlockFace;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.structures.StructureGenerator;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class StandingColumnB extends StructureGenerator
{
	// Materials used in this class
	Material dirt          = Material.DIRT;
	Material grass         = Material.GRASS;
	Material ice           = Material.ICE;
	Material leaves1       = Material.LEAVES;
	Material leaves2       = Material.LEAVES_2;
	Material quartz        = Material.QUARTZ_BLOCK;
	Material quartz_stairs = Material.QUARTZ_STAIRS;
	Material slab          = Material.STEP;
	Material snow          = Material.SNOW;
	Material water1        = Material.WATER;
	Material water2        = Material.STATIONARY_WATER;

	/**
	 * Constructor
	 **/
	public StandingColumnB(boolean notifyOnBlockChanges)
	{
		// Forward settings to the base class
		super(notifyOnBlockChanges);
	}

	/** 
	 * Generate a broken standing column
	 **/
	public boolean generate(World w, Random r, int x, int y, int z)
	{
		// Get the base location of the column for tracking
		Location start = new Location(w, x, y, z);

		// Add the column base
		addPiece(start, w, x, y, z, 1);

		// Preset height and breakage status
		int height = 5;
		boolean broken = false;

		// Iterate through the column's height
		for (int cy = y + 1; cy < y + height; cy++)
		{
			// Check whether this is the top of the column
			boolean last = (cy == y + 4);
			// If the column is broken, drop a piece to the ground
			if (broken)
				addChunk(start, w, x, cy, z, r, last);
			// If the column is not broken, break it on a 1/2 chance
			else if (r.nextBoolean())
			{
				// Set the column as broken
				broken = true;
				// Randomly select a piece type
				int type = r.nextInt(3);

				// On a 1/3 chance, leave stairs
				if (type == 0)
					addStair(start, w, x, cy, z, r);
				// On a 1/3 chance, leave a slab
				else if (type == 1)
					addSlab(start, w, y, cy, z);
				// On a 1/3 chance, drop a piece to the ground
				else
					addChunk(start, w, x, cy, z, r, last);
			}
			// If the column is not broken and this is the top
			else if (last)
				// Add the chiseled quartz capstone
				addPiece(start, w, x, cy, z, 1);
			// If the column is not broken and this is not the top
			else
				// Add a vertical quartz column piece
				addPiece(start, w, x, cy, z, 2);
		}

		// Place the column
		return placeBlocks(start, true);
	}

	/**
	 * Drops a broken piece of the column to the ground
	 **/
	private void addChunk(Location s, World w, int x, int y, int z,
		Random r, boolean end)
	{
		// Get a random offset within 2 blocks
		int cx = x + r.nextInt(5) - 3;
		int cz = z + r.nextInt(5) - 3;

		// Ensure this is in a generated chunk, return otherwise
		if (!isChunkValid(w, cx, cz))
			return;

		// Get the height of the ground at the selected offset
		int cy = getTopSoilY(w, cx, y, cz) + 1;

		// Cancel if the offset is invalid
		if (cy <= 1)
			return;

		// Get a random value
		int type = r.nextInt(5);

		// On a 1/5 chance, place quartz stairs
		if (type == 0)
			addStair(s, w, cx, cy, cz, r);
		// On a 1/5 chance, place a quartz slab
		else if (type == 1)
			addSlab(s, w, cx, cy, cz);
		// On a 1/5 chance, place a smooth quartz block
		else if (type == 2)
			addPiece(s, w, cx, cy, cz, 0);
		// On a 1/5 chance, place a chiseled quartz block
		else if (type == 3 && end)
			addPiece(s, w, cx, cy, cz, 1);
		// On a 1/5 chance, place a randomly oriented quartz column
		else
			addPiece(s, w, cx, cy, cz, r.nextInt(3) + 2);
	}

	/**
	 * Add a stairs block to 'break' the column
	 **/
	private void addStair(Location s, World w, int x, int y, int z,
		Random r)
	{
		// Get the current block
		Block block = w.getBlockAt(x, y, z);

		// Skip if the block isn't whitelisted
		if (!isInBlacklist(block))
			return;

		// Add a randomly oriented quartz stairs block to the column
		addBlock(s, block, quartz_stairs, r.nextInt(4));
	}

	/**
	 * Add a slab block to 'break' the column
	 **/
	private void addSlab(Location s, World w, int x, int y, int z)
	{
		// Get the current block
		Block block = w.getBlockAt(x, y, z);

		// Skip if the block isn't whitelisted
		if (!isInBlacklist(block))
			return;

		// Add a quartz slab to the column
		addBlock(s, block, slab, 7);
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

		// Add a quartz block to the column
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
		if (!isInBlockList(s, block))
			addBlock(s, block, snow);
	}

	/**
	 * Calculates the Y elevation of the top layer of ground
	 *  --
	 * Returns -1 if placement is invalid
	 **/
	public int getTopSoilY(World w, int x, int maxY, int z)
	{
		// Check elevation, starting at height 97 and descending
		for (int y = maxY; y >= 0; y--)
		{
			// Get the material at the current coordinates
			Block b = w.getBlockAt(x, y, z);
			Material material = b.getType();

			// Return if the material is water
			if (material == water1 || material == water2)
				return -1;

			// Return the current elevation if a grass or dirt block
			// is found and the block above is non-solid
			boolean solid;
			solid = b.getRelative(BlockFace.UP).getType().isSolid();
			if ((material == dirt || material == grass) && !solid)
				return y;
		}

		// No valid elevation found
		return -1;
	}
}
