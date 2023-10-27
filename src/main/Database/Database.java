package Database;

import Models.AuthToken;
import Models.Game;
import Models.User;

import java.util.ArrayList;

public interface Database {

    public void writeGame(Game game);
    public void writeAuth(AuthToken authToken);
    public void writeUser(User user);

    public Game readGame(Integer gameID);
    public User readUser(String username);
    public AuthToken readAuth(String authToken);

    public void removeGame(Integer gameID);
    public void removeAuth(String authToken);
    public void removeUser(String username);

    public void updateGame(Game game);
    public void updateUser(User user);

    public ArrayList<Game> readAllGames();

    public void clearGames();
    public void clearUsers();
    public void clearAuth();
    public void clearDB();

    public boolean noGamesInDB();
    public boolean noUsersInDB();
    public boolean noAuthInDB();
}
