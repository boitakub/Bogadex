package fr.boitakub.bogadex.boardgame.ui.component

import android.content.res.Configuration
import android.view.KeyEvent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.boitakub.boardgame_list.R
import fr.boitakub.bogadex.common.ui.theme.BogadexTheme

@Composable
fun SearchInputField(
    modifier: Modifier = Modifier,
    currentTerm: String = "",
    onSearchTermChange: (String) -> Unit = {},
) {
    var searchedTermsField by remember { mutableStateOf(TextFieldValue(currentTerm)) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        modifier = modifier
            .padding(8.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    onSearchTermChange(searchedTermsField.text)
                    focusManager.clearFocus()
                    true
                }
                false
            },
        value = searchedTermsField,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.decorative_image),
            )
        },
        trailingIcon = {
            if (searchedTermsField.text.isNotBlank()) {
                IconButton(onClick = {
                    searchedTermsField = TextFieldValue("")
                    onSearchTermChange(searchedTermsField.text)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.searchbar_clear),
                    )
                }
            }
        },
        onValueChange = {
            searchedTermsField = it
            onSearchTermChange(searchedTermsField.text)
        },
        maxLines = 1,
        singleLine = true,
        placeholder = { Text(text = stringResource(id = R.string.searchbar_hint)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            },
        ),
    )
}

//region Preview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun SearchInputFieldLight() {
    BogadexTheme {
        SearchInputField()
    }
}

//endregion
