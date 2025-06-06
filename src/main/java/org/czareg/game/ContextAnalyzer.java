package org.czareg.game;

public class ContextAnalyzer {

    public boolean isOver(Context context) {
        return isCheckMate(context) || isStalemate(context) || isDrawnBy75MoveRule(context) || isInsufficientMaterial(context);
    }

    /*
     A checkmate occurs when a king is placed in check and has no legal moves to escape.
     */
    public boolean isCheckMate(Context context) {
        return false;
    }

    /*
    A stalemate occurs when the player whose turn it is to move has no legal moves, but their king is not in check.
    */
    public boolean isStalemate(Context context) {
        return false;
    }

    /*
    The 50-move rule states that a player CAN CLAIM a draw in chess if no one moves a pawn or captures a piece for 50 consecutive moves.
    After 75 moves, the game is AUTOMATICALLY DRAWN - regardless of if a player has claimed the 50 move rule.
     */
    public boolean isDrawnBy75MoveRule(Context context) {
        return false;
    }

    /*
    The game is declared a draw whenever both sides do not have the "sufficient material" to force a checkmate.
    Insufficient material (no checkmates are possible or no checkmates can be forced):
    - King vs king
    - King + bishop or knight vs king
    - King + Bishop vs. King + Bishop (both bishops on the same color)
    - King + 2 knights vs King

    In the specific case of two knights versus a lone king
    USCF (United States Chess Federation): The rule specifies a game is drawn if there is no forced mate.
    FIDE (International Chess Federation): The rule states a game is only drawn when a checkmate is absolutely impossible,
    meaning that two knights versus a lone king may not be an automatic draw since a checkmate could theoretically occur
    if the lone king 'helps' you by making specific moves to allow the checkmate.

    Chess.com follows the USCF rule in this case and calls two knights insufficient mating material because the checkmate can not be forced.

    I am going with USCF rule also.
     */
    public boolean isInsufficientMaterial(Context context) {
        return false;
    }
}
