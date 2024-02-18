package io.familymoments.app.core.util

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ObserverModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.observeReads
import androidx.compose.ui.platform.InspectorInfo
import io.familymoments.app.feature.bottomnav.model.BottomNavItem

@Stable
class ScaffoldState {
    var hasShadow by mutableStateOf(false)
        private set

    var hasBackButton by mutableStateOf(false)
        private set

    var selectedBottomNav by mutableStateOf(BottomNavItem.Home.route)
        private set

    fun updateShadow(hasShadow: Boolean) {
        this.hasShadow = hasShadow
    }

    fun updateBackButton(hasBackButton: Boolean) {
        this.hasBackButton = hasBackButton
    }

    fun updateBottomNavSelection(bottomNavItem: BottomNavItem) {
        this.selectedBottomNav = bottomNavItem.route
    }
}

val LocalScaffoldState = compositionLocalOf { ScaffoldState() }

private class ScaffoldNode(
    var hasShadow: Boolean,
    var hasBackButton: Boolean,
    var selectedBottomNav: BottomNavItem
) : Modifier.Node(), CompositionLocalConsumerModifierNode, ObserverModifierNode {
    private var observedValue: ScaffoldState? = null
    override fun onAttach() {
        onObservedReadsChanged()
    }

    override fun onDetach() {
        observedValue = null
    }

    override fun onObservedReadsChanged() {
        observeReads {
            observedValue = currentValueOf(LocalScaffoldState).let {
                it.updateShadow(hasShadow)
                it.updateBackButton(hasBackButton)
                it.updateBottomNavSelection(selectedBottomNav)
                it
            }
        }
    }
}

private data class ScaffoldElement(
    val hasShadow: Boolean,
    val hasBackButton: Boolean,
    val selectedBottomNav: BottomNavItem
) : ModifierNodeElement<ScaffoldNode>() {
    override fun InspectorInfo.inspectableProperties() {
        name = "topAppBar"
        properties["hasShadow"] = hasShadow
        properties["hasBackButton"] = hasBackButton
    }

    override fun create() =
        ScaffoldNode(hasShadow = hasShadow, hasBackButton = hasBackButton, selectedBottomNav = selectedBottomNav)

    override fun update(node: ScaffoldNode) {
        node.hasShadow = hasShadow
        node.hasBackButton = hasBackButton
        node.selectedBottomNav = selectedBottomNav
    }
}

fun Modifier.scaffoldState(
    hasShadow: Boolean,
    hasBackButton: Boolean,
    selectedBottomNav: BottomNavItem
) = this then ScaffoldElement(hasShadow, hasBackButton, selectedBottomNav)
