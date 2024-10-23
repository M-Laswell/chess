package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{
    private static MemoryAuthDAO instance;
    public Collection<AuthData> tokensList = new HashSet<AuthData>();

    public static MemoryAuthDAO getInstance(){
        if (instance == null) {
            instance = new MemoryAuthDAO();
        }
        return instance;
    }


    @Override
    public AuthData createAuth(AuthData token) throws DataAccessException {
        tokensList.add(token);
        return token;
    }

    @Override
    public AuthData getAuth(String uuid) throws DataAccessException {
        System.out.println(tokensList);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryAuthDAO that = (MemoryAuthDAO) o;
        return Objects.equals(tokensList, that.tokensList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tokensList);
    }

    @Override
    public void clear() throws DataAccessException {
        tokensList.clear();

    }
}
