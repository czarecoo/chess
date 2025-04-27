package org.czareg;

import lombok.Value;

@Value
class ClassicPosition implements Position {

    ClassicRank rank;
    ClassicFile file;

    IndexPosition toIndexPosition() {
        return new IndexPosition(getRank().getIndex(), getFile().getIndex());
    }

    IndexPosition toIndexPosition(IndexChange indexChange) {
        return new IndexPosition(
                getRank().getIndex() + indexChange.rankIndexChange(),
                getFile().getIndex() + indexChange.fileIndexChange()
        );
    }

    static ClassicPosition from(int rank, String file) {
        return new ClassicPosition(new ClassicRank(rank), new ClassicFile(file));
    }

    static boolean isValid(IndexPosition indexPosition) {
        return ClassicRank.isIndexValid(indexPosition.rankIndex()) && ClassicFile.isIndexValid(indexPosition.fileIndex());
    }
}
