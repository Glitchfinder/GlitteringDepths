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
	import org.bukkit.Material;
	import org.bukkit.World;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.datatypes.BlockValues;
//* IMPORTS: OTHER
	//* NOT NEEDED

public abstract class StructureGenerator
{
	private Map<Location, Map<Block, BlockValues>> modifiedBlocks =
		new HashMap<Location, Map<Block, BlockValues>>();

	private List<BlockValues> replaceBlacklist =
		new ArrayList<BlockValues>();

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

	public boolean addBlock(Location start, Block block, Material material) {
		return addBlock(start, block, new BlockValues(material));
	}

	public boolean addBlock(Location start, Location location, Material material) {
		return addBlock(start, location.getBlock(), new BlockValues(material));
	}

	public boolean addBlock(Location start, Block block, Material material, int data) {
		return addBlock(start, block, new BlockValues(material, data));
	}

	public boolean addBlock(Location start, Location location, Material material, int data) {
		return addBlock(start, location.getBlock(), new BlockValues(material, data));
	}

	public boolean addBlock(Location start, World world, int x, int y, int z, Material material) {
		Location block = new Location(world, x, y, z);
		return addBlock(start, block.getBlock(), new BlockValues(material));
	}

	public boolean addBlock(Location start, World world, int x, int y, int z, Material material,
		int data)
	{
		Location block = new Location(world, x, y, z);
		return addBlock(start, block.getBlock(), new BlockValues(material, data));
	}

	public StructureGenerator addToBlacklist(Material material) {
		replaceBlacklist.add(new BlockValues(material));
		return this;
	}

	public StructureGenerator addToBlacklist(Material material, int data) {
		replaceBlacklist.add(new BlockValues(material, data));
		return this;
	}

	protected abstract boolean generate(World world, Random random, int x, int y, int z);

	public boolean isInBlacklist(Block block) {
		for (BlockValues listItem : replaceBlacklist) {
			if (listItem.getType() != block.getType())
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

	public boolean place(World world, Random random, int x, int y, int z) {
		return generate(world, random, x, y, z);
	}

	public boolean placeBlocks(Location start, boolean fastFail) {
		if (!modifiedBlocks.containsKey(start))
			return fail(start);

		Map<Block, BlockValues> modified = modifiedBlocks.get(start);

		for(Block block : modified.keySet()) {
			setBlock(block, modified.get(block));
		}

		modifiedBlocks.remove(start);
		return true;
	}

	protected boolean fail(Location start) {
		modifiedBlocks.remove(start);
		return false;
	}

	public StructureGenerator removeFromBlacklist(BlockValues values) {
		for(BlockValues listItem : replaceBlacklist) {
			if(listItem.getType() != values.getType())
				continue;

			if(listItem.getData() != values.getData())
				continue;

			return this;
		}

		replaceBlacklist.remove(values);

		return this;
	}



	public StructureGenerator removeFromBlacklist(Material material) {
		return removeFromBlacklist(material, 0);
	}

	public StructureGenerator removeFromBlacklist(Material material, int data) {
		for(BlockValues listItem : replaceBlacklist) {
			if(listItem.getType() != material)
				continue;

			if(listItem.getData() != (byte) data)
				continue;

			replaceBlacklist.remove(listItem);
			return this;
		}

		return this;
	}

	private void setBlock(Block block, BlockValues values) {
		setBlock(block, values.getType(), values.getData());
	}

	private void setBlock(Block block, Material material) {
		setBlock(block, material, (byte) 0);
	}

	private void setBlock(Block block, Material material, byte data) {
		BlockState state = block.getState();
		state.setType(material);
		state.setRawData(data);
		state.update(true, false);
	}

	private void setBlock(Block block, Material material, int data) {
		setBlock(block, material, (byte) data);
	}

	public boolean isChunkValid(World world, int x, int z)
	{
		x = x >> 4; // Chunk X
		z = z >> 4; // Chunk Z

		// If the chunk is not loaded, and does not exist
		if (!world.isChunkLoaded(x, z) && !world.loadChunk(x, z, false))
			return false;

		return true;
	}
}
