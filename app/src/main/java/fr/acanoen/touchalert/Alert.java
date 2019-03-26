package fr.acanoen.touchalert;

public class Alert {

    private String name;
    private String date;
    private Integer image;

    public Alert() {
        //
    }

    public Alert(String name, String date, Integer image) {
        this.name = name;
        this.date = date;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Integer getImage() {
        return image;
    }
}