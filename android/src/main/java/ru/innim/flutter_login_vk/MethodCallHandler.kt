package ru.innim.flutter_login_vk

import android.app.Activity
import android.content.Context
import com.vk.api.sdk.VK
import com.vk.api.sdk.VK.getApiVersion
import com.vk.api.sdk.VK.initialize
import com.vk.api.sdk.VK.isLoggedIn
import com.vk.api.sdk.VK.logout
import com.vk.api.sdk.auth.VKScope
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.util.*

class MethodCallHandler(private val _context: Context, private val _loginCallback: LoginCallback) : MethodChannel.MethodCallHandler {
    private var _activity: Activity? = null
    fun updateActivity(activity: Activity?) {
        _activity = activity
    }

    override fun onMethodCall(call: MethodCall, r: MethodChannel.Result) {
        if (_activity != null) {
            when (call.method) {
                _LOGIN_METHOD -> {
                    val scope = call.argument<List<String>>(_SCOPE_LOGIN_ARG)!!
                    logIn(scope, r)
                }
                _LOGOUT_METHOD -> {
                    logOut()
                    result(null, r)
                }
                _GET_ACCESS_TOKEN -> result(accessToken, r)
                _GET_USER_PROFILE -> { }
                _GET_SDK_VERSION -> result(sdkVersion, r)
                _INIT_SDK_METHOD -> {
                    val rawAppId = call.argument<String>(_APP_ID_INIT_ARG)
                    var appId = 0
                    if (rawAppId != null) {
                        appId = rawAppId.toInt()
                        if (appId != 0) {
//                            var apiVersion = call.argument<String>(_API_VERSION_INIT_ARG)
//                            val initScope = call.argument<List<String>>(_SCOPE_INIT_ARG)!!
                            result(initSdk(), r)
                        } else {
                            error(FlutterError.invalidArgs("Arguments is invalid", null), r)
                        }
                    } else {
                        error(FlutterError.invalidArgs("Arguments is invalid", null), r)
                    }
                }
                else -> r.notImplemented()
            }
        }
    }

    private fun initSdk(): Boolean {
        initialize(_context)
        return true
    }

    private fun logIn(scope: List<String>, result: MethodChannel.Result) {
        _loginCallback.addPending(result)
        VK.login(_activity!!, scope.map { VKScope.valueOf(it.toUpperCase(Locale.ROOT)) })
    }

    private fun logOut() {
        logout()
    }

    private val accessToken: Map<String, Any>?
        get() {
            if (isLoggedIn()) {
                val token = _loginCallback.accessToken
                if (token != null) {
                    return Results.accessToken(token)
                }
            }
            return null
        }

    //    private void getUserProfile(final Result r) {
    //        final VKAccessToken token = VKAccessToken.currentToken();
    //        if (token != null) {
    //            VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
    //                    VKApiUser.FIELDS_DEFAULT));
    //            request.executeWithListener(new VKRequest.VKRequestListener() {
    //                @Override
    //                public void onComplete(VKResponse response) {
    //                    @SuppressWarnings("unchecked")
    //                    final List<VKApiUserFull> users = (List<VKApiUserFull>) response.parsedModel;
    //                    result(Results.userProfile(users.get(0)), r);
    //                }
    //
    //                @Override
    //                public void onError(VKError error) {
    //                    error(FlutterError.apiError("Get profile error: " + error.errorMessage, error), r);
    //                }
    //
    //                @Override
    //                public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
    //                    error(FlutterError.invalidResult("Get user profile attempt failed", null), r);
    //                }
    //            });
    //        }
    //    }
    val sdkVersion: String
        get() = getApiVersion()

    private fun result(data: Any?, r: MethodChannel.Result) {
        r.success(data)
    }

    private fun error(error: FlutterError, r: MethodChannel.Result) {
        r.error(error.code, error.message, error.details)
    }

    companion object {
        private const val _LOGIN_METHOD = "logIn"
        private const val _LOGOUT_METHOD = "logOut"
        private const val _GET_ACCESS_TOKEN = "getAccessToken"
        private const val _GET_USER_PROFILE = "getUserProfile"
        private const val _GET_SDK_VERSION = "getSdkVersion"
        private const val _INIT_SDK_METHOD = "initSdk"
        private const val _SCOPE_LOGIN_ARG = "scope"
        private const val _SCOPE_INIT_ARG = "scope"
        private const val _APP_ID_INIT_ARG = "appId"
        private const val _API_VERSION_INIT_ARG = "apiVersion"
    }

}