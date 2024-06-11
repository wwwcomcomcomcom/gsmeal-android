package com.example.android_learn

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.android_learn.ui.theme.AndroidlearnTheme
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AndroidlearnTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            HeaderBar(modifier = Modifier.padding(innerPadding))
            MealList()
          }
        }
      }
    }
  }
}

@Composable
fun HeaderBar(modifier: Modifier = Modifier){
  Text(
    text = "GSMeal",
    fontWeight = FontWeight.Bold,
    fontSize = 40.sp,
    modifier = modifier
  )
}

val colors = listOf(Color.Red, Color.Green, Color.Blue)
@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealList() {
  val pagerState = rememberPagerState(
    pageCount = { colors.size },
  );
  val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope;
  var meals by remember { mutableStateOf(mutableListOf<String>()) };
  var isLoading by remember { mutableStateOf(true) };
  val client = OkHttpClient();


  LaunchedEffect(Unit) {
    lifecycleScope.launch {

    }
  }

  if (isLoading) {
    Loading()
  }else{
    HorizontalPager(state = pagerState) { page ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(colors[page])
      ) {
        // Optional: Add some content to each page
        Text(
          text = "Page ${page + 1}",
          modifier = Modifier.align(Alignment.Center)
        )
      }
    }
  }

}

private suspend fun fetchMeals(client: OkHttpClient, meals: MutableList<String>) {
  val request = okhttp3.Request.Builder()
    .url("https://localhost:3000/")
    .build();
  val response = client.newCall(request).execute();

  if (response.isSuccessful) {
    val mealListData = JSONArray(response.body!!.string())
    for (i in 0 until mealListData.length()) {
      val mealList = mealListData.getJSONObject(i).getJSONArray("menu");
      val meal = mealList.join("/");
      meals.add(meal);
    }
  }

}

@Composable
fun Loading(modifier: Modifier = Modifier){
  Text(text = "Loading", fontSize = 50.sp, fontWeight = FontWeight.Bold, modifier = modifier)
}