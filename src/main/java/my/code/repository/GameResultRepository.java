package my.code.repository;

import my.code.model.GameResult;

import java.util.List;

public interface GameResultRepository {

    void save(GameResult result);

    List<GameResult> findByUserId(int userId);

    List<GameResult> findByLevelId(int levelId);
}
