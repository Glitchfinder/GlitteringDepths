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
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	//* NOT NEEDED
//* IMPORTS: OTHER
	//* NOT NEEDED

public class RandomColumn
{
	FallenColumn    typeA;
	FallenColumnB   typeB;
	StandingColumn  typeC;
	StandingColumnB typeD;

	public RandomColumn(boolean notifyOnBlockChanges) {
		typeA = new FallenColumn(notifyOnBlockChanges);
		typeB = new FallenColumnB(notifyOnBlockChanges);
		typeC = new StandingColumn(notifyOnBlockChanges);
		typeD = new StandingColumnB(notifyOnBlockChanges);
	}

	public RandomColumn addToBlacklist(int id) {
		typeA.addToBlacklist(id);
		typeB.addToBlacklist(id);
		typeC.addToBlacklist(id);
		typeD.addToBlacklist(id);

		return this;
	}

	public boolean place(World world, Random random, int x, int y, int z) {
		if (y < 2)
			return false;

		return generate(world, random, x, y, z);
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		int type = random.nextInt(4);

		switch (type) {
			case  1: return typeA.place(world, random, x, y, z);
			case  2: return typeB.place(world, random, x, y, z);
			case  3: return typeC.place(world, random, x, y, z);
			default: return typeD.place(world, random, x, y, z);
		}
	}
}
