package com.yannickpulver.plans.ui.feature.plans.add

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.seiko.imageloader.rememberImagePainter
import com.yannickpulver.plans.ui.components.AppBar
import com.yannickpulver.plans.ui.feature.plans.detail.PlanDetailViewState

@Composable
fun AddPlanContent(
    state: PlanDetailViewState,
    onTitleChanged: (String) -> Unit,
    onSave: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val navigator = LocalNavigator.current
    Scaffold(
        topBar = { AppBar(onClick = { navigator?.pop() }) }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                .padding(WindowInsets.ime.asPaddingValues())
        ) {
            val title = remember { mutableStateOf("") }
            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it; onTitleChanged(it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = { onSave() }
                )
            )

            //ImageCard(state)

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
