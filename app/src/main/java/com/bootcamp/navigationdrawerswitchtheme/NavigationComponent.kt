package com.bootcamp.navigationdrawerswitchtheme

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bootcamp.navigationdrawerswitchtheme.ui.theme.NavigationDrawerSwitchThemeTheme

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNavDrawerApp() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen || drawerState.isClosed,
        drawerContent = {
            MyDrawerContent(
                onItemSelected = { title ->
                    scope.launch {
                        drawerState.close()
                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = context.resources.getString(R.string.coming_soon, title),
                            actionLabel = context.resources.getString(R.string.subscribe_question),
                        )
                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                            Toast.makeText(
                                context,
                                context.resources.getString(R.string.subscribed_info),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
                    snackbarHostState.currentSnackbarData?.dismiss()
                },
                onBackPress = {
                    if (drawerState.isOpen) {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                },
            )
        },
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                MyTopBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                )
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,

            ) {

            }
        }
    }

    LaunchedEffect(snackbarHostState.currentSnackbarData?.visuals?.duration) {
        delay(2000)
        snackbarHostState.currentSnackbarData?.dismiss()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(onMenuClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    // Agrega esto para manejar el estado del switch
    var isSwitchChecked by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = {
                onMenuClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = colors.primary,
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    color = colors.primary,
                )

                // Agrega el switch a la AppBar
                Switch(
                    checked = isSwitchChecked,
                    onCheckedChange = { isSwitchChecked = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.tertiary
                    )
                )
            }
        },
    )
}





data class MenuItem(val title: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDrawerContent(
    modifier: Modifier = Modifier,
    onItemSelected: (title: String) -> Unit,
    onBackPress: () -> Unit,
) {
    val menu = listOf(
        MenuItem(
            title = stringResource(R.string.home),
            icon = Icons.Default.Home,
        ),
        MenuItem(
            title = stringResource(R.string.favourite),
            icon = Icons.Default.Favorite,
        ),
        MenuItem(
            title = stringResource(R.string.profile),
            icon = Icons.Default.AccountCircle,
        ),
    )

    ModalDrawerSheet(modifier) {
        Column(modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .height(190.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
            )
            LazyColumn {
                items(menu) { menuList ->
                    NavigationDrawerItem(
                        shape = MaterialTheme.shapes.small,
                        label = {
                            Text(
                                text = menuList.title,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        },
                        selected = menuList == menu[0],
                        icon = {
                            Icon(
                                imageVector = menuList.icon,
                                contentDescription = menuList.title,
                                tint = Color.DarkGray,
                            )
                        },
                        onClick = {
                            onItemSelected.invoke(menuList.title)
                        },
                    )
                }
            }
            Divider()
        }
    }
    BackPressHandler {
        onBackPress()
    }
}

@Composable
fun BackPressHandler(enabled: Boolean = true, onBackPressed: () -> Unit) {
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    SideEffect {
        backCallback.isEnabled = enabled
    }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NavigationDrawerSwitchThemeTheme() {
        MyNavDrawerApp()
    }
}
