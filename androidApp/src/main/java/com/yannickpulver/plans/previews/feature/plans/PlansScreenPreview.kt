package com.yannickpulver.plans.previews.feature.plans

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yannickpulver.plans.ui.feature.locations.PlanScreenContent
import com.yannickpulver.plans.ui.feature.locations.LocationsScreen
import com.yannickpulver.plans.ui.feature.locations.LocationsState
import com.yannickpulver.plans.ui.theme.AppTheme

@Preview
@Composable
fun PlansScreenPreview() {
    AppTheme(useDarkTheme = isSystemInDarkTheme(), dynamicColor = false) {
        LocationsScreen(state = LocationsState.Preview, {}, {}, {})
    }
}

@Preview
@Composable
fun PlansScreenContentPreview() {
    AppTheme(useDarkTheme = isSystemInDarkTheme(), dynamicColor = false) {
        PlanScreenContent(state = LocationsState.Preview, true, {}, {}, {})
    }
}