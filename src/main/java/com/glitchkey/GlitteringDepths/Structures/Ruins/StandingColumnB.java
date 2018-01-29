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
	import org.bukkit.Location;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.structures.StructureGenerator;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class StandingColumnB extends StructureGenerator
{
	public StandingColumnB(boolean notifyOnBlockChanges) {
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

		int height = 5;
		boolean broken = false;

		for (int cy = y + 1; cy < y + height; cy++) {
			boolean last = (cy == y + 4);
			if (broken)
				addChunk(start, world, x, cy, z, random, last);
			else if (random.nextInt(2) == 0) {
				broken = true;
				int type = random.nextInt(3);

				if (type == 0)
					addStair(start, world, x, cy, z, random);
				else if (type == 1)
					addSlab(start, world, y, cy, z);
				else
					addChunk(start, world, x, cy, z, random, last);
			}
			else if (last)
				addPiece(start, world, x, cy, z, 1);
			else
				addPiece(start, world, x, cy, z, 2);
		}

		return placeBlocks(start, true);
	}

	private void addChunk(Location s, World w, int x, int y, int z, Random r, boolean end) {
		int cx = x + r.nextInt(5) - 3;
		int cz = z + r.nextInt(5) - 3;
		int cy = getTopSoilY(w, cx, y, cz) + 1;

		if (cy <= 1)
			return;

		int type = r.nextInt(4);

		if (type == 0)
			addStair(s, w, cx, cy, cz, r);
		else if (type == 1)
			addSlab(s, w, cx, cy, cz);
		else if (type == 2)
			addPiece(s, w, cx, cy, cz, 0);
		else if (type == 3 && end)
			addPiece(s, w, cx, cy, cz, 1);
		else
			addPiece(s, w, cx, cy, cz, r.nextInt(3) + 2);
	}

	private void addStair(Location s, World w, int x, int y, int z, Random r) {
		Block block = w.getBlockAt(x, y, z);

		if (!isInBlacklist(block))
			return;

		if (!isChunkValid(w, x, z))
			return;

		addBlock(s, block, 156, r.nextInt(4));
	}

	private void addSlab(Location s, World w, int x, int y, int z) {
		Block block = w.getBlockAt(x, y, z);

		if (!isInBlacklist(block))
			return;

		if (!isChunkValid(w, x, z))
			return;

		addBlock(s, block, 44, 7);
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

	public int getTopSoilY(World w, int x, int maxY, int z)
	{
		int chunkX = x >> 4;
		int chunkZ = z >> 4;

		if (!w.isChunkLoaded(chunkX, chunkZ)) {
			if (!w.loadChunk(chunkX, chunkZ, false)) {
				return -1;
			}
		}

		for (int y = maxY; y >= 0; y--) {
			Block b = w.getBlockAt(x, y, z);
			int id = b.getTypeId();

			if (id == 8 || id == 9)
				return -1;

			boolean solid;
			solid = b.getRelative(BlockFace.UP).getType().isSolid();
			if ((id == 2 || id == 3) && !solid)
				return y;
		}

		return -1;
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
