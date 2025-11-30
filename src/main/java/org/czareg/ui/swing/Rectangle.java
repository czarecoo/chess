package org.czareg.ui.swing;

record Rectangle(int width, int height) {

    int getShorterSide() {
        return Math.min(width, height);
    }
}
