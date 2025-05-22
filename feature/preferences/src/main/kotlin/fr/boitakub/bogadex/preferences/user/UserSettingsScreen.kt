/*
 * Copyright (c) 2023-2025, Boitakub
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of mosquitto nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package fr.boitakub.bogadex.preferences.user

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import fr.boitakub.bogadex.common.UserSettings
import fr.boitakub.bogadex.common.ui.theme.Theme
import fr.boitakub.preferences.R

@Composable
fun UserSettingsScreen(navController: NavHostController, viewModel: UserSettingsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var isBggUsernameDialogVisible by remember { mutableStateOf(false) }
    var isThemeDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar(navigator = navController) },
    ) { innerPadding ->
        if (isBggUsernameDialogVisible) {
            BggUsernameTextDialog(
                onConfirmClick = {
                    viewModel.updateBggUsername(it)
                    isBggUsernameDialogVisible = false
                },
                onCancelClick = { isBggUsernameDialogVisible = false },
                onDismiss = { isBggUsernameDialogVisible = false },
            )
        }
        if (isThemeDialogVisible) {
            ThemeListDialog(
                currentTheme = state.activeTheme,
                onConfirmClick = {
                    viewModel.updateTheme(it)
                    isThemeDialogVisible = false
                },
                onCancelClick = { isThemeDialogVisible = false },
                onDismiss = { isThemeDialogVisible = false },
            )
        }
        SettingsContent(
            innerPadding,
            state,
            viewModel,
            onBggUsernameClick = { isBggUsernameDialogVisible = true },
            onThemeSelectionClick = { isThemeDialogVisible = true },
        )
    }
}

@Composable
private fun SettingsContent(
    innerPadding: PaddingValues,
    state: UserSettings,
    viewModel: UserSettingsViewModel,
    onBggUsernameClick: () -> Unit,
    onThemeSelectionClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            ClickableLineComponent(
                iconRes = fr.boitakub.bogadex.common.R.drawable.ic_user_solid,
                titleRes = fr.boitakub.bogadex.common.R.string.bgg_username,
                subtitleRes = fr.boitakub.bogadex.common.R.string.bgg_username_summary,
                onClick = onBggUsernameClick,
            )
            SwitchComponent(
                iconRes = fr.boitakub.bogadex.common.R.drawable.ic_person_carry_box_solid,
                titleRes = fr.boitakub.bogadex.common.R.string.filter_previously_own,
                subtitleRes = fr.boitakub.bogadex.common.R.string.filter_previously_own_summary,
                previousState = state.displayPreviouslyOwned,
                updateSelection = {
                    viewModel.updateFilterPreviouslyOwned(it)
                },
            )
            ClickableLineComponent(
                iconRes = fr.boitakub.bogadex.common.R.drawable.ic_eclipse_solid,
                titleRes = fr.boitakub.bogadex.common.R.string.pref_night_title,
                subtitleRes = fr.boitakub.bogadex.common.R.string.pref_night_summary,
                onClick = onThemeSelectionClick,
            )
        },
    )
}

@Composable
fun BggUsernameTextDialog(
    onConfirmClick: (String) -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDismiss: () -> Unit,
) {
    var username by remember { mutableStateOf("") }

    TextDialog(
        title = {
            Text(
                text = stringResource(id = fr.boitakub.bogadex.common.R.string.bgg_username),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        content = {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
            )
        },
        dismissButton = {
            TextButton(
                onClick = onCancelClick,
                content = { Text(stringResource(id = android.R.string.cancel)) },
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmClick(username) },
                content = { Text(stringResource(id = android.R.string.ok)) },
            )
        },
        onDismiss = onDismiss,
    )
}

@Composable
fun ThemeListDialog(
    currentTheme: Theme,
    onConfirmClick: (Theme) -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDismiss: () -> Unit,
) {
    val (selected, setSelected) = remember { mutableStateOf(currentTheme) }

    TextDialog(
        title = {
            Text(
                text = stringResource(id = fr.boitakub.bogadex.common.R.string.pref_night_title),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        content = {
            ThemeRadioComponent(
                items = Theme.values().toList(),
                selected = selected,
                setSelected = setSelected,
            )
        },
        dismissButton = {
            TextButton(
                onClick = onCancelClick,
                content = { Text(stringResource(id = android.R.string.cancel)) },
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmClick(selected) },
                content = { Text(stringResource(id = android.R.string.ok)) },
            )
        },
        onDismiss = onDismiss,
    )
}

@Composable
fun ThemeRadioComponent(items: List<Theme>, selected: Theme, setSelected: (selected: Theme) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        setSelected(item)
                    }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(id = item.titleRes),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                RadioButton(
                    selected = selected == item,
                    onClick = {
                        setSelected(item)
                    },
                    enabled = true,
                )
            }
        }
    }
}

@Composable
fun SwitchComponent(
    @DrawableRes iconRes: Int,
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    previousState: Boolean,
    updateSelection: (Boolean) -> Unit = {},
) {
    ClickableLineComponent(
        iconRes = iconRes,
        titleRes = titleRes,
        subtitleRes = subtitleRes,
        onClick = {
            updateSelection(!previousState)
        },
    ) {
        Switch(
            modifier = Modifier.padding(horizontal = 8.dp),
            checked = previousState,
            onCheckedChange = {
                updateSelection(it)
            },
        )
    }
}

@Composable
fun ClickableLineComponent(
    @DrawableRes iconRes: Int,
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp),
            painter = rememberVectorPainter(
                image = ImageVector.vectorResource(id = iconRes),
            ),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = subtitleRes),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        content.invoke()
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = MaterialTheme.colorScheme.primary),
    )
}

@Composable
fun TextDialog(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column {
                Column(Modifier.padding(24.dp)) {
                    title.invoke()
                    Spacer(Modifier.size(16.dp))
                    content.invoke()
                }
                Spacer(Modifier.size(4.dp))
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    dismissButton.invoke()
                    confirmButton.invoke()
                }
            }
        }
    }
}

@Composable
fun TopBar(navigator: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {
            navigator.popBackStack()
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(fr.boitakub.bogadex.common.R.drawable.ic_arrow_left),
                contentDescription = stringResource(id = fr.boitakub.bogadex.common.R.string.back),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = stringResource(id = fr.boitakub.bogadex.common.R.string.settings),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}
