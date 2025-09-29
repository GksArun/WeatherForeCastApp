package com.gks.weatherforecastapp.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.gks.weatherforecastapp.R
import com.gks.weatherforecastapp.data.database.entity.CityEntity
import com.gks.weatherforecastapp.data.database.entity.CityWithWeather
import com.gks.weatherforecastapp.data.database.entity.WeatherForeCastEntity
import com.gks.weatherforecastapp.ui.theme.Blue
import com.gks.weatherforecastapp.ui.theme.Yellow
import com.gks.weatherforecastapp.utils.formatDate
import com.gks.weatherforecastapp.utils.formatDecimal

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

    val state = homeViewModel.state.collectAsState().value

    LaunchedEffect(state) {
        when (state) {
            is HomeState.NotStarted -> {
                homeViewModel.getLastFetchedCity()
            }

            else -> Unit
        }
    }

    HomePageContent(
        state = state,
        onClick = { homeViewModel.getWeatherDetailsForTheCity(it) }
    )
}

@Composable
fun HomePageContent(
    state: HomeState,
    onClick: (String) -> Unit,
) {
    val weatherList = state.getWeatherListOrEmpty()
    var city by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    isError = state is HomeState.Error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = city,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            onValueChange = {
                city = it
            },
            isError = isError,
            label = { Text(stringResource(R.string.enter_the_city_name)) }
        )

        if (isError) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 4.dp),
                text = stringResource(R.string.city_empty_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            modifier = Modifier.padding(10.dp),
            onClick = { if (city.isNotEmpty()) onClick(city) }
        ) {
            Text(stringResource(R.string.get_weather_detail))
        }

        LazyColumn {
            if (state is HomeState.Success) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        text = (state.cityWithWeather.city.name + ", " + state.cityWithWeather.city.country),
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            items(weatherList.size) { index ->
                WeatherCard(weatherList[index])
            }
        }
    }
}

@Composable
fun WeatherCard(weather: WeatherForeCastEntity) {
    val imageUrl = "https://openweathermap.org/img/wn/${weather.icon}.png"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(10.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Blue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDate(weather.date),
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.height(80.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Yellow
                ) {
                    Text(
                        weather.weather,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Text(
                    text = formatDecimal(weather.maxTemp) + "°C" + " / ${formatDecimal(weather.minTemp)}°C",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {

    val cityWithWeather = CityWithWeather(
        city = CityEntity(1, "Banglore", "IN"),
        weatherList = listOf(
            WeatherForeCastEntity(
                1,
                12L,
                1662030000,
                "Rain",
                "https://openweathermap.org/img/wn/10d.png",
                28.06,
                29.38
            )
        )
    )

    HomePageContent(HomeState.Success(cityWithWeather)) { }
}

@Composable
@Preview
fun WeatherCardPreview() {

    val weatherForeCastEntity = WeatherForeCastEntity(
        1,
        12L,
        1662030000,
        "Rain",
        "https://openweathermap.org/img/wn/10d.png",
        28.06,
        29.38

    )
    WeatherCard(weather = weatherForeCastEntity)
}