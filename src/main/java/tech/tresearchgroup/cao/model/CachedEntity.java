package tech.tresearchgroup.cao.model;

import java.io.Serializable;
import java.util.Arrays;

public class CachedEntity implements Serializable {
    private String date;
    private byte[] data;

    public CachedEntity(String date, byte[] data) {
        this.date = date;
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CachedEntity{" +
            "date='" + date + '\'' +
            ", data=" + Arrays.toString(data) +
            '}';
    }
}
