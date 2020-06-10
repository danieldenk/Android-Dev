package com.example.whatsinmyfridge;

/**
 * This is the class representing a recipe
 */
public class Recipe {
    // Every Recipe has a ...
    // Title
    private String title;
    // Description
    private String desc;
    // Difficulty
    private String diff;
    // List of ingredients separated by ;
    private String ingredients;
    // int 0 or 1 if it is a favorite or not (not boolean because of SQLite Requirements)
    private int favorite;
    // URL to the image thumbnail
    private String img_url;

    // Simple Constructor offering the minimum parameters to create a recipe
    public Recipe(String title, String diff, String url) {
        this.title = title;
        this.diff = diff;
        this.img_url = url;
    }

    // This Constructor is used to create a proper object of a recipe, it is not really used yet though
    // For later purposes like if the user gets an opportunity to add his own recipe, these parameters
    // and this constructor are going to be needed, therefore it is going to stay here for the moment
    // TODO: Implement user adds recipe
    public Recipe(String title, String desc, String diff, String ing, int fav, String url) {
        this.title = title;
        this.desc = desc;
        this.diff = diff;
        this.ingredients = ing;
        this.favorite = fav;
        this.img_url = url;
    }

    ///////// BENEATH ARE GETTERS AND SETTERS /////////

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
