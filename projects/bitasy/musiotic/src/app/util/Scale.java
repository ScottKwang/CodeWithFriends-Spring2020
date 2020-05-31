package util;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class Scale {
    private List<SimpleStringProperty> names;
    private List<SimpleIntegerProperty> values;

    public Scale(List<SimpleStringProperty> names, List<SimpleIntegerProperty> values) {
        this.names = names;
        this.values = values;
    }


    public List<SimpleStringProperty> getNames() {
        return names;
    }

    public void setNames(List<SimpleStringProperty> names) {
        this.names = names;
    }

    public List<SimpleIntegerProperty> getValues() {
        return values;
    }

    public void setValues(List<SimpleIntegerProperty> values) {
        this.values = values;
    }
}
