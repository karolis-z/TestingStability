package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screens.PagerScreen
import com.example.myapplication.screens.SettingsDeviceListScreen
import com.example.myapplication.screens.performanepage.PerformancePageScreen
import com.example.myapplication.screens.performanepage.PerformancePageScreen2
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()
            MyApplicationTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "starting"
                    ) {
                        composable("starting") {
                            StartingScreen(
                                gotoList = { navController.navigate("listscreen") },
                                goToPager = { navController.navigate("pager") },
                                goToPerformancePage = { navController.navigate("performance") },
                                goToPerformancePage2 = { navController.navigate("performance2") },
                            )
                        }
                        composable("listscreen") {
                            SettingsDeviceListScreen(
                                onBackClick = { navController.popBackStack() },
                                onDeviceClick = { locationId, deviceId, locationName ->
                                    sendToast(this@MainActivity, locationId, deviceId, locationName)
                                },
                                onConfigureNewHeartnwtworkClick = {},
                            )
                        }
                        composable("pager") {
                            PagerScreen(
                                onBackClick = { navController.popBackStack() },
                            )
                        }
                        composable("performance") {
                            BackHandler {
                                navController.popBackStack()
                            }
                            PerformancePageScreen()
                        }
                        composable("performance2") {
                            BackHandler {
                                navController.popBackStack()
                            }
                            PerformancePageScreen2()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StartingScreen(
    gotoList: () -> Unit,
    goToPager: () -> Unit,
    goToPerformancePage: () -> Unit,
    goToPerformancePage2: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = gotoList) {
            Text(text = "Go to list", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = goToPager) {
            Text(text = "Go to Pager", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = goToPerformancePage) {
            Text(text = "Go to Performance", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = goToPerformancePage2) {
            Text(text = "Go to Performance 2", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

fun sendToast(context: Context, locationId: String, deviceId: String, locationName: String) {
    Toast.makeText(context, "Clicked on $locationId-$deviceId-$locationName", Toast.LENGTH_SHORT)
        .show()
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}