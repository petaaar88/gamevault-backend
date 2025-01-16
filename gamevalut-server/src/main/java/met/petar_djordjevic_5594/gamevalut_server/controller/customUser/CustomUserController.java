package met.petar_djordjevic_5594.gamevalut_server.controller.customUser;

import jakarta.validation.Valid;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.LoginUserDTO;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.NewCustomUserDTO;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.ICustomUserRepository;
import met.petar_djordjevic_5594.gamevalut_server.service.customUser.CustomUserService;
import met.petar_djordjevic_5594.gamevalut_server.service.webhook.OnlineFriendNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomUserController {

    @Autowired
    OnlineFriendNotificationService onlineFriendNotificationService;
    @Autowired
    ICustomUserRepository userService;

    @PostMapping("/login")
    private void login(@Valid @RequestBody LoginUserDTO loginUserDTO){

        CustomUser user = userService.findByUsername(loginUserDTO.username()).get();

        onlineFriendNotificationService.subscribe(user.getId(), loginUserDTO.url());
    }

    @PostMapping("/register")
    private void register(@Valid @RequestBody NewCustomUserDTO newCustomUserDTO){

    }

    @GetMapping("/{userId}/friends")
    private void getAllFriends(@PathVariable("userId") Integer userId){
        System.out.println("Nesto se desilo");
    }

    @GetMapping("/search?username={username}&limit={limit}")
    private void search(@PathVariable("username") String username, @PathVariable("Limit") Integer limit){

    }

    @DeleteMapping("/logout/{userId}")
    private  void logout(@PathVariable("userId") Integer userId){
        //TODO: uradi bolju logiku za logout
        onlineFriendNotificationService.unsubscribe(userId);
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
