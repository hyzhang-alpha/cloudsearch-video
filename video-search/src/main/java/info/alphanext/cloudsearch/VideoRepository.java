package info.alphanext.cloudsearch;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.cloudsearch.v1.model.Item;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;
import com.google.enterprise.cloudsearch.sdk.CheckpointCloseableIterable;
import com.google.enterprise.cloudsearch.sdk.CheckpointCloseableIterableImpl;
import com.google.enterprise.cloudsearch.sdk.InvalidConfigurationException;
import com.google.enterprise.cloudsearch.sdk.StartupException;
import com.google.enterprise.cloudsearch.sdk.indexing.IndexingItemBuilder;
import com.google.enterprise.cloudsearch.sdk.indexing.IndexingItemBuilder.FieldOrValue;
import com.google.enterprise.cloudsearch.sdk.indexing.IndexingService;
import com.google.enterprise.cloudsearch.sdk.indexing.template.*;
import com.google.enterprise.cloudsearch.sdk.indexing.Acl;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.stream.IntStream;

public class VideoRepository implements Repository {

 /**
     * Log output
     */
    private Logger log = Logger.getLogger(VideoRepository.class.getName());
    private List<Video> videoEntries = null;
    private int numberOfVideos = 0;

    VideoRepository() {
    }

    /**
     * Initializes the connection to GSuiteUpdate
     *
     * @param context the {@link RepositoryContext}, not used here
     */
    @Override
    public void init(RepositoryContext context) throws StartupException {
        log.info("Initializing Repository - GCS Video");

        videoEntries = VideoCollections.getInstance().getVideoList();
        if (videoEntries == null)
            throw new InvalidConfigurationException("Failed to retrive data from video parsing results.");
        else
            numberOfVideos = videoEntries.size();
    }

    /**
     * Performs any data repository shut down code here.
     */
    @Override
    public void close() {
        log.info("Closing repository");
    }

    /**
     * Gets all the data repository documents.
     *
     * This is the core of the {@link Repository} implemented code for a full
     * traversal connector. A complete traversal of the entire data repository is
     * performed here.
     *
     * For this sample there are only a small set of statically created documents
     * defined.
     *
     * @param checkpoint save state from last iteration
     * @return An iterator of {@link RepositoryDoc} instances
     */
    @Override
    public CheckpointCloseableIterable<ApiOperation> getAllDocs(byte[] checkpoint) {
        log.info("Start retrieving all videos.");

        Iterator<ApiOperation> allDocs = IntStream.range(0, numberOfVideos).mapToObj(this::buildDocument).iterator();

        // [START cloud_search_content_sdk_checkpoint_iterator]
        CheckpointCloseableIterable<ApiOperation> iterator = new CheckpointCloseableIterableImpl.Builder<>(allDocs)
                .build();
        // [END cloud_search_content_sdk_checkpoint_iterator]
        return iterator;
    }

    /**
     * Creates a GSuite Update document for indexing.
     *
     * @param id unique local id for the document
     * @return the fully formed document ready for indexing
     */
    private ApiOperation buildDocument(int index) {
        
        Video videoEntry = videoEntries.get(index);

        // Make the document publicly readable within the domain
        Acl acl = new Acl.Builder().setReaders(Collections.singletonList(Acl.getCustomerPrincipal())).build();

        Hasher hasher = Hashing.farmHashFingerprint64().newHasher();
        hasher.putUnencodedChars(videoEntry.getVideoUrl());
        String resourceName = hasher.hash().toString();

        FieldOrValue<String> title = FieldOrValue.withValue(videoEntry.getVideoTitle());
        FieldOrValue<String> url = FieldOrValue.withValue(videoEntry.getVideoPublicUrl());
        
        // Structured data based on the schema
        Multimap<String, Object> structuredData = ArrayListMultimap.create();

        for (String label : videoEntry.getVideoLabelListUnique()) {
            structuredData.put("videoLabel", label);
        }

        for (String category : videoEntry.getVideoCategoryListUnique()) {
            structuredData.put("videoCategory", category);
        }

        String language = videoEntry.getVideoLanguage();
        structuredData.put("videoLanguage", language);
    
        Item item = IndexingItemBuilder.fromConfiguration(resourceName)
            .setTitle(title)
            .setSourceRepositoryUrl(url)
            .setItemType(IndexingItemBuilder.ItemType.CONTENT_ITEM)
            .setObjectType("videoObject")
            .setAcl(acl)
            .setValues(structuredData)
            .setVersion(Longs.toByteArray(System.currentTimeMillis()))
            .build();
    
        // Index the video transcription
        String entryBody = videoEntry.getVideoTranscriptSting();
        ByteArrayContent byteContent = ByteArrayContent.fromString("text/plain", entryBody);
        
        log.info("Document Built Successfully - " + title);

        return new RepositoryDoc.Builder()
            .setItem(item)
            .setContent(byteContent, IndexingService.ContentFormat.TEXT)
            .setRequestMode(IndexingService.RequestMode.SYNCHRONOUS)
            .build();
        
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This method is not required by the FullTraversalConnector and is
     * unimplemented.
     */
    @Override
    public CheckpointCloseableIterable<ApiOperation> getChanges(byte[] checkpoint) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This method is not required by the FullTraversalConnector and is
     * unimplemented.
     */
    @Override
    public CheckpointCloseableIterable<ApiOperation> getIds(byte[] checkpoint) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This method is not required by the FullTraversalConnector and is
     * unimplemented.
     */
    @Override
    public ApiOperation getDoc(Item item) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This method is not required by the FullTraversalConnector and is
     * unimplemented.
     */
    @Override
    public boolean exists(Item item) {
        return false;
    }

}