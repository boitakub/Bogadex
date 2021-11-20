/*
 * Copyright 2021 Boitakub
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.boitakub.common.ui.application

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.boitakub.architecture.Presenter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel(), Presenter {
    private val _applicationState = MutableStateFlow(ApplicationState())
    val applicationState: StateFlow<ApplicationState> = _applicationState

    fun switchLayout() {
        if (_applicationState.value.viewType == 1) {
            _applicationState.value = ApplicationState(_applicationState.value.collection, 2)
        } else {
            _applicationState.value = ApplicationState(_applicationState.value.collection, 1)
        }
    }

    fun filterCollectionWith(filter: String) {
        _applicationState.value = ApplicationState(filter, _applicationState.value.viewType)
    }
}
