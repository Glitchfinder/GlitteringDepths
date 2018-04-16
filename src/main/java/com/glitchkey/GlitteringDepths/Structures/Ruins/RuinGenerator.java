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
	import java.util.ArrayList;
	import java.util.List;
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

public abstract class RuinGenerator extends StructureGenerator
{
	// Floor and wall template information
	protected List<Integer> floorN, floorS, floorE, floorW;
	protected List<Integer> wallN, wallS, wallE, wallW;

	// Materials used in this class
	private Material air           = Material.AIR;
	private Material grass         = Material.GRASS;
	private Material ice           = Material.ICE;
	private Material leaves1       = Material.LEAVES;
	private Material leaves2       = Material.LEAVES_2;
	private Material packed_ice    = Material.PACKED_ICE;
	private Material purpur        = Material.PURPUR_BLOCK;
	private Material purpur_pillar = Material.PURPUR_PILLAR;
	private Material purpur_slab   = Material.PURPUR_SLAB;
	private Material purpur_stairs = Material.PURPUR_STAIRS;
	private Material quartz        = Material.QUARTZ_BLOCK;
	private Material quartz_stairs = Material.QUARTZ_STAIRS;
	private Material slab          = Material.STEP;
	private Material snow          = Material.SNOW;

	/**
	 * Constructor
	 **/
	public RuinGenerator(boolean notifyOnBlockChanges)
	{
		// Forward settings to the base class
		super(notifyOnBlockChanges);

		// Add air, ice, packed ice, and snow to the whitelist
		addToBlacklist(air);
		addToBlacklist(ice);
		addToBlacklist(packed_ice);
		addToBlacklist(snow);

		// Add all leaves to the whitelist
		for (int i = 0; i < 16; i++)
		{
			addToBlacklist(leaves1, i);
			addToBlacklist(leaves2, i);
		}

		// Fill in the template information
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

	/**
	 * Converts an index to a material
	 **/
	protected Material getType(int index)
	{
		switch (index)
		{
			case  1:
			case  2:
			case  9: return purpur_pillar;
			case  3:
			case  4:
			case  7:
			case 10:
			case 11: return quartz;
			case  5:
			case  6:
			case 12: 
			case 13: return quartz_stairs;
			case  8: return purpur;
			case 14: return slab;
			default: return air;
		}
	}

	/**
	 * Converts an index to a data value
	 **/
	protected int getData(int index)
	{
		switch (index)
		{
			case  3:
			case  6: return 1;
			case 12:
			case  7: return 2;
			case  5:
			case 10: return 3;
			case  1:
			case 11: return 4;
			case 14: return 7;
			case  2: return 8;
			default: return 0;
		}
	}

	/**
	 * Generates a templated, dynamically-damaged ruin
	 **/
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		// Zero coordinates to the lowest value chunk edge
		x = (x >> 4) << 4;
		z = (z >> 4) << 4;

		// Get the base location of the ruin for tracking
		Location start = new Location(world, x, y, z);

		// Preset height check
		int base  = 255;
		int max   = 0;
		// Set a minimum count for use in floor damage
		int count = 5;
		// Define the current template
		List<Integer> floor, wall;

		// Select a template direction at random
		switch (random.nextInt(4))
		{
			case 0:  floor = floorN; wall = wallN; break;
			case 1:  floor = floorS; wall = wallS; break;
			case 2:  floor = floorE; wall = wallE; break;
			default: floor = floorW; wall = wallW; break;
		}

		// Iterate through the chunk along the x axis
		for (int cx = 0; cx < 16; cx++)
		{
			// Iterate through the chunk along the z axis
			for (int cz = 0; cz < 16; cz++)
			{
				// Get the current floor block
				int i = floor.get((cz * 16) + cx);

				// Skip if the floor is unspecified
				if (i == 0)
					continue;

				// Increase the floor damage buffer
				count += 1;

				// Find the current block's base height
				int temp = getBaseY(world, cx + x, cz + z);

				// Calculate the current height difference
				base = Math.min(base, temp);
				max  = Math.max(max,  temp);

				// Fail if the ground is too steep
				if (max - base > 4)
					return fail(start);
				// Fail if invalid terrain is found
				if (base < 0)
					return fail(start);
			}
		}

		// Determine the clear height for the ruin
		max = Math.max(max, base + 3);

		// Set iteration bounds for the ruin
		int xm = 1;
		int zm = 0;
		int xmin = 0;
		int zmin = 0;
		int xmax = 15;
		int zmax = 15;
		int cx = 0;
		int cz = 0;

		// Preset support information for the walls
		List<Boolean> below = new ArrayList<Boolean>(256);
		List<Boolean> current = new ArrayList<Boolean>(256);

		// Fill in the support information arrays
		for (int n = 0; n < 256; n++)
		{
			below.add(false);
			current.add(false);
		}

		// Iterate through the floor array
		// (Iteration is performed as a spiral from edge to center)
		for (int n = 0; n < 256; n++)
		{
			// Get the current floor block
			int i = floor.get((cz << 4) + cx);

			// If the block is set
			if (i != 0)
			{
				// Predefine the block variable
				Block b;
				// Get the current coordinates
				int bx = cx + x;
				int bz = cz + z;

				// Iterate through the clear height
				for (int cy = base; cy <= max; cy++)
				{
					// Get the current block
					b = world.getBlockAt(bx, cy + 1, bz);
					// Clear and prefill the column
					prefill(start, b, cy == base);
				}

				// If the floor is not 'damaged'
				if (random.nextInt(count / 3) != 0)
				{
					// Get the current block
					b = world.getBlockAt(bx, base, bz);
					// Grab the template information
					Material m = getType(i);
					int data = getData(i);
					// Set the floor block
					add(start, b, m, data, true, true);
					// Set potential walls as supported
					below.set((cz << 4) + cx, true);
				}
				// If the floor is 'damaged'
				else
				{
					// Set the current block to grass
					b = world.getBlockAt(bx, base, bz);
					add(start, b, grass, 0, true, true);
				}

				// Decrement the floor's damage buffer
				count -= 1;
			}

			// Adjust the curent coordinates
			cx += xm;
			cz += zm;

			// If at the southeast corner
			if (cx == xmax && cz == zmax && zm == 1)
			{
				// Change the direction to west
				xm = -1;
				zm = 0;
				// Move the east edge in one block
				xmax -= 1;
			}
			// If at the southwest corner
			else if (cx == xmin && cz == zmax && xm == -1)
			{
				// Change the direction to north
				xm = 0;
				zm = -1;
				// Move the south edge in one block
				zmax -= 1;
			}
			// If at the northwest corner
			else if (cx == xmin && cz == zmin && zm == -1)
			{
				// Change the direction to east
				xm = 1;
				zm = 0;
				// Move the west edge in one block
				xmin += 1;
			}
			// If at the northeast corner
			else if (cx == xmax && cz <= zmin && xm == 1)
			{
				// Change the direction to south
				xm = 0;
				zm = 1;
				// Move the north edge in one block
				zmin += 1;
			}
		}

		// Place the floor blocks to prevent wall undercutting
		placeBlocks(start, true);

		// Iterate through the full height of the wall
		for (int cy = 0; cy < 4; cy++)
		{
			// Preset repeated math
			int iy = cy << 8;
			int my = base + 1 + cy;

			// Iterate through the chunk along the x axis
			for (cx = 0; cx < 16; cx++)
			{
				// Preset repeated math
				int mx = cx + x;

				// Iterate through the chunk along the z axis
				for (cz = 0; cz < 16; cz++)
				{
					// Preset repeated math
					int iz = (cz << 4) + cx;
					int mz = cz + z;

					// Get the current block
					int i = wall.get(iy + iz);

					// Skip if there is no block to place
					if (i == 0)
						continue;

					// If the block is unsupported
					if (!below.get(iz))
					{
						// Have the block fall
						fall(world, start, random, mx,
							my, mz, i, false);
						// Move to the next block
						continue;
					}

					// Preset loop variables
					int chance = 1;
					int index;

					// Iterate through potential supports
					for (int tx = -1; tx < 2; tx++)
					{
						for (int tz = -1; tz < 2; tz++)
						{
							// Calculate the index
							index = (cz + tz) << 4;
							index += cx + tx;

							// If a support is found
							if (below.get(index))
								// Add support
								chance += 1;
						}
					}

					// If the support is insufficient
					if (random.nextInt(chance) == 0)
					{
						// Have the block fall
						fall(world, start, random, mx,
							my, mz, i, true);
						// Move to the next block
						continue;
					}

					// Get the current block information
					Block b = world.getBlockAt(mx, my, mz);
					Material t = getType(i);
					int d = getData(i);

					// Check the block type
					switch (i) {
						// If the block is non-cubic
						case 5:
						case 6:
						case 12:
						case 13:
							// Add without snow
							add(start, b, t, d,
								false, true);
							break;
						// If the block is cubic
						default:
							// Add with snow
							add(start, b, t, d,
								true, true);
					}

					// Set the block as supporting
					current.set(iz, true);
				}
			}

			// Flip and reset the support arrays
			below = current;
			current = new ArrayList<Boolean>(256);

			// Prefill the new support array
			for (int n = 0; n < 256; n++)
			{
				current.add(false);
			}
		}

		// Place the walls
		return placeBlocks(start, true);
	}

	/**
	 * Clears space for a ruin
	 **/
	private void prefill(Location start, Block b, boolean base)
	{
		// Check the current block type
		switch (b.getType())
		{
			// If the block is replaceable
			case COAL_ORE:
			case DIAMOND_ORE:
			case DIRT:
			case EMERALD_ORE:
			case GLOWING_REDSTONE_ORE:
			case GOLD_ORE:
			case GRASS:
			case ICE:
			case IRON_ORE:
			case LAPIS_ORE:
			case PACKED_ICE:
			case REDSTONE_ORE:
			case SNOW:
			case STONE:
				// If one block above the ground
				if (base)
					// Replace with snow
					addBlock(start, b, snow);
				// If more than one block above ground
				else
					// Replace with air
					addBlock(start, b, air);
			// Skip if the block is not considered replaceable
			default:
				return;
		}
	}

	/**
	 * Drops a wall piece to the ground, potentially 'breaking' it
	 **/
	private void fall(World w, Location s, Random r, int x, int y, int z,
		int i, boolean support)
	{
		// Preset loop variables
		int data = 0;
		int dice = 0;

		// On a 1/3 chance if the block is supported, leave part behind
		if (r.nextInt(3) == 0 && support)
		{
			// Get the original block location
			Block b = w.getBlockAt(x, y, z);

			// Check the block type
			switch(i)
			{
				// If the block is a full quartz block
				case 3:
				case 4:
				case 7:
				case 10:
				case 11:
					// Get a random
					dice = r.nextInt(3);
					// On a 1/3 chance, leave a quartz slab
					if (dice == 0)
						add(s, b, slab, 7, false);
					// On a 1/3 chance, leave quartz stairs
					else if (dice == 1)
						add(s, b, quartz_stairs,
							r.nextInt(4), false);

					// Don't place a block below
					return;
				// If the block is quartz stairs
				case 5:
				case 6:
				case 12:
				case 13:
					// On a 1/2 chance, leave a quartz slab
					if (r.nextBoolean())
						add(s, b, slab, 7, false);

					// Don't place a block below
					return;
				// If the block is a purpur block
				case 8:
					// Get a random
					dice = r.nextInt(3);
					// On a 1/3 chance, leave a purpur slab
					if (dice == 0)
						add(s, b, purpur_slab, 0,
							false);
					// On a 1/3 chance, leave purpur stairs
					else if (dice == 1)
						add(s, b, purpur_stairs,
							r.nextInt(4), false);

					// Don't place a block below
					return;
				// If the block is non-applicable, leave nothing
				default: return;
			}
		}

		// Determine a random offset for the 'fallen' block
		//
		// This cannot spill over into a new chunk due to the centering
		// above, in combination with the templates having two blocks
		// of space along their edges. If either of those changes, it
		// needs to have a check added to prevent generating new chunks.
		int cx = (x - 2) + r.nextInt(5);
		int cz = (z - 2) + r.nextInt(5);
		// Get the height of the ground at the specified coordinates
		int cy = getBaseY(w, cx, cz);

		// Cancel if the location is invalid
		if (cy <= 0 || cy >= y)
			return;

		// Get the current block
		Block b = w.getBlockAt(cx, cy + 1, cz);

		// Check the type of the 'fallen' block
		switch(i)
		{
			// If the block is a full quartz block
			case 3:
			case 4:
			case 7:
			case 10:
			case 11:
				// Get a random
				dice = r.nextInt(5);
				// On a 1/5 chance, place a quartz slab
				if (dice == 0)
					add(s, b, slab, 7, false);
				// On a 1/5 chance, place quartz stairs
				else if (dice == 1)
					add(s, b, quartz_stairs, r.nextInt(4),
						false);
				// On a 1/5 chance, place a smooth quartz block
				else if (dice == 2)
					add(s, b, quartz, 0);

				// On a 2/5 chance, place nothing
				return;
			// If the block is quartz stairs
			case 5:
			case 6:
			case 12:
			case 13:
				// On a 1/2 chance, place a quartz slab
				if (r.nextBoolean())
					add(s, b, slab, 7, false);

				// On a 1/2 chance, place nothing
				return;
			// If the block is a purpur block
			case 8:
				// Get a random
				dice = r.nextInt(5);
				// On a 1/5 chance, place a purpur slab
				if (dice == 0)
					add(s, b, purpur_slab, 0, false);
				// On a 1/5 chance, place purpur stairs
				else if (dice == 1)
					add(s, b, purpur_stairs, r.nextInt(4),
						false);
				// On a 1/5 chance, place a purpur block
				else if (dice == 2)
					add(s, b, purpur, 0);

				// On a 2/5 chance, place nothing
				return;
			// If the block is non-applicable, leave nothing
			default: return;
		}
	}

	/**
	 * Add a block to be placed
	 **/
	private void add(Location s, Block b, Material t, int d, boolean sn,
		boolean force)
	{
		// Prevent placing multiple blocks in the same place
		if (isInBlockList(s, b) && !force)
			return;

		// Add the block to the ruin
		addBlock(s, b, t, d);

		// If no snow should be placed
		if (!sn)
		{
			// Ensure the block above is clear
			addBlock(s, b.getRelative(BlockFace.UP), air);
			return;
		}

		// Get the block above the current block
		b = b.getRelative(BlockFace.UP);

		// Skip if the block isn't whitelisted
		if (!isInBlacklist(b))
			return;

		// Get the block's material
		Material type = b.getType();

		// Skip if the block is ice or leaves
		if (type == ice || type == leaves1 || type == leaves2)
			return;

		// Place snow if the block above will be empty otherwise
		if (!isInBlockList(s, b) || force)
			addBlock(s, b, snow);
	}

	/**
	 * A wrapper for the add method above
	 **/
	private void add(Location s, Block b, Material t, int d, boolean snow)
	{
		add(s, b, t, d, snow, false);
	}

	/**
	 * A wrapper for the add method above
	 **/
	private void add(Location s, Block b, Material t, int d)
	{
		add(s, b, t, d, true, false);
	}

	/**
	 * Calculates the Y elevation of the top layer of ground
	 *  --
	 * Returns -1 if placement is invalid
	 **/
	private int getBaseY(World world, int x, int z)
	{
		// Check elevation, starting at height 97 and descending
		for (int y = 97; y > 0; y--)
		{
			// Get the material at the current coordinates
			Material type = world.getBlockAt(x, y, z).getType();

			// Check the material type
			switch (type)
			{
				case COAL_ORE:
				case DIAMOND_ORE:
				case DIRT:
				case EMERALD_ORE:
				case GLOWING_REDSTONE_ORE:
				case GOLD_ORE:
				case GRASS:
				case ICE:
				case IRON_ORE:
				case LAPIS_ORE:
				case PACKED_ICE:
				case REDSTONE_ORE:
				case SNOW:
				case STONE:
					// Return the current elevation
					return y;
				case STATIONARY_WATER:
				case WATER:
					// No valid elevation found
					return -1;
				default:
					// Descend another block
					continue;
			}
		}

		// No valid elevation found
		return -1;
	}
}
