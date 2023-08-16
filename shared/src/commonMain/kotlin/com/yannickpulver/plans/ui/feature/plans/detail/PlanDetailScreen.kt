package com.yannickpulver.plans.ui.feature.plans.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.seiko.imageloader.rememberImagePainter
import org.koin.compose.koinInject

data class PlansDetailRoute(val id: String?) : Screen {
    @Composable
    override fun Content() {
        PlansDetailScreen(id)
    }
}

@Composable
fun PlansDetailScreen(id: String?, viewModel: PlanDetailViewModel = koinInject()) {
    val state = viewModel.state.collectAsState()
    val navigator = LocalNavigator.current

    if (id == null) {
        AddPlanContent(
            state = state.value,
            onTitleChanged = viewModel::onTitleChanged,
            onSave = { navigator?.parent?.pop() }
        )
    }
}

@Composable
fun AddPlanContent(
    state: PlanDetailViewState,
    onTitleChanged: (String) -> Unit,
    onSave: () -> Unit
) {
    Scaffold {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {
            Card(
                Modifier.height(200.dp).aspectRatio(0.8f),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                AnimatedVisibility(
                    visible = state.imageUrl.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                    Image(
                        painter = rememberImagePainter(state.imageUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            val title = remember { mutableStateOf("") }

            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it; onTitleChanged(it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )

            if (state.title.isNotEmpty()) {
                Button(
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}
