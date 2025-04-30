package org.czareg;

interface Order {

    Player getNowMovingPlayer(Player lastMovingPlayer);

    Player startingPlayer();
}
