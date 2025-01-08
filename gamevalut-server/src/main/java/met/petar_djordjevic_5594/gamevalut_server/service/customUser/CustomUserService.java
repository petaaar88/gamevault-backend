package met.petar_djordjevic_5594.gamevalut_server.service.customUser;

import met.petar_djordjevic_5594.gamevalut_server.model.country.Country;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.*;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.ICustomUserRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IFriendCommentRepostiory;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IFriendshipRepository;
import met.petar_djordjevic_5594.gamevalut_server.service.country.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class CustomUserService {
    @Autowired
    ICustomUserRepository customUserRepository;
    @Autowired
    IFriendshipRepository friendshipRepository;
    @Autowired
    IFriendCommentRepostiory friendCommentRepostiory;

    @Autowired
    CountryService countryService;

    public CustomUserService() {
    }

    public void addUser(CustomUser newUser) {
        customUserRepository.save(newUser);
    }

    public void addUser(NewCustomUserDTO newCustomUserDTO) {

        CustomUser newUser = convertToEntity(newCustomUserDTO);

        Optional<Country> userCountry = countryService.findById(Integer.parseInt(newCustomUserDTO.countryId()));

        newUser.setCountry(userCountry.get());

        System.out.println(newUser.getCountry());

        newUser.setCreatedAt(LocalDate.now());

        customUserRepository.save(newUser);


    }

    public void sendFriendRequest(Integer userId, Integer potentialFrinedId) {

        Optional<CustomUser> user = customUserRepository.findById(userId);
        Optional<CustomUser> potentialFriend = customUserRepository.findById(potentialFrinedId);


        Friendship friendship = new Friendship(FriendshipStatus.Pending, LocalDate.now(), user.get(), potentialFriend.get());

        friendshipRepository.save(friendship);

    }

    public void postFriendComment(Integer userId, Integer friendId) {
        Optional<CustomUser> user = customUserRepository.findById(userId);
        Optional<CustomUser> friend = customUserRepository.findById(friendId);

        if (user.isEmpty()) {
            //TODO: Treba da se baci error da user nije pronadjen
        }

        if (friend.isEmpty()) {
            //TODO: Treba da se baci error da friend nije pronadjen
        }

        Optional<Friendship> friendship = friendshipRepository.findByFriendsId(userId, friendId);

        if (friendship.isEmpty()) {
            //TODO: Treba da se baci error ako prijateljstvo ne postjoji
        }

        Optional<FriendComment> checkingComment = friendCommentRepostiory.findBySenderId(userId);

        if (checkingComment.isEmpty()) {
            FriendComment comment = new FriendComment("najjaki igra", friendId, userId, LocalDate.now(), friendship.get());
            friendCommentRepostiory.save(comment);
        } else {
            //TODO: Baci error da postoji vec komentar
        }


    }

    public CustomUser convertToEntity(NewCustomUserDTO newCustomUserDTO) {
        return new CustomUser(newCustomUserDTO.username(), newCustomUserDTO.password());
    }
}
