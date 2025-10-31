package id.xtramile.indonesia.util;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.InMemoryIndonesiaCache;
import id.xtramile.indonesia.loader.CsvIndonesiaDataLoader;
import id.xtramile.indonesia.service.DefaultIndonesiaService;

public final class IndonesiaServiceFactory {

    private IndonesiaServiceFactory() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static IndonesiaService createDefault() {
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();
        IndonesiaDataLoader loader = new CsvIndonesiaDataLoader();
        return new DefaultIndonesiaService(cache, loader);
    }

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
