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
	import org.bukkit.Material;
	import org.bukkit.util.Vector;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.structures.StructureGenerator;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class MegaRedwood extends StructureGenerator
{
	Material air     = Material.AIR;
	Material dirt    = Material.DIRT;
	Material grass   = Material.GRASS;
	Material ice     = Material.ICE;
	Material leaves1 = Material.LEAVES;
	Material leaves2 = Material.LEAVES_2;
	Material log     = Material.LOG;
	Material snow    = Material.SNOW;

	public MegaRedwood(boolean notifyOnBlockChanges)
	{
		super(notifyOnBlockChanges);

		addToBlacklist(air);
		addToBlacklist(ice);
		addToBlacklist(snow);

		for (int i = 0; i < 16; i++)
		{
			addToBlacklist(leaves1, i);
			addToBlacklist(leaves2, i);
		}
	}

	public boolean generate(World world, Random random, int x, int y, int z)
	{
		int maxHeight = random.nextInt(25) + 25;
		Location start = new Location(world, x, y, z);

		if (!isChunkValid(world, x, z))
			return fail(start);

		if (y < 1 || (y + maxHeight + 1) > 128)
			return fail(start);

		Material type = world.getBlockAt(x, y - 1, z).getType();

		if ((type != grass && type != dirt) || y >= (255 - maxHeight))
			return fail(start);

		addBlock(start, world.getBlockAt(x, y - 1, z), dirt, 0);

		int branches = (int) (((float) maxHeight) / 1.5);
		int direction = random.nextInt(360);
		double angle = 0D;
		double minL = random.nextInt(3) + 1;
		double maxL = random.nextInt(3) + 4;
		double minA = random.nextDouble() + 2D;
		double maxA = random.nextDouble() + 1D;

		for (int i = 0; i < branches; i++)
		{
			direction += random.nextInt(90) + 45;
			direction %= 360;

			while (direction % 90 < 20 || direction % 90 > 70) {
				direction += random.nextInt(20);
			}

			angle = Math.toRadians(direction);
			double perc = (((double) i) / ((double) branches));
			double xm = Math.cos(angle);
			double zm = Math.sin(angle);
			double ym = lerp(-minA, -maxA, perc);
			double cx = x;
			double cz = z;
			double cy = y + maxHeight - i;
			Vector v = new Vector(xm, ym, zm);
			Vector mod = new Vector(1D, lerp(0.9D, 0.8D, perc), 1D);
			v.normalize();
			int limit = (int) lerp(minL, maxL, perc);

			int length = random.nextInt(limit);
			length += (int) lerp(3D, 7D, perc);
			length = Math.min(7, length);

			for (int l = 0; l < length; l++)
			{
				if (i < 4)
					addLeaf(start, world, cx, cy, cz);
				else
					addLeaves(start, world, cx, cy, cz);

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

	private void addLeaves(Location s, World w, double x, double y,
		double z)
	{
		addLeaf(s, w, Math.floor(x), y, Math.floor(z));
		addLeaf(s, w, Math.floor(x), y, Math.ceil(z));
		addLeaf(s, w, Math.ceil(x),  y, Math.floor(z));
		addLeaf(s, w, Math.ceil(x),  y, Math.ceil(z));
	}

	private void addLeaf(Location s, World w, double x, double y, double z)
	{
		addLeaf(s, w, (int) x, (int) y, (int) z);
	}

	private void addLeaf(Location s, World w, int x, int y, int z)
	{
		if (!isChunkValid(w, x, z))
			return;

		Block block = w.getBlockAt(x, y, z);

		if (!isInBlacklist(block))
			return;

		addBlock(s, block, leaves1, 1);

		block = w.getBlockAt(x, y + 1, z);

		if (!isInBlacklist(block))
			return;

		Material type = block.getType();

		if (type == ice || type == leaves1 || type == leaves2)
			return;

		if (!isInBlockList(s, block))
			addBlock(s, block, snow);
	}

	private void addTrunk(Location s, World w, int x, int y, int z)
	{
		if (!isChunkValid(w, x, z))
			return;

		Block block = w.getBlockAt(x, y, z);

		if (!isInBlacklist(block))
			return;

		addBlock(s, block, log, 1);
	}

	double lerp(double start, double end, double percent)
	{
		return (start + (percent * (end - start)));
	}
}
