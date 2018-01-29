/*
 * Copyright (c) 2013-2018 Sean Porter <glitchkey@gmail.com>
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

package com.glitchkey.glitteringdepths.listeners;

//* IMPORTS: JDK/JRE
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.attribute.Attribute;
	import org.bukkit.Bukkit;
	import org.bukkit.DyeColor;
	import org.bukkit.enchantments.Enchantment;
	import org.bukkit.entity.AbstractHorse;
	import org.bukkit.entity.ElderGuardian;
	import org.bukkit.entity.EntityType;
	import org.bukkit.entity.Guardian;
	import org.bukkit.entity.LivingEntity;
	import org.bukkit.entity.Llama;
	import org.bukkit.entity.PigZombie;
	import org.bukkit.entity.PolarBear;
	import org.bukkit.entity.Rabbit;
	import org.bukkit.entity.Sheep;
	import org.bukkit.entity.Skeleton;
	import org.bukkit.entity.SkeletonHorse;
	import org.bukkit.entity.Slime;
	import org.bukkit.entity.Snowman;
	import org.bukkit.entity.Squid;
	import org.bukkit.entity.Stray;
	import org.bukkit.entity.Witch;
	import org.bukkit.entity.WitherSkeleton;
	import org.bukkit.entity.Wolf;
	import org.bukkit.entity.Zombie;
	import org.bukkit.entity.ZombieHorse;
	import org.bukkit.entity.ZombieVillager;
	import org.bukkit.event.Listener;
	import org.bukkit.event.entity.CreatureSpawnEvent;
	import org.bukkit.event.EventHandler;
	import org.bukkit.event.EventPriority;
	import org.bukkit.event.HandlerList;
	import org.bukkit.inventory.EntityEquipment;
	import org.bukkit.inventory.ItemStack;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.material.Tree;
	import org.bukkit.TreeSpecies;
//* IMPORTS: GLITTERING DEPTHS
	import com.glitchkey.glitteringdepths.GlitteringDepthsPlugin;
//* IMPORTS: OTHER
	//* NOT NEEDED

public final class GlacierMobListener implements Listener {
	private final GlitteringDepthsPlugin plugin;
	private List<Enchantment> helmet;
	private List<Enchantment> chestplate;
	private List<Enchantment> leggings;
	private List<Enchantment> boots;
	private List<Enchantment> tool;
	private List<Enchantment> sword;
	private List<Enchantment> bow;
	private Random rand = new Random();

	public GlacierMobListener(final GlitteringDepthsPlugin plugin) {
		this.plugin = plugin;
		this.helmet = new ArrayList<Enchantment>();
		this.helmet.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		this.helmet.add(Enchantment.PROTECTION_FIRE);
		this.helmet.add(Enchantment.PROTECTION_EXPLOSIONS);
		this.helmet.add(Enchantment.PROTECTION_PROJECTILE);
		this.helmet.add(Enchantment.OXYGEN);
		this.helmet.add(Enchantment.WATER_WORKER);
		this.helmet.add(Enchantment.THORNS);
		this.helmet.add(Enchantment.DURABILITY);
		this.helmet.add(Enchantment.MENDING);
		this.helmet.add(Enchantment.BINDING_CURSE);
		this.chestplate = new ArrayList<Enchantment>();
		this.chestplate.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		this.chestplate.add(Enchantment.PROTECTION_FIRE);
		this.chestplate.add(Enchantment.PROTECTION_EXPLOSIONS);
		this.chestplate.add(Enchantment.PROTECTION_PROJECTILE);
		this.chestplate.add(Enchantment.THORNS);
		this.chestplate.add(Enchantment.DURABILITY);
		this.chestplate.add(Enchantment.MENDING);
		this.chestplate.add(Enchantment.BINDING_CURSE);
		this.leggings = new ArrayList<Enchantment>();
		this.leggings.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		this.leggings.add(Enchantment.PROTECTION_FIRE);
		this.leggings.add(Enchantment.PROTECTION_EXPLOSIONS);
		this.leggings.add(Enchantment.PROTECTION_PROJECTILE);
		this.leggings.add(Enchantment.THORNS);
		this.leggings.add(Enchantment.DURABILITY);
		this.leggings.add(Enchantment.MENDING);
		this.leggings.add(Enchantment.BINDING_CURSE);
		this.boots = new ArrayList<Enchantment>();
		this.boots.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		this.boots.add(Enchantment.PROTECTION_FIRE);
		this.boots.add(Enchantment.PROTECTION_FALL);
		this.boots.add(Enchantment.PROTECTION_EXPLOSIONS);
		this.boots.add(Enchantment.PROTECTION_PROJECTILE);
		this.boots.add(Enchantment.DEPTH_STRIDER);
		this.boots.add(Enchantment.FROST_WALKER);
		this.boots.add(Enchantment.THORNS);
		this.boots.add(Enchantment.DURABILITY);
		this.boots.add(Enchantment.MENDING);
		this.boots.add(Enchantment.BINDING_CURSE);
		this.tool = new ArrayList<Enchantment>();
		this.tool.add(Enchantment.DIG_SPEED);
		this.tool.add(Enchantment.SILK_TOUCH);
		this.tool.add(Enchantment.LOOT_BONUS_BLOCKS);
		this.tool.add(Enchantment.DURABILITY);
		this.tool.add(Enchantment.MENDING);
		this.sword = new ArrayList<Enchantment>();
		this.sword.add(Enchantment.DAMAGE_ALL);
		this.sword.add(Enchantment.DAMAGE_UNDEAD);
		this.sword.add(Enchantment.DAMAGE_ARTHROPODS);
		this.sword.add(Enchantment.KNOCKBACK);
		this.sword.add(Enchantment.FIRE_ASPECT);
		this.sword.add(Enchantment.SWEEPING_EDGE);
		this.sword.add(Enchantment.LOOT_BONUS_MOBS);
		this.sword.add(Enchantment.DURABILITY);
		this.sword.add(Enchantment.MENDING);
		this.bow = new ArrayList<Enchantment>();
		this.bow.add(Enchantment.ARROW_DAMAGE);
		this.bow.add(Enchantment.ARROW_KNOCKBACK);
		this.bow.add(Enchantment.ARROW_FIRE);
		this.bow.add(Enchantment.ARROW_INFINITE);
		this.bow.add(Enchantment.DURABILITY);
		this.bow.add(Enchantment.MENDING);

		registerEvents();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCreatureSpawn(final CreatureSpawnEvent event) {
		LivingEntity entity = event.getEntity();

		if (entity.getWorld() != plugin.world)
			return;

		switch (event.getSpawnReason()) {
			case CHUNK_GEN:
			case DEFAULT:
			case NATURAL:
				event.setCancelled(true);
				break;

			case VILLAGE_INVASION:
				entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(35);
				entity.setHealth(35);
				return;
			default:
				return;
		}

		switch (entity.getType()) {
			case BLAZE:
			case CAVE_SPIDER:
			case CREEPER:
			case ENDER_DRAGON:
			case ENDERMAN:
			case ENDERMITE:
			case EVOKER:
			case GHAST:
			case GIANT:
			case HUSK:
			case ILLUSIONER:
			case MAGMA_CUBE:
			case PIG_ZOMBIE:
			case SHULKER:
			case SILVERFISH:
			case SKELETON:
			case SLIME:
			case SPIDER:
			case STRAY:
			case VEX:
			case VINDICATOR:
			case WITCH:
			case WITHER:
			case WITHER_SKELETON:
			case ZOMBIE:
			case ZOMBIE_VILLAGER:
				spawnHostile(entity.getLocation());
				break;
			case BAT:
				event.setCancelled(false);
				return;
			case SQUID:
			case GUARDIAN:
			case ELDER_GUARDIAN:
				spawnAquatic(entity.getLocation());
				break;
			case CHICKEN:
			case COW:
			case DONKEY:
			case HORSE:
			case IRON_GOLEM:
			case LLAMA:
			case MULE:
			case MUSHROOM_COW:
			case OCELOT:
			case PARROT:
			case PIG:
			case POLAR_BEAR:
			case RABBIT:
			case SHEEP:
			case SKELETON_HORSE:
			case SNOWMAN:
			case VILLAGER:
			case WOLF:
			case ZOMBIE_HORSE:
				spawnPassive(entity.getLocation());
				break;
			default:
				return;
		}
	}

	private void equipSpawnedMob(LivingEntity entity) {
		EntityEquipment inv = entity.getEquipment();
		inv.setHelmet(getHelmet());
		inv.setChestplate(getChestplate());
		inv.setLeggings(getLeggings());
		inv.setBoots(getBoots());
		inv.setItemInMainHand(getHeldItem());
		inv.setItemInOffHand(getHeldItem());
		inv.setHelmetDropChance(0.5F);
		inv.setChestplateDropChance(0.5F);
		inv.setLeggingsDropChance(0.5F);
		inv.setBootsDropChance(0.5F);
		inv.setItemInMainHandDropChance(0.5F);
		inv.setItemInOffHandDropChance(0.5F);
	}

	private void spawnHostile(Location loc) {
		int dice = rand.nextInt(10);

		if (dice < 4) {
			spawnPassive(loc);
			return;
		}

		dice = rand.nextInt(100);

		try {
			if (dice < 8)
				spawnPigZombie(loc);
			else if (dice < 28)
				spawnSkeleton(loc);
			else if (dice < 48)
				spawnSlime(loc);
			else if (dice < 53)
				spawnStray(loc);
			else if (dice < 68)
				spawnWitch(loc);
			else if (dice < 73)
				spawnWitherSkeleton(loc);
			else if (dice < 88)
				spawnZombie(loc);
			else if (dice < 95)
				spawnZombieVillager(loc);
			else
				spawnKillerBunny(loc);
		} catch (Exception e) {}
	}

	public void spawnPigZombie(Location loc) {
		PigZombie entity = (PigZombie) plugin.world.spawnEntity(loc, EntityType.PIG_ZOMBIE);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
		entity.setHealth(40);
	}

	public void spawnSkeleton(Location loc) {
		Skeleton entity = (Skeleton) plugin.world.spawnEntity(loc, EntityType.SKELETON);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
		entity.setHealth(40);
		equipSpawnedMob((LivingEntity) entity);
	}

	public void spawnSlime(Location loc) {
		Slime entity = (Slime) plugin.world.spawnEntity(loc, EntityType.SLIME);

		int type = rand.nextInt(4);
		int health = (int) (Math.pow(4, type) * 2D);
		entity.setSize(type + 1);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		entity.setHealth(health);
	}

	public void spawnStray(Location loc) {
		Stray entity = (Stray) plugin.world.spawnEntity(loc, EntityType.STRAY);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
		entity.setHealth(40);
	}

	public void spawnWitch(Location loc) {
		Witch entity = (Witch) plugin.world.spawnEntity(loc, EntityType.WITCH);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(52);
		entity.setHealth(52);
	}

	public void spawnWitherSkeleton(Location loc) {
		WitherSkeleton entity = (WitherSkeleton) plugin.world.spawnEntity(loc, EntityType.WITHER_SKELETON);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
		entity.setHealth(40);
	}

	public void spawnZombie(Location loc) {
		Zombie entity = (Zombie) plugin.world.spawnEntity(loc, EntityType.ZOMBIE);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
		entity.setHealth(40);

		if (rand.nextInt(4) == 0)
			entity.setBaby(true);

		equipSpawnedMob((LivingEntity) entity);
	}

	public void spawnZombieVillager(Location loc) {
		ZombieVillager entity = (ZombieVillager) plugin.world.spawnEntity(loc, EntityType.ZOMBIE_VILLAGER);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
		entity.setHealth(40);

		if (rand.nextInt(4) == 0)
			entity.setBaby(true);

		equipSpawnedMob((LivingEntity) entity);
	}

	public void spawnKillerBunny(Location loc) {
		Rabbit entity = (Rabbit) plugin.world.spawnEntity(loc, EntityType.RABBIT);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(12);
		entity.setHealth(12);
		entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(8);
		entity.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);

		if (rand.nextInt(4) == 0)
			entity.setBaby();
	}

	private void spawnAquatic(Location loc) {
		int dice = rand.nextInt(100);

		try {
			if (dice < 65)
				spawnSquid(loc);
			else if (dice < 95)
				spawnGuardian(loc);
			else
				spawnElderGuardian(loc);
		} catch (Exception e) {}
	}

	public void spawnSquid(Location loc) {
		Squid entity = (Squid) plugin.world.spawnEntity(loc, EntityType.SQUID);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
		entity.setHealth(40);
	}

	public void spawnGuardian(Location loc) {
		Guardian entity = (Guardian) plugin.world.spawnEntity(loc, EntityType.GUARDIAN);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(120);
		entity.setHealth(120);
	}

	public void spawnElderGuardian(Location loc) {
		ElderGuardian entity = (ElderGuardian) plugin.world.spawnEntity(loc, EntityType.ELDER_GUARDIAN);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(240);
		entity.setHealth(240);
	}

	private void spawnPassive(Location loc) {
		int dice = rand.nextInt(100);

		try {
			if (dice < 30)
				spawnRabbit(loc);
			else if (dice < 60)
				spawnSheep(loc);
			else if (dice < 70)
				spawnLlama(loc);
			else if (dice < 80)
				spawnWolf(loc);
			else if (dice < 90)
				spawnHorse(loc);
			else if (dice < 95)
				spawnBear(loc);
			else
				spawnGolem(loc);
		} catch (Exception e) {}
	}

	public void spawnRabbit(Location loc) {
		Rabbit entity = (Rabbit) plugin.world.spawnEntity(loc, EntityType.RABBIT);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(12);
		entity.setHealth(12);

		int dice = rand.nextInt(100);

		if (dice < 60)
			entity.setRabbitType(Rabbit.Type.WHITE);
		else if (dice < 75)
			entity.setRabbitType(Rabbit.Type.BLACK_AND_WHITE);
		else if (dice < 90)
			entity.setRabbitType(Rabbit.Type.SALT_AND_PEPPER);
		else if (dice < 97)
			entity.setRabbitType(Rabbit.Type.BROWN);
		else
			entity.setRabbitType(Rabbit.Type.BLACK);

		if (rand.nextInt(4) == 0)
			entity.setBaby();
	}

	public void spawnSheep(Location loc) {
		Sheep entity = (Sheep) plugin.world.spawnEntity(loc, EntityType.SHEEP);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(32);
		entity.setHealth(32);

		int dice = rand.nextInt(100);

		if (dice < 30)
			entity.setColor(DyeColor.WHITE);
		else if (dice < 90)
			entity.setColor(DyeColor.SILVER);
		else if (dice < 97)
			entity.setColor(DyeColor.GRAY);
		else
			entity.setColor(DyeColor.BLACK);

		if (rand.nextInt(4) == 0)
			entity.setBaby();
	}

	public void spawnLlama(Location loc) {
		Llama entity = (Llama) plugin.world.spawnEntity(loc, EntityType.LLAMA);

		int health = (rand.nextInt(15) + 15) * 4;
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		entity.setHealth(health);
		entity.setStrength(rand.nextInt(5) + 1);

		int dice = rand.nextInt(100);

		if (dice < 30)
			entity.setColor(Llama.Color.WHITE);
		else if (dice < 90)
			entity.setColor(Llama.Color.GRAY);
		else if (dice < 97)
			entity.setColor(Llama.Color.CREAMY);
		else
			entity.setColor(Llama.Color.BROWN);

		if (rand.nextInt(4) == 0)
			entity.setBaby();
	}

	public void spawnWolf(Location loc) {
		Wolf entity = (Wolf) plugin.world.spawnEntity(loc, EntityType.WOLF);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(32);
		entity.setHealth(32);

		if (rand.nextInt(4) == 0)
			entity.setBaby();
	}

	public void spawnHorse(Location loc) {
		AbstractHorse entity;
		if (rand.nextBoolean())
			entity = (AbstractHorse) plugin.world.spawnEntity(loc, EntityType.ZOMBIE_HORSE);
		else
			entity = (AbstractHorse) plugin.world.spawnEntity(loc, EntityType.SKELETON_HORSE);

		int health = (rand.nextInt(15) + 15) * 4;
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		entity.setHealth(health);
		entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue((rand.nextDouble() * 0.225D) + 0.1125D);
		entity.setJumpStrength((rand.nextDouble() * 0.6D) + 0.4D);

		if (rand.nextInt(4) == 0)
			entity.setBaby();
	}

	public void spawnBear(Location loc) {
		PolarBear entity = (PolarBear) plugin.world.spawnEntity(loc, EntityType.POLAR_BEAR);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(120);
		entity.setHealth(120);

		if (rand.nextInt(4) == 0)
			entity.setBaby();
	}

	public void spawnGolem(Location loc) {
		Snowman entity = (Snowman) plugin.world.spawnEntity(loc, EntityType.SNOWMAN);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(16);
		entity.setHealth(16);

		if (rand.nextInt(4) == 0)
			entity.setDerp(true);
	}

	private ItemStack getHelmet() {
		int dice = rand.nextInt(100);

		if (dice < 90)
			return null;
		else if (dice < 93)
			return getHelmetBlock();

		dice = rand.nextInt(100);
		ItemStack result = null;

		if (dice < 70)
			result = new ItemStack(Material.LEATHER_HELMET, 1);
		else if (dice < 85)
			result = new ItemStack(Material.GOLD_HELMET, 1);
		else if (dice < 93)
			result = new ItemStack(Material.IRON_HELMET, 1);
		else if (dice < 97)
			result = new ItemStack(Material.DIAMOND_HELMET, 1);
		else
			result = new ItemStack(Material.CHAINMAIL_HELMET, 1);

		result.setDurability((short) (rand.nextInt(
			result.getDurability() + 1)));
		dice = rand.nextInt(100);

		if (dice < 85)
			return result;

		for (Enchantment ench: helmet) {
			if (rand.nextInt(3) != 0)
				continue;

			int level = getEnchantLevel(ench.getMaxLevel());
			result.addUnsafeEnchantment(ench, level);
		}

		return result;
	}

	private ItemStack getHelmetBlock() {
		int dice = rand.nextInt(100);

		if (dice < 25)
			return (new ItemStack(Material.GLASS, 1));
		else if (dice < 95)
			return (new ItemStack(Material.ICE, 1));
		else
			return (new ItemStack(Material.PUMPKIN, 1));
	}

	private ItemStack getChestplate() {
		int dice = rand.nextInt(100);

		if (dice < 90)
			return null;

		dice = rand.nextInt(100);
		ItemStack result = null;

		if (dice < 70)
			result = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		else if (dice < 85)
			result = new ItemStack(Material.GOLD_CHESTPLATE, 1);
		else if (dice < 93)
			result = new ItemStack(Material.IRON_CHESTPLATE, 1);
		else if (dice < 97)
			result = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		else
			result = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);

		result.setDurability((short) (rand.nextInt(
			result.getDurability() + 1)));
		dice = rand.nextInt(100);

		if (dice < 85)
			return result;

		for (Enchantment ench: chestplate) {
			if (rand.nextInt(3) != 0)
				continue;

			int level = getEnchantLevel(ench.getMaxLevel());
			result.addUnsafeEnchantment(ench, level);
		}

		return result;
	}

	private ItemStack getLeggings() {
		int dice = rand.nextInt(100);

		if (dice < 90)
			return null;

		dice = rand.nextInt(100);
		ItemStack result = null;

		if (dice < 70)
			result = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		else if (dice < 85)
			result = new ItemStack(Material.GOLD_LEGGINGS, 1);
		else if (dice < 93)
			result = new ItemStack(Material.IRON_LEGGINGS, 1);
		else if (dice < 97)
			result = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		else
			result = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);

		result.setDurability((short) (rand.nextInt(
			result.getDurability() + 1)));
		dice = rand.nextInt(100);

		if (dice < 85)
			return result;

		for (Enchantment ench: leggings) {
			if (rand.nextInt(3) != 0)
				continue;

			int level = getEnchantLevel(ench.getMaxLevel());
			result.addUnsafeEnchantment(ench, level);
		}

		return result;
	}

	private ItemStack getBoots() {
		int dice = rand.nextInt(100);

		if (dice < 90)
			return null;

		dice = rand.nextInt(100);
		ItemStack result = null;

		if (dice < 70)
			result = new ItemStack(Material.LEATHER_BOOTS, 1);
		else if (dice < 85)
			result = new ItemStack(Material.GOLD_BOOTS, 1);
		else if (dice < 93)
			result = new ItemStack(Material.IRON_BOOTS, 1);
		else if (dice < 97)
			result = new ItemStack(Material.DIAMOND_BOOTS, 1);
		else
			result = new ItemStack(Material.CHAINMAIL_BOOTS, 1);

		result.setDurability((short) (rand.nextInt(
			result.getDurability() + 1)));
		dice = rand.nextInt(100);

		if (dice < 85)
			return result;

		for (Enchantment ench: boots) {
			if (rand.nextInt(3) != 0)
				continue;

			int level = getEnchantLevel(ench.getMaxLevel());
			result.addUnsafeEnchantment(ench, level);
		}

		return result;
	}

	private ItemStack getHeldItem() {
		int dice = rand.nextInt(3);

		if (dice < 1)
			return getMeleeWeapon();
		else if (dice < 2) 
			return getBow();
		else
			return getTool();
	}

	private ItemStack getMeleeWeapon() {
		int dice = rand.nextInt(100);

		if (dice < 90)
			return null;

		dice = rand.nextInt(100);
		ItemStack result = null;

		if (dice < 30)
			result = new ItemStack(Material.WOOD_SWORD, 1);
		else if (dice < 60)
			result = new ItemStack(Material.WOOD_AXE, 1);
		else if (dice < 68)
			result = new ItemStack(Material.STONE_SWORD, 1);
		else if (dice < 76)
			result = new ItemStack(Material.STONE_AXE, 1);
		else if (dice < 82)
			result = new ItemStack(Material.GOLD_SWORD, 1);
		else if (dice < 88)
			result = new ItemStack(Material.GOLD_AXE, 1);
		else if (dice < 92)
			result = new ItemStack(Material.IRON_SWORD, 1);
		else if (dice < 96)
			result = new ItemStack(Material.IRON_AXE, 1);
		else if (dice < 98)
			result = new ItemStack(Material.DIAMOND_SWORD, 1);
		else
			result = new ItemStack(Material.DIAMOND_AXE, 1);

		result.setDurability((short) (rand.nextInt(
			result.getDurability() + 1)));
		dice = rand.nextInt(100);

		if (dice < 85)
			return result;

		for (Enchantment ench: sword) {
			if (rand.nextInt(3) != 0)
				continue;

			int level = getEnchantLevel(ench.getMaxLevel());
			result.addUnsafeEnchantment(ench, level);
		}

		return result;
	}

	private ItemStack getBow() {
		int dice = rand.nextInt(100);

		if (dice < 90)
			return null;

		ItemStack result = new ItemStack(Material.BOW, 1);

		result.setDurability((short) (rand.nextInt(
			result.getDurability() + 1)));
		dice = rand.nextInt(100);

		if (dice < 85)
			return result;

		for (Enchantment ench: bow) {
			if (rand.nextInt(3) != 0)
				continue;

			int level = getEnchantLevel(ench.getMaxLevel());
			result.addUnsafeEnchantment(ench, level);
		}

		return result;
	}

	private ItemStack getTool() {
		int dice = rand.nextInt(5);

		if (dice < 1)
			return getShovel();
		else if (dice < 2) 
			return getPickaxe();
		else if (dice < 3)
			return getAxe();
		else if (dice < 4)
			return getHoe();
		else
			return getOtherTool();
	}

	private ItemStack getShovel() {
		int dice = rand.nextInt(100);

		if (dice < 90)
			return null;

		dice = rand.nextInt(100);
		ItemStack result = null;

		if (dice < 60)
			result = new ItemStack(Material.WOOD_SPADE, 1);
		else if (dice < 76) 
			result = new ItemStack(Material.STONE_SPADE, 1);
		else if (dice < 88)
			result = new ItemStack(Material.GOLD_SPADE, 1);
		else if (dice < 96)
			result = new ItemStack(Material.IRON_SPADE, 1);
		else
			result = new ItemStack(Material.DIAMOND_SPADE, 1);

		result.setDurability((short) (rand.nextInt(
			result.getDurability() + 1)));
		dice = rand.nextInt(100);

		if (dice < 85)
			return result;

		boolean silk = false;

		for (Enchantment ench: tool) {
			if (rand.nextInt(3) != 0)
				continue;
			if (silk && ench == Enchantment.LOOT_BONUS_BLOCKS)
				continue;

			int level = getEnchantLevel(ench.getMaxLevel());
			result.addUnsafeEnchantment(ench, level);

			if (ench == Enchantment.SILK_TOUCH)
				silk = true;
		}

		return result;
	}

	private ItemStack getPickaxe() {
		int dice = rand.nextInt(100);

		if (dice < 90)
			return null;

		dice = rand.nextInt(100);
		ItemStack result = null;

		if (dice < 60)
			result = new ItemStack(Material.WOOD_PICKAXE, 1);
		else if (dice < 76) 
			result = new ItemStack(Material.STONE_PICKAXE, 1);
		else if (dice < 88)
			result = new ItemStack(Material.GOLD_PICKAXE, 1);
		else if (dice < 96)
			result = new ItemStack(Material.IRON_PICKAXE, 1);
		else
			result = new ItemStack(Material.DIAMOND_PICKAXE, 1);

		result.setDurability((short) (rand.nextInt(
			result.getDurability() + 1)));
		dice = rand.nextInt(100);

		if (dice < 85)
			return result;

		boolean silk = false;

		for (Enchantment ench: tool) {
			if (rand.nextInt(3) != 0)
				continue;
			if (silk && ench == Enchantment.LOOT_BONUS_BLOCKS)
				continue;

			int level = getEnchantLevel(ench.getMaxLevel());
			result.addUnsafeEnchantment(ench, level);

			if (ench == Enchantment.SILK_TOUCH)
				silk = true;
		}

		return result;
	}

	private ItemStack getAxe() {
		int dice = rand.nextInt(100);

		if (dice < 90)
			return null;

		dice = rand.nextInt(100);
		ItemStack result = null;

		if (dice < 60)
			result = new ItemStack(Material.WOOD_AXE, 1);
		else if (dice < 76) 
			result = new ItemStack(Material.STONE_AXE, 1);
		else if (dice < 88)
			result = new ItemStack(Material.GOLD_AXE, 1);
		else if (dice < 96)
			result = new ItemStack(Material.IRON_AXE, 1);
		else
			result = new ItemStack(Material.DIAMOND_AXE, 1);

		result.setDurability((short) (rand.nextInt(
			result.getDurability() + 1)));
		dice = rand.nextInt(100);

		if (dice < 85)
			return result;

		boolean silk = false;

		for (Enchantment ench: tool) {
			if (rand.nextInt(3) != 0)
				continue;
			if (silk && ench == Enchantment.LOOT_BONUS_BLOCKS)
				continue;

			int level = getEnchantLevel(ench.getMaxLevel());
			result.addUnsafeEnchantment(ench, level);

			if (ench == Enchantment.SILK_TOUCH)
				silk = true;
		}

		return result;
	}

	private ItemStack getHoe() {
		int dice = rand.nextInt(100);

		if (dice < 90)
			return null;

		dice = rand.nextInt(100);
		ItemStack result = null;

		if (dice < 60)
			result = new ItemStack(Material.WOOD_HOE, 1);
		else if (dice < 76) 
			result = new ItemStack(Material.STONE_HOE, 1);
		else if (dice < 88)
			result = new ItemStack(Material.GOLD_HOE, 1);
		else if (dice < 96)
			result = new ItemStack(Material.IRON_HOE, 1);
		else
			result = new ItemStack(Material.DIAMOND_HOE, 1);

		result.setDurability((short) (rand.nextInt(
			result.getDurability() + 1)));
		dice = rand.nextInt(100);

		return result;
	}

	private ItemStack getOtherTool() {
		int dice = rand.nextInt(5);

		if (dice == 0)
			return null;

		dice = rand.nextInt(11);
		ItemStack result = null;

		if (dice < 1)
			result = new ItemStack(Material.FLINT_AND_STEEL, 1);
		else if (dice < 2)
			result = new ItemStack(Material.STICK, 1);
		else if (dice < 3)
			result = new ItemStack(Material.ARROW, 1);
		else if (dice < 4)
			result = new ItemStack(Material.BOWL, 1);
		else if (dice < 5)
			result = new ItemStack(Material.SHEARS, 1);
		else if (dice < 6)
			result = new ItemStack(Material.GLASS_BOTTLE, 1);
		else if (dice < 7) {
			result = (new Tree(TreeSpecies.GENERIC)).toItemStack(1);
			result.setType(Material.SAPLING);
		} else if (dice < 8) {
			result = (new Tree(TreeSpecies.REDWOOD)).toItemStack(1);
			result.setType(Material.SAPLING);
		} else if (dice < 9) {
			result = (new Tree(TreeSpecies.BIRCH)).toItemStack(1);
			result.setType(Material.SAPLING);
		} else if (dice < 10) {
			result = (new Tree(TreeSpecies.JUNGLE)).toItemStack(1);
			result.setType(Material.SAPLING);
		} else
			result = new ItemStack(Material.FISHING_ROD, 1);

		return result;
	}

	private int getEnchantLevel(int maximum) {
		for (int i = 1; i < maximum; i++) {
			if (rand.nextInt(100) < 70)
				return i;
		}

		return maximum;
	}

	public void registerEvents() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void unregisterEvents() {
		HandlerList.unregisterAll(this);
	}
}
