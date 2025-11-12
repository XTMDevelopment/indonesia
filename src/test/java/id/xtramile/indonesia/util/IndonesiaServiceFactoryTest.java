package id.xtramile.indonesia.util;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.IndonesiaDataLoader;
import id.xtramile.indonesia.IndonesiaService;
import id.xtramile.indonesia.cache.InMemoryIndonesiaCache;
import id.xtramile.indonesia.loader.CsvIndonesiaDataLoader;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

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
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();
        IndonesiaDataLoader loader = new CsvIndonesiaDataLoader();

        IndonesiaService service = IndonesiaServiceFactory.create(cache, loader);
        assertNotNull(service);
    }

    @Test
    void testCreateThrowsNullPointerExceptionWhenCacheIsNull() {
        IndonesiaDataLoader loader = new CsvIndonesiaDataLoader();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> IndonesiaServiceFactory.create(null, loader)
        );

        assertEquals("Cache cannot be null", exception.getMessage());
    }

    @Test
    void testCreateThrowsNullPointerExceptionWhenLoaderIsNull() {
        IndonesiaDataCache cache = new InMemoryIndonesiaCache();

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
                Constructor<IndonesiaServiceFactory> constructor =
                        IndonesiaServiceFactory.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();

            } catch (Exception e) {
                if (e.getCause() instanceof AssertionError) {
                    throw e.getCause();
                }

                throw new RuntimeException(e);
            }
        });
    }
}

