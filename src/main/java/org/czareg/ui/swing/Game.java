package org.czareg.ui.swing;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.czareg.board.Board;
import org.czareg.board.ClassicPieceStartingPositionPlacer;
import org.czareg.board.PiecePlacer;
import org.czareg.game.*;
import org.czareg.move.MoveGenerators;
import org.czareg.piece.Piece;
import org.czareg.position.Index;
import org.czareg.position.Position;
import org.czareg.position.PositionFactory;

import java.util.Optional;
import java.util.Set;

@Slf4j
class Game {

    private final Context context;
    private final Board board;
    private final PositionFactory pf;
    private final MoveGenerators moveGenerators;
    private final History history;
    private final MoveMaker moveMaker;

    @Getter
    private Position selectedPosition;
    @Getter
    private Set<Move> highlightedMoves = Set.of();

    private GeneratedMoves generatedMoves;

    Game() {
        context = ClassicContext.create();
        board = context.getBoard();
        pf = board.getPositionFactory();
        moveGenerators = context.getMoveGenerators();
        history = context.getHistory();
        moveMaker = context.getMoveMaker();

        PiecePlacer placer = new ClassicPieceStartingPositionPlacer();
        placer.place(board);

        generatedMoves = moveGenerators.generateLegal(context);
    }

    int getMaxFile() {
        return pf.getMaxFile();
    }

    int getMaxRank() {
        return pf.getMaxRank();
    }

    Position create(int fileIndex, int rankIndex) {
        return pf.create(fileIndex, rankIndex);
    }

    Index create(Position position) {
        return pf.create(position);
    }

    boolean hasPiece(Position position) {
        return board.hasPiece(position);
    }

    Piece getPiece(Position position) {
        return board.getPiece(position);
    }

    void updateSelectedPositionAndHighlightedMoves(Position clicked) {
        for (Move move : highlightedMoves) {
            if (move.getEnd().equals(clicked)) {
                moveMaker.make(context, move);
                generatedMoves = moveGenerators.generateLegal(context);
                selectedPosition = null;
                highlightedMoves = Set.of();
                return;
            }
        }

        if (board.hasPiece(clicked) && board.getPiece(clicked).getPlayer() == history.getCurrentPlayer()) {
            selectedPosition = clicked;
            highlightedMoves = generatedMoves.getMovesStarting(clicked);
        } else {
            selectedPosition = null;
            highlightedMoves = Set.of();
        }
    }

    void makeRandomMove() {
        Optional<Move> optionalMove = generatedMoves.findRandom();
        if (optionalMove.isEmpty()) {
            log.warn("No moves generated");
            return;
        }
        Move move = optionalMove.orElseThrow();
        moveMaker.make(context, move);

        generatedMoves = moveGenerators.generateLegal(context);
        selectedPosition = null;
        highlightedMoves = Set.of();
    }
}
