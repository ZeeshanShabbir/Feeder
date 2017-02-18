package io.droidninja.feeder.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zeeshan on 2/15/17.
 */

public class FeedsDTO {
    @Expose
    @SerializedName("status")
    String status;
    @Expose
    @SerializedName("source")
    String source;
    @Expose
    @SerializedName("sortBy")
    String sortBy;
    @Expose
    @SerializedName("articles")
    List<ArticleDTO> article = new ArrayList<ArticleDTO>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<ArticleDTO> getArticle() {
        return article;
    }

    public void setArticle(List<ArticleDTO> article) {
        this.article = article;
    }
}
