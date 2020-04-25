package info.alphanext.cloudsearch;

import java.util.ArrayList;
import java.util.List;

public class GCSVideoGallery {

    List<GCSVideoEntry> gcsVideoEntries = new ArrayList<>();

    public GCSVideoGallery()
    {}

    public void put(String gcsInternalUrl, String gcsPublicUrl, String videoTitle, String videoLanguage)
    {
        GCSVideoEntry entry = new GCSVideoEntry(gcsInternalUrl, gcsPublicUrl,videoTitle, videoLanguage);
        gcsVideoEntries.add(entry);
    }

    public int size()
    {
        return gcsVideoEntries.size();
    }
    
    public List<GCSVideoEntry> getVideoGallery()
    {
        return gcsVideoEntries;
    }

 
    public class GCSVideoEntry 
    {
        private String gcsInternalUrl;
        private String gcsPublicUrl;
        private String videoTitle;
        private String videoLanguage;
    
        public GCSVideoEntry(String gcsInternalUrl, String gcsPublicUrl, String videoTitle, String videoLanguage) {
            this.gcsInternalUrl = gcsInternalUrl;
            this.gcsPublicUrl = gcsPublicUrl;
            this.videoTitle = videoTitle;
            this.videoLanguage = videoLanguage;
        }

        public String getGCSInternalUrl()
        {
            return this.gcsInternalUrl;
        }

        public String getGCSPublicUrl()
        {
            return this.gcsPublicUrl;
        }

        public String getVideoTitle()
        {
            return this.videoTitle;
        }

        public String getVideoLanguage()
        {
            return this.videoLanguage;
        }
    }
}