package com.builtbroken.atomic;

import com.builtbroken.atomic.lib.gui.IGuiTile;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class CommonProxy implements IGuiHandler
{

    public void preInit()
    {

    }

    public void init()
    {

    }

    public void postInit()
    {

    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == 10002)
        {
            return getServerGuiElement(y, player, x);
        }
        else if (ID == 10001)
        {
            return getServerGuiElement(y, player, world.getEntityByID(x));
        }
        return getServerGuiElement(ID, player, world.getTileEntity(x, y, z));
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, int slot)
    {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (stack != null && stack.getItem() instanceof IGuiTile)
        {
            return ((IGuiTile) stack.getItem()).getServerGuiElement(ID, player);
        }
        return null;
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, TileEntity tile)
    {
        if (tile instanceof IGuiTile)
        {
            return ((IGuiTile) tile).getServerGuiElement(ID, player);
        }
        return null;
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, Entity entity)
    {
        if (entity instanceof IGuiTile)
        {
            return ((IGuiTile) entity).getServerGuiElement(ID, player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == 10002)
        {
            return getServerGuiElement(y, player, world.getEntityByID(x));
        }
        else if (ID == 10001)
        {
            return getClientGuiElement(y, player, world.getEntityByID(x));
        }
        return getClientGuiElement(ID, player, world.getTileEntity(x, y, z));
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, int slot)
    {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (stack != null && stack.getItem() instanceof IGuiTile)
        {
            return ((IGuiTile) stack.getItem()).getClientGuiElement(ID, player);
        }
        return null;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, TileEntity tile)
    {
        if (tile instanceof IGuiTile)
        {
            return ((IGuiTile) tile).getClientGuiElement(ID, player);
        }
        return null;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, Entity entity)
    {
        if (entity instanceof IGuiTile)
        {
            return ((IGuiTile) entity).getClientGuiElement(ID, player);
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public boolean isShiftHeld()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }
}
