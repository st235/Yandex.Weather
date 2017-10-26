package suhockii.dev.weather.utils;

/**
 * Created by Maksim Sukhotski on 8/10/2017.
 */

public class Settings {

    private final int temp;
    private final int speed;
    private final int pressure;

    public Settings(int temp, int speed, int pressure) {
        this.temp = temp;
        this.speed = speed;
        this.pressure = pressure;
    }

    public int getTemp() {
        return temp;
    }

    public int getSpeed() {
        return speed;
    }

    public int getPressure() {
        return pressure;
    }
}
