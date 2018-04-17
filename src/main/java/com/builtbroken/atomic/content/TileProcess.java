package com.builtbroken.atomic.content;

import com.builtbroken.mc.prefab.tile.TileModuleMachine;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/** General class for all machines that do traditional recipe processing
 *
 * @author Calclavia */
public abstract class TileProcess<I extends IInventory> extends TileModuleMachine<I>
{
    protected int inputSlot;
    protected int outputSlot;

    protected int tankInputFillSlot;
    protected int tankInputDrainSlot;
    protected int tankOutputFillSlot;
    protected int tankOutputDrainSlot;

    protected String machineName;

    /**
     * Default constructor
     *
     * @param name     - name of the tile, used for localizations mainly
     * @param material - material of the tile's block
     */
    public TileProcess(String name, Material material)
    {
        super(name, material);
    }

    @Override
    public void update()
    {
        super.update();

        if (getInputTank() != null)
        {
            fillOrDrainTank(tankInputFillSlot, tankInputDrainSlot, getInputTank());
        }
        if (getOutputTank() != null)
        {
            fillOrDrainTank(tankOutputFillSlot, tankOutputDrainSlot, getOutputTank());
        }
    }

    /** Takes an fluid container item and try to fill the tank, dropping the remains in the output slot. */
    public void fillOrDrainTank(int containerInput, int containerOutput, FluidTank tank)
    {
        ItemStack inputStack = getStackInSlot(containerInput);
        ItemStack outputStack = getStackInSlot(containerOutput);

        if (FluidContainerRegistry.isFilledContainer(inputStack))
        {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(inputStack);
            ItemStack result = inputStack.getItem().getContainerItem(inputStack);

            if (result != null && tank.fill(fluidStack, false) >= fluidStack.amount && (outputStack == null || result.isItemEqual(outputStack)))
            {
                tank.fill(fluidStack, true);
                decrStackSize(containerInput, 1);
                incrStackSize(containerOutput, result);
            }
        }
        else if (FluidContainerRegistry.isEmptyContainer(inputStack))
        {
            FluidStack avaliable = tank.getFluid();

            if (avaliable != null)
            {
                ItemStack result = FluidContainerRegistry.fillFluidContainer(avaliable, inputStack);
                FluidStack filled = FluidContainerRegistry.getFluidForFilledItem(result);

                if (result != null && filled != null && (outputStack == null || result.isItemEqual(outputStack)))
                {
                    decrStackSize(containerInput, 1);
                    incrStackSize(containerOutput, result);
                    tank.drain(filled.amount, true);
                }
            }
        }
    }

    public abstract FluidTank getInputTank();

    public abstract FluidTank getOutputTank();
}
