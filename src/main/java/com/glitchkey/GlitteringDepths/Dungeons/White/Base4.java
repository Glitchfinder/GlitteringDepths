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

public class Base4 extends DungeonPart
{
	// Materials used in this class
	private Material air        = Material.AIR;
	private Material brick      = Material.SMOOTH_BRICK;
	private Material prismarine = Material.PRISMARINE;
	private Material quartz     = Material.QUARTZ_BLOCK;
	private Material stairs     = Material.QUARTZ_STAIRS;

	/**
	 * Constructor
	 **/
	public Base4()
	{
		// Set the height of this dungeon piece
		height = 3;
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
			0, 0, 0, 0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 1, 2, 5, 5, 5, 5, 2, 1, 0, 0, 0, 0,
			0, 0, 0, 2, 5, 5, 3, 3, 3, 3, 5, 5, 2, 0, 0, 0,
			0, 0, 2, 5, 3, 3, 3, 3, 3, 3, 3, 3, 5, 2, 0, 0,
			0, 1, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 1, 0,
			0, 2, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 2, 0,
			1, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 1,
			2, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 2,
			2, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 2,
			1, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 1,
			0, 2, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 2, 0,
			0, 1, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 1, 0,
			0, 0, 2, 5, 3, 3, 3, 3, 3, 3, 3, 3, 5, 2, 0, 0,
			0, 0, 0, 2, 5, 5, 3, 3, 3, 3, 5, 5, 2, 0, 0, 0,
			0, 0, 0, 0, 1, 2, 5, 5, 5, 5, 2, 1, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 1, 2, 2, 1, 0, 0, 0, 0, 0, 0,

			// One block above the base
			0, 0, 0, 0, 0, 0, 4, 9, 8, 4, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 4, 8, 5, 5, 5, 5, 9, 4, 0, 0, 0, 0,
			0, 0, 0, 8, 5, 5, 3, 3, 3, 3, 5, 5, 9, 0, 0, 0,
			0, 0, 6, 5, 3, 3, 3, 3, 3, 3, 3, 3, 5, 6, 0, 0,
			0, 4, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 4, 0,
			0, 6, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 6, 0,
			4, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 4,
			7, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 7,
			6, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 6,
			4, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 4,
			0, 7, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 7, 0,
			0, 4, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 4, 0,
			0, 0, 7, 5, 3, 3, 3, 3, 3, 3, 3, 3, 5, 7, 0, 0,
			0, 0, 0, 8, 5, 5, 3, 3, 3, 3, 5, 5, 9, 0, 0, 0,
			0, 0, 0, 0, 4, 8, 5, 5, 5, 5, 9, 4, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 4, 9, 8, 4, 0, 0, 0, 0, 0, 0,

			// Two blocks above the base
			0,  0,  0,  0,  0, 0,  4, 13, 12, 4, 0,  0, 0,  0,  0,  0,
			0,  0,  0,  0,  4, 12, 5, 5,  5,  5, 13, 4, 0,  0,  0,  0,
			0,  0,  0,  12, 5, 5,  3, 3,  3,  3, 5,  5, 13, 0,  0,  0,
			0,  0,  10, 5,  3, 3,  3, 3,  3,  3, 3,  3, 5,  10, 0,  0,
			0,  4,  5,  3,  3, 3,  3, 3,  3,  3, 3,  3, 3,  5,  4,  0,
			0,  10, 5,  3,  3, 3,  3, 3,  3,  3, 3,  3, 3,  5,  10, 0,
			4,  5,  3,  3,  3, 3,  3, 3,  3,  3, 3,  3, 3,  3,  5,  4,
			11, 5,  3,  3,  3, 3,  3, 3,  3,  3, 3,  3, 3,  3,  5,  11,
			10, 5,  3,  3,  3, 3,  3, 3,  3,  3, 3,  3, 3,  3,  5,  10,
			4,  5,  3,  3,  3, 3,  3, 3,  3,  3, 3,  3, 3,  3,  5,  4,
			0,  11, 5,  3,  3, 3,  3, 3,  3,  3, 3,  3, 3,  5,  11, 0,
			0,  4,  5,  3,  3, 3,  3, 3,  3,  3, 3,  3, 3,  5,  4,  0,
			0,  0,  11, 5,  3, 3,  3, 3,  3,  3, 3,  3, 5,  11, 0,  0,
			0,  0,  0,  12, 5, 5,  3, 3,  3,  3, 5,  5, 13, 0,  0,  0,
			0,  0,  0,  0,  4, 12, 5, 5,  5,  5, 13, 4, 0,  0,  0,  0,
			0,  0,  0,  0,  0, 0,  4, 13, 12, 4, 0,  0, 0,  0,  0,  0
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
			case  4: return quartz;
			case  3: return brick;
			case  5: return prismarine;
			case  6:
			case  7:
			case  8:
			case  9:
			case 10:
			case 11:
			case 12:
			case 13: return stairs;
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
			case  3: return random.nextInt(3);
			case  1:
			case  9: return 1;
			case  4:
			case  5:
			case  6: return 2;
			case  7: return 3;
			case 12: return 4;
			case 13: return 5;
			case 10: return 6;
			case 11: return 7;
			default: return 0;
		}
	}
}
