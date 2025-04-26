package org.czareg;

interface Move {

    IndexPosition getIndexPosition();

    boolean canCapture();

    boolean canJump();
}
