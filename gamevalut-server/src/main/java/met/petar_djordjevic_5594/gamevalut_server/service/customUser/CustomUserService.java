package met.petar_djordjevic_5594.gamevalut_server.service.customUser;

import met.petar_djordjevic_5594.gamevalut_server.exception.CannotAddFriendException;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.*;
import met.petar_djordjevic_5594.gamevalut_server.model.pagination.Pages;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.ICustomUserRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IFriendCommentRepostiory;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IFriendRequestRepository;
import met.petar_djordjevic_5594.gamevalut_server.repository.customUser.IFriendshipRepository;
import met.petar_djordjevic_5594.gamevalut_server.service.notification.FriendRequestNotificationService;
import met.petar_djordjevic_5594.gamevalut_server.service.redis.RedisService;
import met.petar_djordjevic_5594.gamevalut_server.utils.Paginator;
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
    FriendRequestNotificationService friendRequestNotificationService;


    public CustomUserService() {
    }

    public void addUser(CustomUser newUser) {
        userRepository.save(newUser);
    }

    public void addUser(NewCustomUserDTO newCustomUserDTO) {

        CustomUser newUser = convertToEntity(newCustomUserDTO);

        newUser.setCreatedAt(LocalDate.now());

        userRepository.save(newUser);


    }

    public CustomUser getUserById(Integer userId) {
        Optional<CustomUser> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new NoSuchElementException("User not found!");

        return optionalUser.get();
    }

    public boolean isUserOnline(Integer userId) {
        return redisService.checkIfHashExist(this.getUserById(userId).getId().toString());
    }

    public boolean isUserInGame(Integer userId) {
        return redisService.checkIfHashExist(this.getUserById(userId).getId().toString(), "plays");
    }

    // prepravi kod da bi izbrisao ovu funkciju
    public List<CustomUser> getAllFriends(Integer userId) {

        List<CustomUser> friends = new ArrayList<>();

        Optional<List<CustomUser>> optonalFriends = userRepository.findAllFriends(userId);
        if (optonalFriends.isPresent())
            friends = optonalFriends.get();

        return friends;

    }

    public AllFriendsDTO getAllUsersFriends(Integer userId) {
        CustomUser user = this.getUserById(userId);


        List<FriendDTO> onlineFriends = new ArrayList<>();
        List<FriendDTO> offlineFriends = new ArrayList<>();

        user.getUserWithFriends().forEach(friendship -> {
            CustomUser friend = friendship.getUser2();

            if (redisService.checkIfHashExist(friend.getId().toString())) {
                String friendGame = (String) redisService.getFromRedis(friend.getId().toString(), "plays");
                onlineFriends.add(new FriendDTO(friend.getId(), friend.getUsername(), friend.getImageUrl(), null, friendGame));
            } else
                offlineFriends.add(new FriendDTO(friend.getId(), friend.getUsername(), friend.getImageUrl(), null, null));

        });

        Map<String, List<FriendDTO>> friends = new HashMap<>();

        friends.put("online", onlineFriends);
        friends.put("offline", offlineFriends);

        return new AllFriendsDTO(friends);

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

        if (redisService.checkIfHashExist(potentialFriend.getId().toString()))
            friendRequestNotificationService.notifyOnlineFriends(potentialFriend, user);

    }

    public FriendRequestsDTO getAllFriendRequests(Integer userId) {
        CustomUser user = this.getUserById(userId);

        List<SingleFriendRequestDTO> receivedRequest = new ArrayList<>();
        List<SingleFriendRequestDTO> sentRequest = new ArrayList<>();

        if (!user.getSentRequests().isEmpty()) {
            user.getSentRequests().forEach(request -> {
                sentRequest.add(new SingleFriendRequestDTO(request.getId(), new FriendDTO(request.getUid2().getId(), request.getUid2().getUsername(), request.getUid2().getImageUrl(), null, null)));
            });
        }

        if (!user.getReceivedRequests().isEmpty()) {
            user.getReceivedRequests().forEach(request -> {
                if (request.getUid2().getId() == userId) {
                    receivedRequest.add(new SingleFriendRequestDTO(request.getId(), new FriendDTO(request.getUid1().getId(), request.getUid1().getUsername(), request.getUid1().getImageUrl(), null, null)));

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

        if (optionalFriendRequest.get().getUid2().getId() != userId)
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

    public void deleteRequest(Integer requestId) {

        Optional<FriendRequest> optionalFriendRequest = friendRequestRepository.findById(requestId);

        if (optionalFriendRequest.isEmpty())
            throw new NoSuchElementException("Request not found");

        friendRequestRepository.deleteById(requestId);

    }

    public UserDescriptionDTO getUserProfileDescription(Integer userId) {
        CustomUser user = this.getUserById(userId);

        return new UserDescriptionDTO(user.getId(), user.getUsername(), user.getImageUrl(), user.getDescription());

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

    public Pages getFriendCommentsAndPaginate(Integer userId,Integer page,Integer limit){

        Paginator.validatePageAndLimit(page,limit);

        Integer offset = (page - 1) * limit;

        CustomUser user = this.getUserById(userId);
        List<CustomUser> friends = this.getAllFriends(userId);

        List<FriendCommentDTO> friendsComments = new ArrayList<>();

        friendCommentRepostiory.findCommentsAndPaginate(userId,limit,offset).get().forEach(comment -> {
            CustomUser friend = this.getUserById(comment.getFriendship().getUser1().getId());
            friendsComments.add(new FriendCommentDTO(comment.getId(), new FriendDTO(friend.getId(), friend.getUsername(), friend.getImageUrl(), null, null), comment.getContent(), comment.getPosted_at()));
        });

        return Paginator.getResoultAndPages(page, limit, friendCommentRepostiory.countFriendComments(userId), friendsComments);
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

    public Pages searchUsers(String username, Integer page, Integer limit) {

        Paginator.validatePageAndLimit(page, limit);

        Integer offset = (page - 1) * limit;

        List<FriendDTO> users = new ArrayList<>();

        userRepository.findByUsernameAndPaginate(username, offset, limit).get().forEach(user -> {
            users.add(new FriendDTO(user.getId(), user.getUsername(), user.getImageUrl(), null, null));
        });


        return Paginator.getResoultAndPages(page, limit, userRepository.countByUsername(username), users);
    }

    public CustomUser convertToEntity(NewCustomUserDTO newCustomUserDTO) {
        return new CustomUser(newCustomUserDTO.username(), newCustomUserDTO.password());
    }

    public FriendDTO convertToFriendDTO(CustomUser user) {
        return new FriendDTO(user.getId(), user.getUsername(), user.getImageUrl(), null, null);
    }
}
