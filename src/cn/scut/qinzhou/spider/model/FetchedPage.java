package cn.scut.qinzhou.spider.model;

public class FetchedPage {
    private String url;
    private Integer level;
    private String content;
    private int statusCode;

    public FetchedPage() {

    }

    public FetchedPage(urlStruct url_level, String content, int statusCode) {
        this.url = url_level.url;
        this.level = url_level.level;
        this.content = content;
        this.statusCode = statusCode;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public Integer getLevel(){ return level;}
    private void  setLevel(Integer level){ this.level = level;}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
