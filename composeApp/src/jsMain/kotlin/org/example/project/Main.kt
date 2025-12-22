package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import kotlinx.browser.window
import org.example.project.di.appModule
import org.example.project.viewmodel.LocationSearchViewModel
import org.example.project.viewmodel.LocationWeatherViewModel
import org.example.project.weather.remote.DayWeatherEntity
import org.example.project.weather.remote.HourWeatherEntity
import org.example.project.weather.remote.WeatherEntity
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
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.minHeight
import org.jetbrains.compose.web.css.minWidth
import org.jetbrains.compose.web.css.overflowX
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.right
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import org.koin.core.context.GlobalContext.startKoin


fun main() {
    Firebase.initialize(
        context = null,
        options = FirebaseOptions(
            apiKey = "AIzaSyAfM-u3TdIkbYF6LIskOAZu6yG66hQtq34",
            authDomain = "weather-kmp.firebaseapp.com",
            projectId = "weather-kmp",
            storageBucket = "weather-kmp.firebasestorage.app",
            applicationId = "1:672157854590:web:a4285ed817ffcaf7c1612f",
            databaseUrl = "https://weather-kmp-default-rtdb.firebaseio.com/"
        )
    )

    // Внутри вашего main.kt или там, где вызывается renderComposable
    val koin = startKoin {
        modules(appModule) // Ваш общий модуль с вьюмоделью
    }.koin

    renderComposable(rootElementId = "root") {
        // 1. Получаем ViewModel из контекста Koin
        val viewModel = remember { koin.get<LocationWeatherViewModel>() }
        val searchViewModel = remember { koin.get<LocationSearchViewModel>() }

        // 2. Подписываемся на состояния (States)
        // Используем collectAsState() для превращения Flow из ViewModel в состояние Compose
        val uiState by viewModel.uiState.collectAsState()
        val selectedLocation by viewModel.selectedLocation.collectAsState()

        var mapRef by remember { mutableStateOf<dynamic>(null) }
        var markerRef by remember { mutableStateOf<dynamic>(null) }

        var isHistoryOpen by remember { mutableStateOf(false) }

        LaunchedEffect(selectedLocation) {
            val leaflet = window.asDynamic().L
            val map = mapRef

            if (leaflet != null && map != null && selectedLocation != null) {
                val lat = selectedLocation!!.lat
                val lon = selectedLocation!!.lon
                val coords = arrayOf(lat, lon)

                // Ставим или перемещаем маркер
                if (markerRef == null) {
                    markerRef = leaflet.marker(coords).addTo(map)
                } else {
                    markerRef.setLatLng(coords)
                }

                // Зуммируем карту на пин (13 — уровень приближения)
                map.setView(coords, 13)

                selectedLocation?.name?.let { viewModel.loadWeather(it) }
            }
        }

        // Карта
        Div(attrs = {
            id("map-id")
            style {
                flex(1)
                height(100.percent)
                minHeight(600.px) // Важно: явно задайте минимальную высоту
            }
        }) {
            DomSideEffect { element ->
                val leaflet = window.asDynamic().L
                if (leaflet != null && mapRef == null) { // Инициализируем только 1 раз
                    val map = leaflet.map(element).setView(arrayOf(55.7512, 37.6184), 8)

                    leaflet.tileLayer(
                        "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
                        js("({ attribution: '&copy; OSM' })")
                    ).addTo(map)

                    map.on("click") { event: dynamic ->
                        val lat = event.latlng.lat as Double
                        val lng = event.latlng.lng as Double
                        viewModel.onLocationSelectedOnMap(lat, lng)
                    }

                    mapRef = map // Сохраняем ссылку на карту для LaunchedEffect

                    window.setTimeout({ map.invalidateSize() }, 2000)
                }
            }
        }

        // Далее ваш код с Div и Leaflet...
        Div({
            style { display(DisplayStyle.Flex); width(100.percent); height(100.vh) }
        }) {
            WebSearchComponent(searchViewModel, viewModel)

            Button(attrs = {
                style {
                    padding(10.px); borderRadius(8.px);
                    cursor("pointer")
                    backgroundColor(if (isHistoryOpen) Color("royalblue") else Color("white"))
                    color(if (isHistoryOpen) Color("white") else Color("black"))
                    border(1.px, LineStyle.Solid, Color("lightgray"))
                }
                onClick {
                    isHistoryOpen = !isHistoryOpen
                    if (isHistoryOpen) viewModel.onLocationDeselected() // Закрываем погоду при открытии БД
                }
            }) {
                Text("📁ghsdhfksjfhshkfs")
            }
        }

        // Боковая панель
        if (selectedLocation != null) {
            SidePanel(
                weather = uiState,
                onSave = { viewModel.onSelectedLocationSavedStateChanged() }
            )
        }
    }
}

@Composable
fun SidePanel(weather: WeatherEntity?, onSave: () -> Unit) {
    Div({
        style {
            position(Position.Fixed)
            right(0.px)
            top(0.px)
            width(400.px)
            height(100.vh)
            backgroundColor(Color("white"))
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            property("z-index", 3000)
            property("box-shadow", "0 0 15px rgba(0,0,0,0.15)")
            overflowY("auto")
        }
    }
    ) {
        if (weather == null) {
            Div({ style { padding(20.px) } }) { Text("Загрузка погоды...") }
        } else {
            // 1. Текущие условия (аналог CurrentConditionsItem)
            WebCurrentConditions(weather, onSave)

            Div({ style { padding(16.px) } }) {
                // 2. Локация
                H3 { Text("${weather.location.region}, ${weather.location.country}") }

                // 3. Почасовой прогноз (аналог HourlyForecastRow)
                val firstDay = weather.forecast.days.firstOrNull()
                firstDay?.let { WebHourlyForecastRow(it.hours) }

                // 4. Прогноз на дни (аналог ForecastWeatherItem в цикле)
                Hr() // Разделитель
                weather.forecast.days.forEach { dayWeather ->
                    WebForecastWeatherItem(dayWeather)
                }
            }
        }
    }
}

@Composable
fun WebCurrentConditions(weather: WeatherEntity, onSave: () -> Unit) {
    Div({
        style {
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            padding(16.px)
        }
    }) {
        // Иконка погоды
        Img(src = "https:${weather.current.weatherState.icon}", attrs = {
            style { width(80.px); height(80.px) }
        })

        // Температура
        Span({
            style {
                fontSize(40.px)
                fontWeight("bold")
                margin(16.px)
            }
        }) { Text("${weather.current.tempF}°") }

        // Описание
        Div({ style { flex(1) } }) {
            Div { Text(weather.current.weatherState.text) }
            Div({ style { fontSize(14.px); color(Color.gray) } }) {
                Text("Feels like ${weather.current.feelsLikeF}°")
            }
        }

        // Кнопка сохранения (аналог Icon)
        Button(attrs = {
            onClick { onSave() }
            style { cursor("pointer"); fontSize(20.px); border(0.px); backgroundColor(Color.transparent) }
        }) {
            Text("★") // Или используйте <img> с вашей иконкой
        }
    }
}

@Composable
fun WebHourlyForecastRow(hours: List<HourWeatherEntity>) {
    Div({
        style {
            display(DisplayStyle.Flex)
            overflowX("auto") // Аналог LazyRow
            gap(16.px)
            paddingTop(16.px)
        }
    }) {
        hours.forEach { hour ->
            Div({
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    alignItems(AlignItems.Center)
                    minWidth(60.px)
                }
            }) {
                Span({ style { fontSize(12.px) } }) { Text(hour.timestamp.toString()) }
                Img(src = "https:${hour.weatherState.icon}", attrs = {
                    style { width(40.px); height(40.px) }
                })
                Span({ style { fontWeight("bold") } }) { Text("${hour.tempF}°") }
            }
        }
    }
}

@Composable
fun WebForecastWeatherItem(dayWeather: DayWeatherEntity) {
    var isOpen by remember { mutableStateOf(false) }

    Div({
        style {
            //borderBottom(1.px, LineStyle.Solid, Color("whitesmoke"))
            paddingTop(10.px)
            paddingBottom(10.px)
            cursor("pointer")
        }
        onClick { isOpen = !isOpen }
    }) {
        Div({
            style {
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.SpaceBetween)
            }
        }) {
            Img(src = "https:${dayWeather.day.weatherState.icon}", attrs = {
                style { width(50.px); height(50.px) }
            })

            Div({ style { flex(1); padding((10.px)) } }) {
                Div { Text(dayWeather.timestamp.toString()) }
                Div({ style { fontSize(12.px); color(Color.gray) } }) {
                    Text(dayWeather.day.weatherState.text)
                }
            }

            Div {
                Span({ style { fontWeight("bold") } }) { Text("${dayWeather.day.maxTempF}°") }
                Span({ style { marginLeft(8.px); color(Color.gray) } }) { Text("${dayWeather.day.minTempF}°") }
            }
        }

        if (isOpen) {
            Div({
                style {
                    marginTop(10.px); backgroundColor(Color.whitesmoke); borderRadius(8.px); padding(
                    8.px
                )
                }
            }) {
                WebHourlyForecastRow(dayWeather.hours)
            }
        }
    }
}