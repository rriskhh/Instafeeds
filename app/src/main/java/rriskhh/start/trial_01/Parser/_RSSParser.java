package rriskhh.start.trial_01.Parser;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import rriskhh.start.trial_01.Extras._WebLink;

/**
 * Created by rriskhh on 16/07/16.
 */
public class _RSSParser {

    _XMLParser xmlParser;

    public _RSSParser(){
        xmlParser = new _XMLParser();
    }

    public List<_WebLink> parseLinkAndGetItems(String url){

        List<_WebLink> rssItem = null;
        try{
            Document doc = null;
            if(url.endsWith(".xml")){
                doc = this.getDomObject(url,true);
            }else{
                String rssUrl = this.getRSSLinkFromURL(url);
                String xmlUrl = null;
                //if(rssUrl != null)
                    xmlUrl = this.getXmlFromUrl(rssUrl);
                //if(xmlUrl != null)
                    doc = this.getDomObject(xmlUrl, false);
            }

            if(doc != null) {
                rssItem = xmlParser.getRSSFeed(doc);
            }
            return rssItem;
        }catch(Exception e){

            e.printStackTrace();
        }

        return null;
    }

    public String getRSSLinkFromURL(String url) {
        // RSS url
        String rss_url = null;

        try {
            // Using JSoup library to parse the html source code
            org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
            // finding rss links which are having link[type=application/rss+xml]
            org.jsoup.select.Elements links = doc
                    .select("link[type=application/rss+xml]");
            Log.i("Found", links.size() + " ");
            Log.d("No of RSS links found", " " + links.size());

            // check if urls found or not
            if (links.size() > 0) {
                rss_url = links.get(0).attr("href").toString();
            } else {
                // finding rss links which are having link[type=application/rss+xml]
                org.jsoup.select.Elements links1 = doc
                        .select("link[type=application/atom+xml]");
                if(links1.size() > 0){
                    rss_url = links1.get(0).attr("href").toString();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // returing RSS url
        return rss_url;
    }

    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }

    private Document getDomObject(String xml,boolean flag){
        Document doc = null;
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            if(flag) {
                doc = documentBuilder.parse(xml);
            }else{
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                doc = (Document) documentBuilder.parse(is);
            }
            return doc;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getLinkTitle(String url){
        String title = "";
        try{
            Document doc;
            if(url.endsWith(".xml")){
                doc = this.getDomObject(url,true);

            }else{
                String rssUrl = this.getRSSLinkFromURL(url);
                String xmlUrl = null;
                //if(rssUrl != null)
                xmlUrl = this.getXmlFromUrl(rssUrl);
                //if(xmlUrl != null)
                doc = this.getDomObject(xmlUrl, false);
            }
            if(doc != null){
                NodeList nodeList = doc.getElementsByTagName("channel");
                Element e = (Element) nodeList.item(0);
                String val = new _XMLParser().getValue(e,"title");
                return val;
            }
        }catch(Exception e){

            e.printStackTrace();
        }
        return title;
    }
}
