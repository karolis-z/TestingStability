package com.example.myapplication.screens.performanepage

import android.util.Log
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

private val SpacingBetweenFlowAndTiles = 12.dp
private val SpacingBetweenTiles = 14.dp
private val PowerFlowVerticalPadding = 16.dp
private val PowerFlowHorizontalPadding = 16.dp

@Composable
fun PerformancePageScreen(
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var savingsTileExpanded by remember { mutableStateOf(false)}
    var controlPlanTileExpanded by remember { mutableStateOf(false)}
    var powerFlowSize by remember { mutableStateOf(IntSize.Zero) }
    var tileSize by remember { mutableStateOf(IntSize.Zero) }
    val fullContainerSize by remember {
        derivedStateOf {
            if (tileSize != IntSize.Zero && powerFlowSize != IntSize.Zero) {
                with(density) { IntSize(
                    width = powerFlowSize.width,
                    height = powerFlowSize.height + tileSize.height + (SpacingBetweenFlowAndTiles + PowerFlowVerticalPadding * 2).roundToPx()
                ) }
            } else {
                IntSize.Zero
            }
        }
    }
    LaunchedEffect(powerFlowSize, tileSize, fullContainerSize) {
        Log.d("TESTING", "Sizes: powerFlowSize = $powerFlowSize tileSize = $tileSize fullContainerSize = $fullContainerSize")
        Log.d("TESTING", "in DP: powerFlowSize = ${with(density){powerFlowSize.width.toDp() to powerFlowSize.height.toDp()}}")
        Log.d("TESTING", "in DP: tileSize = ${with(density){tileSize.width.toDp() to tileSize.height.toDp()}}")
        Log.d("TESTING", "in DP: fullContainerSize = ${with(density){fullContainerSize.width.toDp() to fullContainerSize.height.toDp()}}")
    }

    Column {
        Switch(checked = savingsTileExpanded, onCheckedChange = { savingsTileExpanded = !savingsTileExpanded }) // FIXME: TESTING ONLY
        Switch(checked = controlPlanTileExpanded, onCheckedChange = { controlPlanTileExpanded = !controlPlanTileExpanded }) // FIXME: TESTING ONLY
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = modifier
                .padding(
                    vertical = PowerFlowVerticalPadding,
                    horizontal = PowerFlowHorizontalPadding
                )
                .then(
                    if (fullContainerSize != IntSize.Zero) {
                        Modifier.size(
                            with(density) {
                                DpSize(
                                    fullContainerSize.width.toDp(),
                                    fullContainerSize.height.toDp()
                                )
                            }
                        )
                    } else {
                        Modifier.wrapContentSize()
                    }
                )
                .border(1.dp, Color.Blue),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Color.LightGray)
                    .onGloballyPositioned {
                        Log.d(
                            "TESTING",
                            "grid size = ${with(density) { it.size.width.toDp() to it.size.height.toDp() }}"
                        )
                        if (powerFlowSize == IntSize.Zero) {
                            powerFlowSize = it.size
                        }
                    }
                    .border(2.dp, Color.Green)
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Grid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }

            if (powerFlowSize != IntSize.Zero) {
                Tile(
                    modifier = Modifier
                        .width(with(density) { (powerFlowSize.width / 2).toDp() - SpacingBetweenTiles / 2 })
                        .aspectRatio(1.143f)
                        .alpha(0f)
                        .onGloballyPositioned { if (tileSize == IntSize.Zero) tileSize = it.size },
                )
            }

            val savingsTileSize by animateIntSizeAsState(
                targetValue = if (savingsTileExpanded) fullContainerSize else tileSize,
                label = "Savings tile top padding animation",
            )
            val controlPlanSize by animateIntSizeAsState(
                targetValue = if (controlPlanTileExpanded) fullContainerSize else tileSize,
                label = "Savings tile top padding animation",
            )

            androidx.compose.animation.AnimatedVisibility(
                visible = powerFlowSize != IntSize.Zero && tileSize != IntSize.Zero,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .then(if (savingsTileExpanded) Modifier.zIndex(1f) else Modifier.zIndex(0f))
                ,
            ) {
                Tile(
                    modifier = Modifier
                        .size(with(density) {
                            DpSize(
                                savingsTileSize.width.toDp(),
                                savingsTileSize.height.toDp()
                            )
                        })

                    ,
                    color = Color.Cyan
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = powerFlowSize != IntSize.Zero,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .then(
                        if (controlPlanTileExpanded) Modifier.zIndex(1f) else Modifier.zIndex(
                            0f
                        )
                    )
                ,
            ) {
                Tile(
                    modifier = Modifier
                        .size(with(density) {
                            DpSize(
                                controlPlanSize.width.toDp(),
                                controlPlanSize.height.toDp()
                            )
                        })

                )
            }
        }
    }
}

@Composable
private fun Tile(
    color: Color = Color.Yellow,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier
        .background(color))
}

@Composable
private fun Grid(
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(90.dp),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        items(
            count = 9,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF1F1F1))
                    .aspectRatio(1f)
                ,
                contentAlignment = Alignment.Center,
            ) { Text(text = "Flow") }
        }
    }
}