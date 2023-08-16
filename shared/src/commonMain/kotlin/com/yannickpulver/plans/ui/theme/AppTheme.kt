package com.yannickpulver.plans.ui.theme

import androidx.compose.runtime.Composable

@Composable
expect fun AppTheme(
    useDarkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
)
