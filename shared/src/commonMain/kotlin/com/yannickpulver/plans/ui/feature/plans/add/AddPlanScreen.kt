package com.yannickpulver.plans.ui.feature.plans.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.yannickpulver.plans.MR
import com.yannickpulver.plans.ui.components.AppBar
import com.yannickpulver.plans.ui.feature.plans.detail.PlanStyle
import com.yannickpulver.plans.ui.feature.plans.detail.PlansDetailRoute
import dev.icerock.moko.graphics.parseColor
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.koinInject

object AddPlanRoute : Screen {
    @Composable
    override fun Content() {
        AddPlanScreen()
    }
}

@Composable
fun AddPlanScreen(
    viewModel: AddPlanViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    val navigator = LocalNavigator.current

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect {
            when (it) {
                is AddPlanEvent.Success -> {
                    navigator?.pop()
                    navigator?.push(PlansDetailRoute(it.id))
                }
            }
        }
    }


    Scaffold(
        topBar = { AppBar(onClick = { navigator?.pop() }) }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                .padding(WindowInsets.ime.asPaddingValues())
        ) {
            state.style?.let {
                EmojiCard(it, onClick = { viewModel.updateStyle() })
            }

            val title = remember { mutableStateOf("") }
            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it; viewModel.onTitleChanged(it) },
                placeholder = { Text(stringResource(MR.strings.plans_add_placeholder)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = { viewModel.save() }
                )
            )


            if (state.title.isNotEmpty()) {
                Button(
                    onClick = viewModel::save,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun EmojiCard(style: PlanStyle, onClick: () -> Unit) {
    Card(
        Modifier.height(120.dp).fillMaxWidth().padding(horizontal = 24.dp)
            .clickable(onClick = { onClick() },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        colors = CardDefaults.cardColors(
            try {
                Color(dev.icerock.moko.graphics.Color.parseColor(style.color).argb)
            } catch (exception: Exception) {
                Color(0xFF000000)
            }
        )
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(style.emoji, style = MaterialTheme.typography.titleLarge)
        }

    }
}

// @Composable
// private fun ImageCard(state: PlanDetailViewState) {
//     Card(
//         Modifier.height(200.dp).fillMaxWidth().padding(horizontal = 24.dp),
//         colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
//     ) {
//         Box(Modifier.fillMaxSize()) {
//             if (state.imageUrl.isEmpty()) {
//                 Icon(
//                     Icons.Rounded.PhotoCamera,
//                     contentDescription = null,
//                     modifier = Modifier.align(Alignment.Center),
//                     tint = MaterialTheme.colorScheme.onSecondaryContainer
//                 )
//             } else {
//                 Box(Modifier.fillMaxSize()) {
//                     CircularProgressIndicator(Modifier.align(Alignment.Center))
//                 }
//                 Image(
//                     painter = rememberImagePainter(state.imageUrl),
//                     contentDescription = null,
//                     modifier = Modifier.fillMaxSize(),
//                     contentScale = ContentScale.Crop
//                 )
//             }
//         }
//     }
// }
