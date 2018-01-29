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

package com.glitchkey.glitteringdepths.structures.trees;

//* IMPORTS: JDK/JRE
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.Location;
	import org.bukkit.util.Vector;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.structures.StructureGenerator;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class MegaRedwood extends StructureGenerator
{
	public MegaRedwood(boolean notifyOnBlockChanges) {
		super(notifyOnBlockChanges, true);

		addToBlacklist(0);

		for (int i = 0; i < 16; i++) {
			addToBlacklist(18, i);
		}
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		int maxHeight = random.nextInt(25) + 25;
		Location start = new Location(world, x, y, z);

		if (y < 1 || (y + maxHeight + 1) > 128)
			return fail(start);

		int baseId = world.getBlockTypeIdAt(x, y - 1, z);

		if ((baseId != 2 && baseId != 3) || y >= (128 - maxHeight - 1))
			return fail(start);

		addToWhitelist(start, world.getBlockAt(x, y - 1, z));
		addBlock(start, world.getBlockAt(x, y - 1, z), 3, 0);

		int branches = (int) (((float) maxHeight) / 1.5);
		int dir = random.nextInt(360);
		double ang = 0D;
		double minL = random.nextInt(3) + 1;
		double maxL = random.nextInt(3) + 4;
		double minA = random.nextDouble() + 2D;
		double maxA = random.nextDouble() + 1D;

		for (int i = 0; i < branches; i++) {
			dir += random.nextInt(90) + 45;
			dir %= 360;

			while (dir % 90 < 20 || dir % 90 > 70) {
				dir += random.nextInt(20);
			}

			ang = Math.toRadians(dir);
			double perc = (((double) i) / ((double) branches));
			double xm = Math.cos(ang);
			double zm = Math.sin(ang);
			double ym = lerp(-minA, -maxA, perc);
			double cx = x;
			double cz = z;
			double cy = y + maxHeight - i;
			Vector v = new Vector(xm, ym, zm);
			Vector mod = new Vector(1D, lerp(0.9D, 0.8D, perc), 1D);
			v.normalize();

			int length = random.nextInt((int) lerp(minL, maxL, perc)) + (int) lerp(3D, 7D, perc);
			length = Math.min(7, length);

			for (int l = 0; l < length; l++) {
				if (i < 4)
					addLeaf(start, world, (int) cx, (int) cy, (int) cz);
				else {
					addLeaf(start, world, (int) Math.floor(cx), (int) cy, (int) Math.floor(cz));
					addLeaf(start, world, (int) Math.floor(cx), (int) cy, (int) Math.ceil(cz));
					addLeaf(start, world, (int) Math.ceil(cx), (int) cy, (int) Math.floor(cz));
					addLeaf(start, world, (int) Math.ceil(cx), (int) cy, (int) Math.ceil(cz));
				}

				cx += v.getX();
				cy += v.getY();
				cz += v.getZ();

				v.multiply(mod);
				v.normalize();
			}
		}

		for (int cy = 0; cy < maxHeight - 1; ++cy)
		{
			addTrunk(start, world, x, y + cy, z);
		}

		return placeBlocks(start, true);
	}

	private void addLeaf(Location s, World w, int x, int y, int z) {
		Block block = w.getBlockAt(x, y, z);

		if (!isInBlacklist(block))
			return;

		if (!isChunkValid(w, x, z))
			return;

		addBlock(s, block, 18, 1);

		block = w.getBlockAt(x, y + 1, z);

		if (!isInBlacklist(block))
			return;

		if (block.getTypeId() == 79)
			return;

		if (!isInBlockList(s, block))
			addBlock(s, block, 78);
	}

	private void addTrunk(Location s, World w, int x, int y, int z) {
		Block block = w.getBlockAt(x, y, z);

		if (!isInBlacklist(block))
			return;

		if (!isChunkValid(w, x, z))
			return;

		addBlock(s, block, 17, 1);
	}

	public boolean isChunkValid(World world, int x, int z) {
		x = x >> 4; // Chunk X
		z = z >> 4; // Chunk Z

		// If the chunk is not loaded, and does not exist
		if (!world.isChunkLoaded(x, z) && !world.loadChunk(x, z, false))
			return false;

		return true;
	}

	double lerp(double start, double end, double percent) {
		return (start + (percent * (end - start)));
	}
}
