package com.example.myapplication;

public class exempleItemHTTP {
    private String mImageUrl;
    private String mCreator;
    private int mLikes;

    public exempleItemHTTP(String imageUrl, String creator, int likes){
        mCreator = creator;
        mImageUrl = imageUrl;
        mLikes = likes;
    }

    public String getImageUrl(){
        return mImageUrl;
    }
    public String getCreator(){
        return mCreator;
    }
    public int getLikeCount(){
        return mLikes;
    }
}
