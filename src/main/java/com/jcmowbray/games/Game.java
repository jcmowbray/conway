package com.jcmowbray.games;

import io.vavr.collection.Stream;

public interface Game {

    Stream<? extends Board> play();

}
