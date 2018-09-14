package com.builtbroken.atomic.content.items.cell;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASItems;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Clone of {@link ModelDynBucket} to be more customized towards the application of VE's bucket
 * Though a lot of the code is custom All credit goes to the orginal creator plus fry, lex, and anyone else.
 */
public class ModelFluidBucket implements IModel
{
    public static final ResourceLocation fluid_mask_texture = new ResourceLocation(AtomicScience.DOMAIN, "items/cell_fluid_mask");
    public static final ResourceLocation base_texture = new ResourceLocation(AtomicScience.DOMAIN, "items/cell_empty"); //TODO change out with texture for fluid and power cell

    // minimal Z offset to prevent depth-fighting
    private static final float NORTH_Z_FLUID = 7.498f / 16f;
    private static final float SOUTH_Z_FLUID = 8.502f / 16f;

    public static final IModel MODEL = new ModelFluidBucket();

    protected final Fluid fluid;
    protected final Item item;

    public ModelFluidBucket()
    {
        this(null, null);
    }

    public ModelFluidBucket(Fluid fluid, Item item)
    {
        this.fluid = fluid;
        this.item = item;
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

        //Add main textures
        builder.add(fluid_mask_texture);
        builder.add(base_texture); //TODO add powered cell

        //Add unique fluid cell textures
        builder.addAll(ASItems.itemFluidCell.supportedFluidToTexturePath.values());
        builder.addAll(ASItems.itemPoweredCell.supportedFluidToTexturePath.values());

        return builder.build();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);

        if (transformMap.isEmpty())
        {
            TRSRTransformation thirdperson = get(0, 3, 1, 0, 0, 0, 0.55f);
            TRSRTransformation firstperson = get(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f);
            ImmutableMap.Builder<TransformType, TRSRTransformation> builder = ImmutableMap.builder();
            builder.put(TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5f));
            builder.put(TransformType.HEAD, get(0, 13, 7, 0, 180, 0, 1));
            builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdperson);
            builder.put(TransformType.THIRD_PERSON_LEFT_HAND, leftify(thirdperson));
            builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, firstperson);
            builder.put(TransformType.FIRST_PERSON_LEFT_HAND, leftify(firstperson));
            transformMap = PerspectiveMapWrapper.getTransforms(new SimpleModelState(builder.build()));
        }

        // if the fluid is a gas wi manipulate the initial state to be rotated 180? to turn it upside down
        if (fluid != null && fluid.isGaseous())
        {
            state = new ModelStateComposition(state, TRSRTransformation.blockCenterToCorner(new TRSRTransformation(null, new Quat4f(0, 0, 1, 0), null, null)));
        }

        TRSRTransformation transform = state.apply(java.util.Optional.empty()).orElse(TRSRTransformation.identity());
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();


        TextureAtlasSprite fluidSprite = null;
        if (fluid != null)
        {
            if(item instanceof ItemFluidCell && ((ItemFluidCell) item).supportedFluidToTexturePath.containsKey(fluid))
            {
                ResourceLocation texture = ((ItemFluidCell) item).supportedFluidToTexturePath.get(fluid);
                fluidSprite = bakedTextureGetter.apply(texture);

                IBakedModel modelFluid = (new ItemLayerModel(ImmutableList.of(texture))).bake(state, format, bakedTextureGetter);
                builder.addAll(modelFluid.getQuads(null, null, 0));
            }
            else
            {
                // build base (insidest)
                IBakedModel modelCell = (new ItemLayerModel(ImmutableList.of(base_texture))).bake(state, format, bakedTextureGetter);
                builder.addAll(modelCell.getQuads(null, null, 0));

                //Build fluid texture
                fluidSprite = bakedTextureGetter.apply(fluid.getStill());
                if (fluidSprite != null)
                {
                    TextureAtlasSprite liquid = bakedTextureGetter.apply(fluid_mask_texture);
                    // build liquid layer (inside)
                    builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, NORTH_Z_FLUID, EnumFacing.NORTH, fluid.getColor()));
                    builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, SOUTH_Z_FLUID, EnumFacing.SOUTH, fluid.getColor())); //seems to be darker
                }
            }
        }
        //No fluid state
        else
        {
            // build base (insidest)
            IBakedModel modelCell = (new ItemLayerModel(ImmutableList.of(base_texture))).bake(state, format, bakedTextureGetter); //TODO get custom texture for empty
            builder.addAll(modelCell.getQuads(null, null, 0));
        }

        return new BakedFluidBucket(this, builder.build(), fluidSprite, format, Maps.immutableEnumMap(transformMap), Maps.newHashMap());
    }

    private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s)
    {
        return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
                new Vector3f(tx / 16, ty / 16, tz / 16),
                TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
                new Vector3f(s, s, s),
                null));
    }

    private static final TRSRTransformation flipX = new TRSRTransformation(null, null, new Vector3f(-1, 1, 1), null);

    private static TRSRTransformation leftify(TRSRTransformation transform)
    {
        return TRSRTransformation.blockCenterToCorner(flipX.compose(TRSRTransformation.blockCornerToCenter(transform)).compose(flipX));
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData)
    {
        String fluidName = customData.get("fluid");
        Fluid fluid = FluidRegistry.getFluid(fluidName);

        if (fluid == null)
        {
            fluid = this.fluid;
        }

        String itemName = customData.get("item");
        Item item = Item.REGISTRY.getObject(new ResourceLocation(itemName));

        // create new model with correct liquid
        return new ModelFluidBucket(fluid, item);
    }

    private static final class BakedDynBucketOverrideHandler extends ItemOverrideList
    {
        public static final BakedDynBucketOverrideHandler INSTANCE = new BakedDynBucketOverrideHandler();

        private BakedDynBucketOverrideHandler()
        {
            super(ImmutableList.<ItemOverride>of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
        {
            BakedFluidBucket model = (BakedFluidBucket) originalModel;

            String fluidName = "";

            if (stack.getItem() instanceof ItemFluidCell)
            {
                //Get fluid from container
                FluidStack fluidStack = ((ItemFluidCell) stack.getItem()).getFluid(stack);

                //Get fluid name for key

                if (fluidStack != null && fluidStack.getFluid() != null)
                {
                    fluidName = fluidStack.getFluid().getName();
                }
            }

            //Create key for cache
            String key = fluidName;

            //Populate cached value if it doesn't exist
            if (!model.cache.containsKey(key))
            {
                IModel parent = model.parent.process(ImmutableMap.of("fluid", fluidName, "item", stack.getItem().getRegistryName().toString()));
                Function<ResourceLocation, TextureAtlasSprite> textureGetter;
                textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
                {
                    public TextureAtlasSprite apply(ResourceLocation location)
                    {
                        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                    }
                };

                IBakedModel bakedModel = parent.bake(new SimpleModelState(model.transforms), model.format, textureGetter);
                model.cache.put(key, bakedModel);
                return bakedModel;
            }

            return model.cache.get(key);
        }
    }

    // the dynamic bucket is based on the empty bucket
    private static final class BakedFluidBucket implements IBakedModel
    {

        private final ModelFluidBucket parent;
        // FIXME: guava cache?
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final ImmutableMap<TransformType, TRSRTransformation> transforms;
        private final ImmutableList<BakedQuad> quads;
        private final TextureAtlasSprite particle;
        private final VertexFormat format;

        public BakedFluidBucket(ModelFluidBucket parent,
                                ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format, ImmutableMap<TransformType, TRSRTransformation> transforms,
                                Map<String, IBakedModel> cache)
        {
            this.quads = quads;
            this.particle = particle;
            this.format = format;
            this.parent = parent;
            this.transforms = transforms;
            this.cache = cache;
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return BakedDynBucketOverrideHandler.INSTANCE;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
        {
            return PerspectiveMapWrapper.handlePerspective(this, transforms, cameraTransformType);
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
        {
            if (side == null)
            {
                return quads;
            }
            return ImmutableList.of();
        }

        @Override
        public boolean isAmbientOcclusion()
        {
            return true;
        }

        @Override
        public boolean isGui3d()
        {
            return false;
        }

        @Override
        public boolean isBuiltInRenderer()
        {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture()
        {
            return particle;
        }
    }
}