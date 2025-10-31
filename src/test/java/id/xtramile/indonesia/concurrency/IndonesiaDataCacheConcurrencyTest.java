package id.xtramile.indonesia.concurrency;

import id.xtramile.indonesia.IndonesiaDataCache;
import id.xtramile.indonesia.cache.InMemoryIndonesiaCache;
import id.xtramile.indonesia.model.City;
import id.xtramile.indonesia.model.District;
import id.xtramile.indonesia.model.Province;
import id.xtramile.indonesia.model.Village;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class IndonesiaDataCacheConcurrencyTest {

    private IndonesiaDataCache cache;

    @BeforeEach
    void setUp() {
        cache = new InMemoryIndonesiaCache();
    }

    @Test
    void testConcurrentPutAndGet() throws InterruptedException {
        Map<Long, Province> provinces = createTestProvinces(100);
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        AtomicInteger successCount = new AtomicInteger(0);
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    cache.putProvinces(provinces);
                    Map<Long, Province> retrieved = cache.getProvinces();
                    if (retrieved.size() == provinces.size()) {
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    fail("Exception during concurrent operation: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertTrue(successCount.get() > 0);
        assertEquals(provinces.size(), cache.getProvinces().size());
    }

    @Test
    void testConcurrentReadOperations() throws InterruptedException {
        Map<Long, Province> provinces = createTestProvinces(50);
        Map<Long, City> cities = createTestCities(100);
        
        cache.putProvinces(provinces);
        cache.putCities(cities);
        
        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(20);
        AtomicInteger readCount = new AtomicInteger(0);
        
        for (int i = 0; i < 20; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        cache.getProvinces();
                        cache.getCities();
                        cache.getCitiesByProvince();
                        cache.getStats();
                        readCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    fail("Exception during concurrent read: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertEquals(200, readCount.get());
        assertEquals(provinces.size(), cache.getProvinces().size());
        assertEquals(cities.size(), cache.getCities().size());
    }

    @Test
    void testConcurrentRefresh() throws InterruptedException {
        Map<Long, Province> provinces = createTestProvinces(50);
        cache.putProvinces(provinces);
        
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        AtomicInteger refreshCount = new AtomicInteger(0);
        
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                try {
                    cache.refresh();
                    refreshCount.incrementAndGet();
                } catch (Exception e) {
                    fail("Exception during concurrent refresh: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(3, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertEquals(5, refreshCount.get());
        assertFalse(cache.isLoaded());
        assertTrue(cache.getProvinces().isEmpty());
    }

    @Test
    void testConcurrentPutDifferentEntities() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(4);
        
        executor.submit(() -> {
            try {
                cache.putProvinces(createTestProvinces(10));
            } finally {
                latch.countDown();
            }
        });
        
        executor.submit(() -> {
            try {
                cache.putCities(createTestCities(20));
            } finally {
                latch.countDown();
            }
        });
        
        executor.submit(() -> {
            try {
                cache.putDistricts(createTestDistricts(30));
            } finally {
                latch.countDown();
            }
        });
        
        executor.submit(() -> {
            try {
                cache.putVillages(createTestVillages(40));
            } finally {
                latch.countDown();
            }
        });
        
        latch.await(3, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertTrue(cache.isLoaded());
        assertFalse(cache.getProvinces().isEmpty());
        assertFalse(cache.getCities().isEmpty());
        assertFalse(cache.getDistricts().isEmpty());
        assertFalse(cache.getVillages().isEmpty());
    }

    @Test
    void testConcurrentReadDuringWrite() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        AtomicInteger readSuccess = new AtomicInteger(0);
        
        // Start writing thread
        executor.submit(() -> {
            for (int i = 0; i < 10; i++) {
                cache.putProvinces(createTestProvinces(10 + i));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // Start multiple reading threads
        for (int i = 0; i < 9; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 20; j++) {
                        Map<Long, Province> result = cache.getProvinces();
                        if (result != null) {
                            readSuccess.incrementAndGet();
                        }
                        Thread.sleep(5);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        executor.submit(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });
        
        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertTrue(readSuccess.get() > 0);
    }

    @Test
    void testConcurrentStatsAccess() throws InterruptedException {
        cache.putProvinces(createTestProvinces(100));
        cache.putCities(createTestCities(200));
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        List<Integer> statsValues = Collections.synchronizedList(new ArrayList<>());
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 50; j++) {
                        int provinceCount = cache.getStats().getProvinceCount();
                        statsValues.add(provinceCount);
                        cache.getStats().getCityCount();
                    }
                } catch (Exception e) {
                    fail("Exception accessing stats: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(3, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertEquals(500, statsValues.size());
        assertTrue(statsValues.stream().allMatch(count -> count >= 0));
    }

    @Test
    void testThreadSafetyOfIndexedMaps() throws InterruptedException {
        Map<Long, City> cities = createTestCities(100);
        cache.putCities(cities);
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        AtomicInteger successCount = new AtomicInteger(0);
        
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 20; j++) {
                        Map<Long, List<City>> citiesByProvince = cache.getCitiesByProvince();
                        if (citiesByProvince != null) {
                            int total = citiesByProvince.values().stream()
                                    .mapToInt(List::size)
                                    .sum();
                            if (total == cities.size()) {
                                successCount.incrementAndGet();
                            }
                        }
                    }
                } catch (Exception e) {
                    fail("Exception accessing indexed maps: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(3, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertTrue(successCount.get() > 0);
    }

    private Map<Long, Province> createTestProvinces(int count) {
        Map<Long, Province> provinces = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            long code = i;
            Province province = new Province(code, "Province " + i, -6.0 + i * 0.1, 106.0 + i * 0.1);
            provinces.put(code, province);
        }
        return provinces;
    }

    private Map<Long, City> createTestCities(int count) {
        Map<Long, City> cities = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            long code = 1000L + i;
            long provinceCode = (i % 10) + 1;
            City city = new City(code, provinceCode, "City " + i, -6.0 + i * 0.01, 106.0 + i * 0.01);
            cities.put(code, city);
        }
        return cities;
    }

    private Map<Long, District> createTestDistricts(int count) {
        Map<Long, District> districts = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            long code = 100000L + i;
            long cityCode = 1000L + (i % 20);
            District district = new District(code, cityCode, "District " + i, -6.0 + i * 0.001, 106.0 + i * 0.001);
            districts.put(code, district);
        }
        return districts;
    }

    private Map<Long, Village> createTestVillages(int count) {
        Map<Long, Village> villages = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            long code = 1000000000L + i;
            long districtCode = 100000L + (i % 30);
            Village village = new Village(code, districtCode, "Village " + i, -6.0 + i * 0.0001, 106.0 + i * 0.0001);
            villages.put(code, village);
        }
        return villages;
    }
}
