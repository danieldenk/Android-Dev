package com.example.whatsinmyfridge;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is the class providing the basic structure for the ingredients
 */
public class IngredientItem implements Parcelable {
    public static final Creator<IngredientItem> CREATOR = new Creator<IngredientItem>() {
        @Override
        public IngredientItem createFromParcel(Parcel in) {
            return new IngredientItem(in);
        }

        @Override
        public IngredientItem[] newArray(int size) {
            return new IngredientItem[size];
        }
    };
    // the img resource is actually not yet needed, but I plan to
    // set different images depending on what type of ingredient we have
    // Icon for Vegetable | Icon for Fruit | Icon for Meat | Icon for Fish
    // Therefore it is needed to include it here for a later expansion
    // I further plan to add filtering functionality for allergies etc.
    // TODO: User has allergies, preferences --> Different IMG RES and filtering functionality
    private int img_res;
    // Title of the ingredient
    private String title;
    // Short description of it (Useful if exotic stuff present)
    private String desc;
    // Level of Kcal
    private String kcal;
    // Level of nutrients
    private String nutr;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Using parcelable for serialization and item passing, as we need to pass a list of these items      //
    // to an other activity that is processing it later                                                   //
    // The beneath methods are therefore either simple getters and setters or simply implement parcelable //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Creator<IngredientItem> getCREATOR() {
        return CREATOR;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getNutr() {
        return nutr;
    }

    public void setNutr(String nutr) {
        this.nutr = nutr;
    }

    // Constructor
    public IngredientItem(int img_res, String title, String desc, String kcal, String nutr) {
        this.img_res = img_res;
        this.title = title;
        this.desc = desc;
        this.kcal = kcal;
        this.nutr = nutr;
    }

    protected IngredientItem(Parcel in) {
        img_res = in.readInt();
        title = in.readString();
        desc = in.readString();
    }

    public int getImg_res() {
        return this.img_res;
    }

    public void setImg_res(int img) {
        this.img_res = img;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String d) {
        this.desc = d;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(img_res);
        dest.writeString(title);
        dest.writeString(desc);
    }
}