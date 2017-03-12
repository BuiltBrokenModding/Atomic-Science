package resonantinduction.atomic.process.sensor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeDirection;
import resonant.lib.content.module.TileBlock;
import resonantinduction.core.Reference;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.vector.Vector3;

/** Siren block */
public class TileSiren extends TileBlock
{
    public TileSiren()
    {
        super(UniversalElectricity.machine);
    }

    @Override
    public void onWorldJoin()
    {
        scheduelTick(1);
    }

    @Override
    public void onNeighborChanged()
    {
        scheduelTick(1);
    }

    @Override
    public void updateEntity()
    {
        if (worldObj == null)
        {
            return;
        }

        int metadata = worldObj.getBlockMetadata(x(), y(), z());

        if (worldObj.getBlockPowerInput(x(), y(), z()) > 0)
        {
            float volume = 0.5f;

            for (int i = 0; i < 6; i++)
            {
                Vector3 check = position().translate(ForgeDirection.getOrientation(i));
                int blockID = check.getBlockID(worldObj);

                if (blockID == blockID())
                {
                    volume *= 1.5f;
                }
            }

            worldObj.playSoundEffect(x(), y(), z(), Reference.PREFIX + "alarm", volume, 1f - 0.18f * (metadata / 15f));
            scheduelTick(30);
        }
    }

    @Override
    protected boolean configure(EntityPlayer player, int side, Vector3 hit)
    {
        int metadata = world().getBlockMetadata(x(), y(), z());

        if (player.isSneaking())
        {
            metadata -= 1;
        }
        else
        {
            metadata += 1;
        }

        metadata = Math.max(metadata % 16, 0);

        world().setBlockMetadataWithNotify(x(), y(), z(), metadata, 2);
        return true;
    }
}
