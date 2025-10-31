package id.xtramile.indonesia.model;

public class Indonesia {
    private final Province province;
    private final City city;
    private final District district;
    private final Village village;

    public Indonesia(Province province, City city, District district, Village village) {
        this.province = province;
        this.city = city;
        this.district = district;
        this.village = village;
    }

    public Province getProvince() {
        return province;
    }

    public City getCity() {
        return city;
    }

    public District getDistrict() {
        return district;
    }

    public Village getVillage() {
        return village;
    }

    @Override
    public String toString() {
        return "Indonesia{" +
                "province=" + province +
                ", city=" + city +
                ", district=" + district +
                ", village=" + village +
                '}';
    }
}

