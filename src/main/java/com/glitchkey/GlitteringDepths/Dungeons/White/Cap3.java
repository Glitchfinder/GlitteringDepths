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

package com.glitchkey.glitteringdepths.dungeons.white;

//* IMPORTS: JDK/JRE
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.List;
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.block.BlockFace;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.dungeons.DungeonPart;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class Cap3 extends DungeonPart
{
	// Materials used in this class
	private Material air        = Material.AIR;
	private Material brick      = Material.SMOOTH_BRICK;
	private Material chest      = Material.CHEST;
	private Material prismarine = Material.PRISMARINE;
	private Material quartz     = Material.QUARTZ_BLOCK;
	private Material spawner    = Material.MOB_SPAWNER;
	private Material slab       = Material.STEP;
	private Material stone      = Material.STONE;
	private Material stairs     = Material.QUARTZ_STAIRS;

	/**
	 * Constructor
	 **/
	public Cap3()
	{
		// Set the height of this dungeon piece
		height = 8;
	}

	/**
	 * Fills in the template data for a north-facing ruin
	 **/
	protected void fillNorth()
	{
		// Create an array for the tiles
		Integer[] temp =
		{
			// Base level
			0, 0, 0,  0,  0,  0,  1,  2,  2,  1,  0,  0,  0,  0, 0, 0,
			0, 0, 0,  0,  1,  2,  4,  4,  4,  4,  2,  1,  0,  0, 0, 0,
			0, 0, 0,  2,  4,  4,  4,  21, 3,  4,  4,  4,  2,  0, 0, 0,
			0, 0, 2,  4,  5,  5,  4,  3,  21, 4,  5,  5,  4,  2, 0, 0,
			0, 1, 4,  5,  4,  5,  4,  21, 3,  4,  5,  4,  5,  4, 1, 0,
			0, 2, 4,  5,  5,  5,  4,  3,  21, 4,  5,  5,  5,  4, 2, 0,
			1, 4, 4,  4,  4,  4,  4,  21, 3,  4,  4,  4,  4,  4, 4, 1,
			2, 4, 21, 3,  21, 3,  21, 3,  21, 3,  21, 3,  21, 3, 4, 2,
			2, 4, 3,  21, 3,  21, 3,  21, 3,  21, 3,  21, 3, 21, 4, 2,
			1, 4, 4,  4,  4,  4,  4,  3,  21, 4,  4,  4,  4,  4, 4, 1,
			0, 2, 4,  5,  5,  5,  4,  21, 3,  4,  5,  5,  5,  4, 2, 0,
			0, 1, 4,  5,  4,  5,  4,  3,  21, 4,  5,  4,  5,  4, 1, 0,
			0, 0, 2,  4,  5,  5,  4,  21, 3,  4,  5,  5,  4,  2, 0, 0,
			0, 0, 0,  2,  4,  4,  4,  3,  21, 4,  4,  4,  2,  0, 0, 0,
			0, 0, 0,  0,  1,  2,  4,  4,  4,  4,  2,  1,  0,  0, 0, 0,
			0, 0, 0,  0,  0,  0,  1,  2,  2,  1,  0,  0,  0,  0, 0, 0,

			//
			0, 0, 0, 0, 0, 0,  6,  0, 0, 6,  0,  0, 0, 0, 0, 0,
			0, 0, 0, 0, 6, 0,  0,  0, 0, 0,  0,  6, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0,  0,  0, 0, 0,  0,  0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0,  0,  0, 0, 0,  0,  0, 0, 0, 0, 0,
			0, 6, 0, 0, 0, 0,  0,  8, 8, 0,  0,  0, 0, 0, 6, 0,
			0, 0, 0, 0, 0, 0,  11, 7, 7, 11, 0,  0, 0, 0, 0, 0,
			6, 0, 0, 0, 0, 13, 4,  9, 9, 4,  14, 0, 0, 0, 0, 6,
			0, 0, 0, 0, 8, 7,  9,  1, 1, 9,  7,  8, 0, 0, 0, 0,
			0, 0, 0, 0, 8, 7,  9,  1, 1, 9,  7,  8, 0, 0, 0, 0,
			6, 0, 0, 0, 0, 13, 4,  9, 9, 4,  14, 0, 0, 0, 0, 6,
			0, 0, 0, 0, 0, 0,  12, 7, 7, 12, 0,  0, 0, 0, 0, 0,
			0, 6, 0, 0, 0, 0,  0,  8, 8, 0,  0,  0, 0, 0, 6, 0,
			0, 0, 0, 0, 0, 0,  0,  0, 0, 0,  0,  0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0,  0,  0, 0, 0,  0,  0, 0, 0, 0, 0,
			0, 0, 0, 0, 6, 0,  0,  0, 0, 0,  0,  6, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0,  6,  0, 0, 6,  0,  0, 0, 0, 0, 0,

			0, 0, 0, 0, 0, 0, 6, 0,  0,  6, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 6, 0, 0, 0,  0,  0, 0, 6, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 6, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 6, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			6, 0, 0, 0, 0, 0, 6, 8,  8,  6, 0, 0, 0, 0, 0, 6,
			0, 0, 0, 0, 0, 0, 8, 10, 10, 8, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 8, 10, 10, 8, 0, 0, 0, 0, 0, 0,
			6, 0, 0, 0, 0, 0, 6, 8,  8,  6, 0, 0, 0, 0, 0, 6,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 6, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 6, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 6, 0, 0, 0,  0,  0, 0, 6, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 6, 0,  0,  6, 0, 0, 0, 0, 0, 0,

			0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1, 0, 0, 0, 0, 0, 6, 0, 0, 6, 0, 0, 0, 0, 0, 1,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1, 0, 0, 0, 0, 0, 6, 0, 0, 6, 0, 0, 0, 0, 0, 1,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,

			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 6, 0, 0, 6, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 6, 0, 0, 6, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 6,  16, 16, 6,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 18, 0,  0,  17, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 18, 0,  0,  17, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 6,  15, 15, 6,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,

			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 19, 11, 11, 19, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 13, 18, 16, 14, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 13, 15, 17, 14, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 19, 12, 12, 19, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0, 0, 0, 0, 0, 0,

			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 19, 19, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 19, 19, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0,  0,  0, 0, 0, 0, 0, 0, 0
		};

		// Convert the arrays to lists and add them to the template data
		blocksN = Arrays.asList(temp);
	}

	/**
	 * Fills in the template data for a south-facing ruin
	 **/
	protected void fillSouth()
	{
		// This dungeon has no rotations, so copy the North array
		blocksS = new ArrayList(blocksN);
	}

	/**
	 * Fills in the template data for an east-facing ruin
	 **/
	protected void fillEast()
	{
		// This dungeon has no rotations, so copy the North array
		blocksE = new ArrayList(blocksN);
	}

	/**
	 * Fills in the template data for a west-facing ruin
	 **/
	protected void fillWest()
	{
		// This dungeon has no rotations, so copy the North array
		blocksW = new ArrayList(blocksN);
	}

	/**
	 * Converts an index to a material
	 **/
	protected Material getType(int index)
	{
		switch (index)
		{
			case  1:
			case  2:
			case  6: return quartz;
			case  3:
			case  4:
			case  7: return brick;
			case  5: return prismarine;
			case  8:
			case 19: 
			case 20: return slab;
			case  9: return chest;
			case 10: return spawner;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18: return stairs;
			case 21: return stone;
			default: return air;
		}
	}

	/**
	 * Converts an index to a data value
	 **/
	protected int getData(int index, Random random)
	{
		switch (index)
		{
			case  1:
			case 14: return 1;
			case  5:
			case  6:
			case 11: return 2;
			case  4:
			case 12: return 3;
			case 17: return 4;
			case  8:
			case 18: return 5;
			case 15:
			case 21: return 6;
			case 19:
			case 16: return 7;
			case 20: return 15;
			default: return 0;
		}
	}
}
