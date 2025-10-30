package id.xtramile.indonesia.model;

import java.util.Objects;

public class Village {
    private final String id;
    private final String name;
    private final String districtId;
    private final String cityId;
    private final String provinceId;

    public Village(String id, String name, String districtId, String cityId, String provinceId) {
        this.id = id;
        this.name = name;
        this.districtId = districtId;
        this.cityId = cityId;
        this.provinceId = provinceId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDistrictId() {
        return districtId;
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
        Village village = (Village) o;
        return Objects.equals(id, village.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Village{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", districtId='" + districtId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", provinceId='" + provinceId + '\'' +
                '}';
    }
}
