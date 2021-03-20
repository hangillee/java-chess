package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.board.Cell;
import chess.domain.board.Coordinate;
import chess.domain.board.Direction;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    private static final String NAME = "N";
    private static final double SCORE = 2.5;

    public Knight(TeamType teamType) {
        super(teamType, NAME, SCORE, Direction.getKnightDirections());
    }

    @Override
    public boolean isMovableTo(Board board, Coordinate currentCoordinate,
        Coordinate targetCoordinate) {
        Direction moveCommandDirection = currentCoordinate.calculateDirection(targetCoordinate);
        List<Coordinate> possibleCoordinates = new ArrayList<>();
        List<Direction> directions = getDirections();
        if (!directions.contains(moveCommandDirection)) {
            return false;
        }
        Coordinate movingCoordinate = currentCoordinate.move(moveCommandDirection);

        Cell cell = board.find(movingCoordinate);
        if (cell.isMovable(getTeamType())) {
            possibleCoordinates.add(movingCoordinate);
        }
        return possibleCoordinates.contains(targetCoordinate);
    }
}
