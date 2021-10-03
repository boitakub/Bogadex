package fr.boitakub.bgg.client;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CollectionRequestQueuedInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        okhttp3.Response response = chain.proceed(request);
        if (response.code() == 202) {
            throw new CollectionRequestQueuedException();
        }
        return response;
    }

}
