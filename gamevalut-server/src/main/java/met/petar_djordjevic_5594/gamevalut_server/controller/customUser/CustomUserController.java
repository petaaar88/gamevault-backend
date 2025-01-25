package met.petar_djordjevic_5594.gamevalut_server.controller.customUser;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CustomUserController {


    @Autowired
    ICustomUserRepository userRepository;
    @Autowired
    CustomUserService userService;
    @Autowired
    UserOnlineNotificationService userOnlineNotificationService;
    @Autowired
    RedisService redisService;

    @PostMapping("/login")
    private void login(@Valid @RequestBody LoginUserDTO loginUserDTO) {

        List<CustomUser> users = userRepository.findByUsername(loginUserDTO.username()).get();

        CustomUser user = users.get(0);
        System.out.println("Prijavlej je korisnik sa ID:" + user.getId() + ", username: " + user.getUsername());

        //TODO: prepravi logiku i izbrisi ovo
        redisService.saveToRedis(user.getId().toString(), "username", user.getUsername());

        List<CustomUser> onlineFriends = userService.getOnlineFriends(user.getId());

        userOnlineNotificationService.notifyOnlineFriends(user, onlineFriends);
    }

    @PostMapping("/register")
    private void register(@Valid @RequestBody NewCustomUserDTO newCustomUserDTO) {

    }

    @GetMapping("/friends/{userId}")
    private AllFriendsDTO getAllFriends(@PathVariable("userId") Integer userId) {
        return userService.getAllUsersFriends(userId);
    }

    @GetMapping("/search")
    private Pages search(@PathParam("username") String username, @PathParam("page") Integer page, @PathParam("limit") Integer limit) {
        return userService.searchUsers(username, page, limit);
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


}
