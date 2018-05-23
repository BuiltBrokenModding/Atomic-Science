package com.builtbroken.atomic.content.items.cell;

import com.builtbroken.atomic.AtomicScience;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic fluid item
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public class ItemFluidCell extends Item implements IFluidContainerItem
{
    @SideOnly(Side.CLIENT)
    protected HashMap<Fluid, IIcon> fluidToIcon;

    @SideOnly(Side.CLIENT)
    protected IIcon fluidMask;

    /** Map of supported fluids to there texture path */
    public HashMap<Fluid, String> supportedFluidToTexturePath = new HashMap();

    /** Map of supported fluids to localization */
    public HashMap<Fluid, String> supportedFluidToLocalization = new HashMap();

    /** Max size of the container in mb(1000mb to a bucket) */
    public final int fluidCapacity;

    public ItemFluidCell(int fluidCapacity)
    {
        this.fluidCapacity = fluidCapacity;
        this.setTextureName(AtomicScience.PREFIX + "cell_empty");
        this.setUnlocalizedName(AtomicScience.PREFIX + "cell.fluid");
        this.setCreativeTab(AtomicScience.creativeTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack cell, EntityPlayer player, List lines, boolean held)
    {
        FluidStack fluidStack = getFluid(cell);
        if(fluidStack != null)
        {
            lines.add("Fluid: " + fluidStack.getLocalizedName() + " Amount: " + fluidStack.amount);
        }
    }

    //----------------------------------------------------------------
    //---------- Textures
    //----------------------------------------------------------------

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass)
    {
        FluidStack fluidStack = getFluid(stack);
        if (fluidStack != null)
        {
            IIcon icon = fluidToIcon.get(fluidStack.getFluid());
            if (icon != null)
            {
                return icon;
            }
        }
        return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        super.registerIcons(reg);
        fluidMask = reg.registerIcon(AtomicScience.PREFIX + "cell_fluid_mask");
        fluidToIcon = new HashMap();
        for (Map.Entry<Fluid, String> entry : supportedFluidToTexturePath.entrySet())
        {
            fluidToIcon.put(entry.getKey(), reg.registerIcon(entry.getValue()));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        if (meta == -1)
        {
            return fluidMask;
        }
        return this.itemIcon;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return 1; //requiresMultipleRenderPasses() ? 2 : 1; TODO add option for overlays
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true; //Fixes getIcon(Stack, pass) not being called
    }

    public void addSupportedFluid(Fluid fluid, String texture, String name)
    {
        supportedFluidToTexturePath.put(fluid, texture);
        supportedFluidToLocalization.put(fluid, name);
    }

    //----------------------------------------------------------------
    //---------- Properties
    //----------------------------------------------------------------

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        FluidStack fluidStack = getFluid(stack);
        if(fluidStack != null && supportedFluidToLocalization.containsKey(fluidStack.getFluid()))
        {
            return supportedFluidToLocalization.get(fluidStack.getFluid());
        }
        return super.getUnlocalizedName();
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        return isEmpty(stack) ? Items.bucket.getItemStackLimit(stack) : 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
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
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack)
    {
        return isEmpty(stack);
    }

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


    @Override
    public int getCapacity(ItemStack container)
    {
        return fluidCapacity;
    }

    /* IFluidContainerItem */
    @Override
    public FluidStack getFluid(ItemStack container)
    {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
        {
            return null;
        }
        return FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
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

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill)
    {
        if (canSupportFluid(container, resource))
        {
            if (!doFill)
            {
                if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
                {
                    return Math.min(getCapacity(container), resource.amount);
                }

                FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));

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

            if (container.stackTagCompound == null)
            {
                container.stackTagCompound = new NBTTagCompound();
            }

            if (!container.stackTagCompound.hasKey("Fluid"))
            {
                NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());

                if (getCapacity(container) < resource.amount)
                {
                    fluidTag.setInteger("Amount", getCapacity(container));
                    container.stackTagCompound.setTag("Fluid", fluidTag);
                    return getCapacity(container);
                }

                container.stackTagCompound.setTag("Fluid", fluidTag);
                return resource.amount;
            }
            else
            {

                NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
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

                container.stackTagCompound.setTag("Fluid", stack.writeToNBT(fluidTag));
                return filled;
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Fluid"))
        {
            return null;
        }

        FluidStack stack = FluidStack.loadFluidStackFromNBT(container.stackTagCompound.getCompoundTag("Fluid"));
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
                container.stackTagCompound.removeTag("Fluid");

                if (container.stackTagCompound.hasNoTags())
                {
                    container.stackTagCompound = null;
                }
                return stack;
            }

            NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag("Fluid");
            fluidTag.setInteger("Amount", currentAmount - stack.amount);
            container.stackTagCompound.setTag("Fluid", fluidTag);
        }
        return stack;
    }
}
