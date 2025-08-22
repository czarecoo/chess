package org.czareg.game.validator;

import org.czareg.BaseTests;
import org.czareg.piece.*;
import org.junit.jupiter.api.Test;

import static org.czareg.piece.Player.BLACK;
import static org.czareg.piece.Player.WHITE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InsufficientMaterialCheckerTest extends BaseTests {

    @Test
    void givenEmptyBoard_whenCheckingForInsufficientMaterial_returnsTrue() {
        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertFalse(insufficientMaterialChecker.checkOnlyKings());
        assertFalse(insufficientMaterialChecker.check());
    }

    @Test
    void givenOnlyTwoKingsOnBoard_whenCheckingForInsufficientMaterial_returnsTrue() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertTrue(insufficientMaterialChecker.checkOnlyKings());
        assertTrue(insufficientMaterialChecker.check());
    }

    @Test
    void givenTwoKingsAndAPawnOnBoard_whenCheckingForInsufficientMaterial_returnsFalse() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("A", 2), new Pawn(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertFalse(insufficientMaterialChecker.checkOnlyKings());
        assertFalse(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndBishopVsKing_whenCheckingForInsufficientMaterial_returnsTrue() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("C", 1), new Bishop(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertTrue(insufficientMaterialChecker.checkKingAndMinorVsKing());
        assertTrue(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndKnightVsKing_whenCheckingForInsufficientMaterial_returnsTrue() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("B", 3), new Knight(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertTrue(insufficientMaterialChecker.checkKingAndMinorVsKing());
        assertTrue(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndRookVsKing_whenCheckingForInsufficientMaterial_returnsFalse() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("A", 2), new Rook(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertFalse(insufficientMaterialChecker.checkKingAndMinorVsKing());
        assertFalse(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndMinorPieceVsKingAndPawn_whenCheckingForInsufficientMaterial_returnsFalse() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("B", 3), new Knight(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));
        board.placePiece(pf.create("H", 7), new Pawn(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertFalse(insufficientMaterialChecker.checkKingAndMinorVsKing());
        assertFalse(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndTwoKnightsVsKing_whenCheckingForInsufficientMaterial_returnsTrue() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("B", 3), new Knight(WHITE));
        board.placePiece(pf.create("C", 4), new Knight(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertTrue(insufficientMaterialChecker.checkTwoKnightsVsKing());
        assertTrue(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndTwoKnightsVsKingAndPawn_whenCheckingForInsufficientMaterial_returnsFalse() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("B", 3), new Knight(WHITE));
        board.placePiece(pf.create("C", 4), new Knight(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));
        board.placePiece(pf.create("H", 7), new Pawn(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertFalse(insufficientMaterialChecker.checkTwoKnightsVsKing());
        assertFalse(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndKnightVsKing_whenCheckingForInsufficientMaterial_returnsFalseForTwoKnightsCheckButTrueForOverallCheck() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("B", 3), new Knight(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertFalse(insufficientMaterialChecker.checkTwoKnightsVsKing());
        assertTrue(insufficientMaterialChecker.check()); // still insufficient material (covered by checkKingAndMinorVsKing)
    }

    @Test
    void givenKingAndTwoKnightsAndExtraPieceVsKing_whenCheckingForInsufficientMaterial_returnsFalse() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("B", 3), new Knight(WHITE));
        board.placePiece(pf.create("C", 4), new Knight(WHITE));
        board.placePiece(pf.create("D", 4), new Bishop(WHITE)); // extra piece makes it not insufficient
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertFalse(insufficientMaterialChecker.checkTwoKnightsVsKing());
        assertFalse(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndBishopVsKingAndBishopOnSameColorSquares_whenCheckingForInsufficientMaterial_returnsTrue() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("C", 2), new Bishop(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));
        board.placePiece(pf.create("G", 8), new Bishop(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertTrue(insufficientMaterialChecker.checkBishopsSameColorDraw());
        assertTrue(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndBishopVsKingAndBishopOnOppositeColorSquares_whenCheckingForInsufficientMaterial_returnsFalse() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("C", 2), new Bishop(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));
        board.placePiece(pf.create("E", 3), new Bishop(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertFalse(insufficientMaterialChecker.checkBishopsSameColorDraw());
        assertFalse(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndBishopVsKing_whenCheckingForInsufficientMaterial_returnsFalse() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("C", 1), new Bishop(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertFalse(insufficientMaterialChecker.checkBishopsSameColorDraw());
        assertTrue(insufficientMaterialChecker.check()); // covered by king+minor vs king
    }

    @Test
    void givenKingsAndExtraPiecesWithBishops_whenCheckingForInsufficientMaterial_returnsFalse() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("C", 1), new Bishop(WHITE));
        board.placePiece(pf.create("B", 2), new Knight(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));
        board.placePiece(pf.create("F", 3), new Bishop(BLACK));

        InsufficientMaterialChecker insufficientMaterialChecker = new InsufficientMaterialChecker(context);
        assertFalse(insufficientMaterialChecker.checkBishopsSameColorDraw());
        assertFalse(insufficientMaterialChecker.check());
    }

    @Test
    void givenKingAndBishopVsKingAndKnight_whenCheckingForInsufficientMaterial_returnsFalse() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("C", 1), new Bishop(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));
        board.placePiece(pf.create("G", 6), new Knight(BLACK));

        InsufficientMaterialChecker checker = new InsufficientMaterialChecker(context);
        assertFalse(checker.check());
    }

    @Test
    void givenKingAndTwoBishopsOnSameColorVsKing_whenCheckingForInsufficientMaterial_returnsTrue() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("C", 3), new Bishop(WHITE));
        board.placePiece(pf.create("E", 3), new Bishop(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker checker = new InsufficientMaterialChecker(context);
        assertTrue(checker.check());
    }

    @Test
    void givenKingAndTwoBishopsOnOppositeColorsVsKing_whenCheckingForInsufficientMaterial_returnsFalse() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("C", 3), new Bishop(WHITE));
        board.placePiece(pf.create("D", 3), new Bishop(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));

        InsufficientMaterialChecker checker = new InsufficientMaterialChecker(context);
        assertFalse(checker.check()); // mate possible with 2 bishops
    }

    @Test
    void givenBlackHasKnightAndKingVsWhiteKing_whenCheckingForInsufficientMaterial_returnsTrue() {
        board.placePiece(pf.create("A", 1), new King(WHITE));
        board.placePiece(pf.create("H", 8), new King(BLACK));
        board.placePiece(pf.create("F", 6), new Knight(BLACK));

        InsufficientMaterialChecker checker = new InsufficientMaterialChecker(context);
        assertTrue(checker.check());
    }

}