package com.test.omdb.data.network;

import android.content.Context;
import com.test.omdb.data.network.exceptions.NetworkControllerException;
import com.test.omdb.data.network.exceptions.NetworkFailureReason;
import com.test.omdb.data.network.exceptions.NonSuccessfulResponseCodeException;
import com.test.omdb.utils.AsynchronousCallback;

/**
 * Instead of directly calling the NetworkController we use this class. Reason for this is to keep
 * the code clean and to easier navigate to the issues that my arise if there is any.
 *
 * Note: This class makes calls from the NetworkController instead!
 */

public class NetworkServiceManager {

    private final NetworkController networkController;
    private final Context context;

    public NetworkServiceManager(boolean automaticNetworkRetry, Context context){
        this.networkController = new NetworkController(automaticNetworkRetry, context);;
        this.context = context;
    }

    public void getTitle(String value, AsynchronousCallback.WorkerThread<String, NetworkFailureReason> callback){
        try {
            callback.onComplete(networkController.getTitle(value));
        } catch (NonSuccessfulResponseCodeException | NetworkControllerException e) {
            callback.onError(NetworkFailureReason.fromException(e));
        }
    }

    public String searchOmdb(String query, int page){
        try {
            return networkController.searchOMDB(query, page);
        } catch (NonSuccessfulResponseCodeException | NetworkControllerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String searchOmdbWithType(String query, String type, int page){
        try {
            return networkController.searchOmdbWithType(query, type, page);
        } catch (NonSuccessfulResponseCodeException | NetworkControllerException e){
            e.printStackTrace();
        }
        return null;
    }

}
