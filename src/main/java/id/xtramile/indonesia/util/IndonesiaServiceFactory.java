package id.xtramile.indonesia.util;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.InMemoryIndonesiaCache;
import id.xtramile.indonesia.loader.CsvIndonesiaDataLoader;
import id.xtramile.indonesia.service.DefaultIndonesiaService;

/**
 * Factory class for creating IndonesiaService instances.
 * <p>
 * Provides convenient factory methods for creating IndonesiaService with default
 * or custom cache and loader implementations.
 *
 * @author Rigsto
 */
public final class IndonesiaServiceFactory {

    private IndonesiaServiceFactory() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Creates a default IndonesiaService instance.
     * <p>
     * Uses InMemoryIndonesiaCache and CsvIndonesiaDataLoader as default implementations.
     *
     * @return a new IndonesiaService instance with default configurations
     */
    public static IndonesiaService createDefault() {
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();
        IndonesiaDataLoader loader = new CsvIndonesiaDataLoader();
        return new DefaultIndonesiaService(cache, loader);
    }

    /**
     * Creates an IndonesiaService instance with custom cache and loader.
     *
     * @param cache  the cache implementation to use (must not be null)
     * @param loader the data loader implementation to use (must not be null)
     * @return a new IndonesiaService instance with the specified cache and loader
     * @throws NullPointerException if cache or loader is null
     */
    public static IndonesiaService create(IndonesiaDataCache cache, IndonesiaDataLoader loader) {
        if (cache == null) {
            throw new NullPointerException("Cache cannot be null");
        }
        if (loader == null) {
            throw new NullPointerException("Loader cannot be null");
        }
        return new DefaultIndonesiaService(cache, loader);
    }
}
