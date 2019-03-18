package com.builtbroken.atomic.content.machines.reactor.fission.core;

import com.builtbroken.atomic.content.ASBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.FastTESR;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/9/2019.
 */
public class FastTESRReactorCell extends FastTESR<TileEntityReactorCell>
{
    @Override
    public void renderTileEntityFast(TileEntityReactorCell te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
    {
        if (te._renderFuel)
        {
            //Setting up states
            if (ReactorStructureType.CORE.rodState == null)
            {
                ReactorStructureType.CORE.rodState = ASBlocks.blockReactorCell.getDefaultState().withProperty(BlockReactorCell.REACTOR_STRUCTURE_TYPE, ReactorStructureType.ROD);
                ReactorStructureType.CORE_TOP.rodState = ASBlocks.blockReactorCell.getDefaultState().withProperty(BlockReactorCell.REACTOR_STRUCTURE_TYPE, ReactorStructureType.ROD_TOP);
                ReactorStructureType.CORE_MIDDLE.rodState = ASBlocks.blockReactorCell.getDefaultState().withProperty(BlockReactorCell.REACTOR_STRUCTURE_TYPE, ReactorStructureType.ROD_MIDDLE);
                ReactorStructureType.CORE_BOTTOM.rodState = ASBlocks.blockReactorCell.getDefaultState().withProperty(BlockReactorCell.REACTOR_STRUCTURE_TYPE, ReactorStructureType.ROD_BOTTOM);
            }

            //Get model
            BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            IBakedModel model = dispatcher.getModelForState(te.getStructureType().rodState);

            //Render
            BlockPos pos = te.getPos();
            buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
            dispatcher.getBlockModelRenderer().renderModel(te.world(), model, te.getStructureType().rodState, pos, buffer, true);
        }
    }
}
