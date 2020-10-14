package ru.innim.flutter_login_vk

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodChannel

/**
 * FlutterLoginVkPlugin
 */
class FlutterLoginVkPlugin : FlutterPlugin, ActivityAware {
    private var _dartChannel: MethodChannel? = null
    private var _loginCallback: LoginCallback? = null
    private var _methodCallHandler: MethodCallHandler? = null
    private var _activityListener: ActivityListener? = null
    private var _activityPluginBinding: ActivityPluginBinding? = null
    override fun onAttachedToEngine(flutterPluginBinding: FlutterPluginBinding) {
        val messenger = flutterPluginBinding.binaryMessenger
        _dartChannel = MethodChannel(messenger, _CHANNEL_NAME)
        _loginCallback = LoginCallback()
        _methodCallHandler = MethodCallHandler(flutterPluginBinding.applicationContext,
                _loginCallback!!)
        _activityListener = ActivityListener(_loginCallback!!)
        _dartChannel!!.setMethodCallHandler(_methodCallHandler)
    }

    override fun onAttachedToActivity(activityPluginBinding: ActivityPluginBinding) {
        _setActivity(activityPluginBinding)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        _resetActivity()
    }

    override fun onReattachedToActivityForConfigChanges(activityPluginBinding: ActivityPluginBinding) {
        _setActivity(activityPluginBinding)
    }

    override fun onDetachedFromActivity() {
        _resetActivity()
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        _methodCallHandler = null
        _loginCallback = null
        _dartChannel!!.setMethodCallHandler(null)
        _activityPluginBinding = null
        _activityListener = null
    }

    private fun _setActivity(activityPluginBinding: ActivityPluginBinding) {
        _methodCallHandler!!.updateActivity(activityPluginBinding.activity)
        activityPluginBinding.addActivityResultListener(_activityListener!!)
        _activityPluginBinding = activityPluginBinding
    }

    private fun _resetActivity() {
        if (_activityPluginBinding != null) {
            _activityPluginBinding!!.removeActivityResultListener(_activityListener!!)
            _activityPluginBinding = null
            _methodCallHandler!!.updateActivity(null)
        }
    }

    companion object {
        private const val _CHANNEL_NAME = "flutter_login_vk"
    }
}