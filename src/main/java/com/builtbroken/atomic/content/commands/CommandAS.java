package com.builtbroken.atomic.content.commands;

import com.builtbroken.atomic.content.ASIndirectEffects;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/21/2018.
 */
public class CommandAS extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "atomic-science";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/" + getCommandName() + " help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args == null || args.length == 0 || args[0].equals("help") || args[0].equals("?"))
        {
            displayHelp(sender);
        }
        else
        {
            final String sub = args[0];
            if (sub.equalsIgnoreCase("rad"))
            {
                commandRad(sender, args);
            }
            else
            {
                throw new CommandNotFoundException();
            }
        }
    }

    public void displayHelp(ICommandSender sender)
    {
        if (sender instanceof EntityPlayer)
        {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender) + " rad -> show radiation of self"));
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender) + " rad set <value> -> set radiation of self"));
        }
        sender.addChatMessage(new ChatComponentText(getCommandUsage(sender) + " rad <player> -> show radiation of player"));
        sender.addChatMessage(new ChatComponentText(getCommandUsage(sender) + " rad set <player> <value> -> set radiation of player"));
    }

    public void commandRad(ICommandSender sender, String[] args)
    {
        if (args.length == 1 && sender instanceof EntityPlayer)
        {
            sender.addChatMessage(new ChatComponentText("Your radiation level is " + ASIndirectEffects.getRadiation((EntityPlayer) sender)));
        }
        else if (args.length > 1)
        {
            if (args[1].equalsIgnoreCase("set"))
            {
                if (args.length == 3 && sender instanceof EntityPlayer)
                {
                    ASIndirectEffects.setRadiation((EntityPlayer) sender, Float.parseFloat(args[2]));
                    sender.addChatMessage(new ChatComponentText("Your radiation level is now " + ASIndirectEffects.getRadiation((EntityPlayer) sender)));
                }
                else if (args.length == 4)
                {
                    EntityPlayer player = getPlayer(sender, args[2]);
                    ASIndirectEffects.setRadiation(player, Float.parseFloat(args[3]));
                    player.addChatMessage(new ChatComponentText("Radiation level for '" + player.getCommandSenderName() + "' is now " + ASIndirectEffects.getRadiation(player)));
                    player.addChatMessage(new ChatComponentText("Your radiation level is now " + ASIndirectEffects.getRadiation(player)));
                }
                //Invalid (likely ran 'rad set' from command line)
                else if (args.length == 3)
                {
                    sender.addChatMessage(new ChatComponentText(getCommandUsage(sender) + " rad set <player> <value> -> set radiation of player"));
                }
            }
            else if (args.length == 2)
            {
                EntityPlayerMP player = getPlayer(sender, args[1]);
                player.addChatMessage(new ChatComponentText("Radiation level for '" + player.getCommandSenderName() + "' is " + ASIndirectEffects.getRadiation(player)));
            }
            else
            {
                throw new CommandNotFoundException();
            }
        }
        else
        {
            throw new CommandNotFoundException();
        }
    }
}
