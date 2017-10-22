package com.jcmowbray.games.conway;

import com.jcmowbray.games.Location;
import io.vavr.Function2;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import java.util.function.Function;
import java.util.function.Predicate;

final class Board implements com.jcmowbray.games.Board {

    private static final Function<Set<Location>, Integer> height = cells -> cells
            .maxBy(location -> location.y())
            .map(location -> location.y()).getOrElse(0);

    private final Function<Integer, Boolean> edge;
    private final Lazy<Map<Location, Cell>> cells;

    private final Lazy<Map<Integer, Row>> rows;

    private static String concat(String a, String b) {
        return a + b;
    }


    @Override
    public String show() {
        return this.rows.get().values().map(Row::show).fold("", Board::concat);
    }

    public Option<Cell> cell(Location location) {
        return cells.get().get(location);
    }


    public Option<Row> row(Integer row) {
        List<Cell> cells = this.cells.get().filterKeys(location -> location.y() == row).values().toList();
        return cells.isEmpty() ? Option.none() : Option.some(new Row(cells, row, edge.apply(row)));
    }

    @Override
    public Board turn() {
        return new Board(this.cells.get().mapValues(Cell::turn));
    }

    private Board(Map<Location, Cell> cells) {
        this.cells = Lazy.of(() -> cells);
        this.edge = row -> Board.height.apply(this.cells.get().keySet()) == row;
        this.rows = Lazy.of(() -> this.cells.get().values().toList()
                .groupBy(cell -> cell.location.y())
                .map((r, cs) -> Tuple.of(r, new Row(cs, r, this.edge.apply(r)))));
    }

    static Function2<Integer, Integer, List<Location>> locations =
            (height, width) -> List.range(0, height)
                    .flatMap(y -> List.range(0, width)
                            .map(x -> new Location(x, y)));

    static Function<Location, Predicate<Location>> allowed =
            size -> (location -> location.x() > - 1 && location.x() < size.x() && location.y() > -1 && location.y() < size.y());




    public Board(final Integer height, final Integer width, Function<Board, Function<Location, Cell>> cellGenerator) {

        final Function<List<Location>, Map<Location, Cell>> boardGenerator =
                ls -> ls.map(l ->
                        cellGenerator.apply(this).apply(l)).groupBy(c -> c.location).map((l, cs) ->
                Tuple.of(l, cs.head()));

        this.cells = Lazy.of(() -> locations.andThen(boardGenerator).apply(height, width));
        this.edge = row -> height == row;
        this.rows = Lazy.of(() -> this.cells.get().values().toList()
                .groupBy(cell -> cell.location.y())
                .map((r, cells) -> Tuple.of(r, new Row(cells, r, Board.height.apply(this.cells.get().keySet()) == r))));

        this.neighbors = c -> List.of(-1, 0, 1)
                .flatMap(dy -> List.of(-1, 0, 1).map(dx -> c.location.move(dx, dy))
                        .filter(allowed.apply(new Location(height, width)).and(l -> !l.equals(c.location))))
                .map(l -> cells.get().apply(l));

    }


    protected Function<Cell, List<Cell>> neighbors;
}
