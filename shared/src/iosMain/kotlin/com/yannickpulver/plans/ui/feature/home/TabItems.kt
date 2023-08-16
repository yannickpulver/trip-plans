package com.yannickpulver.plans.ui.feature.home

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import com.yannickpulver.plans.ui.feature.locations.LocationsTab
import com.yannickpulver.plans.ui.feature.plans.PlansTab
import com.yannickpulver.plans.ui.feature.profile.ProfileTab

@Composable
actual fun RowScope.TabItems() {
    TabItem(LocationsTab)
    TabItem(PlansTab)
    TabItem(ProfileTab)
}
