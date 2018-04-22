package com.builtbroken.atomic.content.commands;

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
        if (args == null || args.length == 0 || args[1].equals("help") || args[1].equals("?"))
        {
            displayHelp(sender);
        }
        else
        {
            final String sub = args[0];
            if (sub.equalsIgnoreCase("rad"))
            {
                if (args.length == 1 && sender instanceof EntityPlayer)
                {
                    //TODO show self rad
                }
                else if (args.length > 1)
                {
                    if (args[1].equalsIgnoreCase("set"))
                    {
                        if (args.length == 3 && sender instanceof EntityPlayer)
                        {
                            float value = Float.parseFloat(args[2]);
                            //TODO set rad
                        }
                        else if (args.length == 4)
                        {
                            EntityPlayerMP player = getPlayer(sender, args[2]);
                            float value = Float.parseFloat(args[3]);
                            //TODO set rad

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
                        //TODO show player rad
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
}
