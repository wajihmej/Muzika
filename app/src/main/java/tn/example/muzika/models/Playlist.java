package tn.example.muzika.models;

public class Playlist {

    private String href;
    private String name;
    private String description;
    private String imagePlaylist;

    public Playlist(){}
    public Playlist(String href,String name,String description,String imagePlaylist){
        this.href=href;
        this.name=name;
        this.description=description;
        this.imagePlaylist=imagePlaylist;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePlaylist() {
        return imagePlaylist;
    }

    public void setImagePlaylist(String imagePlaylist) {
        this.imagePlaylist = imagePlaylist;
    }
}
