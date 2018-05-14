package com.builtbroken.jlib.data.science.units;

/**
 * Units of measure for temperature
 *
 * @author Robert Seifert
 */
public enum TemperatureUnit
{
	Fahrenheit("Fahrenheit", "F", new ITempConversion()
	{
		@Override
		public float toKelvin(float temp)
		{
			return (float) ((temp + 459.67) * (5 / 9));
		}

		@Override
		public float fromKelvin(float temp)
		{
			return (float) ((temp * (9 / 5)) - 459.67);
		}

	}),
	Celsius("Celsius", "C", new ITempConversion()
	{
		@Override
		public float toKelvin(float temp)
		{
			return (float) (temp + 273.15);
		}

		@Override
		public float fromKelvin(float temp)
		{
			return (float) (temp - 273.15);
		}

	}),
	Rankine("Rankine", "R", new ITempConversion()
	{
		@Override
		public float toKelvin(float temp)
		{
			return (temp * (5 / 9));
		}

		@Override
		public float fromKelvin(float temp)
		{
			return temp * (9 / 5);
		}

	}),
	Kelvin("Degrees", "F", new ITempConversion()
	{
		@Override
		public float toKelvin(float temp)
		{
			return temp;
		}

		@Override
		public float fromKelvin(float temp)
		{
			return temp;
		}

	});

	public String name, symbol;
	public ITempConversion conversion;

	TemperatureUnit(String name, String symbol, ITempConversion conversion)
	{
		this.name = name;
		this.symbol = symbol;
		this.conversion = conversion;
	}

	public static float convert(TemperatureUnit a, TemperatureUnit b, float temperature)
	{
		temperature = a.conversion.toKelvin(temperature);
		return b.conversion.fromKelvin(temperature);
	}

	public float convert(TemperatureUnit unit, float temperature)
	{
		return TemperatureUnit.convert(this, unit, temperature);
	}

	public interface ITempConversion
	{
		float toKelvin(float temp);

		float fromKelvin(float temp);
	}
}
