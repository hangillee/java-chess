package view;

import view.dto.RankInfo;
import view.dto.RankInfos;

public class OutputView {
    private OutputView() {
    }

    public static void printGameStartMessage() {
        System.out.print("""
                > 체스 게임을 시작합니다.
                > 게임 시작 : start
                > 게임 종료 : end
                > 게임 현황 : status
                > 게임 이동 : move source위치 target위치 - 예. move b2 b3
                """);
    }

    public static void printChessBoard(final RankInfos ranks) {
        for (final RankInfo rank : ranks.getRankInfos()) {
            printRank(rank);
            System.out.println();
        }
        System.out.println();
    }

    private static void printRank(final RankInfo rank) {
        for (final String piece : rank.getPieces()) {
            System.out.print(piece);
        }
    }

    public static void printErrorMessage(final String message) {
        System.err.println(message);
        System.out.println();
    }

    public static void printGameStatus(final double whiteScore, final double blackScore) {
        System.out.println("백: " + whiteScore + "점, 흑: " + blackScore + "점");
        if (whiteScore > blackScore) {
            System.out.println("백이 이기고 있습니다.");
        }
        if (whiteScore < blackScore) {
            System.out.println("흑이 이기고 있습니다.");
        }
        if (whiteScore == blackScore) {
            System.out.println("비기고 있습니다.");
        }
        System.out.println();
    }

    public static void printGameEndMessage(final double whiteScore, final double blackScore) {
        System.out.println("백: " + whiteScore + "점, 흑: " + blackScore + "점");
        if (whiteScore > blackScore) {
            System.out.println("백이 승리하였습니다.");
        }
        if (whiteScore < blackScore) {
            System.out.println("흑이 승리하였습니다.");
        }
        if (whiteScore == blackScore) {
            System.out.println("무승부입니다.");
        }
    }
}
