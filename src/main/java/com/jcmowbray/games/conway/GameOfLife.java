package com.jcmowbray.games.conway;

import com.jcmowbray.games.Game;
import com.jcmowbray.games.Location;
import io.vavr.collection.Stream;

import java.util.function.Function;

import static java.lang.System.out;


public class GameOfLife implements Game {


    private final Board zero;

    public GameOfLife(Integer height, Integer width, Function<Board, Function<Location, Cell>> generator) {
        this.zero = new Board(height, width, generator);
    }

    public GameOfLife(Integer height, Integer width) {
        this(height, width, Cell.Generator.CELL_EQ_SPACE.apply(2));
    }



    @Override
    public Stream<? extends Board> play() {
        return Stream.iterate(zero, Board::turn);
    }


    public static void main(String [] args) {
        GameOfLife game = new GameOfLife(10, 10);
        game.play().take(4).map(Board::show).forEach(out::println);
    }

}
