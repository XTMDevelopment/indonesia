package id.xtramile.indonesia.model;

import java.util.Objects;

public class Province {
    private final String id;
    private final String name;

    public Province(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Province province = (Province) o;
        return Objects.equals(id, province.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Province{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
