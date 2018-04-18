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

public class RandomRuin
{
	// Ruin patterns
	private Ruin1 type1;
	private Ruin2 type2;
	private Ruin3 type3;
	private Ruin4 type4;
	private Ruin5 type5;

	/**
	 * Constructor
	 **/
	public RandomRuin()
	{
		// Instanciate the five types of ruin
		type1 = new Ruin1();
		type2 = new Ruin2();
		type3 = new Ruin3();
		type4 = new Ruin4();
		type5 = new Ruin5();
	}

	/**
	 * Adds materials to the blacklist (actually a material whitelist)
	 **/
	public RandomRuin addToBlacklist(Material type)
	{
		// Forward settings to the ruin classes
		type1.addToBlacklist(type);
		type2.addToBlacklist(type);
		type3.addToBlacklist(type);
		type4.addToBlacklist(type);
		type5.addToBlacklist(type);

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
	 * Generates a random ruin
	 **/
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		// Pick a ruin type at random
		int type = random.nextInt(5);

		// Generate the selected ruin type and return the result
		switch (type)
		{
			case  1: return type1.place(world, random, x, y, z);
			case  2: return type2.place(world, random, x, y, z);
			case  3: return type3.place(world, random, x, y, z);
			case  4: return type4.place(world, random, x, y, z);
			default: return type5.place(world, random, x, y, z);
		}
	}
}
