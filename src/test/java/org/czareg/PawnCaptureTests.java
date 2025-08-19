package org.czareg;

import org.czareg.game.Metadata;
import org.czareg.game.Move;
import org.czareg.game.MoveType;
import org.czareg.move.piece.PieceMoveGenerator;
import org.czareg.move.piece.pawn.PawnCaptureMoveGenerator;
import org.czareg.piece.Knight;
import org.czareg.piece.Pawn;
import org.czareg.position.IndexChange;
import org.czareg.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.czareg.game.Metadata.Key.CAPTURE_PIECE;
import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class PawnCaptureTests extends BaseTests {

    private PieceMoveGenerator pieceMoveGenerator;

    @BeforeEach
    void setUp() {
        pieceMoveGenerator = new PawnCaptureMoveGenerator();
    }

    @Test
    void givenWhitePawnOnSeventhRank_whenCapturingLeftToEighth_thenExceptionIsThrown() { //only promotion capture can capture to eighth rank
        Knight blackKnight = new Knight(BLACK);
        Position c8 = pf.create("C", 8);
        board.placePiece(c8, blackKnight);
        Pawn whitePawn = new Pawn(WHITE);
        Position d7 = pf.create("D", 7);
        board.placePiece(d7, whitePawn);
        Optional<Move> optionalCaptureMove = pieceMoveGenerator.generate(context, whitePawn, d7, new IndexChange(-1, 1));
        assertTrue(optionalCaptureMove.isEmpty());
        Move captureMove = new Move(whitePawn, d7, c8, new Metadata(MoveType.CAPTURE).put(CAPTURE_PIECE, blackKnight));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> moveMaker.make(context, captureMove));
        assertEquals("Move is not legal Move(piece=Pawn(player=WHITE), start=Position(file=D, rank=7), end=Position(file=C, rank=8), metadata=Metadata(data={MOVE_TYPE=CAPTURE, CAPTURE_PIECE=Knight(player=BLACK)}))", e.getMessage());
    }
}
