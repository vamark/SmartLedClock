package dev.vagvolgyi.smartledclock.background.weather;

public enum Trend {
    UP("up"),
    DOWN("down"),
    STABLE("stable");

    private final String value;

    Trend(String value) {
        this.value = value;
    }

    public static Trend fromString(String value) {
        for(Trend trend : values()) {
            if(trend.value.equals(value)) {
                return trend;
            }
        }
        return null;
    }
}
