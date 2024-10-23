package handler;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearApplicationHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        return new Gson().toJson("{}");
    }
}
