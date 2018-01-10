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

package com.glitchkey.glitteringdepths.structures;

//* IMPORTS: JDK/JRE
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.block.BlockState;
	import org.bukkit.Location;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.datatypes.BlockValues;
//* IMPORTS: OTHER
	//* NOT NEEDED

public abstract class StructureGenerator
{
	private Map<Location, Map<Block, BlockValues>> modifiedBlocks = new HashMap<Location, Map<Block, BlockValues>>();
	private List<BlockValues> replaceBlacklist = new ArrayList<BlockValues>();
	private Map<Location, List<Block>> replaceWhitelist = new HashMap<Location, List<Block>>();

	private final boolean notifyOnBlockChanges;
	private boolean invertBlacklist = false;

	public StructureGenerator() {
		this (false, false);
	}

	public StructureGenerator(boolean notifyOnBlockChanges) {
		this (notifyOnBlockChanges, false);
	}

	public StructureGenerator(boolean notifyOnBlockChanges,
		boolean invertBlacklist)
	{
		this.notifyOnBlockChanges = notifyOnBlockChanges;
		this.invertBlacklist = invertBlacklist;
	}

	public boolean addBlock(Location start, Block block, BlockValues values) {
		if (modifiedBlocks == null)
			modifiedBlocks = new HashMap<Location, Map<Block, BlockValues>>();

		if (!modifiedBlocks.containsKey(start))
			modifiedBlocks.put(start, new HashMap<Block, BlockValues>());

		modifiedBlocks.get(start).put(block, values);

		if (isInBlacklist(block))
			return false;

		return true;
	}

	public boolean addBlock(Location start, Block block, int id) {
		return addBlock(start, block, new BlockValues(id));
	}

	public boolean addBlock(Location start, Location location, int id) {
		return addBlock(start, location.getBlock(), new BlockValues(id));
	}

	public boolean addBlock(Location start, Block block, int id, int data) {
		return addBlock(start, block, new BlockValues(id, data));
	}

	public boolean addBlock(Location start, Location location, int id, int data) {
		return addBlock(start, location.getBlock(), new BlockValues(id, data));
	}

	public boolean addBlock(Location start, World world, int x, int y, int z, int id) {
		Location block = new Location(world, x, y, z);
		return addBlock(start, block.getBlock(), new BlockValues(id));
	}

	public boolean addBlock(Location start, World world, int x, int y, int z, int id,
		int data)
	{
		Location block = new Location(world, x, y, z);
		return addBlock(start, block.getBlock(), new BlockValues(id, data));
	}

	public StructureGenerator addToBlacklist(int id) {
		replaceBlacklist.add(new BlockValues(id));
		return this;
	}

	public StructureGenerator addToBlacklist(int id, int data) {
		replaceBlacklist.add(new BlockValues(id, data));
		return this;
	}

	public StructureGenerator addToWhitelist(Location start, Block block) {
		if (!replaceWhitelist.containsKey(start))
			replaceWhitelist.put(start, new ArrayList<Block>());

		replaceWhitelist.get(start).add(block);
		return this;
	}

	protected abstract boolean generate(World world, Random random, int x, int y, int z);

	public void invertBlacklist() {
		invertBlacklist = (invertBlacklist ? false : true);
	}

	public boolean isInBlacklist(Block block) {
		for (BlockValues listItem : replaceBlacklist) {
			if (listItem.getId() != block.getTypeId())
				continue;

			if (listItem.getData() != block.getData())
				continue;

			return true;
		}

		return false;
	}

	public boolean isInBlockList(Location start, Block block) {
		if (!modifiedBlocks.containsKey(start))
			return false;

		return modifiedBlocks.get(start).containsKey(block);
	}

	public boolean isInWhitelist(Location start, Block block) {
		if (!replaceWhitelist.containsKey(start))
			return false;

		return replaceWhitelist.get(start).contains(block);
	}

	public boolean place(World world, Random random, int x, int y, int z) {
		return generate(world, random, x, y, z);
	}

	public boolean placeBlocks(Location start, boolean fastFail) {
		if (!modifiedBlocks.containsKey(start))
			return false;

		Map<Block, BlockValues> modified = modifiedBlocks.get(start);
		List<BlockState> blocks = new ArrayList<BlockState>();

		for(Block block : modified.keySet()) {
			boolean blacklisted = isInBlacklist(block);

			if (isInWhitelist(start, block))
				blacklisted = invertBlacklist;

			if(fastFail && blacklisted && !invertBlacklist) {
				return false;
			}
			else if(fastFail && !blacklisted && invertBlacklist) {
				return false;
			}

			BlockState state = block.getState();
			state.setTypeId(modified.get(block).getId());
			state.setRawData(modified.get(block).getData());
			blocks.add(state);
		}

		for(Block block : modified.keySet()) {
			setBlock(block, modified.get(block));
		}

		replaceWhitelist.remove(start);
		modifiedBlocks.remove(start);
		return true;
	}


	public StructureGenerator removeFromBlacklist(BlockValues values) {
		for(BlockValues listItem : replaceBlacklist) {
			if(listItem.getId() != values.getId())
				continue;

			if(listItem.getData() != values.getData())
				continue;

			return this;
		}

		replaceBlacklist.remove(values);

		return this;
	}



	public StructureGenerator removeFromBlacklist(int id) {
		return removeFromBlacklist(id, 0);
	}

	public StructureGenerator removeFromBlacklist(int id, int data) {
		for(BlockValues listItem : replaceBlacklist) {
			if(listItem.getId() != id)
				continue;

			if(listItem.getData() != (byte) data)
				continue;

			replaceBlacklist.remove(listItem);
			return this;
		}

		return this;
	}

	private void setBlock(Block block, BlockValues values) {
		setBlock(block, values.getId(), values.getData());
	}

	private void setBlock(Block block, int id) {
		setBlock(block, id, (byte) 0);
	}

	private void setBlock(Block block, int id, byte data) {
		block.setTypeIdAndData(id, data, notifyOnBlockChanges);
	}

	private void setBlock(Block block, int id, int data) {
		setBlock(block, id, (byte) data);
	}
}
