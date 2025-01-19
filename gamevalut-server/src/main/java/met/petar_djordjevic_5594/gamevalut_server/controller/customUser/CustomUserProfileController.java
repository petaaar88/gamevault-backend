package met.petar_djordjevic_5594.gamevalut_server.controller.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.UserDescriptionDTO;
import met.petar_djordjevic_5594.gamevalut_server.service.customUser.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class CustomUserProfileController {

    @Autowired
    CustomUserService userService;

    public CustomUserProfileController() {
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDescriptionDTO getProfileDescription(@PathVariable("userId") Integer userId){
        return userService.getUserProfileDescription(userId);
    }
}
