package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Questers.Quest;
import com.fullwall.Citizens.Questers.QuestTypes.QuestManager.QuestType;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class QuesterExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private Citizens plugin;

	public QuesterExecutor(Citizens plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(MessageUtils.mustBeIngameMessage);
			return true;
		}
		Player player = (Player) sender;
		HumanNPC npc = null;
		boolean returnval = false;
		if (NPCManager.validateSelected((Player) sender))
			npc = NPCManager
					.getNPC(NPCManager.NPCSelected.get(player.getName()));
		else {
			sender.sendMessage(ChatColor.RED
					+ MessageUtils.mustHaveNPCSelectedMessage);
			return true;
		}
		if (!NPCManager.validateOwnership(player, npc.getUID())) {
			sender.sendMessage(MessageUtils.notOwnerMessage);
			return true;
		}
		if (!npc.isQuester()) {
			sender.sendMessage(ChatColor.RED + "Your NPC isn't a quester yet.");
			return true;
		} else {
			if (args.length == 2 && args[0].equals("reward")) {
				if (BasicExecutor.hasPermission("citizens.quester.reward",
						sender)) {
					Quest quest = new Quest(player, npc, QuestType.BUILD, false);
					quest.giveiConomyReward(player, 10);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
		}
		return returnval;
	}
}
