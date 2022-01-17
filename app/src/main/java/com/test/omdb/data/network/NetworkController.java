package com.test.omdb.data.network;

import android.content.Context;
import android.util.Log;
import com.test.omdb.BuildConfig;
import com.test.omdb.data.network.exceptions.*;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * This is the main controller than handles the network requests being sent or fetched.
 */
public class NetworkController {

    private static final String TAG = NetworkController.class.getName();

    private final Context context;

    private static final String API_KEY = "/?apikey=" + BuildConfig.OMDB_KEY + "&";
    private static final String IMDB_TITLE = "i=%s";
    private static final String TITLE = "t=%s";

    private static final String SEARCH_OMDB = "s=%s&page=%s";
    private static final String SEARCH_OMDB_TYPE = "s=%s&type=%s&page=%s";

    private final long soTimeoutMillis = TimeUnit.SECONDS.toMillis(30);
    private final boolean automaticNetworkRetry;

    private static final ResponseCodeHandler NO_HANDLER = new EmptyResponseCodeHandler();

    public NetworkController(boolean automaticNetworkRetry, Context context) {
        this.automaticNetworkRetry = automaticNetworkRetry;
        this.context = context;
    }

    private static RequestBody jsonRequestBody(String jsonBody) {
        return jsonBody != null
                ? RequestBody.create(MediaType.parse("application/json"), jsonBody)
                : null;
    }

    private interface ResponseCodeHandler {
        void handle(int responseCode) throws NonSuccessfulResponseCodeException, NetworkControllerException;
    }

    private static class EmptyResponseCodeHandler implements ResponseCodeHandler {
        @Override
        public void handle(int responseCode) {
        }
    }

    private String makeRequest(String urlFragment, NetworkMethod method, String jsonBody)
            throws NonSuccessfulResponseCodeException, NetworkControllerException {
        return makeRequest(urlFragment, method, jsonBody, NO_HANDLER);
    }

    private String makeRequest(String urlFragment, NetworkMethod method, String jsonBody, ResponseCodeHandler responseCodeHandler) throws NonSuccessfulResponseCodeException, NetworkControllerException {
        ResponseBody responseBody = makeBodyRequest(urlFragment, method, jsonRequestBody(jsonBody), responseCodeHandler);
        try {
            return responseBody.string();
        } catch (IOException e) {
            throw new NetworkControllerException(e);
        }
    }

    private ResponseBody makeBodyRequest(String urlFragment,
                                         NetworkMethod method,
                                         RequestBody body,
                                         ResponseCodeHandler responseCodeHandler)
            throws NonSuccessfulResponseCodeException, NetworkControllerException {
        return makeRequest(urlFragment, method, body, responseCodeHandler).body();
    }

    private Response makeRequest(String urlFragment,
                                 NetworkMethod method,
                                 RequestBody body,
                                 ResponseCodeHandler responseCodeHandler)
            throws NonSuccessfulResponseCodeException, NetworkControllerException {
        Response response = getServiceConnection(urlFragment, method, body);

        ResponseBody responseBody = response.body();
        try {
            responseCodeHandler.handle(response.code());

            return validateServiceResponse(response);
        } catch (NonSuccessfulResponseCodeException | NetworkControllerException e) {
            if (responseBody != null) {
                responseBody.close();
            }
            throw e;
        }
    }

    private Response getServiceConnection(String urlFragment, NetworkMethod method, RequestBody body)
            throws NetworkControllerException {
        try {
            OkHttpClient okHttpClient = buildOkHttpClient();
            Call call = okHttpClient.newCall(buildServiceRequest(urlFragment, method, body));

            return call.execute();
        } catch (IOException e) {
            throw new NetworkControllerException(e);
        }
    }

    /**
     * @param response
     * @return a response on whether the request to the API has been successful and if not the response code type gets thrown
     * @throws NonSuccessfulResponseCodeException
     */
    private Response validateServiceResponse(Response response) throws NonSuccessfulResponseCodeException {
        int responseCode = response.code();
        String responseMessage = response.message();

        switch (responseCode) {
            case 401:
            case 403:
                throw new AuthorizationFailedException(responseCode, "Authorization failed due to invalid API key!");
            case 404:
                throw new NotFoundException("Not found");
            case 417:
                throw new ExpectationFailedException();
            case 508:
                throw new ServerRejectedException();
        }

        if (responseCode != 200 && responseCode != 202 && responseCode != 204) {
            throw new NonSuccessfulResponseCodeException(responseCode, "Bad response: " + responseCode + " " + responseMessage);
        }

        return response;
    }

    private OkHttpClient buildOkHttpClient() {
        OkHttpClient baseClient = new OkHttpClient();

        return baseClient.newBuilder()
                .connectTimeout(soTimeoutMillis, TimeUnit.MILLISECONDS)
                .readTimeout(soTimeoutMillis, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(automaticNetworkRetry)
                .build();
    }

    private Request buildServiceRequest(String urlFragment, NetworkMethod method, RequestBody body) throws IOException {
        String apiUrl = BuildConfig.OMDB_URL + API_KEY;
        Request.Builder request = new Request.Builder();

        Log.e(TAG, apiUrl + urlFragment);

        request.url(apiUrl + urlFragment);
        request.method(String.valueOf(method), body);

        return request.build();
    }

    /**
     * We check if the string starts with "tt" which signifies that it's a title entity in IMDB
     * otherwise if it's not we return a title search instead
     */
    public String getTitle(String value) throws NonSuccessfulResponseCodeException, NetworkControllerException {
        if (value.startsWith("tt")) {
            return makeRequest(String.format(IMDB_TITLE, value), NetworkMethod.GET, null);
        } else {
            return makeRequest(String.format(TITLE, value), NetworkMethod.GET, null);
        }
    }

    public String searchOMDB(String value, int page) throws NonSuccessfulResponseCodeException, NetworkControllerException {
        String parameters = String.format(SEARCH_OMDB, value, page);
        return makeRequest(parameters, NetworkMethod.GET, null);
    }

    public String searchOmdbWithType(String value, String type, int page) throws NonSuccessfulResponseCodeException, NetworkControllerException {
        String parameters = String.format(SEARCH_OMDB_TYPE, value, type, page);
        return makeRequest(parameters, NetworkMethod.GET, null);
    }

}
