package com.yannickpulver.tripplans.ui.feature.plans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(state: PlansState) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                content = { Icon(Icons.Default.Add, contentDescription = "Add") })
        }, modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            Modifier.padding(it).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.plans) {
                Card(Modifier.fillMaxWidth()) {
                    Text("Item $it", Modifier.padding(16.dp))
                }
            }
        }

    }
}