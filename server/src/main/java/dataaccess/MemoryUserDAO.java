package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {
    Collection<UserData> users = new HashSet<UserData>();

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        try {
            users.add(user);
            return user;
        } catch (Exception e){
            throw new DataAccessException("Failed to add User");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try {
            for(UserData user : users){
                if(user.getUsername().equals(username)){
                    return user;
                }
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessException("Failed to get a user");
        }
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        try {
            users.removeIf(user -> user.getUsername().equals(username));
        } catch (Exception e) {
            throw new DataAccessException("Failed to delete user");
        }
    }

    @Override
    public void clear() throws DataAccessException {

        users.clear();

    }
}
