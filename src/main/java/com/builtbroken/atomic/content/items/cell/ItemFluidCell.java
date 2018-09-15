package com.builtbroken.atomic.content.items.cell;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
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
    public HashMap<Fluid, ResourceLocation> supportedFluidToTexturePath = new HashMap();

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

    public void addSupportedFluid(Fluid fluid, String texture, String name)
    {
        supportedFluidToTexturePath.put(fluid, new ResourceLocation(texture));
        supportedFluidToLocalization.put(fluid, name);
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new FluidHandlerItemStack(stack, fluidCapacity)
        {
            @Override
            public boolean canFillFluidType(FluidStack fluid)
            {
                return canSupportFluid(container, fluid);
            }
        };
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> lines, ITooltipFlag flagIn)
    {
        FluidStack fluidStack = getFluid(stack);
        if (fluidStack != null)
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
        if (fluidStack != null && supportedFluidToLocalization.containsKey(fluidStack.getFluid()))
        {
            return supportedFluidToLocalization.get(fluidStack.getFluid());
        }
        return super.getTranslationKey();
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return !hasContainerItem(stack) ? Items.BUCKET.getItemStackLimit(stack) : 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        super.getSubItems(tab, list);
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
        IFluidHandlerItem handlerItem = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        if (handlerItem != null)
        {
            handlerItem.fill(new FluidStack(fluid, handlerItem.getTankProperties()[0].getCapacity()), true);
            return handlerItem.getContainer();
        }
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

    public FluidStack getFluid(ItemStack stack)
    {
        IFluidHandlerItem handlerItem = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        if (handlerItem != null)
        {
            return handlerItem.drain(Integer.MAX_VALUE, false);
        }
        return null;
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
}
