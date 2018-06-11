package com.oude.Storage;

public class MyList
{
    private String name;
    private int imageId;
    public MyList(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
}
