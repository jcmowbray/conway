package com.jcmowbray.games.conway;

import com.jcmowbray.games.Location;
import io.vavr.Lazy;
import io.vavr.collection.List;

import java.util.function.Function;

final class Cell {

    static class Generator {
        static final Function<Integer, Function<Board, Function<Location, Cell>>> CELL_EQ_SPACE =
                space -> board -> position -> new Cell(board, position, (position.x() + position.y()) % space == 0);

    }


    final Location location;
    private final Boolean alive;
    private final Board board;
    private Lazy<List<Cell>> neighbors;

    public Cell(Board board, Location location, Boolean alive) {
        this.location = location;
        this.alive = alive;
        this.board = board;
        neighbors = Lazy.of(() -> board.neighbors.apply(this));
    }


    public Boolean survive() {
        int n = neighbors.get().count(c -> c.alive);
        return alive &&  n > 2 && n < 4;
    }

    public Boolean alive() {
        int n = neighbors.get().count(c -> c.alive);
        return !alive && n > 3;
    }

    public Cell turn() {
        return new Cell(this.board, this.location, alive() || survive());
    }

    @Override
    public String toString() {
        return "Cell{" +
                "location=" + location +
                ", alive=" + alive +
                '}';
    }

    public String show() {
        return alive ? "o" : "*";
    }
}
