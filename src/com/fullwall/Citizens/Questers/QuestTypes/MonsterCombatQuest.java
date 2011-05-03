package com.fullwall.Citizens.Questers.QuestTypes;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class MonsterCombatQuest implements Quest {
	private HumanNPC questGiver;
	private Player killer;
	private CreatureType monster;
	private ItemStack weapon;

	public MonsterCombatQuest(HumanNPC questGiver, Player killer,
			CreatureType monster, ItemStack weapon) {
		this.questGiver = questGiver;
		this.killer = killer;
		this.monster = monster;
		this.weapon = weapon;
	}

	public CreatureType getMonster() {
		return monster;
	}

	public void setMonster(CreatureType monster) {
		this.monster = monster;
	}

	public ItemStack getWeapon() {
		return weapon;
	}

	public void setWeapon(ItemStack weapon) {
		this.weapon = weapon;
	}

	@Override
	public HumanNPC getQuestGiver() {
		return questGiver;
	}

	@Override
	public void setQuestGiver(HumanNPC questGiver) {
		this.questGiver = questGiver;
	}

	@Override
	public Player getPlayer() {
		return killer;
	}

	@Override
	public QuestType getType() {
		return QuestType.MONSTER_COMBAT;
	}

	@Override
	public String getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}
}