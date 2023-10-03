package com.yannickpulver.plans.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: () -> Unit = {}, onClick: () -> Unit) {
    TopAppBar(
        title = { title() },
        navigationIcon = { BackButton(onClick = onClick) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}