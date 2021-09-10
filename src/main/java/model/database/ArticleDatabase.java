package model.database;

import model.get_article_behavior.WebsiteURL;
import model.scrapping_engine.URLCrawler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArticleDatabase implements Runnable {

    private final PropertyChangeSupport propertyChangeSupport;
    private final ScheduledExecutorService executor;
    private final HashSet<String> articlesCheck;
    private final List<Article> scrapeList;
    private final List<Article> articles;

    public ArticleDatabase() {
        executor = Executors.newSingleThreadScheduledExecutor();
        propertyChangeSupport = new PropertyChangeSupport(this);
        articlesCheck = new HashSet<>();
        scrapeList = new ArrayList<>();
        articles = new ArrayList<>();
    }

    @Override
    public void run() {
        boolean firstLoad = true;
        System.out.println("Start execution");
        executor.scheduleAtFixedRate(() -> {
            try {
                scrapeList.clear();
                if (articlesCheck.size() >= 2000) {
                    articlesCheck.clear();
                }
//                articlesCheck.clear();
                // Perform scraping new articles
                long start = System.currentTimeMillis();
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(new URLCrawler(WebsiteURL.VNEXPRESS.getUrl() + "rss", scrapeList));
                executorService.execute(new URLCrawler(WebsiteURL.TUOITRE.getUrl(), scrapeList));
                executorService.execute(new URLCrawler(WebsiteURL.THANHNIEN.getUrl() + "rss.html", scrapeList));
                executorService.execute(new URLCrawler(WebsiteURL.NHANDAN.getUrl(), scrapeList));
                executorService.execute(new URLCrawler(WebsiteURL.ZINGNEWS.getUrl(), scrapeList));
                executorService.shutdown();
                executorService.awaitTermination(12, TimeUnit.SECONDS);

                List<Article> tmpList = new ArrayList<>();
                for (int i = 0; i < scrapeList.size(); i++) {
                    String tmp = scrapeList.get(i).getTitlePage() + " " + scrapeList.get(i).getSource().getUrl();
                    if (!articlesCheck.contains(tmp)) {
                        articlesCheck.add(tmp);
                        tmpList.add(scrapeList.get(i));
                    }
                }
                if (articles.size() >= 2000) {
                    articles.clear();
                }
//                articles.clear();
                articles.addAll(tmpList);
                articles.sort(Comparator.comparing(Article::getDuration).reversed());

                long end = System.currentTimeMillis();
                long elapsed = end - start;

                System.out.println("Article size: " + articles.size());
                System.out.println("Scraping took: " + (elapsed / 1000) + " seconds");
                doNotify(true);
            } catch (Exception e) {
                System.out.println("Cannot perform background scraping");
                doNotify(false);
            }
        }, 0, 10_000, TimeUnit.MILLISECONDS);
    }
    public void end() {
        executor.shutdown();
        executor.shutdownNow();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void doNotify(boolean b) {
        System.out.println("Notify");
        propertyChangeSupport.firePropertyChange("updateScrapeDone", null, b);
    }

    public List<Article> getArticles() {
        return articles;
    }
}
