package my.code.controller;

import my.code.dto.SetInfoRequest;
import my.code.model.GameResult;
import my.code.service.GameResultsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaderboardController {
    private static final Logger logger = LoggerFactory.getLogger(LeaderboardController.class);
    private final GameResultsService service;

    public LeaderboardController(GameResultsService service) {
        this.service = service;
    }

    @GetMapping("/userinfo/{userId}")
    public ResponseEntity<List<GameResult>> getUserInfo(@PathVariable int userId) {
        logger.info("Received GET /userinfo/{}", userId);
        return ResponseEntity.ok(service.getTopUserResults(userId));
    }

    @GetMapping("/levelinfo/{levelId}")
    public ResponseEntity<List<GameResult>> getLevelInfo(@PathVariable int levelId) {
        logger.info("Received GET /levelinfo/{}", levelId);
        return ResponseEntity.ok(service.getTopLevelResults(levelId));
    }

    @PutMapping("/setinfo")
    public ResponseEntity<Void> setInfo(@Valid @RequestBody SetInfoRequest request) {
        logger.info("Received PUT /setinfo with body: userId={}, levelId={}, result={}",
                request.getUserId(), request.getLevelId(), request.getResult());
        service.setResult(request.getUserId(), request.getLevelId(), request.getResult());
        return ResponseEntity.ok().build();
    }
}
