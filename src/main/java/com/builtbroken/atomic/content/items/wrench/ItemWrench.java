package com.builtbroken.atomic.content.items.wrench;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.processing.TileEntityProcessingMachine;
import com.builtbroken.atomic.lib.LanguageUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Multi-tool for working with and configuring machines
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/29/2018.
 */
public class ItemWrench extends Item
{
    public static final String NBT_COLOR = "toolColor";
    public static final String NBT_MODE = "toolMode";

    @SideOnly(Side.CLIENT)
    private IIcon[] coloredTexture;
    @SideOnly(Side.CLIENT)
    private IIcon[] texture;

    public ItemWrench()
    {
        this.setUnlocalizedName(AtomicScience.PREFIX + "wrench");
        this.setTextureName(AtomicScience.PREFIX + "wrench/wrench");
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    //===============================================
    //======Actions
    //===============================================

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileEntityProcessingMachine)
            {
                ((TileEntityProcessingMachine) tile).onWrench(getMode(stack), getColor(stack), ForgeDirection.getOrientation(side), player);
            }
            return true;
        }
        return false;
    }

    /**
     * Called to handle mouse wheel movement
     *
     * @param stack   - this
     * @param player  - player using the item
     * @param ctrl    - was ctrl held
     * @param forward - is mouse wheel moving forward
     */
    public void handleMouseWheelAction(ItemStack stack, EntityPlayer player, boolean ctrl, boolean forward)
    {
        if (ctrl)
        {
            toggleMode(stack, forward);
        }
        else
        {
            toggleColor(stack, forward);
        }
        player.inventoryContainer.detectAndSendChanges();
    }

    //===============================================
    //========Props
    //===============================================

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean held)
    {
        lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".mode." + getMode(stack).name().toLowerCase() + ".info"));
        lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".color." + getColor(stack).name().toLowerCase() + ".info"));
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
            lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".info"));
            lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".ctrl.info"));
            lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".wheel.info"));
        }
        else
        {
            lines.add(LanguageUtility.getLocal(getUnlocalizedName() + ".more.info"));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return getUnlocalizedName() + "." + getMode(stack).name().toLowerCase() + "." + getColor(stack).name().toLowerCase();
    }

    //===============================================
    //=======Data Accessors
    //===============================================

    public void toggleMode(ItemStack stack, boolean forward)
    {
        setMode(stack, forward ? getMode(stack).next() : getMode(stack).prev());
    }

    public WrenchMode getMode(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            return WrenchMode.get(stack.getTagCompound().getInteger(NBT_MODE));
        }
        return WrenchMode.ROTATION;
    }

    public void setMode(ItemStack stack, WrenchMode mode)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger(NBT_MODE, mode.ordinal());
    }

    public void toggleColor(ItemStack stack, boolean forward)
    {
        setColor(stack, forward ? getColor(stack).next() : getColor(stack).prev());
    }

    public WrenchColor getColor(ItemStack stack)
    {
        if (stack.getTagCompound() != null)
        {
            return WrenchColor.get(stack.getTagCompound().getInteger(NBT_COLOR));
        }
        return WrenchColor.RED;
    }

    public void setColor(ItemStack stack, WrenchColor color)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger(NBT_COLOR, color.ordinal());
    }

    //===============================================
    //=======Render Stuff
    //===============================================

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        coloredTexture = new IIcon[WrenchMode.values().length];
        texture = new IIcon[WrenchMode.values().length];
        for (WrenchMode mode : WrenchMode.values())
        {
            coloredTexture[mode.ordinal()] = reg.registerIcon(this.getIconString() + "." + mode.name().toLowerCase() + ".color");
            texture[mode.ordinal()] = reg.registerIcon(this.getIconString() + "." + mode.name().toLowerCase());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass)
    {
        if (pass == 0)
        {
            return texture[getMode(stack).ordinal()];
        }
        return coloredTexture[getMode(stack).ordinal()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderPasses(int metadata)
    {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        if (pass == 0)
        {
            return super.getColorFromItemStack(stack, pass);
        }
        return getColor(stack).getColorInt();
    }
}
