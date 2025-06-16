/*
 * Copyright (c) 2025, Boitakub
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
package fr.boitakub.bogadex.common.ui.component

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * A TopAppBar that can be collapsed when the content is scrolled.
 *
 * @param modifier The modifier to be applied to the TopAppBar.
 * @param windowInsets The insets to be applied to the TopAppBar.
 * @param scrollBehavior The scroll behavior to be applied to the TopAppBar.
 * @param contentHeight The height of the content when the TopAppBar is expanded.
 * @param contentHeightCollapsing The height of the content when the TopAppBar is collapsed.
 * @param content The content to be displayed in the TopAppBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingTopAppBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    contentHeight: Dp,
    contentHeightCollapsing: Dp,
    content: @Composable () -> Unit,
) {
    ContentTopAppBar(
        modifier = modifier,
        content = content,
        windowInsets = windowInsets,
        scrollBehavior = scrollBehavior,
        contentHeight = contentHeight,
        contentHeightCollapsing = contentHeightCollapsing,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentTopAppBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    windowInsets: WindowInsets,
    scrollBehavior: TopAppBarScrollBehavior?,
    contentHeight: Dp,
    contentHeightCollapsing: Dp,
) {
    // Sets the app bar's height offset to collapse the entire bar's height when content is
    // scrolled.
    val heightOffsetLimit =
        with(LocalDensity.current) { -contentHeightCollapsing.toPx() }
    SideEffect {
        if (scrollBehavior?.state?.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior?.state?.heightOffsetLimit = heightOffsetLimit
        }
    }

    // Set up support for resizing the top app bar when vertically dragging the bar itself.
    val appBarDragModifier = if (scrollBehavior != null && !scrollBehavior.isPinned) {
        Modifier.draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                scrollBehavior.state.heightOffset = scrollBehavior.state.heightOffset + delta
            },
            onDragStopped = { velocity ->
                animateAppBar(
                    scrollBehavior.state,
                    velocity,
                    scrollBehavior.flingAnimationSpec,
                    scrollBehavior.snapAnimationSpec,
                )
            },
        )
    } else {
        Modifier
    }

    // Compose a Surface with a TopAppBarLayout content.
    // The height of the app bar is determined by subtracting the bar's height offset from the
    // app bar's defined constant height value (i.e. the ContainerHeight token).
    Surface(modifier = modifier.then(appBarDragModifier)) {
        val height = LocalDensity.current.run {
            contentHeight.toPx() + (
                scrollBehavior?.state?.heightOffset
                    ?: 0f
                )
        }
        TopContentLayout(
            modifier = Modifier
                .windowInsetsPadding(windowInsets)
                .clipToBounds(),
            heightPx = height,
            content = content,
        )
    }
}

@Composable
private fun TopContentLayout(modifier: Modifier, heightPx: Float, content: @Composable () -> Unit) {
    Layout(
        {
            Box(
                Modifier
                    .layoutId("content")
                    .fillMaxWidth(),

            ) {
                CompositionLocalProvider(
                    content = content,
                )
            }
        },
        modifier = modifier,
    ) { measurables, constraints ->

        val contentPlaceable =
            measurables.first { it.layoutId == "content" }
                .measure(constraints.copy(minWidth = 0, maxWidth = constraints.maxWidth))

        val layoutHeight = heightPx.roundToInt()

        layout(constraints.maxWidth, layoutHeight) {
            contentPlaceable.placeRelative(
                x = (constraints.maxWidth - contentPlaceable.width) / 2,
                y = (layoutHeight - contentPlaceable.height) / 2,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private suspend fun animateAppBar(
    state: TopAppBarState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?,
): Velocity {
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity

    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = velocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    // Snap if animation specs were provided.
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 &&
            state.heightOffset > state.heightOffsetLimit
        ) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec,
            ) { state.heightOffset = value }
        }
    }

    return Velocity(0f, remainingVelocity)
}
