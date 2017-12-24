package app.voron.ph.showcaseapp.Models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TJack on 11.10.2017.
 */

public class ShowcaseItemDataModel implements Serializable {
    public String title = null;
    public String description = null;
    public float cost = 0;
    public String timeFormatted = null;
    public float rating = 0;
    public int reviewsCount = 0;
    public String previewImageName = null;
    public String coverImageName = null;
    public String id = null;
    public int foodCatecory = 0;
    public int itemsInCart = 0;
    public List<String> categoryTags = null;
    //
    public ShowcaseItemDataModel(){

    }
}
