package info.alphanext.cloudsearch;

import java.util.ArrayList;
import java.util.List;

public class VideoCollections {

    private static VideoCollections videoCollectionInstance = null;

    private List<Video> videoList = new ArrayList<>();

    private VideoCollections()
    {}

    public static VideoCollections getInstance()
    {
        if(videoCollectionInstance == null)
            videoCollectionInstance = new VideoCollections();

        return videoCollectionInstance;
    }

    public List<Video> getVideoList()
    {
        return videoList;
    }

    public void addToVideoList(Video videoEntry)
    {
        videoList.add(videoEntry);
    }

}
