package com.builtbroken.atomic.content.items.cell;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

/**
 * Generic fluid item
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public class ItemFluidCell extends Item
{
    /** Map of supported fluids to there texture path */
    public HashMap<Fluid, String> supportedFluidToTexturePath = new HashMap();

    /** Map of supported fluids to localization */
    public HashMap<Fluid, String> supportedFluidToLocalization = new HashMap();

    /** Max size of the container in mb(1000mb to a bucket) */
    public final int fluidCapacity;

    public ItemFluidCell(int fluidCapacity)
    {
        this.fluidCapacity = fluidCapacity;
        this.setTranslationKey(AtomicScience.PREFIX + "cell.fluid");
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> lines, ITooltipFlag flagIn)
    {
        FluidStack fluidStack = getFluid(stack);
        if(fluidStack != null)
        {
            lines.add("Fluid: " + fluidStack.getLocalizedName());
            lines.add("Amount: " + fluidStack.amount);
        }
    }

    //----------------------------------------------------------------
    //---------- Properties
    //----------------------------------------------------------------

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        FluidStack fluidStack = getFluid(stack);
        if(fluidStack != null && supportedFluidToLocalization.containsKey(fluidStack.getFluid()))
        {
            return supportedFluidToLocalization.get(fluidStack.getFluid());
        }
        return super.getTranslationKey();
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return isEmpty(stack) ? Items.BUCKET.getItemStackLimit(stack) : 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if (tab == getCreativeTab())
        {
            for (Fluid fluid : supportedFluidToTexturePath.keySet())
            {
                list.add(getContainerForFluid(fluid));
            }
        }
    }

    //Attempts to locate a bucket that supports the fluid, prevents empty buckets from showing in creative tab
    public ItemStack getContainerForFluid(Fluid fluid)
    {
        ItemStack stack = new ItemStack(this);
        fill(stack, new FluidStack(fluid, getCapacity(stack)), true);
        return stack;
    }

    //----------------------------------------------------------------
    //---------- Crafting stuff
    //----------------------------------------------------------------

    @Override
    public boolean hasContainerItem(ItemStack stack)
    {
        return getFluid(stack) != null;
    }

    //----------------------------------------------------------------
    //----------Fluid stuff
    //----------------------------------------------------------------

    /**
     * Helper method to check if the bucket is empty
     *
     * @param container - bucket
     * @return true if it is empty
     */
    public boolean isEmpty(ItemStack container)
    {
        return getFluid(container) == null;
    }

    /**
     * Helper method to check if the bucket is full
     *
     * @param container - bucket
     * @return true if it is full
     */
    public boolean isFull(ItemStack container)
    {
        FluidStack stack = getFluid(container);
        if (stack != null)
        {
            return stack.amount == getCapacity(container);
        }
        return false;
    }

    public int getCapacity(ItemStack container)
    {
        return fluidCapacity;
    }

    /* IFluidContainerItem */
    public FluidStack getFluid(ItemStack container)
    {
        if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Fluid"))
        {
            return null;
        }
        return FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));
    }

    /**
     * Simple check method that will ensure the resource is not null. As well
     * that the fluid is supported by the container.
     *
     * @param container - this
     * @param resource  - fluid stack, ignores amount of fluid
     * @return true if is supported
     */
    public boolean canSupportFluid(ItemStack container, FluidStack resource)
    {
        return resource != null;// && supportedFluidToTexturePath.containsKey(resource.getFluid());
    }

    public int fill(ItemStack container, FluidStack resource, boolean doFill)
    {
        if (canSupportFluid(container, resource))
        {
            if (!doFill)
            {
                if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Fluid"))
                {
                    return Math.min(getCapacity(container), resource.amount);
                }

                FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));

                if (stack == null)
                {
                    return Math.min(getCapacity(container), resource.amount);
                }

                if (!stack.isFluidEqual(resource))
                {
                    return 0;
                }

                return Math.min(getCapacity(container) - stack.amount, resource.amount);
            }

            if (container.getTagCompound() == null)
            {
                container.setTagCompound(new NBTTagCompound());
            }

            if (!container.getTagCompound().hasKey("Fluid"))
            {
                NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());

                if (getCapacity(container) < resource.amount)
                {
                    fluidTag.setInteger("Amount", getCapacity(container));
                    container.getTagCompound().setTag("Fluid", fluidTag);
                    return getCapacity(container);
                }

                container.getTagCompound().setTag("Fluid", fluidTag);
                return resource.amount;
            }
            else
            {

                NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag("Fluid");
                FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

                if (!stack.isFluidEqual(resource))
                {
                    return 0;
                }

                int filled = getCapacity(container) - stack.amount;
                if (resource.amount < filled)
                {
                    stack.amount += resource.amount;
                    filled = resource.amount;
                }
                else
                {
                    stack.amount = getCapacity(container);
                }

                container.getTagCompound().setTag("Fluid", stack.writeToNBT(fluidTag));
                return filled;
            }
        }
        return 0;
    }

    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
        if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Fluid"))
        {
            return null;
        }

        FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));
        if (stack == null)
        {
            return null;
        }

        int currentAmount = stack.amount;
        stack.amount = Math.min(stack.amount, maxDrain);
        if (doDrain)
        {
            if (currentAmount == stack.amount)
            {
                container.getTagCompound().removeTag("Fluid");

                if (container.getTagCompound().isEmpty())
                {
                    container.setTagCompound(null);
                }
                return stack;
            }

            NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag("Fluid");
            fluidTag.setInteger("Amount", currentAmount - stack.amount);
            container.getTagCompound().setTag("Fluid", fluidTag);
        }
        return stack;
    }
}
