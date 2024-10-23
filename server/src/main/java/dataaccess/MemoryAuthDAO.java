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
    public AuthData getAuth(String token) throws DataAccessException {
        return new AuthData(token, "JohnDoe");
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
