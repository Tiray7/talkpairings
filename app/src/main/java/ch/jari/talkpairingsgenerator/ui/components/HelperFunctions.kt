package ch.jari.talkpairingsgenerator.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.conditional(
    condition: Boolean,
    elseModifier: (@Composable Modifier.() -> Modifier)? = null,
    ifMod: @Composable Modifier.() -> Modifier,
): Modifier = composed {
    if (condition) {
        then(ifMod(Modifier))
    } else {
        elseModifier?.let { then(it(Modifier)) } ?: this
    }
}

fun Modifier.nullClickable(
    onClick: (() -> Unit)?,
): Modifier = composed {
    onClick?.let {
        this.clickable(
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = true),
        )
    } ?: this
}