package id.xtramile.indonesia.util;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.IndonesiaService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndonesiaServiceFactoryTest {

    @Test
    void testCreateDefault() {
        IndonesiaService service = IndonesiaServiceFactory.createDefault();
        assertNotNull(service);
        assertTrue(service.isDataLoaded());
    }

    @Test
    void testCreateWithCustomCacheAndLoader() {
        IndonesiaDataCache cache = new id.xtramile.indonesia.cache.InMemoryIndonesiaCache();
        IndonesiaDataLoader loader = new id.xtramile.indonesia.loader.CsvIndonesiaDataLoader();

        IndonesiaService service = IndonesiaServiceFactory.create(cache, loader);
        assertNotNull(service);
    }

    @Test
    void testCreateThrowsNullPointerExceptionWhenCacheIsNull() {
        IndonesiaDataLoader loader = new id.xtramile.indonesia.loader.CsvIndonesiaDataLoader();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> IndonesiaServiceFactory.create(null, loader)
        );
        assertEquals("Cache cannot be null", exception.getMessage());
    }

    @Test
    void testCreateThrowsNullPointerExceptionWhenLoaderIsNull() {
        IndonesiaDataCache cache = new id.xtramile.indonesia.cache.InMemoryIndonesiaCache();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> IndonesiaServiceFactory.create(cache, null)
        );
        assertEquals("Loader cannot be null", exception.getMessage());
    }

    @Test
    void testConstructorThrowsAssertionError() {
        assertThrows(AssertionError.class, () -> {
            try {
                java.lang.reflect.Constructor<IndonesiaServiceFactory> constructor =
                        IndonesiaServiceFactory.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            } catch (Exception e) {
                if (e.getCause() instanceof AssertionError) {
                    throw (AssertionError) e.getCause();
                }
                throw new RuntimeException(e);
            }
        });
    }
}

