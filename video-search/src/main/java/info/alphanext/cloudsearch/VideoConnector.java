package info.alphanext.cloudsearch;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.google.enterprise.cloudsearch.sdk.indexing.IndexingApplication;
import com.google.enterprise.cloudsearch.sdk.indexing.IndexingConnector;
import com.google.enterprise.cloudsearch.sdk.indexing.template.FullTraversalConnector;
import com.google.enterprise.cloudsearch.sdk.indexing.template.Repository;

public class VideoConnector {
    
    public static void main(String[] args) throws Exception {

        Logger log = Logger.getLogger(VideoConnector.class.getName());

        log.info("Application Start");
        
        try {
            // [START video_speech_transcription_gcs]
            log.info("Start parsing videos by using Video Intelligence API");

            // Supported Language: https://cloud.google.com/speech-to-text/docs/languages
            // gsutil ls -L gs://gcsvideo/cat.mp4

            Map<String, String> videoGallery = new LinkedHashMap<String, String>();
            /*videoGallery.put("gs://gcsvideo/car_visor.mp4#1587203122483031", "en-US");
            videoGallery.put("gs://gcsvideo/cover2.mp4#1587203110031425", "en-US");
            videoGallery.put("gs://gcsvideo/ev_charger.mp4#1587203136140061", "en-US");
            videoGallery.put("gs://gcsvideo/fbv-tmux-nhc-bbtx.mp4#1587203215764064", "en-US");
            videoGallery.put("gs://gcsvideo/gator_cover.mp4#1587203158464059", "en-US");
            videoGallery.put("gs://gcsvideo/occupant_detection.mp4#1587203169127490", "en-US");
            videoGallery.put("gs://gcsvideo/one_pedal.mp4#1587203207079271", "en-US");
            videoGallery.put("gs://gcsvideo/HongKong_Food.mp4#1587304741723181", "zh-HK");
            videoGallery.put("gs://gcsvideo/Shanghai_Food.mp4#1587304718157174", "zh");
            videoGallery.put("gs://gcsvideo/Travis_Japan.mp4#1587304791230327", "ja-JP");*/
            videoGallery.put("gs://gcsvideo/googlework_short.mp4#1587312488793661", "en-US");
            videoGallery.put("gs://gcsvideo/cat.mp4#1587543888759207", "en-US");

            log.info("Total videos to parse: " + videoGallery.size());

            VideoCollections videoEntries = VideoCollections.getInstance();
            
            videoGallery.forEach((url, language) -> {
                Video videoEntry = new Video();
                videoEntry.setVideoURL(url);
                videoEntry.setVideoLanguage(language);
                
                try {
                    VideoParser.analyzeLabels(url, videoEntry);
                } catch (Exception e){
                    System.out.println("Exception while running:\n" + e.getMessage() + "\n");
                    e.printStackTrace();
                }

                log.info("Video Label Analyzing successfully parsed: " + url);

                try {
                    log.info("Parsing video: "+ url);
                    VideoParser.speechTranscription(url, language, videoEntry);
                } catch (Exception e) {
                    System.out.println("Exception while running:\n" + e.getMessage() + "\n");
                    e.printStackTrace();
                }

                log.info("Video Transcription successfully parsed: " + url);

                videoEntries.addToVideoList(videoEntry);
            });

            System.out.println("Video number: " + videoEntries.getVideoList().size());

            for (Video video2Print : videoEntries.getVideoList()) {
                video2Print.printVideoTranscriptDetails();
                video2Print.printVideoLabelCategoryInfo();
            }
            // [END video_speech_transcription_gcs]

            // [START Cloudsearch_Video_Connector]

            /*
            Repository repository = new VideoRepository();
            IndexingConnector connector = new FullTraversalConnector(repository);
            
            IndexingApplication application = new IndexingApplication.Builder(connector, args)
                .build();
            application.start();
            */
            // [END Cloudsearch_Video_Connector]

        } catch (Exception e) {
            System.out.println("Exception while running:\n" + e.getMessage() + "\n");
            e.printStackTrace(System.out);
        }

        
    }
    
    
    
}







