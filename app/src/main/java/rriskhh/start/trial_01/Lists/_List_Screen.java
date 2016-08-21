package rriskhh.start.trial_01.Lists;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rriskhh.start.trial_01.Extras._WebLink;
import rriskhh.start.trial_01.DatabaseHandler._RSSLinks;
import rriskhh.start.trial_01.DatabaseHandler._Database_Tables;
import rriskhh.start.trial_01.Parser._RSSParser;
import rriskhh.start.trial_01.Extras._MyListAdapter;
import rriskhh.start.trial_01.Webpage._Display_Feed;
import rriskhh.start.trial_01.R;

public class _List_Screen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView worldIV;
    ImageView businessIV;
    ImageView sportsIV;
    ImageView technologyIV;
    ImageView healthIV;
    ImageView moviesIV;
    ImageView customIV;
    ImageView changeBack;
    ImageView subscribe;
    Spinner spinner;
    SwipeRefreshLayout swipeLayout;
    ListView listView;
    Context context;
    _Database_Tables tables;
    int icon_id;
    private String KEY_CATEGORY = "category";
    String channel[] = new String[]{"NDTV","BBC","Reuters","CNN","Sky"};
    String category_val;
    String channel_val = "NDTV";
    String url;
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String, String>>();
    HashMap<String,Integer> bookmark = new HashMap<String,Integer>();
    List<_WebLink> rssItems = new ArrayList<_WebLink>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);

        //Hide Action Bar
        getSupportActionBar().hide();

        //Initialisation
        worldIV = (ImageView) findViewById(R.id.world);
        businessIV = (ImageView) findViewById(R.id.business);
        sportsIV = (ImageView) findViewById(R.id.sports);
        technologyIV = (ImageView) findViewById(R.id.technology);
        healthIV = (ImageView) findViewById(R.id.health);
        moviesIV = (ImageView) findViewById(R.id.movies);
        customIV = (ImageView) findViewById(R.id.custom);
        spinner = (Spinner) findViewById(R.id.newsChannel);
        subscribe = (ImageView) findViewById(R.id.subscribe);
        listView = (ListView) findViewById(R.id.list);

        final Bundle extras = getIntent().getExtras();
        category_val = extras.getString(KEY_CATEGORY);

        context = getApplicationContext();
        tables = new _Database_Tables(context);

        //Set Fill Icon as per Selection
        if(category_val.equals("World")) {
            worldIV.setImageResource(R.drawable.w_fill);
            setChangeImageView(worldIV,R.drawable.w);
        }else if(category_val.equals("Business")) {
            businessIV.setImageResource(R.drawable.b_fill);
            setChangeImageView(businessIV,R.drawable.b);
        }else if(category_val.equals("Sports")) {
            sportsIV.setImageResource(R.drawable.s_fill);
            setChangeImageView(sportsIV,R.drawable.s);
        }else if(category_val.equals("Technology")) {
            technologyIV.setImageResource(R.drawable.t_fill);
            setChangeImageView(technologyIV,R.drawable.t);
        }else if(category_val.equals("Health")) {
            healthIV.setImageResource(R.drawable.h_fill);
            setChangeImageView(healthIV,R.drawable.h);
        }else if(category_val.equals("Movies")) {
            moviesIV.setImageResource(R.drawable.m_fill);
            setChangeImageView(moviesIV,R.drawable.m);
        }else {
            customIV.setImageResource(R.drawable.c_fill);
            setChangeImageView(customIV, R.drawable.c);
        }

        //Set Spinner Action Listener
        spinner.setOnItemSelectedListener(this);

        // Set Swip Down Refresh
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetAndDisplayFeeds().execute();
                swipeLayout.setRefreshing(false);
            }
        });

        // Set Feed display Action Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(context, _Display_Feed.class);
                // getting page url
                String page_url = ((TextView) view.findViewById(R.id.page_url)).getText().toString();
                //Toast.makeText(getApplicationContext(), page_url, Toast.LENGTH_SHORT).show();
                in.putExtra("page_url", page_url);
                startActivity(in);
            }
        });

        // Set Long Click Listener on List item
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String thumb_url = ((TextView) view.findViewById(R.id.thumbail_url)).getText().toString();
                confirmAndSave(thumb_url);
                //Toast.makeText(context,img,Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    /*
    *
    * Confirm and Save Image to SDCard
    *
     */
    public void confirmAndSave(final String thumb_url){
        /*
                *
                * Alert Dialog, Confirm to Save
                *
                 */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Download");
        builder.setMessage("Want to download this image");
        builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Picasso.with(context).load(thumb_url).into(target);
                /*
                *
                * Fetch and Save Image
                *
                 */
                try{
                    URL imgUrl = new URL(thumb_url);
                    InputStream inputStream = imgUrl.openStream();
                    File storagePath = Environment.getExternalStorageDirectory().getAbsoluteFile();
                    File file = new File(storagePath + "/download.jpg" );

                    OutputStream outputStream = new FileOutputStream(storagePath + "/download.jpg");

                    byte[] buffer = new byte[1024];
                    int byteRead = 0;
                    while( (byteRead = inputStream.read(buffer,0,buffer.length)) >= 0){
                        outputStream.write(buffer,0,byteRead);
                    }
                    outputStream.close();
                    inputStream.close();
                    Toast.makeText(context,"Download Complete.",Toast.LENGTH_SHORT)
                            .show();
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,"Cannot Save this image.",Toast.LENGTH_SHORT).show();
                }
            }
            /*
           *
           * Fetch from url and save in sdcard
           *
            *
            Target target = new Target(){


                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + thumb_url);
                    try{
                        file.createNewFile();
                        FileOutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,75,outputStream);
                        outputStream.close();
                    }catch(Exception e){
                        Toast.makeText(context,"Error! Cannot access memory.",Toast.LENGTH_SHORT)
                                .show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            */

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

        return;
    }

    /*
    *
    * Get Custom Feed Link
    *
     */
    private void getCustomFeedLink(){
        Log.d("Checkpoint","Here");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Custom Link");

                        /*
                        *
                        * Set up Edit Text
                        *
                        */
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("  https:/androidhive..info/");
        builder.setView(input);

                        /*
                        *
                        * Set up Buttons
                        *
                        */
        // OK button
        builder.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().toLowerCase().matches("^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*")) {
                    Toast.makeText(context, "Invalid Url", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                url = input.getText().toString().toLowerCase();
                new GetAndDisplayFeeds().execute();
            }
        });
        // Cancel Button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                new GetAndDisplayFeeds().execute();
            }
        });
        //builder.create();
        builder.show();
    }

    //Change from last imageview
    private void setChangeImageView(ImageView imageView,int id){
        changeBack = imageView;
        icon_id = id;
        return;
    }

    public void worldIcon(View v){
        changeBack.setImageResource(icon_id);
        worldIV.setImageResource(R.drawable.w_fill);
        setChangeImageView(worldIV, R.drawable.w);
        category_val = "World";
        new GetAndDisplayFeeds().execute();
    }
    public void businessIcon(View v){
        changeBack.setImageResource(icon_id);
        businessIV.setImageResource(R.drawable.b_fill);
        setChangeImageView(businessIV, R.drawable.b);
        category_val = "Business";
        new GetAndDisplayFeeds().execute();
    }
    public void sportsIcon(View v){
        changeBack.setImageResource(icon_id);
        sportsIV.setImageResource(R.drawable.s_fill);
        setChangeImageView(sportsIV, R.drawable.s);
        category_val = "Sports";
        new GetAndDisplayFeeds().execute();
    }
    public void technologyIcon(View v){
        changeBack.setImageResource(icon_id);
        technologyIV.setImageResource(R.drawable.t_fill);
        setChangeImageView(technologyIV, R.drawable.t);
        category_val = "Technology";
        new GetAndDisplayFeeds().execute();
    }
    public void healthIcon(View v){
        changeBack.setImageResource(icon_id);
        healthIV.setImageResource(R.drawable.h_fill);
        setChangeImageView(healthIV, R.drawable.h);
        category_val = "Health";
        new GetAndDisplayFeeds().execute();
    }
    public void moviesIcon(View v){
        changeBack.setImageResource(icon_id);
        moviesIV.setImageResource(R.drawable.m_fill);
        setChangeImageView(moviesIV, R.drawable.m);
        category_val = "Movies";
        new GetAndDisplayFeeds().execute();
    }
    public void customIcon(View v){
        changeBack.setImageResource(icon_id);
        customIV.setImageResource(R.drawable.c_fill);
        setChangeImageView(customIV, R.drawable.c);
        category_val = "Custom";
        getCustomFeedLink();
        //new GetAndDisplayFeeds().execute();
    }

    /*
    *
    *
    * Clicking on the bookmark icon
    *
    *
     */
    public void onBookmark(View v){
        Intent i  = new Intent(context,_Bookmark_List.class);
        startActivity(i);
    }

    /*
    *
    *
    * Clicking on the subscribe icon
    *
    *
     */
    public void onSubscribe(View v){
        if(ParseUser.getCurrentUser() == null){
            Toast.makeText(context, "Login Required.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if(tables.isSubscriptionSiteExists(url)){
            tables.deleteLink(url);
            Toast.makeText(context,"Unsubcribed.",Toast.LENGTH_SHORT)
                    .show();
        }else{
            subscribe.setImageResource(R.drawable.unsubs);
            tables.addLink(rssItems,url);
            Toast.makeText(context,"Subscribed.",Toast.LENGTH_SHORT)
                    .show();
        }
        return;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Log.d("Checkpoint",channel[position]);
        if(category_val.equals("Custom"))
            getCustomFeedLink();
        else{
            channel_val = channel[position];
            new GetAndDisplayFeeds().execute();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*
    *
    * Display the Feed
    *
     */
    class GetAndDisplayFeeds extends AsyncTask<String,String,String> {

        _RSSLinks links;
        _RSSParser rssParser = new _RSSParser();
        private final String TAG_TITLE = "title";
        private final  String TAG_LINK = "link";
        private final String TAG_DESRIPTION = "description";
        private final String TAG_PUB_DATE = "pubDate";
        private final String TAG_IMAGE = "image";
        private final String TAG_THUMB = "thumb";
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute(){
            pDialog = new ProgressDialog(
                    _List_Screen.this);
            pDialog.setMessage("Loading Feeds...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            //swipeLayout.setRefreshing(false);
        }

        @Override
        protected void onPostExecute(String result){
            pDialog.dismiss();
            //swipeLayout.setRefreshing(false);
        }

        @Override
        protected String doInBackground(String... params) {
            if(category_val.equals("Custom")){
                //Do Custom Feed related Jobs Here
                /*String rss_url = rssParser.getRSSLinkFromURL(url);
                url = null;
                if(rss_url != null) {
                    url = rssParser.getXmlFromUrl(rss_url);
                    Log.d("Checkpoint",url);
                }
                */
            }else{
                /*
                *
                * Get News link stored in the system
                *
                 */
                links = new _RSSLinks();
                url = links.getKeyLink(channel_val,category_val);
            }
            /*
            *
            * if no feeds in url, close
            *
             */
            if(url == null || url.isEmpty()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"Link Not Available",Toast.LENGTH_SHORT)
                                .show();
                        finish();
                        return;
                    }
                });
                return null;
            }
            /*
            *
            * Get Feed items
            *
             */
            rssItems = rssParser.parseLinkAndGetItems(url);
            rssItemList.clear();
            bookmark.clear();
            getEachFeedItem(rssItems);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   /*
                   *
                   * Set subscription Icon
                   *
                    */
                    if(ParseUser.getCurrentUser() != null && tables.isSubscriptionSiteExists(url))
                        subscribe.setImageResource(R.drawable.unsubs);
                   /*
                    *
                    * Empty ListView
                    *
                    */
                    listView.setAdapter(null);
                    _MyListAdapter adapter = new _MyListAdapter(_List_Screen.this,rssItemList,R.layout.rss_item_list_row,new String[]{},new int[]{},0);

                    // updating listview
                    listView.setAdapter(adapter);
                    swipeLayout.setRefreshing(false);

                }
            });
            return null;
        }

        private void getEachFeedItem(List<_WebLink> rssItems){

            for(_WebLink _rssItems : rssItems){
                HashMap<String,String> map = new HashMap<String,String>();
                map.put(TAG_LINK,_rssItems.getLink());
                map.put(TAG_TITLE, _rssItems.getTitle());
                map.put(TAG_PUB_DATE,_rssItems.getDate());
                map.put(TAG_THUMB, _rssItems.getThumbnail());
                int image = R.drawable.unset;
                if(tables.isSiteExists(_rssItems.getLink()))
                    image = R.drawable.set;
                map.put(TAG_IMAGE,Integer.toString(image));
                bookmark.put(_rssItems.getLink(),image);

                String description = _rssItems.getDescription();
                if(description.length() > 100){
                    description = description.substring(0, 97) + "..";
                }
                map.put(TAG_DESRIPTION,description);
                rssItemList.add(map);
            }
        }
    }
}
