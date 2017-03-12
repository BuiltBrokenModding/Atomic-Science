package resonantinduction.atomic.particle.fulmination;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import resonant.api.explosion.ExplosionEvent.DoExplosionEvent;
import resonant.api.explosion.IExplosion;
import resonant.lib.flag.FlagRegistry;
import resonant.lib.prefab.poison.PoisonRadiation;
import resonantinduction.atomic.Atomic;
import resonantinduction.atomic.base.ItemCell;
import resonantinduction.core.Reference;
import resonantinduction.core.ResonantInduction;
import resonantinduction.core.Settings;
import universalelectricity.api.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/* Antimatter Cell */
public class ItemAntimatter extends ItemCell
{
    private Icon iconGram;

    public ItemAntimatter(int itemID)
    {
        super(itemID);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "") + "_milligram");
        this.iconGram = iconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "") + "_gram");
    }

    @Override
    public Icon getIconFromDamage(int metadata)
    {
        if (metadata >= 1)
        {
            return this.iconGram;
        }
        else
        {
            return this.itemIcon;
        }
    }

    @Override
    public void getSubItems(int id, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(id, 1, 0));
        par3List.add(new ItemStack(id, 1, 1));
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        return 160;
    }

    @ForgeSubscribe
    public void baoZhaEvent(ItemExpireEvent evt)
    {
        if (evt.entityItem != null)
        {
            ItemStack itemStack = evt.entityItem.getEntityItem();

            if (itemStack != null)
            {
                if (itemStack.itemID == this.itemID)
                {
                    evt.entityItem.worldObj.playSoundEffect(evt.entityItem.posX, evt.entityItem.posY, evt.entityItem.posZ, Reference.PREFIX + "antimatter", 3f, 1f - evt.entityItem.worldObj.rand.nextFloat() * 0.3f);

                    if (!evt.entityItem.worldObj.isRemote)
                    {
                        if (!FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME).containsValue(evt.entityItem.worldObj, Atomic.BAN_ANTIMATTER_POWER, "true", new Vector3(evt.entityItem)))
                        {
                            IExplosion explosive = new BzFanWuSu(evt.entity.worldObj, evt.entityItem, evt.entityItem.posX, evt.entityItem.posY, evt.entityItem.posZ, 4, itemStack.getItemDamage());
                            MinecraftForge.EVENT_BUS.post(new DoExplosionEvent(evt.entityItem.worldObj, explosive));
                            evt.entityItem.worldObj.createExplosion(evt.entityItem, evt.entityItem.posX, evt.entityItem.posY, evt.entityItem.posZ, explosive.getRadius(), true);
                            ResonantInduction.LOGGER.fine("Antimatter cell detonated at: " + evt.entityItem.posX + ", " + evt.entityItem.posY + ", " + evt.entityItem.posZ);

                            final int radius = 20;
                            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(evt.entityItem.posX - radius, evt.entityItem.posY - radius, evt.entityItem.posZ - radius, evt.entityItem.posX + radius, evt.entityItem.posY + radius, evt.entityItem.posZ
                                    + radius);
                            List<EntityLiving> entitiesNearby = evt.entityItem.worldObj.getEntitiesWithinAABB(EntityLiving.class, bounds);

                            for (EntityLiving entity : entitiesNearby)
                            {
                                PoisonRadiation.INSTANCE.poisonEntity(new Vector3(entity), entity);
                            }
                        }
                    }
                }
            }
        }
    }

    public static class BzFanWuSu extends Explosion implements IExplosion
    {
        private int tier;

        public BzFanWuSu(World par1World, Entity par2Entity, double x, double y, double z, float size, int tier)
        {
            super(par1World, par2Entity, x, y, z, size + 2 * tier);
            this.tier = tier;
        }

        @Override
        public float getRadius()
        {
            return this.explosionSize;
        }

        @Override
        public long getEnergy()
        {
            return (long) ((2000000000000000L + (2000000000000000L * 9 * tier)) * Settings.fulminationOutputMultiplier);

        }

        @Override
        public void explode()
        {
            this.doExplosionA();
            this.doExplosionB(true);
        }
    }
}
