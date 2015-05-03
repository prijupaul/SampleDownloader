package au.com.pactera.sampledownloader.network;

import java.io.IOException;
import java.net.HttpURLConnection;

import au.com.pactera.sampledownloader.BuildConfig;
import au.com.pactera.sampledownloader.dto.Constants;
import au.com.pactera.sampledownloader.dto.Error;
import au.com.pactera.sampledownloader.dto.JSONResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.client.UrlConnectionClient;

/**
 * Created by priju.paul on 3/05/15.
 */
public class DownloadHandler  {


    private Client mClient;
    private DownloadHandlerCallback mDownloadCallback;
    private HttpURLConnection mHttpUrlConnection;
    private RestAdapter mRestAdapter;
    private NetworkServices.DownloadJsonService mDownloadJsonService;

    private final int TIMEOUT = 60000;

    public interface DownloadHandlerCallback{
        void onResponse(JSONResponse response);
        void onError(Error error);
    }

    public DownloadHandler(){


        mClient = new UrlConnectionClient(){
            @Override
            protected HttpURLConnection openConnection(Request request) throws IOException {
                mHttpUrlConnection = super.openConnection(request);
                mHttpUrlConnection.setConnectTimeout(TIMEOUT);
                mHttpUrlConnection.setReadTimeout(TIMEOUT);
                return mHttpUrlConnection;
            }
        };

        RestAdapter.Builder restBuilder = new RestAdapter.Builder();
        mRestAdapter = restBuilder
                .setEndpoint(Constants.URL)
                .setClient(mClient)
                .build();


        if(BuildConfig.DEBUG){
            mRestAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
    }

    /**
     * Perform the network operation and
     * download the json
     * @param callback interface callback object
     */
    public void downloadJSON(DownloadHandlerCallback callback){

        mDownloadCallback = callback;

        mDownloadJsonService = mRestAdapter.create(NetworkServices.DownloadJsonService.class);
        mDownloadJsonService.getJson(new Callback<JSONResponse>(){
            @Override
            public void success(JSONResponse jsonResponse, Response response) {
                if(mDownloadCallback != null){
                    mDownloadCallback.onResponse(jsonResponse);
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Error networkError = new Error();
                if(error.getKind() == RetrofitError.Kind.NETWORK){
                    networkError.setErrorDescription("Network Error");
                }

                if(error.getResponse() != null) {
                    networkError.setErrorCode(error.getResponse().getStatus());
                    networkError.setErrorDescription(error.getResponse().getReason());
                }
                mDownloadCallback.onError(networkError);
            }
        });
    }

    /**
     * Cancel the download operation
     */
    public void cancelDownload(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mHttpUrlConnection != null){
                    mHttpUrlConnection.disconnect();
                }
            }
        });
    }
}
