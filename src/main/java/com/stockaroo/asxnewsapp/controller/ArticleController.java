package com.stockaroo.asxnewsapp.controller;

import com.stockaroo.asxnewsapp.model.Article;
import com.stockaroo.asxnewsapp.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@RestController
@RequestMapping("/")
public class ArticleController {

    @Value("${CORS_ORIGIN_URL}")
    private String corsOriginUrl;

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @CrossOrigin(origins = "${CORS_ORIGIN_URL}")
    @GetMapping("/latest")
    public List<Article> getLatestArticles() {
        return articleService.getLatestArticles();
    }

}
