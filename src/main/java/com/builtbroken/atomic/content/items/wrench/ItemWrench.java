package com.builtbroken.atomic.content.items.wrench;

import com.builtbroken.atomic.content.machines.processing.TileEntityProcessingMachine;
import com.builtbroken.atomic.content.prefab.ItemPrefab;
import com.builtbroken.atomic.lib.LanguageUtility;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Multi-tool for working with and configuring machines
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/29/2018.
 */
public class ItemWrench extends ItemPrefab
{
    public static final String NBT_COLOR = "toolColor";
    public static final String NBT_MODE = "toolMode";

    public ItemWrench()
    {
        super("wrench", "wrench");
    }

    //===============================================
    //======Actions
    //===============================================

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityProcessingMachine)
        {
            if (!world.isRemote)
            {
                ItemStack stack = player.getHeldItem(hand);
                ((TileEntityProcessingMachine) tile).onWrench(getMode(stack), getColor(stack), side, player);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, net.minecraft.world.IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return true;
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> lines, ITooltipFlag flagIn)
    {
        lines.add(LanguageUtility.getLocal(getTranslationKey() + ".mode." + getMode(stack).name().toLowerCase() + ".info"));
        lines.add(LanguageUtility.getLocal(getTranslationKey() + ".color." + getColor(stack).name().toLowerCase() + ".info"));
        if (GuiScreen.isShiftKeyDown())
        {
            lines.add(LanguageUtility.getLocal(getTranslationKey() + ".info"));
            lines.add(LanguageUtility.getLocal(getTranslationKey() + ".use.info"));
            lines.add(LanguageUtility.getLocal(getTranslationKey() + ".ctrl.info"));
            lines.add(LanguageUtility.getLocal(getTranslationKey() + ".wheel.info"));
        }
        else
        {
            lines.add(LanguageUtility.getLocal(getTranslationKey() + ".more.info"));
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return getTranslationKey() + "." + getMode(stack).name().toLowerCase() + "." + getColor(stack).name().toLowerCase();
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
}
