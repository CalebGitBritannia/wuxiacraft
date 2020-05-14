package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.networking.CultivationMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class CultivationCommand extends CommandBase {

	@Override
	@Nonnull
	public String getName() {
		return "cultivation";
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public String getUsage(ICommandSender sender) {
		return "/cultivation or /cultivation target";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("cult");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = getCommandSenderAsPlayer(sender);
			if (!player.world.isRemote) {
				boolean wrongUsage = true;
				if (args.length == 0) {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
					IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
					String message = String.format("You are at %s", cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()));
					TextComponentString text = new TextComponentString(message);
					sender.sendMessage(text);
					message = String.format("Progress: %d/%d", (int) cultivation.getCurrentProgress(), (int) cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()));
					text = new TextComponentString(message);
					sender.sendMessage(text);
					//message = String.format("Energy: %d/%d", (int) cultivation.getEnergy(), (int) cultivation.getCurrentLevel().getMaxEnergyByLevel(cultivation.getCurrentSubLevel()));
					message = String.format("Energy: %d/%d", (int) cultivation.getEnergy(), (int) cultivation.getMaxEnergy(foundation));
					text = new TextComponentString(message);
					sender.sendMessage(text);
					message = String.format("Speed: %d/%d%%", (int) cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel()), cultivation.getSpeedHandicap());
					text = new TextComponentString(message);
					sender.sendMessage(text);
					wrongUsage = false;
				} else if (args.length == 2) {
					if (args[0].equals("get")) {
						EntityPlayer target = server.getPlayerList().getPlayerByUsername(args[1]);
						if (target == null) {
							String message = String.format("Player %s not found", args[1]);
							TextComponentString text = new TextComponentString(message);
							sender.sendMessage(text);
							wrongUsage = true;
						} else {
							ICultivation cultivation = CultivationUtils.getCultivationFromEntity(target);
							IFoundation foundation = CultivationUtils.getFoundationFromEntity(target);
							if (target.getUniqueID().equals(player.getUniqueID())) {
								String message = String.format("You are at %s", cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()));
								TextComponentString text = new TextComponentString(message);
								sender.sendMessage(text);
								message = String.format("Progress: %d/%d", (int) cultivation.getCurrentProgress(), (int) cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()));
								text = new TextComponentString(message);
								sender.sendMessage(text);
								message = String.format("Energy: %d/%d", (int) cultivation.getEnergy(), (int) cultivation.getMaxEnergy(foundation));
								text = new TextComponentString(message);
								sender.sendMessage(text);
								message = String.format("Speed: %d/%d%%", (int) cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel()), cultivation.getSpeedHandicap());
								text = new TextComponentString(message);
								sender.sendMessage(text);
							} else {
								String message = String.format("%s is at %s", target.getName(), cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()));
								TextComponentString text = new TextComponentString(message);
								sender.sendMessage(text);
								message = String.format("Progress: %d/%d", (int) cultivation.getCurrentProgress(), (int) cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()));
								text = new TextComponentString(message);
								sender.sendMessage(text);
								message = String.format("Energy: %d/%d", (int) cultivation.getEnergy(), (int) cultivation.getMaxEnergy(foundation));
								text = new TextComponentString(message);
								sender.sendMessage(text);
								message = String.format("Speed: %d/%d%%", (int) cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel()), cultivation.getSpeedHandicap());
								text = new TextComponentString(message);
								sender.sendMessage(text);
							}
							wrongUsage = false;
						}
					}
				} else if (args.length == 3) {
					if (args[0].equals("set")) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						CultivationLevel level = cultivation.getCurrentLevel();
						boolean found_level = false;
						for (CultivationLevel l : CultivationLevel.LOADED_LEVELS.values()) {
							if (l.getUName().equals(args[1])) {
								level = l;
								found_level = true;
								break;
							}
						}
						int subLevel = Integer.parseInt(args[2]) - 1;
						if (found_level) {
							cultivation.setCurrentLevel(level);
							cultivation.setCurrentSubLevel(subLevel);
							NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), player);
							TextComponentString text = new TextComponentString("You're now at " + cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()));
							sender.sendMessage(text);
							wrongUsage = false;
						} else {
							TextComponentString text = new TextComponentString("Couldn't find level " + args[1]);
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
							wrongUsage = true;
						}
					}
				} else if (args.length == 4) {
					EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
					if(targetPlayer != null) {
						if (args[0].equals("set")) {
							ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
							CultivationLevel level = cultivation.getCurrentLevel();
							boolean found_level = false;
							for (CultivationLevel l : CultivationLevel.LOADED_LEVELS.values()) {
								if (l.getUName().equals(args[2])) {
									level = l;
									found_level = true;
									break;
								}
							}
							int subLevel = Integer.parseInt(args[3]) - 1;
							if (found_level) {
								cultivation.setCurrentLevel(level);
								cultivation.setCurrentSubLevel(subLevel);
								NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), player);
								if (targetPlayer.getUniqueID().equals(player.getUniqueID())) {
									TextComponentString text = new TextComponentString("You're now at " + cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()));
									sender.sendMessage(text);
								} else {
									TextComponentString text = new TextComponentString(targetPlayer.getName() + " is now at " + cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()));
									sender.sendMessage(text);
								}
								wrongUsage = false;
							} else {
								TextComponentString text = new TextComponentString("Couldn't find level " + args[2]);
								text.getStyle().setColor(TextFormatting.RED);
								sender.sendMessage(text);
								wrongUsage = true;
							}
						}
					} else {
						TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
					}
				} else if (args.length == 5) {
					EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
					if(targetPlayer != null) {
						if (args[0].equals("set")) {
							ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
							CultivationLevel level = cultivation.getCurrentLevel();
							boolean found_level = false;
							for (CultivationLevel l : CultivationLevel.LOADED_LEVELS.values()) {
								if (l.getUName().equals(args[2])) {
									level = l;
									found_level = true;
									break;
								}
							}
							int subLevel = Integer.parseInt(args[3]) - 1;
							if (found_level) {
								String keepProgress = args[4];
								if(keepProgress.equals("true")) {
									cultivation.setCurrentLevel(level);
									cultivation.setCurrentSubLevel(subLevel);
								} else if (keepProgress.equalsIgnoreCase("false")) {
									cultivation.setCurrentLevel(level);
									cultivation.setCurrentSubLevel(subLevel);
									cultivation.setProgress(1);
								}
								NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), player);
								if (targetPlayer.getUniqueID().equals(player.getUniqueID())) {
									TextComponentString text = new TextComponentString("You're now at " + cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()));
									sender.sendMessage(text);
								} else {
									TextComponentString text = new TextComponentString(targetPlayer.getName() + " is now at " + cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()));
									sender.sendMessage(text);
								}
								wrongUsage = false;
							} else {
								TextComponentString text = new TextComponentString("Couldn't find level " + args[2]);
								text.getStyle().setColor(TextFormatting.RED);
								sender.sendMessage(text);
								wrongUsage = true;
							}
						}
					} else {
						TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
					}
				}
				if (wrongUsage) {
					TextComponentString text = new TextComponentString("Invalid arguments, use /cult [get player]:[set (player) cultivation_level rank]:[set (player) cultivation_level rank keep_progress]");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				}
			}
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			if ("get".startsWith(args[0]))
				completions.add("get");
			if ("set".startsWith(args[0]))
				completions.add("set");
		}else if (args.length == 2) {
			for(String player : server.getPlayerList().getOnlinePlayerNames()) {
				if(player.toLowerCase().startsWith(args[1].toLowerCase())) {
					completions.add(player);
				}
			}
			if(args[0].equalsIgnoreCase("set")) {
				for (CultivationLevel level : CultivationLevel.LOADED_LEVELS.values()) {
					if (level.getUName().toLowerCase().startsWith(args[1].toLowerCase()))
						completions.add(level.getUName());
				}
			}
		}else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("set")) {
				for (CultivationLevel level : CultivationLevel.LOADED_LEVELS.values()) {
					if (level.levelName.toLowerCase().startsWith(args[2].toLowerCase()))
						completions.add(level.getUName());
				}
			}
		}else if(args.length == 5) {
			if("true".startsWith(args[4])){
				completions.add("true");
			}
			if("false".startsWith(args[4])){
				completions.add("false");
			}
		}
		return completions;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}
