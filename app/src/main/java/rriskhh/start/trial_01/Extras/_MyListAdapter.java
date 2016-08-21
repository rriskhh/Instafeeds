package rriskhh.start.trial_01.Extras;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rriskhh.start.trial_01.R;
import rriskhh.start.trial_01.DatabaseHandler._Database_Tables;

/**
 * Created by rriskhh on 17/08/16.
 */
public class _MyListAdapter extends SimpleAdapter {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<? extends Map<String, ?>> data;
    private HashMap<String, Object> map;
    private View view;
    private _Database_Tables handler;
    private ImageView image;
    private ImageView thumbnail;
    //private TextView thumb_url;

    private final String TAG_TITLE = "title";
    private final String TAG_LINK = "link";
    private final String TAG_DESRIPTION = "description";
    private final String TAG_PUB_DATE = "pubDate";
    private final String TAG_THUMB = "thumb";
    private int type;


    public _MyListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, int type) {
        super(context, data, resource, from, to);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.type = type;
        handler = new _Database_Tables(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null)
            view = inflater.inflate(resource, null);
        map = (HashMap<String, Object>) data.get(position);
        /*
        ** Retrieve Text and Image Views
         */
        TextView page_url = (TextView) view.findViewById(R.id.page_url);
        TextView thumbnail_url = (TextView) view.findViewById(R.id.thumbail_url);
        //thumb_url = (TextView) view.findViewById(R.id.thumb_url);
        thumbnail = (ImageView) view.findViewById(R.id.thumbail);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView pubDate = (TextView) view.findViewById(R.id.pub_date);
        image = (ImageView) view.findViewById(R.id.image);
        TextView desc = (TextView) view.findViewById(R.id.desc);
        /*
        ** Set Text and Image Views
         */
        page_url.setText(map.get(TAG_LINK).toString());
        thumbnail_url.setText(map.get(TAG_THUMB).toString());
        //thumb_url.setText("");
        title.setText(map.get(TAG_TITLE).toString());
        pubDate.setText(map.get(TAG_PUB_DATE).toString());
        desc.setText(map.get(TAG_DESRIPTION).toString());

        /*
        ** Set Thumbnail
         */
        thumbnail.setScaleType(ImageView.ScaleType.FIT_XY);

        if (map.get(TAG_THUMB) == null || map.get(TAG_THUMB).toString().isEmpty())
            thumbnail.setImageResource(R.drawable.thumb);
        else {
            String thumb = map.get(TAG_THUMB).toString();
            //thumb_url.setText(thumb);
            Picasso.with(context)
                    .load(thumb)
                    .into(thumbnail);
        }
        /*
        ** Set Small Icon (Add,Delete)
         */
        if (type == 1)
            image.setImageResource(R.drawable.delete);
        else {   //type == 0
            if (handler.isSiteExists(map.get(TAG_LINK).toString()))
                image.setImageResource(R.drawable.set);
            else
                image.setImageResource(R.drawable.unset);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = data.get(position).get(TAG_LINK).toString();
                if (type == 1) {
                    handler.deleteSite(link);
                    data.remove(position);
                    Toast.makeText(context, "Bookmark Deleted.", Toast.LENGTH_SHORT)
                            .show();
                    if (data.size() == 0) {
                        Toast.makeText(context, "No Bookmark(s) left.", Toast.LENGTH_SHORT)
                                .show();
                        ((ListActivity) context).finish();
                    }
                }
                if (type == 0) {
                    if (handler.isSiteExists(link)) {
                        handler.deleteSite(link);
                        image.setImageResource(R.drawable.unset);
                        Toast.makeText(context, "Bookmark Deleted.", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        image.setImageResource(R.drawable.set);
                        _WebLink webLink = new _WebLink();
                        webLink.setTitle(data.get(position).get(TAG_TITLE).toString());
                        webLink.setLink(data.get(position).get(TAG_LINK).toString());
                        webLink.setDescription(data.get(position).get(TAG_DESRIPTION).toString());
                        webLink.setDate(data.get(position).get(TAG_PUB_DATE).toString());
                        if (data.get(position).get(TAG_THUMB) != null)
                            webLink.setThumbnail(data.get(position).get(TAG_THUMB).toString());
                        else
                            webLink.setThumbnail("");
                        handler.addLink(webLink);
                        Toast.makeText(context, "Bookmark Added.", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }
}