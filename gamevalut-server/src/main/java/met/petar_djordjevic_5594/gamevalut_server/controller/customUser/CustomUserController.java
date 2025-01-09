package met.petar_djordjevic_5594.gamevalut_server.controller.customUser;

import jakarta.validation.Valid;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.LoginUserDTO;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.NewCustomUserDTO;
import met.petar_djordjevic_5594.gamevalut_server.service.customUser.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomUserController {

    @PostMapping("/login")
    private void login(@Valid @RequestBody LoginUserDTO loginUserDTO){
        System.out.println(loginUserDTO);

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

    }


}
