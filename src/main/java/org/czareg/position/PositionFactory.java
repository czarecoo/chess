package org.czareg.position;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class PositionFactory {

    private final List<Integer> allowedRankValues;
    private final List<String> allowedFileValues;

    public PositionFactory(int ranks, int files) {
        allowedRankValues = IntStream.rangeClosed(1, 26).limit(ranks).boxed().toList();
        allowedFileValues = IntStream.rangeClosed('A', 'Z').limit(files).mapToObj(Character::toString).toList();
    }

    public Position create(int rank, String file) {
        if (!allowedRankValues.contains(rank)) {
            String message = "Illegal rank: %s allowed values: %s".formatted(rank, allowedRankValues);
            throw new IllegalArgumentException(message);
        }
        if (!allowedFileValues.contains(file)) {
            String message = "Illegal file: %s allowed values: %s".formatted(file, allowedRankValues);
            throw new IllegalArgumentException(message);
        }
        return new Position(rank, file);
    }

    public Position create(int rankIndex, int fileIndex) {
        if (isRankIndexInvalid(rankIndex)) {
            throw new IllegalArgumentException("Rank index out of bounds: " + rankIndex);
        }
        if (isFileIndexInvalid(fileIndex)) {
            throw new IllegalArgumentException("File index out of bounds: " + fileIndex);
        }
        return new Position(allowedRankValues.get(rankIndex), allowedFileValues.get(fileIndex));
    }

    public Index create(Position position) {
        int rankIndex = position.getRank() - 1;
        int fileIndex = allowedFileValues.indexOf(position.getFile());
        return new Index(rankIndex, fileIndex);
    }

    public Optional<Position> create(Index index, IndexChange indexChange) {
        int changedRankIndex = index.getRank() + indexChange.getRank();
        int changedFileIndex = index.getFile() + indexChange.getFile();
        if (isRankIndexInvalid(changedRankIndex) || isFileIndexInvalid(changedFileIndex)) {
            return Optional.empty();
        }
        Position position = create(changedRankIndex, changedFileIndex);
        return Optional.of(position);
    }

    private boolean isRankIndexInvalid(int rankIndex) {
        return rankIndex < 0 || rankIndex >= allowedRankValues.size();
    }

    private boolean isFileIndexInvalid(int fileIndex) {
        return fileIndex < 0 || fileIndex >= allowedFileValues.size();
    }
}
