package org.czareg.game;

public interface StateAnalyzer {
    boolean isOver(Context context);

    /*
     A checkmate occurs when a king is placed in check and has no legal moves to escape.
     */
    boolean isCheckMate(Context context);

    /*
        A stalemate occurs when the player whose turn it is to move has no legal moves, but their king is not in check.
        */
    boolean isStalemate(Context context);

    /*
        The 50-move rule states that a player CAN CLAIM a draw in chess if no one moves a pawn or captures a piece for 50 consecutive moves.
        After 75 moves, the game is AUTOMATICALLY DRAWN - regardless of if a player has claimed the 50 move rule.
         */
    boolean isDrawnBy75MoveRule(Context context);

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
    boolean isInsufficientMaterial(Context context);
}
