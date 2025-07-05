package my.code.model;

public class GameResult implements Comparable<GameResult> {
    private final int userId;
    private final int levelId;
    private final int result;

    public GameResult(int userId, int levelId, int result) {
        this.userId = userId;
        this.levelId = levelId;
        this.result = result;
    }

    public int getUserId() {
        return userId;
    }

    public int getLevelId() {
        return levelId;
    }

    public int getResult() {
        return result;
    }

    @Override
    public int compareTo(GameResult other) {
        int resultCompare = Integer.compare(other.result, this.result);
        if (resultCompare != 0) {
            return resultCompare;
        }
        return Integer.compare(this.levelId, other.levelId);
    }
}
