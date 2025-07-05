package my.code.service;

import my.code.model.GameResult;
import my.code.repository.GameResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameResultsService {
    private static final Logger logger = LoggerFactory.getLogger(GameResultsService.class);
    private final GameResultRepository repository;

    public GameResultsService(GameResultRepository repository) {
        this.repository = repository;
    }

    public void setResult(int userId, int levelId, int result) {
        if (userId <= 0 || levelId <= 0 || result < 0) {
            logger.error("Invalid input: userId={}, levelId={}, result={}", userId, levelId, result);
            throw new IllegalArgumentException("Invalid input parameters");
        }
        logger.info("Setting result: userId={}, levelId={}, result={}", userId, levelId, result);
        repository.save(new GameResult(userId, levelId, result));
    }

    public List<GameResult> getTopUserResults(int userId) {
        if (userId <= 0) {
            logger.error("Invalid userId={}", userId);
            throw new IllegalArgumentException("Invalid user ID");
        }
        logger.info("Fetching top results for userId={}", userId);
        return repository.findByUserId(userId);
    }

    public List<GameResult> getTopLevelResults(int levelId) {
        if (levelId <= 0) {
            logger.error("Invalid levelId={}", levelId);
            throw new IllegalArgumentException("Invalid level ID");
        }
        logger.info("Fetching top results for levelId={}", levelId);
        return repository.findByLevelId(levelId);
    }
}
