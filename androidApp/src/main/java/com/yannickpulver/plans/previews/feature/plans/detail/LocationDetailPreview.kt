package com.yannickpulver.plans.previews.feature.plans.detail

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.ui.feature.locations.detail.LocationDetailScreenContent
import com.yannickpulver.plans.ui.theme.AppTheme

@Preview
@Composable
fun LocationDetailPreview() {
    AppTheme(useDarkTheme = isSystemInDarkTheme(), dynamicColor = false) {
        LocationDetailScreenContent(Place.Preview)
    }
}