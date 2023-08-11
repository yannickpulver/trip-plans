package com.yannickpulver.plans.ui.feature.locations.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.seiko.imageloader.rememberImagePainter
import com.yannickpulver.plans.data.dto.Place
import org.koin.compose.koinInject

data class LocationDetailRoute(val id: String) : Screen {
    @Composable
    override fun Content() {
        LocationDetailScreen(id)
    }
}

@Composable
fun LocationDetailScreen(id: String, viewModel: LocationDetailViewModel = koinInject()) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(id) {
        viewModel.getLocation(id)
    }

    state.value?.let {
        LocationDetailScreenContent(it)
    } ?: LoadingScreen()
}

@Composable
fun LoadingScreen() {
    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationDetailScreenContent(place: Place) {
    val navigator = LocalNavigator.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = { navigator?.pop() },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) {
        BackgroundImage(place)

        Column(Modifier.verticalScroll(rememberScrollState())) {

            Surface(
                modifier = Modifier.padding(it).padding(top = 100.dp),
                shadowElevation = 10.dp,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(place.name, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        place.formatted_address,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        Modifier.height(100.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        place.photoUrls.forEach {
                            Image(
                                painter = rememberImagePainter(it),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier.weight(1f).fillMaxHeight()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }

                    WebsiteButton(place.website)

                    Text(place.toString())
                    Spacer(modifier = Modifier.height(200.dp))
                }
            }
        }
    }
}

@Composable
fun WebsiteButton(website: String?) {
    val uriHandler = LocalUriHandler.current
    if (website == null) return
    Button(
        onClick = { uriHandler.openUri(website) },
        modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text("Website")
    }
}

@Composable
fun BackgroundImage(place: Place) {
    val request = remember { ImageRequest(place.photoUrls.firstOrNull().orEmpty()) }
    val action by rememberImageAction(request)
    val painter = rememberImageActionPainter(action)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.fillMaxWidth().height(200.dp),
        contentScale = ContentScale.Crop
    )
}