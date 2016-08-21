package rriskhh.start.trial_01.Extras;

/**
 * Created by rriskhh on 16/07/16.
 */
public class _WebLink {

    String _title = null;
    String _link = null;
    String _date = null;
    String _description = null;
    String _thumb = null;

    // Constructor
    public _WebLink(){

    }

    // Constructor with parameters
    public _WebLink(String title, String link, String date, String description, String _thumb){
        this._title = title;
        this._link = link;
        this._date = date;
        this._description = description;
        this._thumb = _thumb;
    }

    /**
     * All set methods
     * */
    public void setTitle(String title){
        this._title = title;
    }

    public void setLink(String link){
        this._link = link;
    }

    public void setDate(String date){ this._date = date; }

    public void setDescription(String description){
        this._description = description;
    }

    public void setThumbnail(String thumbnail){ this._thumb = thumbnail; }
    /**
     * All get methods
     * */
    public String getTitle(){
        return this._title;
    }

    public String getLink(){
        return this._link;
    }

    public String getDate(){
        return this._date;
    }

    public String getDescription(){
        return this._description;
    }

    public String getThumbnail(){ return this._thumb; }


}
