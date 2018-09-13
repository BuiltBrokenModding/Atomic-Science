package com.builtbroken.atomic;

import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public abstract class CommonProxy extends ContentProxy implements IGuiHandler
{
    public CommonProxy(String name)
    {
        super(name);
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
        return getServerGuiElement(ID, player, world.getTileEntity(new BlockPos(x, y, z)));
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, int slot)
    {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (!stack.isEmpty() && stack.getItem() instanceof IGuiTile)
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
            return getClientGuiElement(y, player, x);
        }
        else if (ID == 10001)
        {
            return getClientGuiElement(y, player, world.getEntityByID(x));
        }
        return getClientGuiElement(ID, player, world.getTileEntity(new BlockPos(x, y, z)));
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, int slot)
    {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (!stack.isEmpty() && stack.getItem() instanceof IGuiTile)
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

    public void spawnParticle(String particle, double x, double y, double z, double vx, double vy, double vz)
    {

    }
}
