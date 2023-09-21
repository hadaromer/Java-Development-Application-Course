package model;

import javafx.scene.control.TreeItem;
import utils.enums;

public class SimulationTreeItem extends TreeItem<String> {
    private enums.DetailType detailType;
    private Object data;

    public SimulationTreeItem(String name, enums.DetailType type) {
        super(name);
        this.detailType = type;
    }

    public SimulationTreeItem(String name, enums.DetailType type,Object data) {
        super(name);
        this.detailType = type;
        this.data = data;
    }

    public enums.DetailType getDetailType() {
        return detailType;
    }

    public Object getData() {
        return data;
    }
}
