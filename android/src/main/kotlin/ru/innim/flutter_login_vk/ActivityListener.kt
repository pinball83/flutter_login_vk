package ru.innim.flutter_login_vk

import android.content.Intent
import com.vk.api.sdk.VK.onActivityResult
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener

class ActivityListener(private val _loginCallback: LoginCallback) : ActivityResultListener {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return onActivityResult(requestCode, resultCode, data, _loginCallback)
    }
}