package RetrofitDeprecatedClasses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Query {
    @SerializedName("SearchinfoObject")
    @Expose
    Searchinfo searchinfoOldObject;
    @SerializedName("search")
    @Expose
    ArrayList<SearchObject> search = new ArrayList <  > ();


    // Getter Methods

    public Searchinfo getSearchinfo() {
        return searchinfoOldObject;
    }

    // Setter Methods

    public void setSearchinfo(Searchinfo searchinfoOldObject) {
        this.searchinfoOldObject = searchinfoOldObject;
    }
}
