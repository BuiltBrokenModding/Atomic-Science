package com.builtbroken.atomic.api;

import com.builtbroken.atomic.api.effect.IIndirectEffectType;
import com.builtbroken.atomic.api.radiation.IRadiationExposureSystem;
import com.builtbroken.atomic.api.radiation.IRadioactiveMaterialSystem;
import com.builtbroken.atomic.api.thermal.IThermalSystem;

/**
 * Reference object for Atomic Science API
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/13/2017.
 */
public final class AtomicScienceAPI
{
    /** Generic radiation damage type */
    public static IIndirectEffectType RADIATION;

    //DO NOT override these, the option is there but its not used that way
    //  these are meant to exposure access to the system. A lot of internal
    //  code does not use the API directly. Meaning replacing these will
    //  result in mixed logic of some stuff changing while others don't.
    /** System used to access radiation exposure information */
    public static IRadiationExposureSystem radiationExposureSystem;
    /** System used to access radioactive material data on the map */
    public static IRadioactiveMaterialSystem radioactiveMaterialSystem;
    /** System used to access thermal data on the map */
    public static IThermalSystem thermalSystem;
}
