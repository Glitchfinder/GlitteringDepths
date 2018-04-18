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
	import org.bukkit.Material;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	//* NOT NEEDED
//* IMPORTS: OTHER
	//* NOT NEEDED

public final class RandomColumn
{
	// Column types
	private FallenColumn    typeA; // Fallen, whole
	private FallenColumnB   typeB; // Fallen, broken
	private StandingColumn  typeC; // Standing, whole
	private StandingColumnB typeD; // Standing, broken

	// Materials used in this class
	private Material air     = Material.AIR;
	private Material ice     = Material.ICE;
	private Material leaves1 = Material.LEAVES;
	private Material leaves2 = Material.LEAVES_2;
	private Material snow    = Material.SNOW;

	/**
	 * Constructor
	 **/
	public RandomColumn()
	{
		// Instanciate the four types of column
		typeA = new FallenColumn();
		typeB = new FallenColumnB();
		typeC = new StandingColumn();
		typeD = new StandingColumnB();

		// Add air, ice, and snow to the whitelist
		addToBlacklist(air);
		addToBlacklist(ice);
		addToBlacklist(snow);

		// Add all leaves to the whitelist
		for (int i = 0; i < 16; i++)
		{
			addToBlacklist(leaves1, i);
			addToBlacklist(leaves2, i);
		}
	}

	/**
	 * Adds materials to the blacklist (actually a material whitelist)
	 **/
	public RandomColumn addToBlacklist(Material type)
	{
		// Forward settings through the data-based method
		return addToBlacklist(type, 0);
	}

	/**
	 * Adds materials to the blacklist (actually a material whitelist)
	 **/
	public RandomColumn addToBlacklist(Material type, int data)
	{
		// Forward settings to the column classes
		typeA.addToBlacklist(type, data);
		typeB.addToBlacklist(type, data);
		typeC.addToBlacklist(type, data);
		typeD.addToBlacklist(type, data);

		// Return this class for chaining
		return this;
	}

	/**
	 * Wrapper for the generate function
	 **/
	public boolean place(World world, Random random, int x, int y, int z)
	{
		return generate(world, random, x, y, z);
	}

	/**
	 * Generates a random column
	 **/
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		// Prevent placement on bedrock
		if (y < 2)
			return false;

		// Pick a column type at random
		int type = random.nextInt(4);

		// Generate the selected column type and return the result
		switch (type)
		{
			case  1: return typeA.place(world, random, x, y, z);
			case  2: return typeB.place(world, random, x, y, z);
			case  3: return typeC.place(world, random, x, y, z);
			default: return typeD.place(world, random, x, y, z);
		}
	}
}
