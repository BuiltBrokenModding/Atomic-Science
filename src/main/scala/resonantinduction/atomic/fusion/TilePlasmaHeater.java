package resonantinduction.atomic.fusion;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import resonant.api.ITagRender;
import resonant.lib.config.Config;
import resonant.lib.network.IPacketReceiver;
import resonant.lib.network.PacketHandler;
import resonant.lib.prefab.tile.TileElectrical;
import resonant.lib.utility.LanguageUtility;
import resonantinduction.atomic.Atomic;
import resonantinduction.core.ResonantInduction;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;
import universalelectricity.api.vector.Vector3;

import com.google.common.io.ByteArrayDataInput;

public class TilePlasmaHeater extends TileElectrical implements IPacketReceiver, ITagRender, IFluidHandler
{
    public static final long DIAN = 10000000000L;

    @Config
    public static final int plasmaHeatAmount = 100;

    public final FluidTank tankInputDeuterium = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
    public final FluidTank tankInputTritium = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
    public final FluidTank tankOutput = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);

    public float rotation = 0;

    public TilePlasmaHeater()
    {
        energy = new EnergyStorageHandler(DIAN, DIAN / 20);
    }

    @Override
    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        if (tankInputDeuterium.getFluidAmount() > 0 && tankInputTritium.getFluidAmount() > 0)
        {
            return super.onReceiveEnergy(from, receive, doReceive);
        }

        return 0;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        rotation += energy.getEnergy() / 10000f;

        if (!worldObj.isRemote)
        {
            if (energy.checkExtract())
            {
                if (tankInputDeuterium.getFluidAmount() >= plasmaHeatAmount && tankInputTritium.getFluidAmount() >= plasmaHeatAmount)
                {
                    tankInputDeuterium.drain(plasmaHeatAmount, true);
                    tankInputTritium.drain(plasmaHeatAmount, true);
                    tankOutput.fill(new FluidStack(Atomic.FLUID_PLASMA, plasmaHeatAmount), true);
                    energy.extractEnergy();
                }
            }
        }

        if (ticks % 80 == 0)
        {
            PacketHandler.sendPacketToClients(getDescriptionPacket(), worldObj, new Vector3(this), 25);
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return ResonantInduction.PACKET_TILE.getPacket(this, nbt);
    }

    @Override
    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        try
        {
            readFromNBT(PacketHandler.readNBTTagCompound(data));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /** Reads a tile entity from NBT. */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        NBTTagCompound deuterium = nbt.getCompoundTag("tankInputDeuterium");
        tankInputDeuterium.setFluid(FluidStack.loadFluidStackFromNBT(deuterium));
        NBTTagCompound tritium = nbt.getCompoundTag("tankInputTritium");
        tankInputTritium.setFluid(FluidStack.loadFluidStackFromNBT(tritium));
        NBTTagCompound output = nbt.getCompoundTag("tankOutput");
        tankOutput.setFluid(FluidStack.loadFluidStackFromNBT(output));
    }

    /** Writes a tile entity to NBT. */
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        if (tankInputDeuterium.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            tankInputDeuterium.getFluid().writeToNBT(compound);
            nbt.setTag("tankInputDeuterium", compound);
        }
        if (tankInputTritium.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            tankInputTritium.getFluid().writeToNBT(compound);
            nbt.setTag("tankInputTritium", compound);
        }
        if (tankOutput.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            tankOutput.getFluid().writeToNBT(compound);
            nbt.setTag("tankOutput", compound);
        }
    }

    @Override
    public float addInformation(HashMap<String, Integer> map, EntityPlayer player)
    {
        if (energy != null)
        {
            map.put(LanguageUtility.getLocal("tooltip.energy") + ": " + UnitDisplay.getDisplay(energy.getEnergy(), Unit.JOULES), 0xFFFFFF);
        }

        if (tankInputDeuterium.getFluidAmount() > 0)
        {
            map.put(LanguageUtility.getLocal("fluid.deuterium") + ": " + tankInputDeuterium.getFluidAmount() + " L", 0xFFFFFF);
        }

        if (tankInputTritium.getFluidAmount() > 0)
        {
            map.put(LanguageUtility.getLocal("fluid.tritium") + ": " + tankInputTritium.getFluidAmount() + " L", 0xFFFFFF);
        }

        if (tankOutput.getFluidAmount() > 0)
        {
            map.put(LanguageUtility.getLocal("fluid.plasma") + ": " + tankOutput.getFluidAmount() + " L", 0xFFFFFF);
        }

        return 1.5f;
    }

    @Override
    public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract)
    {
        return 0;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (resource.isFluidEqual(Atomic.FLUIDSTACK_DEUTERIUM))
        {
            return tankInputDeuterium.fill(resource, doFill);
        }

        if (resource.isFluidEqual(Atomic.FLUIDSTACK_TRITIUM))
        {
            return tankInputTritium.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return tankOutput.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return fluid.getID() == Atomic.FLUID_DEUTERIUM.getID() || fluid.getID() == Atomic.FLUID_TRITIUM.getID();
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return fluid == Atomic.FLUID_PLASMA;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[]
        { tankInputDeuterium.getInfo(), tankInputTritium.getInfo(), tankOutput.getInfo() };
    }

}
