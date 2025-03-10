package met.petar_djordjevic_5594.gamevalut_server.controller.customUser;

import jakarta.validation.Valid;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.*;
import met.petar_djordjevic_5594.gamevalut_server.model.pagination.Pages;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.ICustomUserRepository;
import met.petar_djordjevic_5594.gamevalut_server.service.customUser.CustomUserService;
import met.petar_djordjevic_5594.gamevalut_server.service.notification.UserOnlineNotificationService;
import met.petar_djordjevic_5594.gamevalut_server.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
public class CustomUserController {

    @Autowired
    ICustomUserRepository userRepository;
    @Autowired
    CustomUserService userService;

    @PostMapping("/login")
    private void login(@Valid @RequestBody LoginUserDTO loginUserDTO) {
        userService.loginUser(loginUserDTO);
    }

    @PostMapping("/register")
    private void register(@Valid @RequestBody NewCustomUserDTO newCustomUserDTO) {

    }

    @GetMapping("/friends/{userId}")
    private AllFriendsDTO getAllFriends(@PathVariable("userId") Integer userId) {
        return userService.getAllUsersFriends(userId);
    }

    @GetMapping("/relationship/{userId}/{freindId}")
    private String getRelationshipOfUsers(@PathVariable("userId") Integer userId, @PathVariable("freindId") Integer freindId) {
        return userService.getRelationshipOfUsers(userId, freindId);
    }

    @GetMapping("/does-have-friends/{userId}")
    private boolean doesHaveFriends(@PathVariable("userId") Integer userId) {
        return userService.doesHaveFriends(userId);
    }

    @GetMapping("/does-have-comments/{userId}")
    private boolean doesHaveComments(@PathVariable("userId") Integer userId) {
        return userService.doesHaveComments(userId);
    }

    @GetMapping("/does-user-post-comment/{userId}/{friendId}")
    private boolean doesUserPostComment(@PathVariable("userId") Integer userId, @PathVariable("friendId") Integer friendId) {
        return userService.doesUserPostComment(userId, friendId);
    }

    @GetMapping("/search/{userId}")
    private Pages search(@PathVariable("userId") Integer userId, @RequestParam(name = "username", defaultValue = "") String username, @RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "limit", defaultValue = "3") Integer limit) {
        return userService.searchUsers(userId, username, page, limit);
    }

    @DeleteMapping("/logout/{userId}")
    private void logout(@PathVariable("userId") Integer userId) {
        userService.logout(userId);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


}
