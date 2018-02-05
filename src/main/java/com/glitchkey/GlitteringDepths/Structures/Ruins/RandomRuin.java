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

public class RandomRuin
{
	Ruin1 type1;
	Ruin2 type2;
	Ruin3 type3;
	Ruin4 type4;
	Ruin5 type5;

	public RandomRuin(boolean notifyOnBlockChanges) {
		type1 = new Ruin1(notifyOnBlockChanges);
		type2 = new Ruin2(notifyOnBlockChanges);
		type3 = new Ruin3(notifyOnBlockChanges);
		type4 = new Ruin4(notifyOnBlockChanges);
		type5 = new Ruin5(notifyOnBlockChanges);
	}

	public RandomRuin addToBlacklist(int id) {
		type1.addToBlacklist(id);
		type2.addToBlacklist(id);
		type3.addToBlacklist(id);
		type4.addToBlacklist(id);
		type5.addToBlacklist(id);

		return this;
	}

	public boolean place(World world, Random random, int x, int y, int z) {
		return generate(world, random, x, y, z);
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		int type = random.nextInt(5);

		switch (type) {
			case  1: return type1.place(world, random, x, y, z);
			case  2: return type2.place(world, random, x, y, z);
			case  3: return type3.place(world, random, x, y, z);
			case  4: return type4.place(world, random, x, y, z);
			default: return type5.place(world, random, x, y, z);
		}
	}
}
