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
	import java.util.ArrayList;
	import java.util.List;
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

public abstract class RuinGenerator extends StructureGenerator
{
	protected List<Integer> floorN, floorS, floorE, floorW;
	protected List<Integer> wallN, wallS, wallE, wallW;

	public RuinGenerator(boolean notifyOnBlockChanges) {
		super(notifyOnBlockChanges, true);

		addToBlacklist(0);

		for (int i = 0; i < 16; i++) {
			addToBlacklist(18, i);
		}

		fillNorth();
		fillSouth();
		fillEast();
		fillWest();
	}

	protected abstract void fillNorth();
	protected abstract void fillSouth();
	protected abstract void fillEast();
	protected abstract void fillWest();

	protected int getID(int index) {
		switch (index) {
			case  1:
			case  2:
			case  9: return 202;
			case  3:
			case  4:
			case  7:
			case 10:
			case 11: return 155;
			case  5:
			case  6:
			case 12: 
			case 13: return 156;
			case  8: return 201;
			case 14: return 44;
			default: return 0;
		}
	}

	protected int getData(int index) {
		switch (index) {
			case  3:
			case  6: return 1;
			case 12:
			case  7: return 2;
			case  5:
			case 10: return 3;
			case  1:
			case 11: return 4;
			case 14: return 7;
			case  2: return 8;
			default: return 0;
		}
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		Location start = new Location(world, x, y, z);
		int base  = 255;
		int max   = 0;
		int count = 5;
		List<Integer> floor, wall;

		switch (random.nextInt(4)) {
			case 0:  floor = floorN; wall = wallN; break;
			case 1:  floor = floorS; wall = wallS; break;
			case 2:  floor = floorE; wall = wallE; break;
			default: floor = floorW; wall = wallW; break;
		}

		for (int cx = 0; cx < 16; cx++) {
			for (int cz = 0; cz < 16; cz++) {
				int i = floor.get((cz * 16) + cx);

				if (i == 0)
					continue;

				count += 1;

				int temp = getBaseY(world, cx + x, cz + z);

				base = Math.min(base, temp);
				max  = Math.max(max,  temp);

				if (max - base > 4)
					return fail(start);
				if (base < 0)
					return fail(start);
			}
		}

		max = Math.max(max, base + 3);

		int xm = 1;
		int zm = 0;
		int xmin = 0;
		int zmin = 0;
		int xmax = 15;
		int zmax = 15;
		int cx = 0;
		int cz = 0;

		List<Boolean> below = new ArrayList<Boolean>(256);
		List<Boolean> current = new ArrayList<Boolean>(256);

		for (int n = 0; n < 256; n++) {
			below.add(false);
			current.add(false);
		}

		for (int n = 0; n < 256; n++) {
			int i = floor.get((cz << 4) + cx);

			if (i != 0) {
				Block b;

				for (int cy = base; cy <= max; cy++) {
					b = world.getBlockAt(cx + x, cy + 1, cz + z);

					switch (b.getTypeId()) {
						case 1:
						case 2:
						case 3:
						case 16:
						case 21:
						case 56:
						case 73:
						case 74:
						case 78:
						case 79:
						case 129:
						case 174:
							if (cy == base)
								addBlock(start, b, 78);
							else
								addBlock(start, b, 0);

							addToWhitelist(start, b);
						default:
							continue;
					}
				}

				if (random.nextInt(count / 3) != 0) {
					b = world.getBlockAt(cx + x, base, cz + z);
					add(start, b, getID(i), getData(i), true, true);
					addToWhitelist(start, b);
					below.set((cz << 4) + cx, true);
				}
				else {
					b = world.getBlockAt(cx + x, base, cz + z);
					add(start, b, 2, 0, true, true);
					addToWhitelist(start, b);
				}

				count -= 1;
			}

			cx += xm;
			cz += zm;

			if (cx == xmax && cz == zmax && zm == 1) {
				xm = -1;
				zm = 0;
				xmax -= 1;
			}
			else if (cx == xmin && cz == zmax && xm == -1) {
				xm = 0;
				zm = -1;
				zmax -= 1;
			}
			else if (cx == xmin && cz == zmin && zm == -1) {
				xm = 1;
				zm = 0;
				xmin += 1;
			}
			else if (cx == xmax && cz <= zmin && xm == 1) {
				xm = 0;
				zm = 1;
				zmin += 1;
			}
		}

		placeBlocks(start, true);

		for (int cy = 0; cy < 4; cy++) {
			for (cx = 0; cx < 16; cx++) {
				for (cz = 0; cz < 16; cz++) {
					int i = wall.get((cy << 8) + (cz << 4) + cx);

					if (i == 0)
						continue;

					if (!below.get((cz << 4) + cx)) {
						fall(world, start, random, cx + x, base + 1 + cy, cz + z, i, false);
						continue;
					}

					int chance = 1;

					for (int tx = -1; tx < 2; tx++) {
						for (int tz = -1; tz < 2; tz++) {
							if (below.get(((cz + tz) << 4) + cx + tx))
								chance += 1;
						}
					}

					if (random.nextInt(chance) == 0) {
						fall(world, start, random, cx + x, base + 1 + cy, cz + z, i, true);
						continue;
					}

					Block b = world.getBlockAt(cx + x, base + 1 + cy, cz + z);
					switch (i) {
						case 5:
						case 6:
						case 12:
						case 13:
							add(start, b, getID(i), getData(i), false, true);
							break;
						default:
							add(start, b, getID(i), getData(i), true, true);
					}
					addToWhitelist(start, b);
					current.set((cz << 4) + cx, true);
				}
			}

			below = current;
			current = new ArrayList<Boolean>(256);

			for (int n = 0; n < 256; n++) {
				current.add(false);
			}
		}

		return placeBlocks(start, true);
	}

	private void fall(World w, Location s, Random r, int x, int y, int z, int i, boolean support) {
		int id = 0;
		int data = 0;
		int dice = 0;

		if (r.nextInt(3) == 0 && support) {
			Block b = w.getBlockAt(x, y, z);
			switch(i) {
				case 3:
				case 4:
				case 7:
				case 10:
				case 11:
					dice = r.nextInt(3);
					if (dice == 0)
						add(s, b, 44, 7, false);
					else if (dice == 1)
						add(s, b, 156, r.nextInt(4), false);

					return;
				case 5:
				case 6:
				case 12:
				case 13:
					if (r.nextBoolean())
						add(s, b, 44, 7, false);

					return;
				case 8:
					dice = r.nextInt(3);
					if (dice == 0)
						add(s, b, 205, 0, false);
					else if (dice == 1)
						add(s, b, 203, r.nextInt(4), false);

					return;
				default: return;
			}
		}

		int cx = (x - 2) + r.nextInt(5);
		int cz = (z - 2) + r.nextInt(5);
		int cy = getBaseY(w, cx, cz);

		if (cy <= 0 || cy >= y)
			return;

		Block b = w.getBlockAt(cx, cy + 1, cz);

		switch(i) {
			case 3:
			case 4:
			case 7:
			case 10:
			case 11:
				dice = r.nextInt(5);
				if (dice == 0)
					add(s, b, 44, 7, false);
				else if (dice == 1)
					add(s, b, 156, r.nextInt(4), false);
				else if (dice == 2)
					add(s, b, 155, 0);

				return;
			case 5:
			case 6:
			case 12:
			case 13:
				if (r.nextBoolean())
					add(s, b, 44, 7, false);

				return;
			case 8:
				dice = r.nextInt(5);
				if (dice == 0)
					add(s, b, 205, 0, false);
				else if (dice == 1)
					add(s, b, 203, r.nextInt(4), false);
				else if (dice == 2)
					add(s, b, 201, 0);

				return;
			default: return;
		}
	}

	private void add(Location s, Block b, int i, int d, boolean snow, boolean force) {
		if (isInBlockList(s, b) && !force)
			return;

		addBlock(s, b, i, d);
		addToWhitelist(s, b);

		if (!snow) {
			addBlock(s, b.getRelative(BlockFace.UP), 0);
			return;
		}

		b = b.getRelative(BlockFace.UP);

		if (!isInBlacklist(b))
			return;

		if (b.getTypeId() == 79)
			return;

		if (!isInBlockList(s, b) || force)
			addBlock(s, b, 78);
	}

	private void add(Location s, Block b, int i, int d, boolean snow) {
		add(s, b, i, d, snow, false);
	}

	private void add(Location s, Block b, int i, int d) {
		add(s, b, i, d, true, false);
	}

	private int getBaseY(World world, int x, int z) {
		for (int y = 97; y > 0; y--) {
			int id = world.getBlockTypeIdAt(x, y, z);

			switch (id) {
				case 1:
				case 2:
				case 3:
				case 16:
				case 21:
				case 56:
				case 73:
				case 74:
				case 129:
				case 155: return y;
				case 8:
				case 9: return -1;
				default: continue;
			}
		}

		return -1;
	}
}
