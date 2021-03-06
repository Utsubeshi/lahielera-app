package com.lahielera.app.notification

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lahielera.app.R
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PrivateChannel
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.channel.PusherEvent
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import java.lang.Exception

class PusherClient (
        val context: Context ) {

    private companion object {
        const val API_KEY = "7357430a418c50d7acec"
        const val AUTH_END_POINT = "https://nta-admin.herokuapp.com/pusher/auth"
        const val CHANNEL_NAME = "private-pedido"
        const val CLUSTER = "us2"
        const val EVENT_NAME = "client-enviado-"
    }

    val authorizer = HttpAuthorizer(AUTH_END_POINT)
    val options = PusherOptions().setCluster(CLUSTER).setAuthorizer(authorizer)

    var pusher = Pusher(API_KEY, options)
    lateinit var channel: PrivateChannel

    fun connect (idPedido: String) {
        pusher.connect(object: ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange?) {
                Log.d("PUSHER STATE", change?.currentState!!.name)
            }

            override fun onError(message: String?, code: String?, e: Exception?) {
                Log.d("PUSHER ERROR", message.toString())
            }
        })
        subscribe(idPedido)
    }

    fun subscribe(idPedido: String) {
        channel = pusher.subscribePrivate(CHANNEL_NAME, object : PrivateChannelEventListener {
            override fun onEvent(event: PusherEvent?) {
                Log.d("PUSHER ONEVENT", event?.data.toString())
            }

            override fun onSubscriptionSucceeded(channelName: String?) {
                Log.d("PUSHER ONSUBSCRIPTION", channelName.toString())
                channelBind(idPedido)

            }

            override fun onAuthenticationFailure(message: String?, e: Exception?) {
                Log.d("PUSHER ONFAIL", message.toString())
            }
        })
    }

    fun channelBind(idPedido: String) {
        channel.bind("$EVENT_NAME$idPedido", object: PrivateChannelEventListener {
            override fun onEvent(event: PusherEvent?) {
                //Log.d("PUSHER ONEVENT BIND", event?.data.toString())
                showNotification()
            }

            override fun onSubscriptionSucceeded(channelName: String?) {
                Log.d("PUSHER ONSUB BIND", channelName.toString())
            }

            override fun onAuthenticationFailure(message: String?, e: Exception?) {
                Log.d("PUSHER ONFAIL BIND", message.toString())
            }
        })
    }

    //Notificacion
    var builder = NotificationCompat.Builder(context, "101")
            .setSmallIcon(R.drawable.ic_app_icon)
            .setContentTitle("Pedido en camino!")
            .setContentText("El repatidor llegará en unos minutos a su domicilio")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    fun showNotification() {
        with(NotificationManagerCompat.from(context)) {
            notify(104, builder.build())
        }
    }

}