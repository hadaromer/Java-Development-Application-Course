package classes.dto;

import classes.Grid;

public class GridDTO {
    private int rows;
    private int columns;

    public GridDTO(Grid grid) {
        this.rows = grid.getRows();
        this.columns = grid.getColumns();
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}
