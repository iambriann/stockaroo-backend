package com.stockaroo.asxnewsapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "articles")
public class Article {

    public Article(Instant detectedTimestamp, String source, String title, String desc, Instant publishedTimestamp, String link) {
        this.detectedTimestamp = detectedTimestamp;
        this.source = source;
        this.title = title;
        this.desc = desc;
        this.publishedTimestamp = publishedTimestamp;
        this.link = link;
    }

    @Id
    private String id;

    @Indexed(direction = IndexDirection.DESCENDING)
    private Instant detectedTimestamp;
    private String source;
    private String title;
    private String desc;
    private Instant publishedTimestamp;
    private String link;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getDetectedTimestamp() {
        return detectedTimestamp;
    }

    public void setDetectedTimestamp(Instant detectedTimestamp) {
        this.detectedTimestamp = detectedTimestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Instant getPublishedTimestamp() {
        return publishedTimestamp;
    }

    public void setPublishedTimestamp(Instant publishedTimestamp) {
        this.publishedTimestamp = publishedTimestamp;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
