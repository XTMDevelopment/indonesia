package id.xtramile.indonesia.model;

import java.util.Objects;

public class City {
    private final String id;
    private final String name;
    private final String provinceId;

    public City(String id, String name, String provinceId) {
        this.id = id;
        this.name = name;
        this.provinceId = provinceId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProvinceId() {
        return provinceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(id, city.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", provinceId='" + provinceId + '\'' +
                '}';
    }
}
