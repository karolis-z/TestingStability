package com.example.myapplication.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun MyTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    isBackButtonDark: Boolean = false,
    actions: (@Composable RowScope.() -> Unit)? = null,
) = BaseTopBar (
    modifier = modifier,
    title = title,
    onBackClick = onBackClick,
    isBackButtonDark = isBackButtonDark,
    actions = actions,
)

@Composable
private fun BaseTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
    isBackButtonDark: Boolean = false,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Crossfade(
            targetState = onBackClick,
            label = "Icon Button Cross Fade",
        ) { backClick ->
            when (backClick) {
                null -> Spacer(modifier = Modifier.size(48.dp))
                else -> {
                    if (isBackButtonDark) {
                        IconButton(onClick = onBackClick ?: {}) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    } else {
                        IconButton(onClick = onBackClick ?: {}) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))
        title?.let { title ->
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        actions?.let {
            Row(
                modifier = if (title == null) Modifier.weight(1f) else Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                it()
            }
        }
    }
}