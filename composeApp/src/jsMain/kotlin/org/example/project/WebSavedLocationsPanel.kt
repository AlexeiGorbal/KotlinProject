package org.example.project

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.example.project.viewmodel.LocationSearchUiState
import org.example.project.viewmodel.LocationSearchViewModel
import org.example.project.viewmodel.LocationWeatherViewModel
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flex
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.outline
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.right
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput

@Composable
fun WebSavedLocationsPanel(
    locations: List<org.example.project.local.LocationInfo>,
    onSelect: (org.example.project.local.LocationInfo) -> Unit,
    onClose: () -> Unit
) {
    Div({
        style {
            position(Position.Fixed); right(0.px); top(0.px); width(400.px); height(100.vh)
            backgroundColor(Color("white")); display(DisplayStyle.Flex); flexDirection(FlexDirection.Column)
            property("z-index", 3000); property("box-shadow", "-2px 0 15px rgba(0,0,0,0.1)")
        }
    }) {
        // Шапка панели
        Div({
            style {
                padding(16.px); display(DisplayStyle.Flex); justifyContent(JustifyContent.SpaceBetween)
                alignItems(AlignItems.Center); /*borderBottom(1.px, LineStyle.Solid, Color("#eee"))*/
            }
        }) {
            H3 { Text("Сохраненные места") }
            Button(attrs = {
                onClick { onClose() }
                style { border(0.px); backgroundColor(Color.transparent); cursor("pointer"); fontSize(20.px) }
            }) { Text("✕") }
        }

        // Список локаций
        Div({ style { overflowY("auto"); flex(1) } }) {
            if (locations.isEmpty()) {
                Div({ style { padding(20.px); color(Color.gray) } }) { Text("Список пуст") }
            } else {
                locations.forEach { location ->
                    Div({
                        style {
                            padding(16.px); cursor("pointer"); /*borderBottom(1.px, LineStyle.Solid, Color("#f9f9f9"))*/
                            display(DisplayStyle.Flex); flexDirection(FlexDirection.Column)
                        }
                        onClick { onSelect(location) }
                    }) {
                        Span({ style { fontWeight("bold") } }) { Text(location.name) }
                        Span({ style { fontSize(12.px); color(Color.gray) } }) {
                            Text("${location.region}, ${location.country}")
                        }
                    }
                }
            }
        }
    }
}