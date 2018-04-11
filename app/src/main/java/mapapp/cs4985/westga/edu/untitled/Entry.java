package mapapp.cs4985.westga.edu.untitled;

public class Entry {

    private String name;
    private String shortForecast;
    private String detailedForecast;
    private String temperature;
    private String imageResource;
    private double lat;
    private double lon;
    private double rating;
    private Boolean isOpen;


    public Entry(String name, String shortForescast, String detailedForcast, String temperature, String imageResource) {
        this.name = name;
        this.shortForecast = shortForescast;
        this.detailedForecast = detailedForcast;
        this.temperature = temperature;
        this.imageResource = imageResource;
    }

    public Entry(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;

    }

    public String getName(){ return this.name; }

    public double getLat(){ return this.lat; }

    public double getLon(){ return this.lon; }
}

