package com.yannickpulver.tripplans.previews.feature.plans

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yannickpulver.tripplans.ui.feature.plans.PlansScreen
import com.yannickpulver.tripplans.ui.feature.plans.PlansState
import com.yannickpulver.tripplans.ui.theme.AppTheme

@Preview
@Composable
fun PlansScreenPreview() {
    AppTheme(useDarkTheme = isSystemInDarkTheme(), dynamicColor = false) {
        PlansScreen(state = PlansState.Preview, {})
    }
}