package com.example.expenses

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var gestureDetector: GestureDetectorCompat

    // Lista głównych zakładek, na których dozwolone jest swipe
    private val topLevelDestinations = setOf(R.id.navigation_home, R.id.navigation_history, R.id.navigation_summary)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Inicjalizacja detektora gestów
        gestureDetector = GestureDetectorCompat(this, SwipeGestureListener())
    }

    // Przechwytywanie wszystkich zdarzeń dotyku i przekazywanie ich do detektora gestów
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            gestureDetector.onTouchEvent(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Wewnętrzna klasa do obsługi gestów
    private inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {

        private val swipeThreshold = 100
        private val swipeVelocityThreshold = 100

        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (e1 == null) return false

            // Upewnij się, że jesteśmy na jednej z głównych zakładek
            if (navController.currentDestination?.id !in topLevelDestinations) {
                return false
            }

            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y

            if (abs(diffX) > abs(diffY)) { // Sprawdź, czy gest jest bardziej horyzontalny niż wertykalny
                if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        // Przesunięcie w prawo
                        handleSwipeRight()
                    } else {
                        // Przesunięcie w lewo
                        handleSwipeLeft()
                    }
                    return true
                }
            }
            return false
        }
    }

    private fun handleSwipeLeft() {
        when (navController.currentDestination?.id) {
            R.id.navigation_home -> navController.navigate(R.id.navigation_history)
            R.id.navigation_history -> navController.navigate(R.id.navigation_summary)
        }
    }

    private fun handleSwipeRight() {
        when (navController.currentDestination?.id) {
            R.id.navigation_summary -> navController.navigate(R.id.navigation_history)
            R.id.navigation_history -> navController.navigate(R.id.navigation_home)
        }
    }
}