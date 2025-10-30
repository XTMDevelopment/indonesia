package id.xtramile.indonesia.model;

import java.util.Objects;

public class District {
    private final String id;
    private final String name;
    private final String cityId;
    private final String provinceId;

    public District(String id, String name, String cityId, String provinceId) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.provinceId = provinceId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCityId() {
        return cityId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        District district = (District) o;
        return Objects.equals(id, district.id);
    }

    @Override
    public String toString() {
        return "District{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cityId='" + cityId + '\'' +
                ", provinceId='" + provinceId + '\'' +
                '}';
    }
}
