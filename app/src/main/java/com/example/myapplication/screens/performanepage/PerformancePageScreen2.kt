package com.example.myapplication.screens.performanepage

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.animateIntSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val SpacingBetweenFlowAndTiles = 12.dp
private val SpacingBetweenTiles = 14.dp
private const val AnimationDurationMillis = 2000

@Composable
fun PerformancePageScreen2(
    modifier: Modifier = Modifier,
) {
    var savingsTileExpanded by remember { mutableStateOf(false)}
    var controlPlanTileExpanded by remember { mutableStateOf(false)}
    var tileSize by remember { mutableStateOf(IntSize.Zero) }
    var tileY by remember { mutableIntStateOf(0)}
    var controlSummaryTileX by remember { mutableIntStateOf(0)}
    var fullContainerSize by remember { mutableStateOf(IntSize.Zero) }

    Column {
        Switch(checked = savingsTileExpanded, onCheckedChange = { savingsTileExpanded = !savingsTileExpanded }) // FIXME: TESTING ONLY
        Switch(checked = controlPlanTileExpanded, onCheckedChange = { controlPlanTileExpanded = !controlPlanTileExpanded }) // FIXME: TESTING ONLY
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, Color.Blue)
            ,
        ) {

            val savingsTileTransition = updateTransition(
                targetState = savingsTileExpanded,
                label = "savings tile transition",
            )
            val savingsTileYOffset by savingsTileTransition.animateIntOffset(
                label = "Savings Tile y offset animation",
                transitionSpec = { tween(durationMillis = AnimationDurationMillis,easing = CubicBezierEasing(0.05f, 0.5f, 0.1f, 1f)) },
            ) { expanded ->
                if (expanded) IntOffset.Zero else IntOffset(x = 0, y = tileY)
            }
            val savingsTileSize by savingsTileTransition.animateIntSize(
                label = "savings tile size animation",
                transitionSpec = { tween(durationMillis = AnimationDurationMillis,easing = CubicBezierEasing(0.05f, 0.5f, 0.1f, 1f)) },
            ) { expanded ->
                if (expanded) fullContainerSize else tileSize
            }

            val controlSummaryTileTransition = updateTransition(
                targetState = controlPlanTileExpanded,
                label = "control summary tile transition",
            )
            val controlSummaryTileOffset by controlSummaryTileTransition.animateIntOffset(
                label = "Control Summary tile offset animation",
                transitionSpec = { tween(durationMillis = AnimationDurationMillis,easing = FastOutSlowInEasing) },
            ) { expanded ->
                if (expanded) IntOffset.Zero else IntOffset(x = controlSummaryTileX, y = tileY)
            }
            val controlSummaryTileSize by controlSummaryTileTransition.animateIntSize(
                label = "control summary size animation",
                transitionSpec = { tween(durationMillis = AnimationDurationMillis,easing = FastOutSlowInEasing) },
            ) { expanded ->
                if (expanded) fullContainerSize else tileSize
            }

            val powerFlowComponent = @Composable {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color.LightGray)
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
            }

            val savingsTileComponent = @Composable {
                Tile(
                    isExpanded = savingsTileExpanded,
                    color = Color.Cyan,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            val controlSummaryTileComponent = @Composable {
                Tile(
                    isExpanded = controlPlanTileExpanded,
                    color = Color.Yellow,
                    modifier = Modifier
                        .fillMaxSize()
                    ,
                )
            }
            
            SubcomposeLayout(
                measurePolicy = { constraints ->
                    measurePowerFlowAndTilesLayout(
                        powerFlowComponent = powerFlowComponent,
                        savingsTileComponent = savingsTileComponent,
                        controlSummaryTileComponent = controlSummaryTileComponent,
                        constraints = constraints,
                        onLayoutSizeMeasured = {
                            if (fullContainerSize == IntSize.Zero) fullContainerSize = it
                        },
                        onTileSizeMeasured = {
                            if (tileSize == IntSize.Zero) tileSize = it
                        },
                        onTileYCalculated = { if (tileY == 0) tileY = it },
                        tileAspectRatio = 1.143f,
                        savingsTileSize = savingsTileSize,
                        savingsTileOffset = savingsTileYOffset,
                        onControlSummaryXCalculated = {controlSummaryTileX = it},
                        controlSummaryTileSize = controlSummaryTileSize,
                        controlSummaryTileOffset = controlSummaryTileOffset,
                    )
                },
                modifier = Modifier,
            )
        }
    }
}

private fun SubcomposeMeasureScope.measurePowerFlowAndTilesLayout(
    tileAspectRatio: Float,
    savingsTileSize: IntSize,
    savingsTileOffset: IntOffset,
    controlSummaryTileSize: IntSize,
    controlSummaryTileOffset: IntOffset,
    powerFlowComponent: @Composable () -> Unit,
    savingsTileComponent: @Composable () -> Unit,
    controlSummaryTileComponent: @Composable () -> Unit,
    constraints: Constraints,
    onLayoutSizeMeasured: (IntSize) -> Unit,
    onTileSizeMeasured: (IntSize) -> Unit,
    onTileYCalculated: (Int) -> Unit,
    onControlSummaryXCalculated: (Int) -> Unit,
): MeasureResult {
    // Measure PowerFlow and an unexpanded Tile
    val powerFlow = subcompose(Slot.PowerFlow, powerFlowComponent).map { it.measure(constraints) }.first()
    val tileWidth = powerFlow.measuredWidth / 2 - (SpacingBetweenTiles / 2).roundToPx()
    val tileHeight = (tileWidth / tileAspectRatio).takeIf { !it.isNaN() }?.roundToInt() ?: 0
    onTileSizeMeasured(IntSize(tileWidth, tileHeight))

    val tileY = powerFlow.measuredHeight + SpacingBetweenFlowAndTiles.roundToPx()
    onTileYCalculated(tileY)

    val controlSummaryTileX = tileWidth + SpacingBetweenTiles.roundToPx()
    onControlSummaryXCalculated(controlSummaryTileX)

    val layoutHeight = powerFlow.measuredHeight + tileHeight + SpacingBetweenFlowAndTiles.roundToPx()
    onLayoutSizeMeasured(IntSize(powerFlow.measuredWidth, layoutHeight))


    val savingsTile = subcompose(Slot.SavingsTile, savingsTileComponent).map {
        it.measure(
            Constraints(
                minWidth = savingsTileSize.width,
                maxWidth = savingsTileSize.width,
                minHeight = savingsTileSize.height,
                maxHeight = savingsTileSize.height,
            )
        )
    }.first()

    val controlSummaryTile = subcompose(Slot.ControlSummaryTile, controlSummaryTileComponent).map {
        it.measure(
            Constraints(
                minWidth = controlSummaryTileSize.width,
                maxWidth = controlSummaryTileSize.width,
                minHeight = controlSummaryTileSize.height,
                maxHeight = controlSummaryTileSize.height,
            )
        )
    }.first()

    return layout(powerFlow.measuredWidth, layoutHeight) {
        powerFlow.place(0,0)
        savingsTile.place(savingsTileOffset, if (savingsTileOffset.y != tileY) 1f else 0f)
        controlSummaryTile.place(controlSummaryTileOffset, if (controlSummaryTileOffset.y != tileY) 1f else 0f)
    }
}

private enum class Slot {
    PowerFlow,
    SavingsTile,
    ControlSummaryTile,
}

@Composable
private fun Tile(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color.Yellow,
) {
    Box(
        modifier = modifier
            .background(color)
        ,
        contentAlignment = Alignment.Center,
    ) {
        Crossfade(targetState = isExpanded, label = "", animationSpec = tween(durationMillis = AnimationDurationMillis,easing = FastOutSlowInEasing)) { expanded ->
            when (expanded) {
                true -> Text(text = "Expanded", modifier = Modifier.fillMaxSize().animateContentSize(), textAlign = TextAlign.Center,)
                false -> {
                    Column(modifier = Modifier.fillMaxSize().padding(all = 6.dp).animateContentSize()) {
                        Text("Not Expanded Text")
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                    }
                }
            }
        }
    }
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