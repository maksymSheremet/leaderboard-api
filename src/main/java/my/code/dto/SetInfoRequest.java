package my.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class SetInfoRequest {
    @JsonProperty("user_id")
    @Positive(message = "user_id must be positive")
    private int userId;

    @JsonProperty("level_id")
    @Positive(message = "level_id must be positive")
    private int levelId;

    @PositiveOrZero(message = "result must be non-negative")
    private int result;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
