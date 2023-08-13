package com.yannickpulver.plans.ui.feature.locations.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yannickpulver.plans.data.dto.Location

@Composable
expect fun MapTile(location: Location, modifier: Modifier = Modifier)