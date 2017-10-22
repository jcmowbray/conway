package com.jcmowbray.games.conway;

import io.vavr.collection.List;

final class Row {

    private final List<Cell> cells;
    private final Integer row;
    private final Boolean last;

    public Row(List<Cell> cells, Integer row, Boolean last) {
        this.cells = cells;
        this.row = row;
        this.last = last;
    }

    String drawEdge() {
        return cells.map(cell -> "-").reduce(String::concat) + "\n";
    }

    String show() {
        return (row == 0 ? drawEdge() : "") +
                cells.map(Cell::show).reduce(String::concat)+ "\n" +
                (last ? "" : drawEdge());
    }
}
