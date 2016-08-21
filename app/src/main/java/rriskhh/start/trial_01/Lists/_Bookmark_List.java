package rriskhh.start.trial_01.Lists;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rriskhh.start.trial_01.Extras._WebLink;
import rriskhh.start.trial_01.Extras._MyListAdapter;
import rriskhh.start.trial_01.DatabaseHandler._Database_Tables;
import rriskhh.start.trial_01.Webpage._Display_Feed;
import rriskhh.start.trial_01.R;

public class _Bookmark_List extends ListActivity {

    List<_WebLink> bookmarks = new ArrayList<_WebLink>();
    List<HashMap<String,String>> bookmarkItems = new ArrayList<HashMap<String, String>>();

    _Database_Tables databaseHandler = new _Database_Tables(this);

    private final String TAG_TITLE = "title";
    private final  String TAG_LINK = "link";
    private final String TAG_DESRIPTION = "description";
    private final String TAG_PUB_DATE = "pubDate";
    private final String TAG_IMAGE = "image";
    private final String TAG_THUMB = "thumb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_list);
        if(databaseHandler.getRows() == 0){
            Toast.makeText(this, "No Bookmarks saved!", Toast.LENGTH_LONG)
                    .show();
            finish();
        }else if(databaseHandler.getRows() == -1){
            Toast.makeText(this, "Error fetching bookmarks.", Toast.LENGTH_LONG)
                    .show();
            finish();
        }else{
            bookmarks = databaseHandler.getAllSites();
            new GetFeedFromDBAndDisplay().execute();
        }
        ListView listView = getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getApplicationContext(), _Display_Feed.class);
                // getting page url
                String page_url = ((TextView) view.findViewById(R.id.page_url)).getText().toString();
                //Toast.makeText(getApplicationContext(), page_url, Toast.LENGTH_SHORT).show();
                in.putExtra("page_url", page_url);
                startActivity(in);
            }
        });
    }

    /*
    *
    *
    *
    * Display Bookmarked Feeds
    *
    *
    *
     */
    class GetFeedFromDBAndDisplay extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            for(_WebLink item : bookmarks){
                HashMap<String,String> map = new HashMap<String,String>();
                map.put(TAG_TITLE,item.getTitle());
                map.put(TAG_LINK, item.getLink());
                map.put(TAG_PUB_DATE, item.getDate());
                map.put(TAG_DESRIPTION, item.getDescription());
                map.put(TAG_IMAGE, Integer.toString(R.drawable.delete));
                map.put(TAG_THUMB, item.getThumbnail());
                bookmarkItems.add(map);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // updating listview
                    _MyListAdapter adapter = new _MyListAdapter(_Bookmark_List.this,bookmarkItems,R.layout.rss_item_list_row,new String[]{},new int[]{},1);
                    setListAdapter(adapter);
                }
            });
            return null;
        }
    }
}
