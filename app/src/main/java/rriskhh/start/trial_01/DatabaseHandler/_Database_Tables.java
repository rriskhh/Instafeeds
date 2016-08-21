package rriskhh.start.trial_01.DatabaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import rriskhh.start.trial_01.Extras._WebLink;

/**
 * Created by rriskhh on 17/08/16.
 */
public class _Database_Tables extends SQLiteOpenHelper{

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "instafeeds_db";

    // Bookmark Table name
    private static final String TABLE_BOOKMARKS = "bookmarks";

    // Subscription Table name
    private static final String TABLE_SUBSCRIBE = "subscribe";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LINK = "link";
    private static final String KEY_PUB_DATE = "pubDate";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_THUMB = "thumbnail";
    private static final String KEY_FEED = "feed";

    public _Database_Tables(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bookmark table creation
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKMARKS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT," + KEY_LINK
                + " TEXT," + KEY_PUB_DATE + " TEXT," + KEY_DESCRIPTION
                + " TEXT," + KEY_THUMB + " TEXT)";
        db.execSQL(CREATE_TABLE);

        // Subscription table creation
        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SUBSCRIBE + "(" + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LINK + " TEXT," +
                KEY_FEED + " TEXT)";
        db.execSQL(CREATE_TABLE);
        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add Row
    public void addLink(_WebLink link){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE,link.getTitle());
        values.put(KEY_LINK,link.getLink());
        values.put(KEY_PUB_DATE,link.getDate());
        values.put(KEY_DESCRIPTION,link.getDescription());
        values.put(KEY_THUMB,link.getThumbnail());
        // Check if row already existed in database
        if (!isSiteExists(link.getLink())) {
            // site not existed, create a new row
            db.insert(TABLE_BOOKMARKS, null, values);
            db.close();
        } else {
            // site already existed update the row
            updateSite(link);
            db.close();
        }
    }

    /**
     * Updating a single row row
     * */
    public int updateSite(_WebLink link) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, link.getTitle());
        values.put(KEY_LINK, link.getLink());
        values.put(KEY_PUB_DATE, link.getDate());
        values.put(KEY_DESCRIPTION, link.getDescription());
        values.put(KEY_THUMB,link.getThumbnail());
        // updating row return
        int update = db.update(TABLE_BOOKMARKS, values, KEY_LINK + " = ?",
                new String[] { (link.getLink()) });
        db.close();
        return update;
    }

    /**
     *   Reading a single row
     */
    public _WebLink getSite(String link){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOKMARKS, new String[]{KEY_ID, KEY_TITLE,
                        KEY_LINK, KEY_LINK, KEY_DESCRIPTION,KEY_THUMB}, KEY_LINK + "=?",
                new String[]{link}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        _WebLink rss_link = new _WebLink();

        rss_link.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        rss_link.setLink(cursor.getString(cursor.getColumnIndex(KEY_LINK)));
        rss_link.setDate(cursor.getString(cursor.getColumnIndex(KEY_PUB_DATE)));
        rss_link.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
        rss_link.setThumbnail(cursor.getString(cursor.getColumnIndex(KEY_THUMB)));
        cursor.close();
        db.close();

        return rss_link;
    }

    /**
     * Deleting single row
     * */
    public void deleteSite(String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKMARKS, KEY_LINK + " = ?",
                new String[]{link});
        db.close();
    }

    /**
     *   Check if a feed exists (isbookmarked)
     */
    public boolean isSiteExists(String link) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKMARKS
                + " WHERE " + KEY_LINK + "= ?", new String[]{link});
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }

    public int getRows(){
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKMARKS, null);
        if(cursor == null)
            count = -1;
        else count = cursor.getCount();
        return count;
    }
    /**
     * Reading all the rows
     * */
    public List<_WebLink> getAllSites() {
        List<_WebLink> linkList = new ArrayList<_WebLink>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOKMARKS
                + " ORDER BY id DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                _WebLink link = new _WebLink();
                link.setTitle(cursor.getString(1));
                link.setLink(cursor.getString(2));
                link.setDate(cursor.getString(3));
                link.setDescription(cursor.getString(4));
                link.setThumbnail(cursor.getString(5));
                // Adding contact to list
                linkList.add(link);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return contact list
        return linkList;
    }

    /*
    *
    *
    *
    * Subscription Table Functions
    *
    *
    *
     */
    public void addLink(List<_WebLink> list,String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (_WebLink temp : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_LINK, link);
            contentValues.put(KEY_FEED,temp.getLink());
            db.insert(TABLE_SUBSCRIBE, null, contentValues);
        }
    }
    public void addSingleLink(String feed,String link){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LINK,link);
        values.put(KEY_FEED,feed);
        db.insert(TABLE_SUBSCRIBE, null, values);
    }
    public void deleteLink(String link){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBSCRIBE, KEY_LINK + " = ?",
                new String[]{link});
        db.close();
    }
    public void deleteAllLinks(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBSCRIBE, null, null);
    }
    public int getCount(){
        int count  = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SUBSCRIBE, null);
        if(cursor == null)
            count = -1;
        else count = cursor.getCount();
        return count;
    }
    public List<String> getAllLinks(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> list = new ArrayList<String>();
        String query = "SELECT DISTINCT "+KEY_LINK+" FROM " + TABLE_SUBSCRIBE;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(cursor.getColumnIndex(KEY_LINK)));
            }while(cursor.moveToNext());
        }
        return list;
    }
    public List<String> getLinkFeed(String link){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> list = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SUBSCRIBE
                + " WHERE " + KEY_LINK + "= ?",new String[]{link});
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(cursor.getColumnIndex(KEY_FEED)));
            }while(cursor.moveToNext());
        }
        return list;
    }
    public boolean isSubscriptionSiteExists(String link) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SUBSCRIBE
                + " WHERE " + KEY_LINK + "= ?", new String[]{link});
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }
}
