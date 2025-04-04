package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Block;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.BlockRepository;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/block")
public class BlockController {
    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/block")
    public ResponseEntity<String> blockUser(@RequestParam Long blockerId, @RequestParam Long blockedId) {
        User blocker = userRepo.findById(blockerId).orElseThrow(() -> new RuntimeException("Blocker not found"));
        User blocked = userRepo.findById(blockedId).orElseThrow(() -> new RuntimeException("Blocked user not found"));

        if (blockRepo.existsByBlockerAndBlocked(blocker, blocked)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already blocked");
        }

        Block block = new Block();
        block.setBlocker(blocker);
        block.setBlocked(blocked);
        blockRepo.save(block);

        return ResponseEntity.ok("User blocked successfully");
    }
    @Transactional
    @DeleteMapping("/unblock")
    public ResponseEntity<String> unblockUser(@RequestParam Long blockerId, @RequestParam Long blockedId) {
        User blocker = userRepo.findById(blockerId).orElseThrow(() -> new RuntimeException("Blocker not found"));
        User blocked = userRepo.findById(blockedId).orElseThrow(() -> new RuntimeException("Blocked user not found"));

        if (!blockRepo.existsByBlockerAndBlocked(blocker, blocked)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Block relationship not found");
        }

        blockRepo.deleteByBlockerAndBlocked(blocker, blocked);

        return ResponseEntity.ok("User unblocked successfully");
    }

    @PostMapping("/isBlock")
    public ResponseEntity<Boolean> isBlockUser(@RequestParam Long blockerId, @RequestParam Long blockedId) {
        User blocker = userRepo.findById(blockerId).orElseThrow(() -> new RuntimeException("Blocker not found"));
        User blocked = userRepo.findById(blockedId).orElseThrow(() -> new RuntimeException("Blocked user not found"));
        return ResponseEntity.ok(blockRepo.existsByBlockerAndBlocked(blocker, blocked));
    }

}
