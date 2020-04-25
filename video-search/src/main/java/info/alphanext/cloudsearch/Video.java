package info.alphanext.cloudsearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Video {
    
    private String videoTitle;
    private String videoURL;
    private String videoPublicURL;
    private String videoLanguage;

    private List<videoTranscript> transcript = new LinkedList<>();
    private List<String> videoLabel = new LinkedList<>();
    private List<String> videoCategory = new LinkedList<>();


    public Video()
    {
        
    }

    public void setVideoPublicUrl(String url)
    {
        this.videoPublicURL = url;
    }

    public String getVideoPublicUrl()
    {
        return this.videoPublicURL;
    }

    public void setVideoTitle(String title)
    {
        videoTitle = title;
    }

    public String getVideoTitle()
    {
        return this.videoTitle;
    }

    public void setVideoLanguage(String language)
    {
        videoLanguage = language;
    }

    public String getVideoLanguage()
    {
        return this.videoLanguage;
    }

    public void setVideoURL(String url)
    {
        videoURL = url;
    }

    public String getVideoUrl()
    {
        return this.videoURL;
    }

    public void addVideoTranscript(videoTranscript transcriptEntry)
    {
        transcript.add(transcriptEntry);
    }

    public String getVideoTranscriptSting()
    {
        String transcriptionText = "";

        for (videoTranscript videoTranscript : transcript) {
            transcriptionText += videoTranscript.getTranscriptText();
            transcriptionText += " ";
        }

        return transcriptionText;
    }

    public void addVideoLabel(String label)
    {
        videoLabel.add(label);
    }

    public List<String> getVideoLabelListUnique()
    {
        Set<String> uniqueLabelSet = new HashSet<String>(videoLabel);

        List<String> uniqueLabelList = new ArrayList<>(uniqueLabelSet);

        return uniqueLabelList;
    }

    public void addVideoCategory(String category)
    {
        videoCategory.add(category);
    }

    public List<String> getVideoCategoryListUnique()
    {
        Set<String> uniqueVideoCatogorySet = new HashSet<String>(videoCategory);

        List<String> uniqueVideoCategoryList = new ArrayList<>(uniqueVideoCatogorySet);

        return uniqueVideoCategoryList;
    }

    public void printVideoTranscriptDetails()
    {
        for (videoTranscript videoTranscript : transcript) {
            System.out.println(videoTranscript.getTranscriptText());
            System.out.println(videoTranscript.getConfidence());

            for (videoTranscriptWordLevel wordEntry : videoTranscript.getVideoTranscriptWorldLevel()) {
                System.out.print("Begin: " + wordEntry.getBeginTime());
                System.out.print(" -> End: " + wordEntry.getEndTime());
                System.out.println("-> Words: " + wordEntry.getWords());
            }
        }
    }

    public void printVideoLabelCategoryInfo()
    {
        System.out.println("All Category count: " + videoCategory.size());
        
        Set<String> uniqueCategory = new HashSet<String>(videoCategory);
        System.out.println("Unique Category count: " + uniqueCategory.size());

        System.out.println("All Label count: " + videoLabel.size());

        Set<String> uniqueLabel = new HashSet<String>(videoLabel);
        System.out.println("Unique Label count: " + uniqueLabel.size());

    }

    public class videoTranscript {
        private String transcript;
        private Float confidence;
        private List<videoTranscriptWordLevel> transcriptWordLevelInfo = new ArrayList<>();

        public void setTranscriptText(String transcriptText)
        {
            this.transcript = transcriptText;
        }

        public String getTranscriptText()
        {
            return this.transcript;
        }

        public void setConfidence(Float transcriptConfidence)
        {
            this.confidence = transcriptConfidence;
        }

        public Float getConfidence()
        {
            return this.confidence;
        }

        public void addVideoTranscriptWorldLevel(videoTranscriptWordLevel wordlevelEntry)
        {
            transcriptWordLevelInfo.add(wordlevelEntry);
        }

        public List<videoTranscriptWordLevel> getVideoTranscriptWorldLevel()
        {
            return this.transcriptWordLevelInfo;
        }
    }
    
    public class videoTranscriptWordLevel {
        Double beginTime;
        Double endTime;
        String words;

        public videoTranscriptWordLevel(Double beginTime, Double endTime, String words)
        {
            this.beginTime = beginTime;
            this.endTime = endTime;
            this.words = words;
        }

        public Double getBeginTime()
        {
            return beginTime;
        }

        public Double getEndTime()
        {
            return endTime;
        }

        public String getWords()
        {
            return words;
        }
    }
}

