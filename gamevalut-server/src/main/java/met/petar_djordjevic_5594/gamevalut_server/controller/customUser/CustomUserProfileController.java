package met.petar_djordjevic_5594.gamevalut_server.controller.customUser;

import jakarta.validation.Valid;
import met.petar_djordjevic_5594.gamevalut_server.exception.CannotAddFriendException;
import met.petar_djordjevic_5594.gamevalut_server.exception.ResourceNotFoundException;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.FriendCommentDTO;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.NewFriendCommentDTO;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.UserDescriptionDTO;
import met.petar_djordjevic_5594.gamevalut_server.service.customUser.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/profile")
public class CustomUserProfileController {

    @Autowired
    CustomUserService userService;

    public CustomUserProfileController() {
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDescriptionDTO getProfileDescription(@PathVariable("userId") Integer userId) {
        return userService.getUserProfileDescription(userId);
    }

    @PostMapping("/comments/{userId}/{friendId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void postCommentToFriendProfile(@PathVariable("userId") Integer userId, @PathVariable("friendId") Integer friendId, @Valid @RequestBody NewFriendCommentDTO newFriendCommentDTO) {
        userService.postCommentToFriendProfile(userId, friendId, newFriendCommentDTO);
    }

    @GetMapping("/comments/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FriendCommentDTO> getComments(@PathVariable("userId") Integer userId) {
        return userService.getFriendComments(userId);
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CannotAddFriendException.class)
    public ResponseEntity<Object> handleCannotAddFriendException(CannotAddFriendException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
