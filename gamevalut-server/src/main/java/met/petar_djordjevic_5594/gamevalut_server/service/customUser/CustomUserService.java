package met.petar_djordjevic_5594.gamevalut_server.service.customUser;

import met.petar_djordjevic_5594.gamevalut_server.exception.CannotAddFriendException;
import met.petar_djordjevic_5594.gamevalut_server.model.country.Country;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.*;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.ICustomUserRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IFriendCommentRepostiory;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IFriendRequestRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IFriendshipRepository;
import met.petar_djordjevic_5594.gamevalut_server.service.country.CountryService;
import met.petar_djordjevic_5594.gamevalut_server.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class CustomUserService {
    @Autowired
    ICustomUserRepository userRepository;
    @Autowired
    IFriendshipRepository friendshipRepository;
    @Autowired
    IFriendCommentRepostiory friendCommentRepostiory;
    @Autowired
    IFriendRequestRepository friendRequestRepository;
    @Autowired
    RedisService redisService;

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

    @Transactional
    public void acceptFriendRequest(Integer userId, Integer requestId) {
        CustomUser user = this.getUserById(userId);

        Optional<FriendRequest> optionalFriendRequest = friendRequestRepository.findById(requestId);

        if (optionalFriendRequest.isEmpty())
            throw new NoSuchElementException("Friend request does not exists!");

        if (optionalFriendRequest.get().getUid1().getId() == userId)
            throw new DataIntegrityViolationException("Invalid user Id!");

        CustomUser potentialFriend = optionalFriendRequest.get().getUid1();


        Friendship newUserFriend = new Friendship();

        newUserFriend.setUser1(user);
        newUserFriend.setUser2(potentialFriend);
        newUserFriend.setAdded_at(LocalDate.now());


        Friendship newFriendUser = new Friendship();

        newFriendUser.setUser1(potentialFriend);
        newFriendUser.setUser2(user);
        newFriendUser.setAdded_at(newUserFriend.getAdded_at());

        friendshipRepository.save(newUserFriend);
        friendshipRepository.save(newFriendUser);

        friendRequestRepository.delete(optionalFriendRequest.get());

    }

    public void deleteRequest(Integer userId, Integer requestSenderId) {
        CustomUser user = this.getUserById(userId);
        CustomUser requestSender = this.getUserById(requestSenderId);

        if (user == requestSender)
            throw new CannotAddFriendException("Same id input for user and friend!");

        Integer[] requestId = new Integer[1];

        user.getReceivedRequests().forEach(request -> {
            if (request.getUid1().getId() == requestSenderId && request.getUid2().getId() == userId)
                requestId[0] = request.getId();
        });


        if (requestId[0] == null)
            throw new NoSuchElementException("Friend Request does not exist!");
        System.out.println(requestId[0]);

        user.getReceivedRequests().removeIf(request -> request.getId() == requestId[0]);
        requestSender.getSentRequests().removeIf(request -> request.getId() == requestId[0]);

        userRepository.save(user);
        userRepository.save(requestSender);

        friendRequestRepository.deleteById(requestId[0]);

    }

    public UserDescriptionDTO getUserProfileDescription(Integer userId) {
        CustomUser user = this.getUserById(userId);

        Map<String, String> nationality = new HashMap<>();
        nationality.put("name", user.getCountry().getName());
        nationality.put("icon", user.getCountry().getFlagUrl());

        return new UserDescriptionDTO(user.getId(), user.getUsername(), user.getImageUrl(), user.getDescription(), nationality);

    }

    public void postCommentToFriendProfile(Integer userId, Integer friendId, NewFriendCommentDTO newFriendCommentDTO) {
        CustomUser user = this.getUserById(userId);
        CustomUser friend = this.getUserById(userId);

        Optional<Friendship> optionalFriendship = friendshipRepository.findByFriendsId(userId, friendId);

        if (optionalFriendship.isEmpty())
            throw new NoSuchElementException("Users are not friends!");

        Optional<FriendComment> comment = friendCommentRepostiory.findByFriendshipId(optionalFriendship.get().getId());

        if (comment.isPresent())
            throw new NoSuchElementException("Already have comment on friend profile!");

        FriendComment newFriendComment = new FriendComment(newFriendCommentDTO.content(), LocalDate.now(), optionalFriendship.get());

        friendCommentRepostiory.save(newFriendComment);


    }

    public List<FriendCommentDTO> getFriendComments(Integer userId) {
        CustomUser user = this.getUserById(userId);

        List<CustomUser> friends = this.getAllFriends(userId);

        if (friends.isEmpty())
            return new ArrayList<>();


        List<FriendCommentDTO> friendsComments = new ArrayList<>();


        //TODO: kada ubacis JWT, proveri da li je user postavio komentar na prijateljevom nalogu
        friends.forEach(friend -> {
            Friendship freindship = friend.getUserWithFriends().stream().filter(user2 -> user2.getUser2().getId() == userId).findFirst().get();

            if (freindship.getComment() != null) {

                FriendComment comment = freindship.getComment();
                friendsComments.add(new FriendCommentDTO(comment.getId(), new FriendDTO(friend.getId(), friend.getUsername(), friend.getImageUrl(), null, null), comment.getContent(), comment.getPosted_at()));
            }

        });

        return friendsComments;


    }

    public List<CustomUser> getOnlineFriends(Integer userId) {
        CustomUser user = this.getUserById(userId);

        List<CustomUser> friends = this.getAllFriends(userId);

        List<CustomUser> onlineFriends = new ArrayList<>();

        friends.forEach(friend -> {
            if (redisService.checkIfHashExist(friend.getId().toString()))
                onlineFriends.add(friend);
        });

        return onlineFriends;
    }

    public void updateUser(Integer userId, UpdatedCustomUserDTO updatedCustomUserDTO) {
        CustomUser user = this.getUserById(userId);

        if (updatedCustomUserDTO.description() == null && updatedCustomUserDTO.icon() == null && updatedCustomUserDTO.username() == null)
            return;

        if (updatedCustomUserDTO.username() != null)
            user.setUsername(updatedCustomUserDTO.username());

        if (updatedCustomUserDTO.icon() != null)
            user.setImageUrl(updatedCustomUserDTO.icon());

        if (updatedCustomUserDTO.description() != null)
            user.setDescription(updatedCustomUserDTO.description());

        userRepository.save(user);

    }

    public void logout(Integer userId) {
        CustomUser user = this.getUserById(userId);

        if (!redisService.checkIfHashExist(user.getId().toString()))
            throw new NoSuchElementException("User is not online!");

        //TODO: ucini jwt nevalidinim
        redisService.deleteFromRedis(user.getId().toString());
    }

    public List<FriendDTO> searchUsers(String username) {
        Optional<List<CustomUser>> optionalUsers = userRepository.findByUsername(username);

        if (optionalUsers.isEmpty())
            return new ArrayList<>();

        List<FriendDTO> users = new ArrayList<>();

        optionalUsers.get().forEach(user -> {
            users.add(new FriendDTO(user.getId(),user.getUsername(),user.getImageUrl(),null,null));
        });

        return users;
    }

    public CustomUser convertToEntity(NewCustomUserDTO newCustomUserDTO) {
        return new CustomUser(newCustomUserDTO.username(), newCustomUserDTO.password());
    }

    public FriendDTO convertToFriendDTO(CustomUser user) {
        return new FriendDTO(user.getId(), user.getUsername(), user.getImageUrl(), null, null);
    }
}
