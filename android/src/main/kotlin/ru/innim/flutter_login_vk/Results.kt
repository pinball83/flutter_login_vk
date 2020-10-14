package ru.innim.flutter_login_vk

import com.vk.api.sdk.auth.VKAccessToken
import java.util.*

object Results {
    fun loginCancelled(): Map<String, Any> {
        return hashMapOf("isCanceled" to true)
    }

    fun loginSuccess(accessToken: VKAccessToken): Map<String, Any> {
        return  hashMapOf("accessToken" to accessToken(accessToken))
    }

    @JvmStatic
    fun error(error: Int): Map<String, Any> {
        return hashMapOf("apiCode" to error)
    }

    fun accessToken(accessToken: VKAccessToken): Map<String, Any> {
        return  hashMapOf(
                "token" to accessToken.accessToken,
                "userId" to accessToken.userId,
                "created" to accessToken.created,
                "email" to accessToken.email.orEmpty(),
                "secret" to accessToken.secret.orEmpty()
        )
    }
    //    public static HashMap<String, Object> userProfile(final VKApiUser user) {

    //        if (user == null)
    //            return null;
    //
    //        return new HashMap<String, Object>() {{
    //            put("userId", user.getId());
    //            put("firstName", user.first_name);
    //            put("lastName", user.last_name);
    //            put("online", user.online);
    //            put("onlineMobile", user.online_mobile);
    //            put("photo50", user.photo_50);
    //            put("photo100", user.photo_100);
    //            put("photo200", user.photo_200);
    //        }};
    //    }
}