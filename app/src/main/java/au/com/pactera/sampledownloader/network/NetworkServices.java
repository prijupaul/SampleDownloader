package au.com.pactera.sampledownloader.network;


import au.com.pactera.sampledownloader.dto.Constants;
import au.com.pactera.sampledownloader.dto.JSONResponse;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by priju.jacobpaul on 3/05/15.
 */
public class NetworkServices {
    public interface DownloadJsonService{
        @GET(Constants.URL_PATH)
        void getJson(Callback<JSONResponse> response);
    }
}
