package met.petar_djordjevic_5594.gamevalut_server.controller.customUser;

import jakarta.validation.Valid;
import met.petar_djordjevic_5594.gamevalut_server.model.country.Country;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.NewCustomUserDTO;
import met.petar_djordjevic_5594.gamevalut_server.service.country.CountryService;
import met.petar_djordjevic_5594.gamevalut_server.service.customUser.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomUserController {

    @Autowired
    CustomUserService customUserService;

    @Autowired
    CountryService countryService;

    public CustomUserController() {
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody NewCustomUserDTO newCustomUserDTO){
        customUserService.addUser(newCustomUserDTO);
    }

    @PostMapping("/country")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(){
        countryService.addCountry(new Country("Srbija","SRB","slika1.npg"));
    }

    @GetMapping("/friendship")
    public void get(){
        customUserService.postFriendComment(3,1);
    }

    @PostMapping("/friends/{userId}/{potentialId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFriend(@PathVariable("userId") Integer userId, @PathVariable("potentialId") Integer potentialId){
        customUserService.sendFriendRequest(userId, potentialId);
    }




}
