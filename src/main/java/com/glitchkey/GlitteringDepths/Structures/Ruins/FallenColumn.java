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
	import org.bukkit.Location;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.structures.StructureGenerator;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class FallenColumn extends StructureGenerator
{
	public FallenColumn(boolean notifyOnBlockChanges) {
		super(notifyOnBlockChanges, true);

		addToBlacklist(0);

		for (int i = 0; i < 16; i++) {
			addToBlacklist(18, i);
		}
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		Location start = new Location(world, x, y, z);
		addPiece(start, world, x, y, z, 1);
		addToWhitelist(start, world.getBlockAt(x, y, z));

		int dir = random.nextInt(4);

		int xm = 0;
		int zm = 0;
		int data = 0;
		int count = 0;
		int xl = 1;
		int zl = 1;

		if (dir < 2) {
			xm = 1;
			xl = 4;
			data = 3;
		}
		else {
			zm = 1;
			zl = 4;
			data = 4;
		}

		if (dir % 2 == 0) {
			xm *= -1;
			zm *= -1;
		}

		for (int cx = x + (xm * 2); count < xl; cx += xm) {
			int zcount = 0;

			for (int cz = z + (zm * 2); zcount < zl; cz += zm) {
				int id = world.getBlockTypeIdAt(cx, y - 1, cz);

				if (id != 2 && id != 3)
					return placeBlocks(start, true);

				count += 1;
				zcount += 1;

				if (count >= xl && zcount >= zl)
					addPiece(start, world, cx, y, cz, 1);
				else
					addPiece(start, world, cx, y, cz, data);
			}
		}

		return placeBlocks(start, true);
	}

	private void addPiece(Location s, World w, int x, int y, int z, int d) {
		Block block = w.getBlockAt(x, y, z);

		if (!isInBlacklist(block))
			return;

		if (!isChunkValid(w, x, z))
			return;

		addBlock(s, block, 155, d);

		block = w.getBlockAt(x, y + 1, z);

		if (!isInBlacklist(block))
			return;

		if (block.getTypeId() == 79)
			return;

		if (!isInBlockList(s, block))
			addBlock(s, block, 78);
	}

	public boolean isChunkValid(World world, int x, int z) {
		x = x >> 4; // Chunk X
		z = z >> 4; // Chunk Z

		// If the chunk is not loaded, and does not exist
		if (!world.isChunkLoaded(x, z) && !world.loadChunk(x, z, false))
			return false;

		return true;
	}
}
