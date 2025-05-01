package org.czareg.board;

import lombok.Value;

@Value
public class BoardSize {

    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 26;
    private static final int MIN_FILES = 1;
    private static final int MAX_FILES = 26;

    int ranks;
    int files;

    public BoardSize(int ranks, int files) {
        if (ranks < MIN_RANK || ranks > MAX_RANK) {
            throw new IllegalArgumentException("Ranks has to be between <%d,%d>".formatted(MIN_RANK, MAX_RANK));
        }
        if (files < MIN_FILES || files > MAX_FILES) {
            throw new IllegalArgumentException("Files has to be between <%d,%d>".formatted(MIN_FILES, MAX_FILES));
        }
        this.ranks = ranks;
        this.files = files;
    }
}
