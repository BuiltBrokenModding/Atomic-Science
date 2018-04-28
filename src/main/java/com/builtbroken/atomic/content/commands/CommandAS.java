package com.builtbroken.atomic.content.commands;

import com.builtbroken.atomic.content.ASIndirectEffects;
import com.builtbroken.atomic.map.RadiationSystem;
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
            if (sub.equalsIgnoreCase("rad") || sub.equalsIgnoreCase("radiation"))
            {
                commandRadiation(sender, args);
            }
            else if (sub.equalsIgnoreCase("exposure"))
            {
                commandExposure(sender, args);
            }
            else if (sub.equalsIgnoreCase("mat") || sub.equalsIgnoreCase("material"))
            {
                commandMat(sender, args);
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
            sender.addChatMessage(new ChatComponentText(getCommandName() + " rad -> show radiation of self"));
            sender.addChatMessage(new ChatComponentText(getCommandName() + " rad set <value> -> set radiation of self"));
            sender.addChatMessage(new ChatComponentText(getCommandName() + " exposure -> shows radiation of where your standing"));
        }
        sender.addChatMessage(new ChatComponentText(getCommandName() + " rad <player> -> show radiation of player"));
        sender.addChatMessage(new ChatComponentText(getCommandName() + " rad set <player> <value> -> set radiation of player"));
        sender.addChatMessage(new ChatComponentText(getCommandName() + " exposure <player> -> shows the radiation exposure of where the player is standing"));

        sender.addChatMessage(new ChatComponentText(getCommandName() + " material get <dim> <x> <y> <z> -> gets the radioactive material level of the block position"));
        sender.addChatMessage(new ChatComponentText(getCommandName() + " material set <dim> <x> <y> <z> <value> -> sets the radioactive material level of the block position"));
    }

    /**
     * Command set to get or set the REM of the player
     *
     * @param sender
     * @param args
     */
    public void commandRadiation(ICommandSender sender, String[] args)
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

    /**
     * Command set to check the RAD value of the environment
     *
     * @param sender
     * @param args
     */
    public void commandExposure(ICommandSender sender, String[] args)
    {
        if (args.length == 1 && sender instanceof EntityPlayer)
        {
            sender.addChatMessage(new ChatComponentText("Your exposure level is " + RadiationSystem.INSTANCE.getRemExposure((EntityPlayer) sender)));
        }
        else if (args.length == 2)
        {
            EntityPlayerMP player = getPlayer(sender, args[1]);
            player.addChatMessage(new ChatComponentText("Exposure level for '" + player.getCommandSenderName() + "' is " + RadiationSystem.INSTANCE.getRemExposure(player)));
        }
        else
        {
            throw new CommandNotFoundException();
        }
    }


    public void commandMat(ICommandSender sender, String[] args)
    {
        if (args.length >= 6)
        {
            int dim = Integer.parseInt(args[2]);
            int x = Integer.parseInt(args[3]);
            int y = Integer.parseInt(args[4]);
            int z = Integer.parseInt(args[5]);

            if (args[1].equalsIgnoreCase("get"))
            {
                int value = RadiationSystem.INSTANCE.getRadioactiveMaterial(dim, x, y, z);
                sender.addChatMessage(new ChatComponentText("The block position contains '" + value + "' units of radioactive material."));
            }
            else if (args[1].equalsIgnoreCase("set") && args.length == 7)
            {
                int value = Integer.parseInt(args[6]);

                int prev_value = RadiationSystem.INSTANCE.getRadioactiveMaterial(dim, x, y, z);
                RadiationSystem.INSTANCE.setRadioactiveMaterial(dim, x, y, z, value);
                int new_value = RadiationSystem.INSTANCE.getRadioactiveMaterial(dim, x, y, z);

                sender.addChatMessage(new ChatComponentText("The block position radioactive material count changes from '" + prev_value + "' to '" + new_value + "'"));
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
