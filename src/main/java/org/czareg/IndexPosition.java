package org.czareg;

record IndexPosition(int rankIndex, int fileIndex) {

    ClassicPosition toPosition() {
        return new ClassicPosition(ClassicRank.fromIndex(rankIndex), ClassicFile.fromIndex(fileIndex));
    }
}
