package my.code.service;

import my.code.model.GameResult;
import my.code.repository.InMemoryGameResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameResultsServiceTest {
    private GameResultsService service;
    private InMemoryGameResultRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryGameResultRepository();
        service = new GameResultsService(repository);
    }

    @Test
    void testSetAndGetUserResults() {
        service.setResult(1, 1, 55);
        service.setResult(1, 2, 8);
        service.setResult(1, 3, 15);

        List<GameResult> results = service.getTopUserResults(1);
        assertEquals(3, results.size());
        assertEquals(55, results.get(0).getResult());
        assertEquals(1, results.get(0).getLevelId());
        assertEquals(15, results.get(1).getResult());
        assertEquals(3, results.get(1).getLevelId());
        assertEquals(8, results.get(2).getResult());
        assertEquals(2, results.get(2).getLevelId());
    }

    @Test
    void testGetLevelResults() {
        service.setResult(1, 3, 15);
        service.setResult(2, 3, 22);

        List<GameResult> results = service.getTopLevelResults(3);
        assertEquals(2, results.size());
        assertEquals(22, results.get(0).getResult());
        assertEquals(2, results.get(0).getUserId());
        assertEquals(15, results.get(1).getResult());
        assertEquals(1, results.get(1).getUserId());
    }

    @Test
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.setResult(-1, 1, 10));
        assertThrows(IllegalArgumentException.class, () -> service.getTopUserResults(0));
        assertThrows(IllegalArgumentException.class, () -> service.getTopLevelResults(-1));
    }
}