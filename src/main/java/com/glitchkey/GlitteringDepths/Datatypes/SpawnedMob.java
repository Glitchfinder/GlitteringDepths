/*
 * Copyright (c) 2013-2016 Sean Porter <glitchkey@gmail.com>
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

package com.glitchkey.glitteringdepths.datatypes;

//* IMPORTS: JDK/JRE
	//* NOT NEEDED
//* IMPORTS: BUKKIT
	import org.bukkit.DyeColor;
	import org.bukkit.entity.EntityType;
	import org.bukkit.inventory.ItemStack;
//* IMPORTS: PANDORA
	//* NOT NEEDED
//* IMPORTS: GLITTERING DEPTHS
	//* NOT NEEDED
//* IMPORTS: OTHER
	//* NOT NEEDED

public final class SpawnedMob {
	public EntityType type;
	public DyeColor color;
	public ItemStack helmet, chestplate, leggings, boots, held;
	public int x, y, z;
	public double maxHealth;
	public String name;
	public boolean isWither, isVillager, isBaby, isNameShown, canDespawn;
	
	public SpawnedMob() {}
}