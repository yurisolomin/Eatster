package ru.baccasoft.eatster.service;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.vaadin.spring.annotation.SpringComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javapns.Push;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.ResponsePacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.eatster.model.PushTokenModel;
import static ru.baccasoft.eatster.service.PushTokenService.PLATFORM_TYPE_ANDROID;
import static ru.baccasoft.eatster.service.PushTokenService.PLATFORM_TYPE_IOS;
import ru.baccasoft.eatster.service.util.Utf8Sender;
import ru.baccasoft.utils.logging.Logger;
import ru.baccasoft.utils.pusher.DeviceToken;
import ru.baccasoft.utils.pusher.Platform;
import ru.baccasoft.utils.pusher.Pusher;
import ru.baccasoft.utils.pusher.android.AndroidPushAgent;
import ru.baccasoft.utils.pusher.ios.IosPushAgent;

@SpringComponent
public class PushService {

    private static final Logger LOG = Logger.getLogger(PushService.class);
    private static final int NUMBER_OF_DEVICES_FOR_ANDROID_PAYLOAD = 250;
    private static final int NUMBER_OF_DEVICES_FOR_IOS_PAYLOAD = 250;
    private static final int NUMBER_OF_THREADS_FOR_IOS_PAYLOAD = 20;
    //private static final int ASYNC_DEMO_SLEEP = 5000;

    @Autowired
    AppProp appProp;

    public int syncPush(List<PushTokenModel> pushTokens, String msg) {
        LOG.info("syncPush:msg={0},pushtokens={1}", msg, pushTokens.size());
        Pusher p = new Pusher();
        //IOS
        IosPushAgent iosAgent = new IosPushAgent();
        boolean iosEnabled = false;
        String iosKeypath = appProp.getPushIosKeypath();
        String iosPassword = appProp.getPushIosPassword();
        boolean iosProduction = appProp.isPushIosProduction();
        LOG.info("ios settings: keypath={0},password={1},production={2}", iosKeypath, iosPassword, iosProduction);
        if (!iosKeypath.isEmpty()) {
            LOG.info("ios agent added");
            iosAgent.addKeystore(iosKeypath, iosPassword, iosProduction);
            p.addPushAgent(iosAgent);
            iosEnabled = true;
        } else {
            LOG.info("ios agent skipped");
        }
        //Android
        boolean androidEnabled = false;
        String androidApikey = appProp.getPushAndroidApikey();
        String collapseKey = appProp.getPushAndroidCollapsekey();
        LOG.info("android settings: apikey={0},collapsekey={1}", androidApikey, collapseKey);
        if (!androidApikey.isEmpty()) {
            LOG.info("android agent added");
            AndroidPushAgent androidAgent = new AndroidPushAgent();
            androidAgent.config(androidApikey, collapseKey);
            p.addPushAgent(androidAgent);
            androidEnabled = true;
        } else {
            LOG.info("android agent skipped");
        }
        //заполним токены
        List<DeviceToken> tokens = new ArrayList();
        for (PushTokenModel token : pushTokens) {
            Platform platform = null;
            if (iosEnabled) {
                if (token.getPlatform().equals(PLATFORM_TYPE_IOS)) {
                    platform = Platform.IOS;
                }
            }
            if (androidEnabled) {
                if (token.getPlatform().equals(PLATFORM_TYPE_ANDROID)) {
                    platform = Platform.ANDROID;
                }
            }
            if (platform != null) {
                tokens.add(new DeviceToken(platform, token.getPushToken()));
            }
        }
        if (tokens.size() > 0) {
            LOG.info("syncPush execute. msg={0},tokens={1}", msg, tokens.size());
            p.push(msg, tokens);
        } else {
            LOG.info("syncPush skipped. msg={0},tokens={1}", msg, tokens.size());
        }
        return tokens.size();
    }

    /*    
    public int push(String phone, String msg) {
        LOG.info("push: phone={0},msg={1}", phone, msg );

    if (items.size() > 0) {
            int sended = push(items,msg);
            LOG.info("Ok.");
            return sended;
        } else {
            LOG.info("Fail. No tokens");
            return 0;
        }
    }
     */

    private boolean asyncPushRunning = false;
    private int asyncAndroidKeysTotal;
    private int asyncAndroidKeysProcessed;
    private int asyncAndroidKeysSent;
    private int asyncIOSKeysTotal;
    private int asyncIOSKeysProcessed;
    private int asyncIOSKeysSent;
    
    public void asyncPushClearIndicators() {
        asyncIOSKeysTotal = 0;
        asyncIOSKeysProcessed = 0;
        asyncIOSKeysSent = 0;
        asyncAndroidKeysTotal = 0;
        asyncAndroidKeysProcessed = 0;
        asyncAndroidKeysSent = 0;
    }
    
    @Async
    public Future<Boolean> asyncPush(String msg, List<PushTokenModel> pushTokens) {
        LOG.info("asyncPush: msg={0}, size={1}", msg, pushTokens.size());
        if (asyncPushRunning) {
            throw new RuntimeException("Error! asyncPush already running");
        }
        //System.out.println("Execute method asynchronously - " + Thread.currentThread().getName());
        asyncPushRunning = true;
        asyncPushClearIndicators();
        try {
            asyncPushStart(msg, pushTokens);
            //Thread.sleep(ASYNC_DEMO_SLEEP);
            LOG.info("ios: Total={0}, Processed={1}, Sent={2}",asyncIOSKeysTotal,asyncIOSKeysProcessed,asyncIOSKeysSent );
            LOG.info("android: Total={0}, Processed={1}, Sent={2}",asyncAndroidKeysTotal,asyncAndroidKeysProcessed,asyncAndroidKeysSent);
            LOG.info("asyncPush:Ok");
            return new AsyncResult(true);
        } catch (Exception e) {
            LOG.info("asyncPush:Fail. Exception={0}", e.getMessage());
            return new AsyncResult(false);
            //
        } finally {
            asyncPushRunning = false;
        }
    }

    private void asyncPushStart(String msg, List<PushTokenModel> pushTokens) {
        LOG.info("asyncPushStart: msg={0}, size={1}", msg, pushTokens.size());
        ArrayList<String> iKeys = new ArrayList();
        ArrayList<String> androidKeys = new ArrayList();
        for (PushTokenModel token : pushTokens) {
            if (token.getPlatform().equals(PLATFORM_TYPE_IOS)) {
                iKeys.add(token.getPushToken());
            }
            if (token.getPlatform().equals(PLATFORM_TYPE_ANDROID)) {
                androidKeys.add(token.getPushToken());
            }
        }
        asyncIOSKeysTotal = iKeys.size();
        asyncAndroidKeysTotal = androidKeys.size();
        int from = 0;
        while (from < iKeys.size()) {
            int to = Math.min(from + NUMBER_OF_DEVICES_FOR_IOS_PAYLOAD, iKeys.size());
            asyncPushIos(msg, iKeys, from, to);
            from = to;
        }
        from = 0;
        while (from < androidKeys.size()) {
            int to = Math.min(from + NUMBER_OF_DEVICES_FOR_ANDROID_PAYLOAD, androidKeys.size());
            asyncPushAndroid(msg, androidKeys, from, to);
            from = to;
        }
        LOG.info("asyncPushStart: Ok.");
    }

    private void asyncPushIos(String msg, List<String> keys, int from, int to) {
        LOG.info("asyncPushIos:msg={0},pushtokens={1},from={2},to={3}", msg, keys.size(), from, to-1);
        String iosKeypath = appProp.getPushIosKeypath();
        String iosPassword = appProp.getPushIosPassword();
        boolean iosProduction = appProp.isPushIosProduction();
        LOG.info("ios settings: keypath={0},password={1},production={2}", iosKeypath, iosPassword, iosProduction);
        if (iosKeypath.isEmpty()) {
            LOG.info("Fail. No settings");
            return;
        }
        try {
            PushNotificationPayload payload = PushNotificationPayload.complex();
            payload.addAlert(msg);
//            payload.addBadge(1);
            payload.addSound("default");
//            Push.combined(msg, to, msg, keys, iosPassword, iosProduction, keys);
            PushedNotifications distr = Push
                    .payload(payload,
                            this.getClass().getClassLoader().getResourceAsStream(iosKeypath),
//                            new File(iosKeypath),
                            iosPassword, iosProduction, NUMBER_OF_THREADS_FOR_IOS_PAYLOAD,
                            keys.subList(from, to)
                    );
            asyncIOSKeysProcessed = to;
            for (PushedNotification notification : distr) {
                ResponsePacket response = notification.getResponse();
                if (response != null) {
                    LOG.info("Response {0}", response.getMessage());
                }
                if (!notification.isSuccessful()) {
                    LOG.error("Failed sending push for token " + notification.getDevice().getToken(),
                            notification.getException());
                } else {
                    ++asyncIOSKeysSent;
                    LOG.debug("Push sent="+notification.getDevice().getToken());
                }
            }
            LOG.info("Ok.");
        } catch (Exception e) {
            LOG.error("Failed sending push for ios devices starting from " + from+": "+e.getMessage());
        }
    }

    private void asyncPushAndroid(String msg, List<String> keys, int from, int to) {
        LOG.info("asyncPushAndroid:msg={0},pushtokens={1},from={2},to={3}", msg, keys.size(), from, to-1);
        String androidApikey = appProp.getPushAndroidApikey();
        String collapseKey = appProp.getPushAndroidCollapsekey();
        LOG.info("android settings: apikey={0},collapsekey={1}", androidApikey, collapseKey);
        if (androidApikey.isEmpty()) {
            LOG.info("Fail. No settings");
            return;
        }
        try {
//            Sender sender = new Sender(androidApikey);
            //использую кастомный сендер, т.к. русские буквы не отправляются
            Sender sender = new Utf8Sender(androidApikey);

            Message.Builder builder = new Message.Builder().addData("message", msg).collapseKey(collapseKey);
            builder.addData("sound", "default");
            //оставим только список для отправки (в оригинале в PushProcessor.java этого не было)
            keys = keys.subList(from, to);
            //
            MulticastResult mcResult = sender.send(builder.build(), keys, 5);
            asyncAndroidKeysProcessed = to;
            for (int i = 0; i < mcResult.getTotal(); i++) {
                Result result = mcResult.getResults().get(i);
                if (result.getMessageId() == null) {
                    LOG.error("Failed sending push for token " + keys.get(i)
                            + " with error " + result.getErrorCodeName());
                } else {
                    ++asyncAndroidKeysSent;
                    LOG.debug("Push sent="+keys.get(i));
                }
            }
            LOG.info("Ok.");
        } catch (Exception e) {
            LOG.error("Failed sending push for android devices starting from " + from+": "+e.getMessage());
        }
    }

    public boolean isAsyncPushRunning() {
        return asyncPushRunning;
    }

    public int getAsyncAndroidKeysTotal() {
        return asyncAndroidKeysTotal;
    }

    public int getAsyncAndroidKeysProcessed() {
        return asyncAndroidKeysProcessed;
    }

    public int getAsyncAndroidKeysSent() {
        return asyncAndroidKeysSent;
    }

    public int getAsyncIOSKeysTotal() {
        return asyncIOSKeysTotal;
    }

    public int getAsyncIOSKeysProcessed() {
        return asyncIOSKeysProcessed;
    }

    public int getAsyncIOSKeysSent() {
        return asyncIOSKeysSent;
    }

    
    
}
