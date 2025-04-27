package org.czareg;

interface Position {

    Rank getRank();
    File getFile();

    IndexPosition toIndexPosition();
}
