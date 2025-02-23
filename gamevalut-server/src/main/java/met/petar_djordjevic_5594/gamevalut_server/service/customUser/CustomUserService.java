package met.petar_djordjevic_5594.gamevalut_server.service.customUser;

import met.petar_djordjevic_5594.gamevalut_server.exception.CannotAddFriendException;
import met.petar_djordjevic_5594.gamevalut_server.model.customUser.*;
import met.petar_djordjevic_5594.gamevalut_server.model.game.AcquiredGameCopy;
import met.petar_djordjevic_5594.gamevalut_server.model.game.GameImage;
import met.petar_djordjevic_5594.gamevalut_server.model.game.GameImageType;
import met.petar_djordjevic_5594.gamevalut_server.model.game.RecentPlayedGameDTO;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public String getRelationshipOfUsers(Integer userId, Integer freindId) {
        CustomUser user = this.getUserById(userId);
        CustomUser friend = this.getUserById(freindId);

        if (user.getUserWithFriends().stream().filter(friendship -> friendship.getUser2().getId() == freindId).findFirst().isPresent())
            return "Friends";
        else if (user.getSentRequests().stream().filter(request -> request.getUid2().getId() == freindId).findFirst().isPresent())
            return "Request Sent";
        else if (user.getReceivedRequests().stream().filter(request -> request.getUid1().getId() == freindId).findFirst().isPresent())
            return "Request Received";
        else
            return "Send Friend Request";

    }

    public boolean doesHaveFriends(Integer userId) {
        CustomUser user = this.getUserById(userId);

        return !user.getUserWithFriends().isEmpty();
    }

    public boolean doesHaveComments(Integer userId) {
        CustomUser user = this.getUserById(userId);

        return user.getFriendsWithUser().stream().map(Friendship::getComment).anyMatch(Objects::nonNull);
    }

    public boolean doesUserPostComment(Integer userId, Integer friendId) {
        CustomUser user = this.getUserById(userId);
        CustomUser friend = this.getUserById(friendId);

        if (this.getAllFriends(userId).isEmpty())
            throw new NoSuchElementException("No friends!");

        if (this.getAllFriends(friendId).isEmpty())
            throw new NoSuchElementException("No friends!");

        if (user.getUserWithFriends().stream().filter(friendship -> friendship.getUser2().getId() == friendId).findAny().isEmpty())
            throw new NoSuchElementException("Not friends!");


        return Objects.nonNull(user.getUserWithFriends().stream().filter(friendship -> friendship.getUser2().getId() == friendId).findFirst().get().getComment());
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

        return new UserDescriptionDTO(user.getId(), user.getUsername(), user.getImageUrl(), user.getDescription(), user.getCreatedAt().toString());

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

    public Pages getFriendCommentsAndPaginate(Integer userId, Integer page, Integer limit) {

        Paginator.validatePageAndLimit(page, limit);

        Integer offset = (page - 1) * limit;

        CustomUser user = this.getUserById(userId);
        List<CustomUser> friends = this.getAllFriends(userId);

        List<FriendCommentDTO> friendsComments = new ArrayList<>();

        friendCommentRepostiory.findCommentsAndPaginate(userId, limit, offset).get().forEach(comment -> {
            CustomUser friend = this.getUserById(comment.getFriendship().getUser1().getId());
            friendsComments.add(new FriendCommentDTO(comment.getId(), new FriendDTO(friend.getId(), friend.getUsername(), friend.getImageUrl(), null, null), comment.getContent(), comment.getPosted_at()));
        });

        return Paginator.getResoultAndPages(page, limit, friendCommentRepostiory.countFriendComments(userId), friendsComments);
    }

    public Pages getRecentPlayedGames(Integer userId, Integer page, Integer limit) {


        CustomUser customUser = this.getUserById(userId);

        long offset;

        if (limit != -1) {
            offset = (long) (page - 1) * limit;

        } else {
            limit = customUser.getAcquiredGameCopies().size();
            offset = 0;
        }


        List<RecentPlayedGameDTO> recentPlayedGameDTOS = customUser
                .getAcquiredGameCopies()
                .stream()
                .sorted(Comparator.comparing(AcquiredGameCopy::getLastPlayedAt, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                .skip(offset)
                .limit(limit)
                .map(acquiredGameCopy -> {
                    String gameImage = acquiredGameCopy.getGame().getImages()
                            .stream()
                            .filter(gameImage1 -> gameImage1.getType() == GameImageType.Catalog)
                            .map(GameImage::getUrl)
                            .findAny()
                            .get();
                    LocalDateTime lastPlayedAt = acquiredGameCopy.getLastPlayedAt();
                    Double timePlayed = (double) acquiredGameCopy.getTimePlayed().intValue() / 3600000;

                    String lastPlayedAtString = Objects.isNull(lastPlayedAt) ? null : lastPlayedAt.toString();


                    return new RecentPlayedGameDTO(acquiredGameCopy.getGame().getId(), gameImage, acquiredGameCopy.getGame().getTitle(), timePlayed.toString(), lastPlayedAtString);
                })
                .toList();


        return Paginator.getResoultAndPages(page, limit == 0 ? 1 : limit, (long) customUser.getAcquiredGameCopies().size(), recentPlayedGameDTOS);
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

    public void updateUser(Integer userId, UpdatedCustomUserDTO updatedCustomUserDTO) throws DataIntegrityViolationException {
        CustomUser user = this.getUserById(userId);

        if (Objects.isNull(updatedCustomUserDTO.getDescription()) && Objects.isNull(updatedCustomUserDTO.getProfileImage()) && Objects.isNull(updatedCustomUserDTO.getUsername()))
            return;

        if (!Objects.isNull(updatedCustomUserDTO.getUsername()) && !updatedCustomUserDTO.getUsername().isBlank() && !updatedCustomUserDTO.getUsername().equals(user.getUsername())) {

            if (!updatedCustomUserDTO.getUsername().equalsIgnoreCase(user.getUsername())) {
                if (userRepository.findUserByUsername(updatedCustomUserDTO.getUsername()).isPresent())
                    throw new DataIntegrityViolationException("Username is already taken!");

            }

            user.setUsername(updatedCustomUserDTO.getUsername());
        }

        //if (Objects.isNull(updatedCustomUserDTO.getProfileImage()))
        //    user.setImageUrl(updatedCustomUserDTO.icon());

        if (!Objects.isNull(updatedCustomUserDTO.getDescription()) && !updatedCustomUserDTO.getDescription().isBlank())
            user.setDescription(updatedCustomUserDTO.getDescription());

        userRepository.save(user);

    }

    public void logout(Integer userId) {
        CustomUser user = this.getUserById(userId);

        if (!redisService.checkIfHashExist(user.getId().toString()))
            throw new NoSuchElementException("User is not online!");

        //TODO: ucini jwt nevalidinim
        redisService.deleteFromRedis(user.getId().toString());
    }

    public Pages searchUsers(Integer userId, String username, Integer page, Integer limit) {

        CustomUser customUser = this.getUserById(userId);
        List<CustomUser> friends = this.getAllFriends(userId);

        Paginator.validatePageAndLimit(page, limit);

        Integer offset = (page - 1) * limit;

        List<FriendDTO> users = new ArrayList<>();

        userRepository.findByUsernameAndPaginate(username, offset, limit).get().forEach(user -> {
            users.add(new FriendDTO(user.getId(), user.getUsername(), user.getImageUrl(), null, null));
        });

        Set<Integer> excludeIds = friends.stream()
                .map(CustomUser::getId) // Mapiramo prijatelje u njihove ID-eve
                .collect(Collectors.toSet());
        excludeIds.add(customUser.getId());

        users.removeIf(user2 -> excludeIds.contains(user2.id()));


        return Paginator.getResoultAndPages(page, limit, userRepository.countByUsername(username), users);
    }

    public CustomUser convertToEntity(NewCustomUserDTO newCustomUserDTO) {
        return new CustomUser(newCustomUserDTO.username(), newCustomUserDTO.password());
    }

    public FriendDTO convertToFriendDTO(CustomUser user) {
        return new FriendDTO(user.getId(), user.getUsername(), user.getImageUrl(), null, null);
    }
}
