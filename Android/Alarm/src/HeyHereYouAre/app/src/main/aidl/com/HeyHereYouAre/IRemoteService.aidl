package com.HeyHereYouAre;
import com.HeyHereYouAre.IRemoteServiceCallback;

interface IRemoteService {
    void registerCallback(IRemoteServiceCallback cb);
    void unregisterCallback(IRemoteServiceCallback cb);
}
