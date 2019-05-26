package com.builtbroken.atomic.content.machines.accelerator.detector;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.prefab.BlockMachine;
import com.builtbroken.atomic.content.prefab.BlockPrefab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2019.
 */
public class BlockParticleDetector extends BlockMachine
{
    public BlockParticleDetector()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "particle_detector");
        setTranslationKey(AtomicScience.PREFIX + "particle.detector");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing sideClicked, float hitX, float hitY, float hitZ)
    {
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityParticleDetector)
        {
            if (playerIn.getHeldItem(hand).getItem() == Items.STICK)
            {
                final TileEntityParticleDetector detector = (TileEntityParticleDetector) tileEntity;
                final IAcceleratorNode node = detector.node;
                if (node != null)
                {
                    TubeSide side = TubeSide.getSideFacingOut(node.getDirection(), sideClicked);
                    if(node.getConnectionType().outputSides.contains(side))
                    {
                        float speed = 0;
                        if(detector.speedSettings.containsKey(side))
                        {
                            speed = detector.speedSettings.get(side);
                        }
                        detector.speedSettings.put(side, speed + 0.1f);
                        playerIn.sendMessage(new TextComponentString(side + " " + speed));
                    }
                }
            }
            else if (!world.isRemote)
            {
                playerIn.sendMessage(new TextComponentString("Tube: " + ((TileEntityParticleDetector) tileEntity).node));
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityParticleDetector();
    }
}
