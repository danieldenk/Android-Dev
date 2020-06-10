package RetrofitDeprecatedClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class originally created to parse JSON Data from the retrofit request
 * Because it did not work though (the passed JSON is terribly nested including at least 4 or 5 other classes
 * I decided to work with the raw data instead as it is sufficient for my purpose
 */
public class WikipediaEntry {
    @SerializedName("batchcomplete")
    @Expose
    String batchcomplete;
    @SerializedName("ContinueObject")
    @Expose
    Continue ContinueObject;
    @SerializedName("QueryObject")
    @Expose
    Query QueryObject;

    public String toString() {
        return batchcomplete + " " + ContinueObject.toString();

    }
    // Getter Methods

    public String getBatchcomplete() {
        return batchcomplete;
    }

    public void setBatchcomplete(String batchcomplete) {
        this.batchcomplete = batchcomplete;
    }

    public Continue getContinue() {
        return ContinueObject;
    }

    // Setter Methods

    public void setContinue(Continue continueObject) {
        this.ContinueObject = continueObject;
    }

    public Query getQuery() {
        return QueryObject;
    }

    public void setQuery(Query queryObject) {
        this.QueryObject = queryObject;
    }

}



