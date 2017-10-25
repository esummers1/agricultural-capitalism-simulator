package main;

public class WeatherBand {
    
    private double minValue;
    private String message;
    
    public WeatherBand(double minValue, String message) {
        this.minValue = minValue;
        this.message = message;
    }
    
    public double getMinValue() {
        return minValue;
    }
    
    public String getMessage() {
        return message;
    }
    
}
