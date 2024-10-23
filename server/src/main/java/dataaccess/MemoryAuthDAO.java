package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO{
    Collection<AuthData> tokensList = new HashSet<AuthData>();
    @Override
    public AuthData createAuth(AuthData token) throws DataAccessException {
        tokensList.add(token);
        return token;
    }

    @Override
    public AuthData getAuth(String uuid) throws DataAccessException {
        for (AuthData token : tokensList){
            if (token.getAuthToken().equals(uuid)) {
                return token;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String uuid) throws DataAccessException {
        tokensList.removeIf(token -> token.getAuthToken().equals(uuid));

    }

    @Override
    public void clear() throws DataAccessException {
        tokensList.clear();

    }
}
