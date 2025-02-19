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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    // Definišite putanju direktorijuma gde će slike biti čuvane
    private static final String UPLOAD_DIRECTORY = "C:/Users/pebn8/Desktop/slikeSaAplikacije";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("username") String username) {
        if (file.isEmpty() || username == null || username.isEmpty()) {
            return new ResponseEntity<>("Invalid file or username", HttpStatus.BAD_REQUEST);
        }

        try {
            // Kreirajte direktorijum ako ne postoji
            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Sačuvajte datoteku u direktorijumu
            Path filePath = Paths.get(UPLOAD_DIRECTORY, username + "_" + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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


}
