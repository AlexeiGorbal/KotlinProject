package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.example.project.viewmodel.LocationSearchUiState
import org.example.project.viewmodel.LocationSearchViewModel
import org.example.project.viewmodel.LocationWeatherViewModel
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.outline
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput

@Composable
fun WebSearchComponent(
    searchViewModel: LocationSearchViewModel,
    weatherViewModel: LocationWeatherViewModel
) {
    val uiState by searchViewModel.uiState.collectAsState()
    val text by searchViewModel.text.collectAsState()

    // Контейнер поиска поверх карты
    Div({
        style {
            position(Position.Absolute)
            left(20.px)
            top(20.px)
            width(300.px)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            property("z-index", 4000) // Выше, чем карта и SidePanel
        }
    }) {
        // Поле ввода
        TextInput(value = text, attrs = {
            onInput { event -> searchViewModel.searchLocations(event.value) }
            placeholder("Поиск города...")
            style {
                padding(10.px)
                borderRadius(8.px)
                border(1.px, LineStyle.Solid, Color("lightgray"))
                property("box-shadow", "0 2px 6px rgba(0,0,0,0.1)")
                outline("none")
            }
        })

        // Выпадающий список результатов
        when (val state = uiState) {
            is LocationSearchUiState.Suggestions -> {
                Div({
                    style {
                        backgroundColor(Color("white"))
                        marginTop(5.px)
                        borderRadius(8.px)
                        property("box-shadow", "0 4px 12px rgba(0,0,0,0.15)")
                        property("max-height", "200.px")
                        overflowY("auto")
                    }
                }) {
                    state.locations.forEach { location ->
                        Div({
                            style {
                                padding(10.px)
                                cursor("pointer")
                                //borderBottom(1.px, LineStyle.Solid, Color("#f0f0f0"))
                            }
                            onClick {
                                weatherViewModel.onLocationSelected(location)
                                searchViewModel.searchLocations("") // Очистить поиск после выбора
                            }
                        }) {
                            Text(location.name)
                        }
                    }
                }
            }
            else -> {} // Скрываем, если загрузка или пусто
        }
    }
}