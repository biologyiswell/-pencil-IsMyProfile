/**
 * MIT License
 *
 * Copyright (c) 2017 biologyiswell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
