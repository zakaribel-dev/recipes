package com.example.recipes.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.recipes.R

@Composable
fun LoadingScreen(onTimeout: () -> Unit) { // lorqu'on appelle loadingSCreen, si je passe une props à false ça va trigger onTimeout
    LaunchedEffect(Unit) {
        delay(2000) // Affiche le loading screen pendant 2 secondes
        onTimeout() // Passe à l'écran suivant
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.loading),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}
