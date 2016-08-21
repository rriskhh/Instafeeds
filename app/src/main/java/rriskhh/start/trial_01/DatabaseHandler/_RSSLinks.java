package rriskhh.start.trial_01.DatabaseHandler;

import java.util.HashMap;

/**
 * Created by rriskhh on 17/08/16.
 */
public class _RSSLinks {
    private static HashMap<String, HashMap<String,String>> _links = new HashMap<String, HashMap<String, String>>();
    private static StringBuilder KEY_LINK = new StringBuilder();

    // Default Constructor
    public _RSSLinks(){
        if(_links.size() == 0)
            sendData();
    }

    // Save all the links
    private void sendData(){
        populateList("NDTV","http://feeds.feedburner.com/ndtvnews-world-news.xml",
                "http://feeds.feedburner.com/ndtvprofit-latest.xml",
                "http://feeds.feedburner.com/ndtvsports-latest.xml",
                "http://feeds.feedburner.com/gadgets360-latest.xml",
                "http://feeds.feedburner.com/ndtvcooks-latest.xml",
                "http://feeds.feedburner.com/ndtvmovies-latest.xml");

        populateList("BBC", "http://feeds.bbci.co.uk/news/world/rss.xml",
                "http://feeds.bbci.co.uk/news/business/rss.xml",
                "http://feeds.bbci.co.uk/sport/rss.xml",
                "http://feeds.bbci.co.uk/news/technology/rss.xml",
                "http://feeds.bbci.co.uk/news/health/rss.xml",
                "http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml"
        );

        populateList("Reuters", "http://feeds.reuters.com/reuters/INworldNews.xml",
                "http://feeds.reuters.com/reuters/INbusinessNews.xml",
                "http://feeds.reuters.com/reuters/INsportsNews.xml",
                "http://feeds.reuters.com/reuters/INtechnologyNews.xml",
                "http://feeds.reuters.com/reuters/INhealth.xml",
                "http://feeds.reuters.com/reuters/INentertainmentNews.xml");

        populateList("CNN", "http://rss.cnn.com/rss/edition_world.xml",
                "http://rss.cnn.com/rss/money_news_international.xml",
                "http://rss.cnn.com/rss/edition_sport.xml",
                "http://rss.cnn.com/rss/edition_technology.xml",
                null,
                "http://rss.cnn.com/rss/edition_entertainment.xml");

        populateList("Sky", "http://feeds.skynews.com/feeds/rss/world.xml",
                "http://feeds.skynews.com/feeds/rss/business.xml",
                null,
                "http://feeds.skynews.com/feeds/rss/technology.xml",
                null,
                "http://feeds.skynews.com/feeds/rss/entertainment.xml");
    }


    // Store all categories and the links
    private void populateList(String channel, String world, String business, String sports, String tech, String health, String movies){
        HashMap<String,String> innerMap;
        /*
        **  News
         */
        innerMap = new HashMap<String,String>();
        KEY_LINK = new StringBuilder("World");
        innerMap.put(KEY_LINK.toString(),world);
        KEY_LINK = new StringBuilder("Business");
        innerMap.put(KEY_LINK.toString(),business);
        KEY_LINK = new StringBuilder("Sports");
        innerMap.put(KEY_LINK.toString(), sports);
        KEY_LINK = new StringBuilder("Technology");
        innerMap.put(KEY_LINK.toString(), tech);
        KEY_LINK = new StringBuilder("Health");
        innerMap.put(KEY_LINK.toString(), health);
        KEY_LINK = new StringBuilder("Movies");
        innerMap.put(KEY_LINK.toString(), movies);
        _links.put(channel,innerMap);
    }

    // Get a specific link
    public String getKeyLink(String newsKey,String categoryKey){
        if(_links.get(newsKey) == null)
            return null;
        String _tempLink = _links.get(newsKey).get(categoryKey);
        return _tempLink;
    }

    // Get a specific channel
    public HashMap<String,String> getChannel(String newsKey){
        return _links.get(newsKey);
    }
}
