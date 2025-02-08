package com.stockaroo.asxnewsapp.service;

import com.stockaroo.asxnewsapp.model.Article;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ArticleService {

    private final MongoTemplate mongoTemplate;

    public ArticleService (MongoTemplate mongoTemplate ) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Article> getLatestArticles() {
        Query query = new Query().limit(50);
        return mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "detectedTimestamp")), Article.class);
    }


    public List<Article> getArticles (Article article) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(article.getTitle()).and("source").is(article.getSource()));
        return mongoTemplate.find(query, Article.class);
    }

    public boolean saveArticleIfNotExists(Article article) {
        // Check if the title already exists by title

        List<Article> articles = getArticles(article);

        if (articles.size() > 0) {
            // Article already exists, do not insert
            System.out.println("Article already exists: " + article.getTitle());
            return false;
        }

        // Save the article if it doesn't exist
        mongoTemplate.save(article);
        System.out.println("Article saved: " + article.getTitle() + " " + article.getLink());
        return true;
    }
}
