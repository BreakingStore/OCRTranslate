package me.niotgg.download;

import javafx.scene.image.ImageView;

public class DownloadLink {

    private String link;
    private String name;
    private String languageName;


    public DownloadLink(String link, String name, String languageName) {
        this.link = link;
        this.name = name;
        this.languageName = languageName;
    }


    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getLanguageName() {
        return languageName;
    }
}
