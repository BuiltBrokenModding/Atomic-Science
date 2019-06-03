package com.builtbroken.test.as.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-22.
 */
public class FakeWorldAccess implements IBlockAccess
{
    public final HashMap<BlockPos, TileEntity> tiles = new HashMap();
    public final HashMap<BlockPos, IBlockState> blocks = new HashMap();

    public void addTile(BlockPos pos, TileEntity tile)
    {
        tiles.put(pos, tile);
    }

    public void addBlock(BlockPos pos, IBlockState tile)
    {
        blocks.put(pos, tile);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos)
    {
        return tiles.get(pos);
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue)
    {
        return 0;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos)
    {
        return blocks.containsKey(pos) ? blocks.get(pos) : null; //TODO air
    }

    @Override
    public boolean isAirBlock(BlockPos pos)
    {
        IBlockState state = getBlockState(pos);
        if(state != null)
        {
            return state.getMaterial() == Material.AIR;
        }
        return false;
    }

    @Override
    public Biome getBiome(BlockPos pos)
    {
        return null;
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction)
    {
        return 0;
    }

    @Override
    public WorldType getWorldType()
    {
        return null;
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default)
    {
        return false;
    }
}
