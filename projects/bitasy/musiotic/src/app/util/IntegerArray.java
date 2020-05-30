package util;

import java.util.Arrays;

public class IntegerArray {
    private Integer[] arrayInstance;

    public IntegerArray(Integer[] a) {
        this.arrayInstance = a;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(arrayInstance);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IntegerArray other = (IntegerArray) obj;
        if (!Arrays.deepEquals(arrayInstance, other.arrayInstance))
            return false;
        return true;
    }
}
