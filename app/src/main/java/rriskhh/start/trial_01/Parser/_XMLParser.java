package rriskhh.start.trial_01.Parser;

/**
 * Created by rriskhh on 16/07/16.
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import rriskhh.start.trial_01.Extras._WebLink;


public class _XMLParser {

    private static final String KEY_CHANNEL = "channel";
    private static final String KEY_ITEM = "item";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LINK = "link";
    private static final String KEY_PUB_DATE = "pubDate";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_THUMBNAIL = "StoryImage";
    private static final String KEY_MEDIA = "media:thumbnail";
    private static final String KEY_MEDIA_2 = "media:content";

    public _XMLParser(){

    }

    public List<_WebLink> getRSSFeed(Document doc){
        List<_WebLink> rssFeed = new ArrayList<_WebLink>();

        try{
            NodeList nodeList = doc.getElementsByTagName(KEY_CHANNEL);
            Element e = (Element) nodeList.item(0);

            NodeList items = e.getElementsByTagName(KEY_ITEM);

            for(int i = 0;i<items.getLength();++i){

                Element e1 = (Element) items.item(i);

                String title = this.getValue(e1,KEY_TITLE);
                String link = this.getValue(e1,KEY_LINK);
                String date = this.getValue(e1,KEY_PUB_DATE);
                String desc = this.getValue(e1,KEY_DESCRIPTION);
                String thumb = this.getValue(e1,KEY_THUMBNAIL);
                if(thumb == null || thumb.isEmpty())
                    thumb = this.getAttributeValue(e1,KEY_MEDIA);
                if(thumb == null || thumb.isEmpty())
                    thumb = this.getAttributeValue(e1,KEY_MEDIA_2);
                _WebLink feedItem = new _WebLink();
                feedItem.setTitle(title);
                feedItem.setLink(link);
                feedItem.setDate(date);
                feedItem.setDescription(desc);
                feedItem.setThumbnail(thumb);

                rssFeed.add(feedItem);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return rssFeed;
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE || ( child.getNodeType() == Node.CDATA_SECTION_NODE)) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public String getAttributeValue(Element item,String str){
        NodeList n = item.getElementsByTagName(str);
        if(n.getLength() == 0)
            return "";
        return n.item(0).getAttributes().getNamedItem("url").getNodeValue();
    }
}
