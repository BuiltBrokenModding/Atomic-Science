package resonantinduction.atomic.fission.reactor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import resonant.lib.path.IPathCallBack;
import resonant.lib.path.Pathfinder;
import resonant.lib.prefab.tile.TileAdvanced;
import universalelectricity.api.vector.Vector3;

/** Reactor Drain
 * 
 * @author Calclavia */
public class TileReactorDrain extends TileAdvanced implements IFluidHandler
{
    private final Set<IFluidTank> tanks = new HashSet<IFluidTank>();
    private long lastFindTime = -1;

    public void find()
    {
        this.tanks.clear();
        final World world = this.worldObj;
        final Vector3 position = new Vector3(this);

        Pathfinder finder = new Pathfinder(new IPathCallBack()
        {
            @Override
            public Set<Vector3> getConnectedNodes(Pathfinder finder, Vector3 currentNode)
            {
                Set<Vector3> neighbors = new HashSet<Vector3>();

                for (int i = 0; i < 6; i++)
                {
                    ForgeDirection direction = ForgeDirection.getOrientation(i);
                    Vector3 position = currentNode.clone().translate(direction);
                    int connectedBlockID = position.getBlockID(world);

                    if (connectedBlockID == 0 || Block.blocksList[connectedBlockID] instanceof BlockFluid || Block.blocksList[connectedBlockID] instanceof IFluidBlock || position.getTileEntity(world) instanceof TileReactorCell)
                    {
                        neighbors.add(position);
                    }
                }

                return neighbors;
            }

            @Override
            public boolean onSearch(Pathfinder finder, Vector3 start, Vector3 node)
            {
                if (node.getTileEntity(world) instanceof TileReactorCell)
                {
                    finder.results.add(node);
                }

                if (node.distance(position) > 6)
                {
                    return true;
                }

                return false;
            }
        }).init(new Vector3(this).translate(ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite()));

        for (Vector3 node : finder.results)
        {
            TileEntity tileEntity = node.getTileEntity(this.worldObj);

            if (tileEntity instanceof TileReactorCell)
            {
                this.tanks.add(((TileReactorCell) tileEntity).tank);
            }
        }

        this.lastFindTime = this.worldObj.getWorldTime();
    }

    public IFluidTank getOptimalTank()
    {
        if (this.lastFindTime == -1 || this.worldObj.getWorldTime() - this.lastFindTime > 20)
        {
            this.find();
        }

        if (this.tanks.size() > 0)
        {
            IFluidTank optimalTank = null;

            for (IFluidTank tank : this.tanks)
            {
                if (tank != null)
                {
                    if (optimalTank == null || (optimalTank != null && getFluidSafe(tank.getFluid()) > getFluidSafe(optimalTank.getFluid())))
                    {
                        optimalTank = tank;
                    }
                }
            }

            return optimalTank;
        }

        return null;
    }

    public int getFluidSafe(FluidStack stack)
    {
        if (stack != null)
        {
            return stack.amount;
        }

        return 0;
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (this.getOptimalTank() != null)
        {
            return this.getOptimalTank().drain(maxDrain, doDrain);
        }

        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        List<FluidTankInfo> tankInfoList = new ArrayList<FluidTankInfo>();

        this.getOptimalTank();
        for (IFluidTank tank : this.tanks)
        {
            tankInfoList.add(tank.getInfo());
        }

        return tankInfoList.toArray(new FluidTankInfo[0]);
    }
}
