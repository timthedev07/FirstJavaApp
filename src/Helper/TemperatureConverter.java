package Helper;

public class TemperatureConverter {

    public static final String K = "K";
    public static final String C = "C";
    public static final String F = "F";
    public static final String RA = "Ra";
    public static final String RE = "Re";

    public TemperatureConverter(){

    }
    public static float convert(float temperature, String from, String to){
        if (from.equals(to)){
            return temperature;
        }

        if (from.equals(K)){
            if (to.equals(C)){
                return temperature - 273.15F;
            } else if (to.equals(F)){
                return temperature * 1.8F - 459.67F;
            } else if (to.equals(RA)){
                return temperature * 1.8F;
            } else if (to.equals(RE)){
                return (temperature - 273.15F) * 0.8F;
            } else {
                return (Float)null;
            }
        } else if (from.equals(F)){
            if (to.equals(C)){
                return (temperature - 32F) / 1.8F;
            } else if (to.equals(K)){
                return (temperature - 459.67F) / 1.8F;
            } else if (to.equals(RA)){
                return temperature + 459.67F;
            } else if (to.equals(RE)){
                return (temperature - 32F) * 4/9;
            } else {
                return (Float)null;
            }
        } else if (from.equals(C)){
            if (to.equals(F)){
                return temperature * 1.8F + 32F;
            } else if (to.equals(K)){
                return temperature + 273.15F;
            } else if (to.equals(RA)){
                return (temperature + 273.15F) * 1.8F;
            } else if (to.equals(RE)){
                return temperature * 0.8F;
            } else {
                return (Float)null;
            }
        } else if (from.equals(RA)){
            if (to.equals(C)){
                return (temperature - 491.67F) / 1.8F;
            } else if (to.equals(F)){
                return temperature - 459.67F;
            } else if (to.equals(K)){
                return temperature / 1.8F;
            } else if (to.equals(RE)){
                return (temperature - 491.67F) * 4/9;
            } else {
                return (Float)null;
            }
        } else if (from.equals(RE)){
            if (to.equals(C)){
                return temperature / 0.8F;
            } else if (to.equals(F)){
                return temperature * 9/4 + 32F;
            } else if (to.equals(K)){
                return temperature * 1.25F + 273.15F;
            } else if (to.equals(RA)){
                return temperature * 9/4 + 491.67F;
            } else {
                return (Float)null;
            }
        }
        return (Float)null;
    }
}
