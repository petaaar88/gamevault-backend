package met.petar_djordjevic_5594.gamevalut_server.service.customUser;

import met.petar_djordjevic_5594.gamevalut_server.exception.CannotAddFriendException;
import met.petar_djordjevic_5594.gamevalut_server.model.country.Country;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.*;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.ICustomUserRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IFriendCommentRepostiory;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IFriendshipRepository;
import met.petar_djordjevic_5594.gamevalut_server.service.country.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomUserService {
    @Autowired
    ICustomUserRepository userRepository;
    @Autowired
    IFriendshipRepository friendshipRepository;
    @Autowired
    IFriendCommentRepostiory friendCommentRepostiory;

    @Autowired
    CountryService countryService;

    public CustomUserService() {
    }

    public void addUser(CustomUser newUser) {
        userRepository.save(newUser);
    }

    public void addUser(NewCustomUserDTO newCustomUserDTO) {

        CustomUser newUser = convertToEntity(newCustomUserDTO);

        Optional<Country> userCountry = countryService.findById(Integer.parseInt(newCustomUserDTO.countryId()));

        newUser.setCountry(userCountry.get());

        System.out.println(newUser.getCountry());

        newUser.setCreatedAt(LocalDate.now());

        userRepository.save(newUser);


    }

    public CustomUser getUserById(Integer userId) {
        Optional<CustomUser> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new NoSuchElementException("User not found!");

        return optionalUser.get();
    }

    public List<CustomUser> getAllFriends(Integer userId) {

        List<CustomUser> friends = new ArrayList<>();

        Optional<List<CustomUser>> optonalFriends = userRepository.findAllFriends(userId);
        if (optonalFriends.isPresent())
            friends = optonalFriends.get();

        return friends;

    }

    public void sendFriendRequest(Integer userId, Integer potentialFrinedId) {

        CustomUser user = this.getUserById(userId);
        CustomUser potentialFriend = this.getUserById(potentialFrinedId);

        if (user == potentialFriend)
            throw new CannotAddFriendException("Same id input for user and friend!");


        List<CustomUser> friends = getAllFriends(userId);

        if (!friends.isEmpty()) {
            friends.forEach(friend -> {
                if (friend == potentialFriend)
                    throw new CannotAddFriendException("Already friends!");
            });
        }

        user.getSentRequests().forEach(request -> {
            if (request.getUid2().getId() == potentialFriend.getId())
                throw new CannotAddFriendException("Request already sent!");
        });

        user.getReceivedRequests().forEach(request -> {
            if (request.getUid1().getId() == potentialFriend.getId())
                throw new CannotAddFriendException("Request already received!");
        });

        FriendRequest newFriendRequest = new FriendRequest(user, potentialFriend);

        user.getSentRequests().add(newFriendRequest);

        userRepository.save(user);

    }

    public FriendRequestsDTO getAllFriendRequests(Integer userId) {
        CustomUser user = this.getUserById(userId);

        List<FriendDTO> receivedRequest = new ArrayList<>();
        List<FriendDTO> sentRequest = new ArrayList<>();

        user.getReceivedRequests().forEach(receivedRequest1 -> {
            if (receivedRequest1.getUid2().getId() == userId) {
                System.out.println(receivedRequest1.getUid1().getId());
                System.out.println(receivedRequest1.getUid1().getUsername());

            }
        });


        if (!user.getSentRequests().isEmpty()) {
            user.getSentRequests().forEach(request -> {
                sentRequest.add(new FriendDTO(request.getUid2().getId(), request.getUid2().getUsername(), request.getUid2().getImageUrl(), null, null));
            });
        }

        if (!user.getReceivedRequests().isEmpty()) {
            user.getReceivedRequests().forEach(request -> {
                if (request.getUid2().getId() == userId) {
                    receivedRequest.add(new FriendDTO(request.getUid1().getId(), request.getUid1().getUsername(), request.getUid1().getImageUrl(), null, null));

                }
            });
        }

        return new FriendRequestsDTO(receivedRequest, sentRequest);

    }

    public void postFriendComment(Integer userId, Integer friendId) {
        Optional<CustomUser> user = userRepository.findById(userId);
        Optional<CustomUser> friend = userRepository.findById(friendId);

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
            FriendComment comment = new FriendComment("najjaki igra", LocalDate.now(), friendship.get());
            friendCommentRepostiory.save(comment);
        } else {
            //TODO: Baci error da postoji vec komentar
        }


    }

    public CustomUser convertToEntity(NewCustomUserDTO newCustomUserDTO) {
        return new CustomUser(newCustomUserDTO.username(), newCustomUserDTO.password());
    }

    public FriendDTO convertToFriendDTO(CustomUser user) {
        return new FriendDTO(user.getId(), user.getUsername(), user.getImageUrl(), null, null);
    }
}
