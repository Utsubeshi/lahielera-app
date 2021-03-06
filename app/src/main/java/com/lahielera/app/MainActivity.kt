package com.lahielera.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lahielera.app.notification.PusherClient
import com.lahielera.app.ui.catalogo.CatalogoFragmentDirections
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val picasso = Picasso.get()
    private lateinit var headerView: View
    //private lateinit var drawerToggle: ActionBarDrawerToggle
    private var pusherClient = PusherClient(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        createNotificationChannel()
        //PUSHER
        pusherClient.pusher.connect()


        //AUTH
        auth = Firebase.auth
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.menu.findItem(R.id.cerrar_session).setOnMenuItemClickListener{ MenuItem -> logOut() }
        //Cargar perfil usuario
        userProfileOnMenu(navView)
        val navController = findNavController(R.id.nav_host_fragment)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            navController.navigate(CatalogoFragmentDirections.actionNavCatalogoToCarritoFragment())
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_carrito, R.id.nav_catalogo, R.id.nav_pedidos
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }



    private fun userProfileOnMenu(navView: NavigationView) {
        headerView = navView.getHeaderView(0)
        val imgUsuario: ImageView = headerView.findViewById(R.id.usuario_imagen)
        val mailUsuario: TextView = headerView.findViewById(R.id.usuario_mail)
        picasso.load(auth.currentUser?.photoUrl.toString()).into(imgUsuario)
        mailUsuario.text = auth.currentUser?.email
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun logOut(): Boolean {
        auth.signOut()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
        return true
    }

//    //Notificacion
//    var builder = NotificationCompat.Builder(this, "101")
//            .setSmallIcon(R.drawable.ic_app_icon)
//            .setContentTitle("Pedido en camino!")
//            .setContentText("El repatidor llegará en unos minutos a su domicilio")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//    fun showNotification() {
//        with(NotificationManagerCompat.from(this)) {
//            notify(104, builder.build())
//        }
//    }
//
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("101", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}