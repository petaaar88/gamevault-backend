package met.petar_djordjevic_5594.gamevalut_server.service.launcher;

import met.petar_djordjevic_5594.gamevalut_server.model.customUser.CustomUser;
import met.petar_djordjevic_5594.gamevalut_server.model.game.Game;
import met.petar_djordjevic_5594.gamevalut_server.service.customUser.CustomUserService;
import met.petar_djordjevic_5594.gamevalut_server.service.game.GameService;
import met.petar_djordjevic_5594.gamevalut_server.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class LauncherService {

    @Autowired
    CustomUserService userService;
    @Autowired
    GameService gameService;
    @Autowired
    RedisService redisService;

    public LauncherService() {
    }

    public void play(Integer userId,Integer gameId){
        CustomUser user = userService.getUserById(userId);
        Game game = gameService.getGameById(gameId);

        if(!gameService.doesUserHaveGame(userId,gameId))
            throw  new NoSuchElementException("User doesnt own this game!");

        if(!userService.isUserOnline(userId))
            throw  new DataIntegrityViolationException("User must be online to play this game!");

        if(userService.isUserInGame(userId))
            throw  new DataIntegrityViolationException("User is already in game!");

        redisService.saveToRedis(user.getId().toString(),"plays",game.getTitle());

    }
}
