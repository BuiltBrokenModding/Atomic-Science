package com.builtbroken.atomic.content.commands;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASIndirectEffects;
import com.builtbroken.atomic.map.MapHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/21/2018.
 */
public class CommandAS extends CommandBase
{
    @Override
    public String getName()
    {
        return "atomic-science";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/" + getName() + " help";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
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
                commandRadiation(server, sender, args);
            }
            else if (sub.equalsIgnoreCase("exposure"))
            {
                commandExposure(server, sender, args);
            }
            else if (sub.equalsIgnoreCase("mat") || sub.equalsIgnoreCase("material"))
            {
                commandMat(server, sender, args);
            }
            else if (sub.equalsIgnoreCase("item-data"))
            {
                if (sender instanceof EntityPlayer)
                {
                    ItemStack held = ((EntityPlayer) sender).getHeldItem(EnumHand.MAIN_HAND);
                    sender.sendMessage(new TextComponentString("Item:" + held.getDisplayName()));

                    if (!held.isEmpty())
                    {
                        sender.sendMessage(new TextComponentString("--Dam:" + held.getItemDamage()));
                        sender.sendMessage(new TextComponentString("--Reg:" + held.getItem().getRegistryName()));
                        if (held.getTagCompound() != null && !held.getTagCompound().isEmpty())
                        {
                            sender.sendMessage(new TextComponentString("--NBT:" + held.getTagCompound().getSize()));
                            sender.sendMessage(new TextComponentString(held.getTagCompound().toString()));

                        }

                        int[] ids = OreDictionary.getOreIDs(held);
                        if (ids.length > 0)
                        {
                            sender.sendMessage(new TextComponentString("--Ore:" + ids.length));
                            int i = 0;
                            for (int id : ids)
                            {
                                sender.sendMessage(new TextComponentString("---[" + (i++) + "]:" + id + "  " + OreDictionary.getOreName(id)));
                            }
                        }
                    }

                }
                else
                {
                    throw new CommandNotFoundException();
                }
            }
            else if (sub.equalsIgnoreCase("ore") && AtomicScience.runningAsDev && sender instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) sender;
                BlockPos pos = player.getPosition();
                Chunk chunk = player.world.getChunk(pos);

                for (int x = 0; x < 16; x++)
                {
                    for (int z = 0; z < 16; z++)
                    {
                        for (int y = 0; y <= chunk.getHeightValue(x, z); y++)
                        {
                            IBlockState state = chunk.getBlockState(x, y, z);
                            Block block = state.getBlock();
                            if (block == Blocks.STONE
                                    || state.getMaterial() == Material.SAND
                                    || state.getMaterial() == Material.WATER
                                    || state.getMaterial() == Material.GROUND
                                    || state.getMaterial() == Material.GRASS)
                            {
                                player.world.setBlockState(new BlockPos(chunk.x * 16 + x, y, chunk.z * 16 + z), Blocks.AIR.getDefaultState());
                            }
                            else if(block == Blocks.AIR && y < player.world.getSeaLevel())
                            {
                                player.world.setBlockState(new BlockPos(chunk.x * 16 + x, y, chunk.z * 16 + z), Blocks.STAINED_GLASS.getDefaultState());
                            }
                        }
                    }
                }
                sender.sendMessage(new TextComponentString("Done"));
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
            sender.sendMessage(new TextComponentString(getName() + " rad -> show radiation of self")); //TODO translate
            sender.sendMessage(new TextComponentString(getName() + " rad set <value> -> set radiation of self"));
            sender.sendMessage(new TextComponentString(getName() + " exposure -> shows radiation of where your standing"));
        }
        sender.sendMessage(new TextComponentString(getName() + " rad <player> -> show radiation of player"));
        sender.sendMessage(new TextComponentString(getName() + " rad set <player> <value> -> set radiation of player"));
        sender.sendMessage(new TextComponentString(getName() + " exposure <player> -> shows the radiation exposure of where the player is standing"));

        sender.sendMessage(new TextComponentString(getName() + " material get <dim> <x> <y> <z> -> gets the radioactive material level of the block position"));
        sender.sendMessage(new TextComponentString(getName() + " material set <dim> <x> <y> <z> <value> -> sets the radioactive material level of the block position"));
    }

    /**
     * Command set to get or set the REM of the player
     *
     * @param sender
     * @param args
     */
    public void commandRadiation(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 1 && sender instanceof EntityPlayer)
        {
            sender.sendMessage(new TextComponentString("Your radiation level is " + ASIndirectEffects.getRadiation((EntityPlayer) sender)));
        }
        else if (args.length > 1)
        {
            if (args[1].equalsIgnoreCase("set"))
            {
                if (args.length == 3 && sender instanceof EntityPlayer)
                {
                    ASIndirectEffects.setRadiation((EntityPlayer) sender, Float.parseFloat(args[2]));
                    sender.sendMessage(new TextComponentString("Your radiation level is now " + ASIndirectEffects.getRadiation((EntityPlayer) sender)));
                }
                else if (args.length == 4)
                {
                    EntityPlayer player = getPlayer(server, sender, args[2]);
                    ASIndirectEffects.setRadiation(player, Float.parseFloat(args[3]));
                    player.sendMessage(new TextComponentString("Radiation level for '" + player.getDisplayNameString() + "' is now " + ASIndirectEffects.getRadiation(player)));
                    player.sendMessage(new TextComponentString("Your radiation level is now " + ASIndirectEffects.getRadiation(player)));
                }
                //Invalid (likely ran 'rad set' from command line)
                else if (args.length == 3)
                {
                    sender.sendMessage(new TextComponentString(getUsage(sender) + " rad set <player> <value> -> set radiation of player"));
                }
            }
            else if (args.length == 2)
            {
                EntityPlayerMP player = getPlayer(server, sender, args[1]);
                player.sendMessage(new TextComponentString("Radiation level for '" + player.getDisplayNameString() + "' is " + ASIndirectEffects.getRadiation(player)));
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
    public void commandExposure(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 1 && sender instanceof EntityPlayer)
        {
            sender.sendMessage(new TextComponentString("Your exposure level is " + MapHandler.RADIATION_MAP.getRemExposure((EntityPlayer) sender)));
        }
        else if (args.length == 2)
        {
            EntityPlayerMP player = getPlayer(server, sender, args[1]);
            player.sendMessage(new TextComponentString("Exposure level for '" + player.getDisplayNameString() + "' is " + MapHandler.RADIATION_MAP.getRemExposure(player)));
        }
        else
        {
            throw new CommandNotFoundException();
        }
    }


    public void commandMat(MinecraftServer server, ICommandSender sender, String[] args) throws CommandNotFoundException
    {
        if (args.length >= 6)
        {
            int dim = Integer.parseInt(args[2]);
            int x = Integer.parseInt(args[3]);
            int y = Integer.parseInt(args[4]);
            int z = Integer.parseInt(args[5]);

            if (args[1].equalsIgnoreCase("get"))
            {
                int value = MapHandler.MATERIAL_MAP.getData(dim, x, y, z);
                sender.sendMessage(new TextComponentString("The block position contains '" + value + "' units of radioactive material."));
            }
            else if (args[1].equalsIgnoreCase("set") && args.length == 7)
            {
                int value = Integer.parseInt(args[6]);

                int prev_value = MapHandler.MATERIAL_MAP.getData(dim, x, y, z);
                MapHandler.MATERIAL_MAP.setData(dim, x, y, z, value);
                int new_value = MapHandler.MATERIAL_MAP.getData(dim, x, y, z);

                sender.sendMessage(new TextComponentString("The block position radioactive material count changes from '" + prev_value + "' to '" + new_value + "'"));
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
