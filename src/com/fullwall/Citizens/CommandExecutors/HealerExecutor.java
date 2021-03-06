package com.fullwall.Citizens.CommandExecutors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fullwall.Citizens.Citizens;
import com.fullwall.Citizens.Economy.EconomyHandler;
import com.fullwall.Citizens.Economy.EconomyHandler.Operation;
import com.fullwall.Citizens.NPCs.NPCManager;
import com.fullwall.Citizens.Utils.HealerPropertyPool;
import com.fullwall.Citizens.Utils.HelpUtils;
import com.fullwall.Citizens.Utils.MessageUtils;
import com.fullwall.Citizens.Utils.StringUtils;
import com.fullwall.resources.redecouverte.NPClib.HumanNPC;

public class HealerExecutor implements CommandExecutor {
	@SuppressWarnings("unused")
	private Citizens plugin;

	public HealerExecutor(Citizens plugin) {
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
		if (!npc.isHealer()) {
			sender.sendMessage(ChatColor.RED + "Your NPC isn't a healer yet.");
			return true;
		} else {
			if (args.length == 1 && args[0].equals("status")) {
				if (BasicExecutor.hasPermission("citizens.healer.status",
						sender)) {
					displayStatus(player, npc);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;

			} else if (args.length == 1 && args[0].equals("level-up")) {
				if (BasicExecutor
						.hasPermission("citizens.healer.level", sender)) {
					levelUp(player, npc, 1);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;

			} else if (args.length == 2 && args[0].equals("level-up")) {
				if (BasicExecutor
						.hasPermission("citizens.healer.level", sender)) {
					try {
						int levels = Integer.parseInt(args[1]);
						int x = HealerPropertyPool.getLevel(npc.getUID())
								+ levels;
						if (x <= 10) {
							levelUp(player, npc, levels);
						} else {
							sender.sendMessage(ChatColor.RED
									+ "You cannot exceed Level 10.");
						}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED
								+ "That's not a number.");
					}
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;

			} else if (args.length == 1 && args[0].equals("help")) {
				if (BasicExecutor.hasPermission("citizens.healer.help", sender)) {
					HelpUtils.sendHealerHelp(sender);
				} else {
					sender.sendMessage(MessageUtils.noPermissionsMessage);
				}
				returnval = true;
			}
			HealerPropertyPool.saveState(npc);
		}
		return returnval;
	}

	private void displayHealerStrength(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.YELLOW + "Health: " + ChatColor.GREEN
				+ HealerPropertyPool.getStrength(npc.getUID()) + ChatColor.RED
				+ "/" + HealerPropertyPool.getMaxStrength(npc.getUID()));
	}

	private void displayHealerLevel(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.GREEN
				+ HealerPropertyPool.getLevel(npc.getUID()) + ChatColor.RED
				+ "/10");
	}

	private void displayStatus(Player player, HumanNPC npc) {
		player.sendMessage(ChatColor.GREEN
				+ "========== "
				+ StringUtils.yellowify(npc.getStrippedName()
						+ "'s Healer Status") + " ==========");
		displayHealerStrength(player, npc);
		displayHealerLevel(player, npc);
	}

	private void levelUp(Player player, HumanNPC npc, int multiple) {
		if (EconomyHandler.useEconomy()) {
			int level = HealerPropertyPool.getLevel(npc.getUID());
			double paid = EconomyHandler.pay(Operation.HEALER_LEVEL_UP, player,
					multiple);
			if (paid > 0) {
				if (level < 10) {
					HealerPropertyPool
							.saveLevel(npc.getUID(), level + multiple);
					player.sendMessage(getLevelUpPaidMessage(
							Operation.HEALER_LEVEL_UP, npc, paid, level
									+ multiple, multiple));
				} else {
					player.sendMessage(StringUtils.yellowify(npc
							.getStrippedName())
							+ " has reached the maximum level.");
				}
			}
		} else {
			player.sendMessage(ChatColor.GRAY
					+ "Your server has not turned economy on for Citizens.");
		}
	}

	private String getLevelUpPaidMessage(Operation op, HumanNPC npc,
			double paid,
			int level, int multiple) {
		String message = ChatColor.GREEN
				+ "You have leveled up the healer "
				+ StringUtils.yellowify(npc.getStrippedName())
				+ " to "
				+ StringUtils.yellowify("Level " + level)
				+ " for "
				+ StringUtils.yellowify(EconomyHandler.getPaymentType(op, ""
						+ paid * multiple, ChatColor.GREEN)
						+ ".");
		return message;
	}
}