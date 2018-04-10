package mapapp.cs4985.westga.edu.untitled;

public class Entry {

    private String name;
    private String shortForecast;
    private String detailedForecast;
    private String temperature;
    private String imageResource;


    public Entry(String name, String shortForescast, String detailedForcast, String temperature, String imageResource) {
        this.name = name;
        this.shortForecast = shortForescast;
        this.detailedForecast = detailedForcast;
        this.temperature = temperature;
        this.imageResource = imageResource;
    }

    public String getName(){ return this.name; }

    public String getShortForecast(){ return this.shortForecast; }

    public String getDetailedForecast() { return this.detailedForecast; }

    public String getTemperature() { return this.temperature; }

    public String getImageResource() { return this.imageResource; }
}

