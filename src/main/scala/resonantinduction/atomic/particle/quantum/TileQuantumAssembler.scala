package resonantinduction.atomic.particle.quantum

import resonantinduction.atomic
import resonant.api.recipe.QuantumAssemblerRecipes
import resonant.lib.network.IPacketReceiver
import com.google.common.io.ByteArrayDataInput
import cpw.mods.fml.common.network.PacketDispatcher
import cpw.mods.fml.common.network.Player
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.packet.Packet
import net.minecraftforge.common.ForgeDirection
import universalelectricity.api.electricity.IVoltageInput
import universalelectricity.api.energy.EnergyStorageHandler
import resonant.lib.content.prefab.TraitInventory
import net.minecraft.block.material.Material
import universalelectricity.api.vector.Vector3
import resonant.lib.prefab.tile.TileElectrical
import resonantinduction.atomic.Atomic
import resonantinduction.core.ResonantInduction
import resonantinduction.core.Reference

class TileQuantumAssembler extends TileElectrical(Material.iron) with TraitInventory with IPacketReceiver with IVoltageInput {
  val ENERGY: Long = 10000000000000L
  val MAX_TIME: Int = 20 * 120
  var time: Int = 0
  /**
   * Used for rendering.
   */
  var rotationYaw1: Float = 0
  var rotationYaw2: Float = 0
  var rotationYaw3: Float = 0
  /**
   * Used for rendering.
   */
  var entityItem: EntityItem = null

  energy = new EnergyStorageHandler(ENERGY)
  maxSlots = 6 + 1
  isOpaqueCube = false;
  normalRender = false;
  customItemRender = true;
  textureName = "machine"

  /**
   * Called when the block is right clicked by the player
   */
  override def use(player: EntityPlayer, side: Int, hit: Vector3): Boolean =
    {
      if (!world().isRemote) {
        player.openGui(Atomic.INSTANCE, 0, world, x, y, z)
        return true
      }

      return true
    }

  override def updateEntity {
    super.updateEntity
    if (!this.worldObj.isRemote) {
      if (this.canProcess) {
        if (energy.checkExtract) {
          if (this.time == 0) {
            this.time = this.MAX_TIME
          }
          if (this.time > 0) {
            this.time -= 1
            if (this.time < 1) {
              this.process
              this.time = 0
            }
          } else {
            this.time = 0
          }
          this.energy.extractEnergy(ENERGY, true)
        }
      } else {
        this.time = 0
      }
      if (this.ticks % 10 == 0) {
        import scala.collection.JavaConversions._
        for (player <- this.getPlayersUsing) {
          PacketDispatcher.sendPacketToPlayer(getDescriptionPacket, player.asInstanceOf[Player])
        }
      }
    } else if (this.time > 0) {
      if (this.ticks % 600 == 0) {
        this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, Reference.PREFIX + "assembler", 0.7f, 1f)
      }
      this.rotationYaw1 += 3
      this.rotationYaw2 += 2
      this.rotationYaw3 += 1
      var itemStack: ItemStack = this.getStackInSlot(6)
      if (itemStack != null) {
        itemStack = itemStack.copy
        itemStack.stackSize = 1
        if (this.entityItem == null) {
          this.entityItem = new EntityItem(this.worldObj, 0, 0, 0, itemStack)
        } else if (!itemStack.isItemEqual(this.entityItem.getEntityItem)) {
          this.entityItem = new EntityItem(this.worldObj, 0, 0, 0, itemStack)
        }
        this.entityItem.age += 1
      } else {
        this.entityItem = null
      }
    }
  }

  override def onReceiveEnergy(from: ForgeDirection, receive: Long, doReceive: Boolean): Long =
    {
      if (this.canProcess) {
        return super.onReceiveEnergy(from, receive, doReceive)
      }
      return 0
    }

  def onReceivePacket(data: ByteArrayDataInput, player: EntityPlayer, extra: AnyRef*) {
    try {
      this.time = data.readInt
      val itemID: Int = data.readInt
      val itemAmount: Int = data.readInt
      val itemMeta: Int = data.readInt
      if (itemID != -1 && itemAmount != -1 && itemMeta != -1) {
        this.setInventorySlotContents(6, new ItemStack(Item.itemsList(itemID), itemAmount, itemMeta))
      }
    } catch {
      case e: Exception =>
        {
          e.printStackTrace
        }
    }
  }

  override def getDescriptionPacket: Packet =
    {
      if (this.getStackInSlot(6) != null) {
        return ResonantInduction.PACKET_TILE.getPacket(this, Int.box(time), Int.box(getStackInSlot(6).itemID), Int.box(getStackInSlot(6).stackSize), Int.box(getStackInSlot(6).getItemDamage))
      }
      return ResonantInduction.PACKET_TILE.getPacket(this, Int.box(time), Int.box(-1), Int.box(-1), Int.box(-1))
    }

  override def openChest {
    if (!this.worldObj.isRemote) {
      import scala.collection.JavaConversions._
      for (player <- this.getPlayersUsing) {
        PacketDispatcher.sendPacketToPlayer(getDescriptionPacket, player.asInstanceOf[Player])
      }
    }
  }

  def canProcess: Boolean =
    {
      if (getStackInSlot(6) != null) {
        if (QuantumAssemblerRecipes.hasItemStack(getStackInSlot(6))) {
          {
            var i: Int = 0
            while (i < 6) {
              {
                if (getStackInSlot(i) == null) {
                  return false
                }
                if (getStackInSlot(i).itemID != Atomic.itemDarkMatter.itemID) {
                  return false
                }
              }
              ({
                i += 1;
                i - 1
              })
            }
          }
          return getStackInSlot(6).stackSize < 64
        }
      }
      return false
    }

  /**
   * Turn one item from the furnace source stack into the appropriate smelted item in the furnace
   * result stack
   */
  def process {
    if (this.canProcess) {
      {
        var i: Int = 0
        while (i < 5) {
          {
            if (getStackInSlot(i) != null) {
              decrStackSize(i, 1)
            }
          }
          ({
            i += 1;
            i - 1
          })
        }
      }
      if (getStackInSlot(6) != null) {
        getStackInSlot(6).stackSize += 1
      }
    }
  }

  /**
   * Reads a tile entity from NBT.
   */
  override def readFromNBT(par1NBTTagCompound: NBTTagCompound) {
    super.readFromNBT(par1NBTTagCompound)
    this.time = par1NBTTagCompound.getInteger("smeltingTicks")
  }

  /**
   * Writes a tile entity to NBT.
   */
  override def writeToNBT(par1NBTTagCompound: NBTTagCompound) {
    super.writeToNBT(par1NBTTagCompound)
    par1NBTTagCompound.setInteger("smeltingTicks", this.time)
  }

  override def isItemValidForSlot(slotID: Int, itemStack: ItemStack): Boolean =
    {
      if (slotID == 6) {
        return true
      }
      return itemStack.itemID == Atomic.itemDarkMatter.itemID
    }

  def getVoltageInput(from: ForgeDirection): Long =
    {
      return 1000
    }

  def onWrongVoltage(direction: ForgeDirection, voltage: Long) {
  }

}