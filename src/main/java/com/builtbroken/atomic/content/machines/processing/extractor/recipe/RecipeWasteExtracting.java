package com.builtbroken.atomic.content.machines.processing.extractor.recipe;

import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.content.recipes.chem.RecipeChemExtractor;
import com.builtbroken.atomic.content.recipes.loot.DustLootTable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/24/2018.
 */
public class RecipeWasteExtracting extends RecipeChemExtractor
{
    public RecipeWasteExtracting()
    {
        super(new ItemStack(ASItems.itemProcessingWaste), null, null, null);
    }

    @Override
    public boolean matches(TileEntityChemExtractor machine)
    {
        ItemStack stack = machine.getInventory().getStackInSlot(TileEntityChemExtractor.SLOT_ITEM_INPUT);
        return !stack.isEmpty() && ASItems.itemProcessingWaste == stack.getItem();
    }

    @Override
    @Nullable
    public List<ItemStack> getPossibleOutputs()
    {
        List<ItemStack> list = new ArrayList();
        list.add(new ItemStack(ASItems.itemToxicWaste, 1, 0));
        list.addAll(DustLootTable.INSTANCE.getPossibleItems());
        return list;
    }


    @Override
    @Nullable
    public ItemStack getOutput(@Nullable TileEntityChemExtractor machine)
    {
        if (machine != null)
        {
            if (machine.nextRandomOutput.isEmpty())
            {
                if (Math.random() > 0.2) //TODO switch over to progress bar/tank so output always contains toxic waste dust
                {
                    machine.nextRandomOutput = new ItemStack(ASItems.itemToxicWaste, 1, 0);
                }
                else
                {
                    machine.nextRandomOutput = DustLootTable.INSTANCE.getRandomItemStack();
                    if (machine.nextRandomOutput.isEmpty())
                    {
                        machine.nextRandomOutput = new ItemStack(ASItems.itemToxicWaste, 1, 0);
                    }
                }
            }
            return machine.nextRandomOutput;
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void doRecipe(TileEntityChemExtractor machine, IItemHandlerModifiable inventory)
    {
        super.doRecipe(machine, inventory);
        machine.nextRandomOutput = ItemStack.EMPTY;
    }
}
