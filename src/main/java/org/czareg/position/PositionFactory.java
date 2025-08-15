package org.czareg.position;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Getter
public class PositionFactory {

    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 26;
    private static final int MIN_FILES = 1;
    private static final int MAX_FILES = 26;

    int minRank;
    int minFile;
    int maxRank;
    int maxFile;
    private final List<Integer> allowedRankValues;
    private final List<String> allowedFileValues;

    public PositionFactory(int maxRank, int maxFile) {
        if (maxRank < MIN_RANK || maxRank > MAX_RANK) {
            throw new IllegalArgumentException("Ranks has to be between <%d,%d>".formatted(MIN_RANK, MAX_RANK));
        }
        if (maxFile < MIN_FILES || maxFile > MAX_FILES) {
            throw new IllegalArgumentException("Files has to be between <%d,%d>".formatted(MIN_FILES, MAX_FILES));
        }
        this.minRank = MIN_RANK;
        this.minFile = MIN_FILES;
        this.maxRank = maxRank;
        this.maxFile = maxFile;
        allowedRankValues = IntStream.rangeClosed(1, maxRank).boxed().toList();
        allowedFileValues = IntStream.rangeClosed('A', 'Z').limit(maxFile).mapToObj(Character::toString).toList();
    }

    public Position create(int rank, String file) {
        if (!allowedRankValues.contains(rank)) {
            String message = "Illegal rank %s allowed values %s".formatted(rank, allowedRankValues);
            throw new IllegalArgumentException(message);
        }
        if (!allowedFileValues.contains(file)) {
            String message = "Illegal file %s allowed values %s".formatted(file, allowedRankValues);
            throw new IllegalArgumentException(message);
        }
        return new Position(rank, file);
    }

    public Position create(int rankIndex, int fileIndex) {
        if (isRankIndexInvalid(rankIndex)) {
            throw new IllegalArgumentException("Rank index out of bounds " + rankIndex);
        }
        if (isFileIndexInvalid(fileIndex)) {
            throw new IllegalArgumentException("File index out of bounds " + fileIndex);
        }
        return new Position(allowedRankValues.get(rankIndex), allowedFileValues.get(fileIndex));
    }

    public Index create(Position position) {
        int rankIndex = position.getRank() - 1;
        int fileIndex = allowedFileValues.indexOf(position.getFile());
        return new Index(rankIndex, fileIndex);
    }

    public Optional<Position> create(Index index, IndexChange indexChange) {
        int changedRankIndex = index.getRank() + indexChange.getRankChange();
        int changedFileIndex = index.getFile() + indexChange.getFileChange();
        if (isRankIndexInvalid(changedRankIndex) || isFileIndexInvalid(changedFileIndex)) {
            return Optional.empty();
        }
        Position position = create(changedRankIndex, changedFileIndex);
        return Optional.of(position);
    }

    public IndexChange create(Position currentPosition, Position endPosition) {
        Index currentIndex = create(currentPosition);
        Index endIndex = create(endPosition);
        int rankChange = endIndex.getRank() - currentIndex.getRank();
        int fileChange = endIndex.getFile() - currentIndex.getFile();
        return new IndexChange(rankChange, fileChange);
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
            between.add(create(rank, file));
            file += fileStep;
            rank += rankStep;
        }

        return between;
    }

    private boolean isRankIndexInvalid(int rankIndex) {
        return rankIndex < 0 || rankIndex >= allowedRankValues.size();
    }

    private boolean isFileIndexInvalid(int fileIndex) {
        return fileIndex < 0 || fileIndex >= allowedFileValues.size();
    }
}
