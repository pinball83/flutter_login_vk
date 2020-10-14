package ru.innim.flutter_login_vk

import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import io.flutter.plugin.common.MethodChannel

class LoginCallback : VKAuthCallback {
    private var _pendingResult: MethodChannel.Result? = null
    var accessToken: VKAccessToken? = null
        private set

    fun addPending(result: MethodChannel.Result?) {
        if (_pendingResult != null) callError(FlutterError.interrupted("Interrupted by another login call", null))
        _pendingResult = result
    }

    override fun onLogin(token: VKAccessToken) {
        accessToken = token
        callResult(Results.loginSuccess(token))
    }

    override fun onLoginFailed(errorCode: Int) {
        if (errorCode == VKAuthCallback.AUTH_CANCELED) {
            callResult(Results.loginCancelled())
        } else {
            callError(FlutterError.apiError("Login failed: ", errorCode))
        }
    }

    private fun callResult(data: Map<String, Any>) {
        if (_pendingResult != null) {
            _pendingResult!!.success(data)
            _pendingResult = null
        }
    }

    private fun callError(error: FlutterError) {
        if (_pendingResult != null) {
            _pendingResult!!.error(error.code, error.message, error.details)
            _pendingResult = null
        }
    }

}