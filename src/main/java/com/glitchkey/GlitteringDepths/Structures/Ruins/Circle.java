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
	import org.bukkit.Material;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	//* NOT NEEDED
//* IMPORTS: OTHER
	//* NOT NEEDED

public final class Circle
{
	// Random quartz column selector
	private RandomColumn column;

	// Materials used in this class
	private Material dirt   = Material.DIRT;
	private Material grass  = Material.GRASS;
	private Material water1 = Material.WATER;
	private Material water2 = Material.STATIONARY_WATER;

	/**
	 * Constructor
	 **/
	public Circle(boolean notifyOnBlockChanges)
	{
		// Instanciate the random column selector
		column = new RandomColumn(notifyOnBlockChanges);
	}

	/**
	 * Adds materials to the blacklist (actually a material whitelist)
	 **/
	public Circle addToBlacklist(Material type)
	{
		// Forward the material and return this class for chaining
		column.addToBlacklist(type);
		return this;
	}

	/**
	 * Wrapper for the generate function
	 **/
	public boolean place(World world, Random random, int x, int y, int z)
	{
		// Forward arguments to the generate function
		return generate(world, random, x, y, z);
	}

	/**
	 * Generates a ring of random columns
	 **/
	public boolean generate(World w, Random r, int x, int y, int z)
	{
		// Zero coordinates to the lowest value chunk edge
		x = (x >> 4) << 4;
		z = (z >> 4) << 4;

		// Attempt to place a ring of 16 columns in preset positions
		column.place(w, r, x     , getY(w, x     , z + 6 ), z + 6 );
		column.place(w, r, x     , getY(w, x     , z + 9 ), z + 9 );
		column.place(w, r, x + 1 , getY(w, x + 1 , z + 4 ), z + 4 );
		column.place(w, r, x + 1 , getY(w, x + 1 , z + 11), z + 11);
		column.place(w, r, x + 4 , getY(w, x + 4 , z + 1 ), z + 1 );
		column.place(w, r, x + 4 , getY(w, x + 4 , z + 14), z + 14);
		column.place(w, r, x + 6 , getY(w, x + 6 , z     ), z     );
		column.place(w, r, x + 6 , getY(w, x + 6 , z + 15), z + 15);
		column.place(w, r, x + 9 , getY(w, x + 9 , z     ), z     );
		column.place(w, r, x + 9 , getY(w, x + 9 , z + 15), z + 15);
		column.place(w, r, x + 11, getY(w, x + 11, z + 1 ), z + 1 );
		column.place(w, r, x + 11, getY(w, x + 11, z + 14), z + 14);
		column.place(w, r, x + 14, getY(w, x + 14, z + 4 ), z + 4 );
		column.place(w, r, x + 14, getY(w, x + 14, z + 11), z + 11);
		column.place(w, r, x + 15, getY(w, x + 15, z + 6 ), z + 6 );
		column.place(w, r, x + 15, getY(w, x + 15, z + 9 ), z + 9 );

		// Return requires a boolean
		return true;
	}

	/**
	 * Calculates the Y elevation of the top layer of ground
	 *  --
	 * Returns -1 if placement is invalid
	 **/
	private int getY(World w, int x, int z)
	{
		// Check elevation, starting at height 97 and descending
		for (int y = 97; y >= 0; y--)
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
				return y + 1;
		}

		// No valid elevation found
		return -1;
	}
}
