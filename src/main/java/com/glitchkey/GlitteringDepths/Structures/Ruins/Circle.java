/*
 * Copyright (c) 2012-2018 Sean Porter <glitchkey@gmail.com>
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
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	//* NOT NEEDED
//* IMPORTS: OTHER
	//* NOT NEEDED

public class Circle
{
	Column column;
	public Circle(boolean notifyOnBlockChanges) {
		column = new Column(notifyOnBlockChanges);
	}

	public Circle addToBlacklist(int id) {
		column.addToBlacklist(id);
		return this;
	}

	public boolean place(World world, Random random, int x, int y, int z) {
		return generate(world, random, x, y, z);
	}

	public boolean generate(World w, Random r, int x, int y, int z) {
		column.place(w, r, x, getTopSoilY(w, x, z + 6), z + 6);
		column.place(w, r, x, getTopSoilY(w, x, z + 9), z + 9);
		column.place(w, r, x + 1, getTopSoilY(w, x + 1, z + 4), z + 4);
		column.place(w, r, x + 1, getTopSoilY(w, x + 1, z + 11), z + 11);
		column.place(w, r, x + 4, getTopSoilY(w, x + 4, z + 1), z + 1);
		column.place(w, r, x + 4, getTopSoilY(w, x + 4, z + 14), z + 14);
		column.place(w, r, x + 6, getTopSoilY(w, x + 6, z), z);
		column.place(w, r, x + 6, getTopSoilY(w, x + 6, z + 15), z + 15);
		column.place(w, r, x + 9, getTopSoilY(w, x + 9, z), z);
		column.place(w, r, x + 9, getTopSoilY(w, x + 9, z + 15), z + 15);
		column.place(w, r, x + 11, getTopSoilY(w, x + 11, z + 1), z + 1);
		column.place(w, r, x + 11, getTopSoilY(w, x + 11, z + 14), z + 14);
		column.place(w, r, x + 14, getTopSoilY(w, x + 14, z + 4), z + 4);
		column.place(w, r, x + 14, getTopSoilY(w, x + 14, z + 11), z + 11);
		column.place(w, r, x + 15, getTopSoilY(w, x + 15, z + 6), z + 6);
		column.place(w, r, x + 15, getTopSoilY(w, x + 15, z + 9), z + 9);

		return true;
	}

	public int getTopSoilY(World w, int x, int z)
	{
		int chunkX = x >> 4;
		int chunkZ = z >> 4;

		if (!w.isChunkLoaded(chunkX, chunkZ)) {
			if (!w.loadChunk(chunkX, chunkZ, false)) {
				return -1;
			}
		}

		for (int y = 97; y >= 0; y--) {
			Block b = w.getBlockAt(x, y, z);
			int id = b.getTypeId();

			if (id == 8 || id == 9)
				return -1;

			boolean solid;
			solid = b.getRelative(BlockFace.UP).getType().isSolid();
			if ((id == 2 || id == 3) && !solid)
				return y + 1;
		}

		return -1;
	}
}
