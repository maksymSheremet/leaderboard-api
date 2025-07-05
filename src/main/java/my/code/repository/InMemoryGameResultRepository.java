package my.code.repository;

import my.code.model.GameResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Repository
public class InMemoryGameResultRepository implements GameResultRepository {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryGameResultRepository.class);
    private static final int TOP_LIMIT = 20;
    private final ConcurrentHashMap<Integer, TreeSet<GameResult>> userResults = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, TreeSet<GameResult>> levelResults = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static final Comparator<GameResult> USER_COMPARATOR = Comparator
            .comparingInt(GameResult::getResult).reversed()
            .thenComparingInt(GameResult::getLevelId);

    private static final Comparator<GameResult> LEVEL_COMPARATOR = Comparator
            .comparingInt(GameResult::getResult).reversed()
            .thenComparingInt(GameResult::getUserId);

    @Override
    public void save(GameResult result) {
        if (result == null) {
            logger.error("Attempted to save null GameResult");
            throw new IllegalArgumentException("GameResult cannot be null");
        }
        logger.debug("Saving result: userId={}, levelId={}, result={}",
                result.getUserId(), result.getLevelId(), result.getResult());
        lock.writeLock().lock();
        try {
            removeOldResult(result.getUserId(), result.getLevelId());

            TreeSet<GameResult> userSet = userResults.computeIfAbsent(
                    result.getUserId(),
                    k -> new TreeSet<>(USER_COMPARATOR)
            );

            TreeSet<GameResult> levelSet = levelResults.computeIfAbsent(
                    result.getLevelId(),
                    k -> new TreeSet<>(LEVEL_COMPARATOR)
            );

            userSet.add(result);
            levelSet.add(result);

            trimToTopLimit(userSet);
            trimToTopLimit(levelSet);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<GameResult> findByUserId(int userId) {
        lock.readLock().lock();
        try {
            logger.debug("Fetching results for userId={}", userId);
            return userResults.getOrDefault(userId, new TreeSet<>(USER_COMPARATOR))
                    .stream()
                    .limit(TOP_LIMIT)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<GameResult> findByLevelId(int levelId) {
        lock.readLock().lock();
        try {
            logger.debug("Fetching results for levelId={}", levelId);
            return levelResults.getOrDefault(levelId, new TreeSet<>(LEVEL_COMPARATOR))
                    .stream()
                    .limit(TOP_LIMIT)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    private void removeOldResult(int userId, int levelId) {
        TreeSet<GameResult> userSet = userResults.get(userId);
        if (userSet != null) {
            userSet.removeIf(r -> r.getLevelId() == levelId);
        }

        TreeSet<GameResult> levelSet = levelResults.get(levelId);
        if (levelSet != null) {
            levelSet.removeIf(r -> r.getUserId() == userId);
        }
    }

    private void trimToTopLimit(TreeSet<GameResult> set) {
        while (set.size() > TOP_LIMIT) {
            set.pollLast();
        }
    }
}