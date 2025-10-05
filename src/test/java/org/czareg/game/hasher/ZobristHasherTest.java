package org.czareg.game.hasher;

import org.czareg.board.ClassicPieceStartingPositionPlacer;
import org.czareg.game.*;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;
import org.junit.jupiter.api.Test;

import static org.czareg.game.MoveType.MOVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ZobristHasherTest {

    @Test
    void samePositionSameHash() {
        Context ctx1 = ClassicContext.create();
        new ClassicPieceStartingPositionPlacer().place(ctx1.getBoard());

        Context ctx2 = ClassicContext.create();
        new ClassicPieceStartingPositionPlacer().place(ctx2.getBoard());

        ZobristHasher hasher = new ZobristHasher(ctx1.getBoard());

        long hash1 = hasher.computeHash(ctx1);
        long hash2 = hasher.computeHash(ctx2);

        assertEquals(hash1, hash2, "Same position must give same hash");
    }

    @Test
    void identicalBoardCopiesProduceSameHash() {
        Context ctx1 = ClassicContext.create();
        new ClassicPieceStartingPositionPlacer().place(ctx1.getBoard());
        ZobristHasher hasher = new ZobristHasher(ctx1.getBoard());

        Context ctx2 = ctx1.duplicate();

        long hash1 = hasher.computeHash(ctx1);
        long hash2 = hasher.computeHash(ctx2);

        assertEquals(hash1, hash2, "Duplicated board should yield identical hash");
    }


    @Test
    void differentPositionsDifferentHashes() {
        Context ctx1 = ClassicContext.create();
        new ClassicPieceStartingPositionPlacer().place(ctx1.getBoard());

        Context ctx2 = ClassicContext.create();
        new ClassicPieceStartingPositionPlacer().place(ctx2.getBoard());

        // Make a move in ctx2 (e.g., move white pawn e2→e4)
        PositionFactory pf = ctx2.getBoard().getPositionFactory();
        Move pawnMove = new Move(
                ctx2.getBoard().getPiece(pf.create("E", 2)),
                pf.create("E", 2),
                pf.create("E", 4),
                new Metadata(MoveType.INITIAL_DOUBLE_FORWARD)
        );
        ctx2.getMoveMaker().make(ctx2, pawnMove);

        ZobristHasher hasher = new ZobristHasher(ctx1.getBoard());

        long hash1 = hasher.computeHash(ctx1);
        long hash2 = hasher.computeHash(ctx2);

        assertNotEquals(hash1, hash2, "Different positions should give different hashes");
    }

    @Test
    void sideToMoveAffectsHash() {
        Context ctx = ClassicContext.create();
        new ClassicPieceStartingPositionPlacer().place(ctx.getBoard());

        ZobristHasher hasher = new ZobristHasher(ctx.getBoard());

        long hashWhite = hasher.computeHash(ctx);

        // Make a move in ctx2 (e.g., move white pawn e2→e4)
        PositionFactory pf = ctx.getBoard().getPositionFactory();
        Move pawnMove = new Move(
                ctx.getBoard().getPiece(pf.create("E", 2)),
                pf.create("E", 2),
                pf.create("E", 4),
                new Metadata(MoveType.INITIAL_DOUBLE_FORWARD)
        );
        ctx.getHistory().save(pawnMove);
        long hashBlack = hasher.computeHash(ctx);

        assertNotEquals(hashWhite, hashBlack, "Side to move must affect hash");
    }

    @Test
    void movingPiecesBackRestoresHash() {
        // Arrange
        Context ctx = ClassicContext.create();
        new ClassicPieceStartingPositionPlacer().place(ctx.getBoard());
        ZobristHasher hasher = new ZobristHasher(ctx.getBoard());

        long originalHash = hasher.computeHash(ctx);

        PositionFactory pf = ctx.getBoard().getPositionFactory();

        // Use the white knight from g1 → f3 → g1 (legal knight moves)
        Position g1 = pf.create("G", 1);
        Position f3 = pf.create("F", 3);

        // Use the black knight from g8 → f6 → g8 (legal knight moves)
        Position g8 = pf.create("G", 8);
        Position f6 = pf.create("F", 6);

        Move moveWhiteThere = new Move(
                ctx.getBoard().getPiece(g1),
                g1,
                f3,
                new Metadata(MOVE)
        );
        ctx.getMoveMaker().make(ctx, moveWhiteThere);
        Move moveBlackThere = new Move(
                ctx.getBoard().getPiece(g8),
                g8,
                f6,
                new Metadata(MOVE)
        );
        ctx.getMoveMaker().make(ctx, moveBlackThere);

        Move moveWhiteBack = new Move(
                ctx.getBoard().getPiece(f3),
                f3,
                g1,
                new Metadata(MOVE)
        );
        ctx.getMoveMaker().make(ctx, moveWhiteBack);
        Move moveBlackBack = new Move(
                ctx.getBoard().getPiece(f6),
                f6,
                g8,
                new Metadata(MOVE)
        );
        ctx.getMoveMaker().make(ctx, moveBlackBack);
        long restoredHash = hasher.computeHash(ctx);

        // Assert
        assertEquals(originalHash, restoredHash,
                "Hash must be identical after moving knight back to its original square");
    }

    @Test
    void captureChangesHash() {
        Context ctx = ClassicContext.create();
        new ClassicPieceStartingPositionPlacer().place(ctx.getBoard());
        ZobristHasher hasher = new ZobristHasher(ctx.getBoard());
        PositionFactory pf = ctx.getBoard().getPositionFactory();

        // Simulate simple capture: remove black pawn on D7
        ctx.getBoard().removePiece(pf.create("D", 7));

        long hash1 = hasher.computeHash(ctx);

        // Remove another piece
        ctx.getBoard().removePiece(pf.create("E", 7));

        long hash2 = hasher.computeHash(ctx);

        assertNotEquals(hash1, hash2, "Capture/removal must change hash");
    }

}