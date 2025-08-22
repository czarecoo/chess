package org.czareg.position;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Getter
public class PositionFactory {

    private static final int MIN_FILES = 1;
    private static final int MAX_FILES = 26;
    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 26;

    int minFile;
    int minRank;
    int maxFile;
    int maxRank;
    private final List<String> allowedFileValues;
    private final List<Integer> allowedRankValues;

    public PositionFactory(int maxFile, int maxRank) {
        if (maxFile < MIN_FILES || maxFile > MAX_FILES) {
            throw new IllegalArgumentException("Files has to be between <%d,%d>".formatted(MIN_FILES, MAX_FILES));
        }
        if (maxRank < MIN_RANK || maxRank > MAX_RANK) {
            throw new IllegalArgumentException("Ranks has to be between <%d,%d>".formatted(MIN_RANK, MAX_RANK));
        }
        this.minFile = MIN_FILES;
        this.minRank = MIN_RANK;
        this.maxFile = maxFile;
        this.maxRank = maxRank;
        allowedFileValues = IntStream.rangeClosed('A', 'Z').limit(maxFile).mapToObj(Character::toString).toList();
        allowedRankValues = IntStream.rangeClosed(1, maxRank).boxed().toList();
    }

    public Position create(String file, int rank) {
        if (!allowedFileValues.contains(file)) {
            String message = "Illegal file %s allowed values %s".formatted(file, allowedRankValues);
            throw new IllegalArgumentException(message);
        }
        if (!allowedRankValues.contains(rank)) {
            String message = "Illegal rank %s allowed values %s".formatted(rank, allowedRankValues);
            throw new IllegalArgumentException(message);
        }
        return new Position(file, rank);
    }

    public Position create(int fileIndex, int rankIndex) {
        if (isFileIndexInvalid(fileIndex)) {
            throw new IllegalArgumentException("File index out of bounds " + fileIndex);
        }
        if (isRankIndexInvalid(rankIndex)) {
            throw new IllegalArgumentException("Rank index out of bounds " + rankIndex);
        }
        return new Position(allowedFileValues.get(fileIndex), allowedRankValues.get(rankIndex));
    }

    public Index create(Position position) {
        int fileIndex = allowedFileValues.indexOf(position.getFile());
        int rankIndex = position.getRank() - 1;
        return new Index(fileIndex, rankIndex);
    }

    public Optional<Position> create(Index index, IndexChange indexChange) {
        int changedFileIndex = index.getFile() + indexChange.getFileChange();
        int changedRankIndex = index.getRank() + indexChange.getRankChange();
        if (isFileIndexInvalid(changedFileIndex) || isRankIndexInvalid(changedRankIndex)) {
            return Optional.empty();
        }
        Position position = create(changedFileIndex, changedRankIndex);
        return Optional.of(position);
    }

    public IndexChange create(Position currentPosition, Position endPosition) {
        Index currentIndex = create(currentPosition);
        Index endIndex = create(endPosition);
        int fileChange = endIndex.getFile() - currentIndex.getFile();
        int rankChange = endIndex.getRank() - currentIndex.getRank();
        return new IndexChange(fileChange, rankChange);
    }

    /*
    Convention:
    light squares are where file + rank is odd,
    and dark squares are where file + rank is even.
     */
    public boolean isLightSquare(Position position) {
        Index index = create(position);
        return (index.getFile() + index.getRank()) % 2 != 0;
    }

    public boolean isDarkSquare(Position position) {
        return !isLightSquare(position);
    }

    public List<Position> between(Index start, Index end) {
        List<Position> between = new ArrayList<>();
        int startFile = start.getFile();
        int endFile = end.getFile();
        int startRank = start.getRank();
        int endRank = end.getRank();

        int fileDiff = Math.abs(startFile - endFile);
        int rankDiff = Math.abs(startRank - endRank);

        boolean isOrthogonal = (startRank == endRank) || (startFile == endFile);
        boolean isDiagonal = (fileDiff == rankDiff);

        if (!isOrthogonal && !isDiagonal) {
            throw new IllegalArgumentException(
                    "Indexes are neither orthogonal nor diagonal: " + start + " -> " + end
            );
        }

        int fileStep = Integer.compare(endFile, startFile);
        int rankStep = Integer.compare(endRank, startRank);

        int file = startFile + fileStep;
        int rank = startRank + rankStep;

        while (file != endFile || rank != endRank) {
            between.add(create(file, rank));
            file += fileStep;
            rank += rankStep;
        }

        return between;
    }

    private boolean isFileIndexInvalid(int fileIndex) {
        return fileIndex < 0 || fileIndex >= allowedFileValues.size();
    }

    private boolean isRankIndexInvalid(int rankIndex) {
        return rankIndex < 0 || rankIndex >= allowedRankValues.size();
    }
}
