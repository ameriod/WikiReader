package com.nordeck.wiki.reader.ui.viewer;

import android.support.annotation.NonNull;

import com.nordeck.wiki.reader.SelectedWiki;
import com.nordeck.wiki.reader.api.ArticleService;
import com.nordeck.wiki.reader.api.RelatedArticleService;
import com.nordeck.wiki.reader.model.ArticleResponse;
import com.nordeck.wiki.reader.model.ISection;
import com.nordeck.wiki.reader.model.RelatedResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by parker on 2/27/16.
 */
public class ArticleViewInteractor implements ArticleViewerContract.Interactor {

    @NonNull
    private ArticleService service;
    @NonNull
    private RelatedArticleService relatedArticleService;

    public ArticleViewInteractor() {
        String url = SelectedWiki.getInstance().getSelectedWiki().getUrl();
        this.service = new ArticleService(url);
        this.relatedArticleService = new RelatedArticleService(url);
    }


    @Override
    public Observable<ArticleResponse> fetchRandomArticle() {
        return service.getRandomArticle();

    }

    @Override
    public Observable<List<ISection>> fetchArticle(@NonNull String id) {
        return service.getArticle(id)
                .map(new Func1<ArticleResponse, List<ISection>>() {
                    @Override
                    public List<ISection> call(ArticleResponse articleResponse) {
                        return new ArrayList<ISection>(articleResponse.getSections());
                    }
                });
    }

    @Override
    public Observable<RelatedResponse> fetchRelatedArticles(@NonNull String id) {
        return relatedArticleService.getRelatedPages(id);
    }
}
