# INFO
This log contains changes made to the project. Each entry contains changed made after the last version but before the number was changed. Any changes made after a number change are considered part of the next release. This is regardless if versions are still being released with that version number attached. 

If this is a problem, use exact build numbers to track changes. As each build logs the git-hash it was created from to better understand changes made.

# Versions
## 3.2.1
### Runtime Changes

### Development Changes

## 3.?.? to 3.2.0
Neglected to keep a good log/update version numbers so we are merging all changes between 3.1.1 and up into this one area.
### Runtime Changes
* Added: Thermal sensor
* Added: Colored hazmat suit
* Added: JEI support for hazmat suit basic dyes 
* Added: particle accelerator tube (not finished)
* Added: Power magnet for accelerator
* Added: test block for accelerator cannon

* Changed: Reactor Core to only show rod visually when inserted and not always.

* Fixed: Reactor rod not shutting off in some cases
* Fixed: Crash due to scala import
* Fixed: Crash for add info 
* Fixed: Issues with rod contueing to work after fully used

* Improved: Inventory handling for reactor cores
* Improved: Threading and performance

* Updated: textures for some items and blocks


### Development Changes
* Changed: License to MIT to allow a stronger open source ecosystem for the mod

## 3.1.1
### Runtime Changes
* Added: Uranium 234 (will act as a waste product for the time being)

* Improved: Radiation distance falloff equation to run in constant time. Should result in massive improvement in runtime of radiation thread.
* Improved: Tooltips

* Fixed: Fuel rod tooltip saying "Format Error:" in place of "Fuel:"

### Development Changes
* Added: Ore dictionary values for U234 (pelletUranium234), U235(pelletUranium235), U238 (pelletUranium238)
* Added: capability for radiation
* Added: capability for heat


## 3.1.0
### Runtime Changes
* Added: Radiation data for several vanilla materials and blocks
* Added: Thermal data for several vanilla materials and blocks
* Added: Redstone can connect support to reactor controller

* Fixed: More issues with data sources (reactor) not clear nodes when disabled

### Development Changes
* Added: Capability for radiation resistance
* Added: Regitry for block, block state, and materials for radiation resistance
* Added: Callback for thread complete to DataChange object
* Added: Callbask for thread complete to data source
* Added: State save method to data source to track changes via NBT
* Added: method to check for if the data source has changed to trigger thread update

* Changed: Sources no longer manually trigger thread updates. This is now done by an update loop in the server tick event.
* Changed: All sources to be tracked by a single object. 
* Changed: Sources to only be removed when dead
* Changed: Sources to remove data from map when invalid but not clear data

## 3.0.6 - 1.12
### Runtime Changes
* Ported: mod to 1.12 version of Minecraft reworking much of the code, recipes, etc

* Fixed: Many issues with port and some that existed before porting
* Fixed: rad overlay showing in F3 menu
* Fixed: Double slot render issues
* Fixed: Colorization issue with slots
* Fixed: Texture bleed over issues with GUI elements
* Fixed: Some issues with thermal and radiation grid not updating
* Fixed: Radiation source not being centered
* Fixed: Many issues with processing recipes
* Fixed: Ore-dictionary issues for Uranium Ore

* Replaced: Interger based thermal/radiation system with Node based system
* Replaced: Iron plate with steel plate in recipes

* Reworked: Thermal systems to use capability system for tiles
* Reworked: Radiation systems to use capability system for tiles
* Reworked: Thermal, Radiation, and Radioactive material data to use nodes
* Reworked: Thermal, Radiation, and Radioactive material systems to use the same node map

* Added: Industrialcraft 2 support
* Added: Buildcraft support
* Added: JEI support for recipes
* Added: cacti -> slime recipe in extractor
* Added: Shift click support to processing machines
* Added: new fluid textures by sukar42
* Added: Start of system radiation protect for any armor
* Added: in-game configs to most configs
* Added: config to disable radiation overlay
* Added: Recipe support for IC2 and ThermalExpansion
* Added: Reactor Pipe rod -> redirects inventroy connections
* Added: Reactor Pipe rod inventory -> single slot, 1 stack size hopper
* Added: Spent rods to creative tab for testing of recipes & logic
* Added: Bucket insertion support to processing machines
* Added: Inventory insertion support to processing machines for most items
* Added: Power usage to processing machines

* Implemented: Fully new models for machines by Morton
* Implemented: Forge Energy support in place of RF/UE support
* Implemented: Inventory ejection for reactor for spent rods
* Implemented: Inventory redirect for reactor stack controller
* Implemented: Basic functionality for controller

### Development Changes
* Added: Debug hot keys to test UIs
* Added: Timer object to make implementing machines easier

## 3.0.6
### Runtime Changes

* Fixed: Crash generating list of reactors for controller
* Fixed: Pathing of thermal system going outside of range

* Added: Cell render handling for controller

* Implemented: New Models for all machines by Morton

## 3.0.5
### Runtime Changes
* Added: fill + drain checks for centrifuge & extractor
* Added: potion effects for radiation exposure
* Added: Reset of radiation map every 5 mins to fix bad data (temp fix)
* Added: blocks for fluids
* Added: radiation cut off lower limit, sets radiation to zero when under values
* Added: early version of ISBR model render for steam funnel

* Reworked: radiation pathfinder and calculations.

* Changed: wrench shift check to use GuiScreen.isShiftKeyDown() to midigate possible crashes
* Changed: reator recipe to use fluid cell instead of powered cell
* Changed: water radiation negation from 10% to 15% by default
* Changed: radiation pathing to have a flat reduction when radiation falls below a limit 
        flat limit = (reduction * 1000)
        Ex: water -> 0.15
            0.15 * 1000 = 150mili-rem    

* Implemented: raytracing for radiation (allows blocking radiation from reactors with walls)

* Fixed: radiation adding in creative mode
* Fixed: radiation values not being the same on all sides of a sources
* Fixed: radiation values not pathing correctly
* Fixed: radiation not decaying through walls correctly
* Fixed: radiation not calculating from center of sources
* Fixed: radiation not calculating to center of blocks
* Fixed: rounding errors in radiation math
* Fixed: missing fluid textures

### Development Changes

## 3.0.4 - 6/1/2018
### Runtime Changes
* Added: Slot validation for centrifuge
* Added: Slot validation for extractor
* Added: Fluid wrench support to centrifuge
* Added: Fluid wrench support to extractor

* Added: death mechanic for radiation, entities will take radiation damage until dead.

* Changed: Radiation death point from 1000 to 10000

* Fixed: Slot validation for boiler
* Fixed: ThermalExpansion recipes loading without the mod
* Fixed: Map data clearing on player death (caused by game unloading chunks while moving the player)

### Development Changes

## 3.0.3 - 6/1/2018
### Runtime Changes
* Added: Wrench color orange
* Added: Orange color for energy slots
* Added: insert limits to boiler slots
* Added: colors to all processing slots
* Added: tooltips for all processing slots

* Added: wrench recipe
* Added: thermal expansion compatibiliy recipes

* Added: config to disable base recipes

* Changed: slot types for processing machines to match expected input

* Fixed: processing machine models being slightly in the ground causing z-fighting
* Fixed: NEI showing 16 turbines

### Development Changes
* Added: Orange to WrenchColor enum for output slots
* Added: ISlotToolTip
* Added: handling for ISlotToolTip to auto add tooltips to GUI from slots

* Added: override to disable base recipes
* Added: override to disable repacement items for recipes
* Added: helper methods for getting recipe items

## 3.0.2 - 5/30/2018
### Runtime Changes
* Added: Configs for content & features
* Added: Settings to disable radiation system
* Added: ISidedInventory support (hopper and pipe support) for reactor cores 
* Added: Mechanic to move lowest time rod to bottom of reactor stack
* Added: Damage display to fuel rods
* Added: Display info to fuel rods
* Added: particles to show machines runing
* Added: particles to show when machine finishes a recipe
* Added: particles to show when reactor is running
* Added: Gui edge colors to tanks and slots
* Added: wrench (adjustor) with:
         Modes: Rotation, items, fluids, redstone
         Colors: green, yellow, red, blue, purple
* Added: tooltip for tanks

* Improved: handling of entity item radiation tracking

* Fixed: GUI localizations
* Fixed: NPE when checking if can output fluids from processing machines
* Fixed: Fuel rods being usable beyond time limit (timer going negative)
* Fixed: Inventory stack size on reactor core (now 1, instead of 64)
* Fixed: Fluid cells not emptying into processing machines
* Fixed: Boiler recipe consuming 1mb waste instead of 1000mb

### Development Changes
* Added: check if a fluid or itemstack was supported by a recipe
* Added: call backs to recipe to allow items and fluids into processing machines

* Renamed: tank fields to reduce confusion

## 3.0.1
### Changes
Full rewrite not based on the code from 1.6.4, 1.5.2, or Resonant Induction versions

Following is a list of content created for the version. As this is an initial release of the rewrite. Which does not include all content as the focus was on a minimum viable product.

### New
* thermal system -> stores heat values
* thermal mechanics -> spreads heat via a thread
* radioactive material map -> stores radioactive material values
* radiation map -> stores radiation values
* radiation mechanics -> calculates radiation from sources
* radiation damage -> harms entities from radiation
    
*  Waste recovery mechanics -> all processing machines generate waste
    
* Contaminated Mineral Solution -> Byproduct of ore processing
* Contaminated Mineral Water -> Byproduct of hexafluoride processing
* Contaminated Water -> Byproduct of processing waste and output of reactor
* Reactor Waste -> Replaces toxic waste from old mod. Also output of reactor
    
* Toxic Mineral Waste -> byproduct of reducing toxic fluids into a solid. Contains some minerals as dust and can be refined into dust
* Toxic Waste -> byproduct of reducing toxic fluids into a solid
    
* Dust loot table -> used in the extractor for for Toxic Mineral Waste processing.
    
* Recipe objects -> all processing machines support adding new recipes
    
* Configs -> mod is heavily customizable with most options being exposed.

### Recreated
* Protective armor
* Fission reactor core
* Extractor + recipes
* Boiler + recipes
* Centrifuge + recipes
* Fuel rod
* Breeder Rod
* U235 Pellet
* U238 Pellet
* Heat probe
* Fluid Cells
* Yellocake
* Uranium Ore
* Turbines
* Steam Funnel
    
### Removed
* Old heat system -> used too much CPU
* Old events -> used too much CPU    
* Potion effects -> didn't allow for good gameplay and potion IDs are limited
* Reactor explosion -> will be replaced by a meltdown event and hydrogen explosions 
* Dependency on large lib -> mod is standalone to improve user experience
* Localizations -> part of rewrite, can't tell if they are valid
    
### Old content
* All models and textures are from the old mod.
* Models have been updated as .obj from .techne
* A few textures have been fixed
* Some textures have been added
    
### Changes
*  No old code is being used beyond a few interfaces from VoltzEngine (previously ResonantEngine which the mod used)
    
* All processing recipes produce waste
    
* Heat is no longer actively released from reactor. Being replace with a more static system that calculated heat, spreads, and then cached heat to reduce CPU usage.
    
* Radiation is no longer a potion effect. It is now handled via its own system.
    
* Radioactive blocks are no longer used. Radioactive material can now be applied to all blocks, items, and entities.
    
* Steam is no longer generated by events. Instead it is calculated based on thermal system.
    
* Turbines and steam funnel no longer use events to get steam. Values are calculated and cached based on map data.
    
* Fluid cells are no longer simple items. Any cell that used a fluid is now a fluid container. This allows any fluid to be stored.
    
* Dark-matter was replaced with strange matter. This was the original name of the item and matched better with current science understanding.
    
* Antimatter & Strange matter are now fluids. Both can be stored but are limited to powered fluid cells.
    
* Turbines will create a new tile per power system. This is to reduce complexity required to support several power systems on a single class.

