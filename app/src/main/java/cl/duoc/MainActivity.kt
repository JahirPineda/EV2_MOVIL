package cl.duoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import cl.duoc.navigation.BitacoraNavGraph
import cl.duoc.ui.theme.BitacoraTheme
import cl.duoc.ui.theme.FondoClaro

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BitacoraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = FondoClaro
                ) {
                    val navController = rememberNavController()
                    BitacoraNavGraph(navController = navController)
                }
            }
        }
    }
}