package com.github.biologyiswell.profiler.command;

import com.github.biologyiswell.profiler.managers.ProfileManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by biologyiswell on 04/10/2017.
 */
public class IsMyProfilerCmd implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("§cSorry, only players can be execute this command.");
            return true;
        }

        if (args.length == 0)
        {
            sender.sendMessage("/profiler skin <name> §8- §7Change your skin.");
            sender.sendMessage("/profiler name <name> §8- §7Change your name.");
            sender.sendMessage("/profiler setprofile <name> §8- §7Change your skin and name.");
            return true;
        }

        final Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("skin"))
        {
            if (args.length < 2)
            {
                player.sendMessage("§cWrong arguments. Use: /profiler skin <name>.");
                return true;
            }

            ProfileManager.setSkin(player, args[1]);

            player.sendMessage("§eYou changed your skin.");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);
            return true;
        }

        if (args[0].equalsIgnoreCase("name"))
        {
            if (args.length < 2)
            {
                player.sendMessage("§cWrong arguments. Use: /profiler name <name>.");
                return true;
            }

            ProfileManager.setName(player, args[1]);

            player.sendMessage("§eYou changed your name.");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);
            return true;
        }

        if (args[0].equalsIgnoreCase("setprofile"))
        {
            if (args.length < 2)
            {
                player.sendMessage("§cWrong arguments. Use: /profiler setprofile <name>.");
                return true;
            }

            ProfileManager.setSkinAndName(player, args[1], args[1]);

            player.sendMessage("§eYou changed your skin and name.");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);
            return true;
        }

        return false;
    }
}
