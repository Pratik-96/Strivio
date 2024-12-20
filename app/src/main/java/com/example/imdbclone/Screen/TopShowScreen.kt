package com.example.imdbclone.Screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.imdbclone.DataClasses.SavedShowDetails
import com.example.imdbclone.DataClasses.ShowDetails
import com.example.imdbclone.ViewModels.HotstarViewModel
import com.example.imdbclone.ViewModels.MainViewModel
import com.example.imdbclone.ui.ImageSlider
import com.example.imdbclone.ui.theme.DeepGray
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private lateinit var auth: FirebaseAuth

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopShowScreen(viewModel: MainViewModel, navigateToDetail: (ShowDetails) -> Unit) {

    auth = FirebaseAuth.getInstance()
    val netflixState by viewModel.showStates[viewModel.netflixSeriesIndex]
    val netflixMovieState by viewModel.showStates[viewModel.netflixMoviesIndex]
    val appleState by viewModel.showStates[viewModel.appleSeriesIndex]
    val appleMovieState by viewModel.showStates[viewModel.appleMoviesIndex]
    val primeState by viewModel.showStates[viewModel.primeSeriesIndex]
    val primeMovieState by viewModel.showStates[viewModel.primeMoviesIndex]
//    val hostarState by viewModel.showStates[viewModel.hotstarSeriesIndex]

    val actionShows by viewModel.genreShowState[viewModel.actionIndex]
    val sciFiShows by viewModel.genreShowState[viewModel.siFiIndex]
    val adventureShows by viewModel.genreShowState[viewModel.adventureIndex]
    val animationShows by viewModel.genreShowState[viewModel.animationIndex]
    val comedyShows by viewModel.genreShowState[viewModel.comedyIndex]
    val crimeShows by viewModel.genreShowState[viewModel.crimeIndex]
    val documentaryShows by viewModel.genreShowState[viewModel.documentaryIndex]
    val dramaShows by viewModel.genreShowState[viewModel.dramaIndex]
    val fantasyShows by viewModel.genreShowState[viewModel.fantasyIndex]
    val horrorShows by viewModel.genreShowState[viewModel.horrorIndex]
    val romanceShows by viewModel.genreShowState[viewModel.romanceIndex]
    val thrillerShows by viewModel.genreShowState[viewModel.thrillerIndex]

    var fetched by remember { mutableStateOf(false) }

    val imageSliderDataState = viewModel.topGenreShows
    val genreState = viewModel.genreState
    val coroutineScope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    val refreshing by remember { mutableStateOf(viewModel.isLoading) }
    val genreItems = mutableListOf<@Composable () -> Unit>()
    var showGenreItems by remember { mutableStateOf(false) }
    when {
        genreState.value.genres.isNotEmpty() -> {
            for (item in viewModel.genres!!) {
                when {
                    item.equals("action", ignoreCase = true) -> {
                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.actionIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "action",
                                    ""
                                )
                            }
                            StateScreen(title = "Action Shows", actionShows, navigateToDetail)
                        }
                    }

                    item.equals("scifi", ignoreCase = true) -> {
                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.siFiIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "scifi",
                                    ""
                                )
                            }

                            StateScreen(
                                title = "Science Fiction Shows",
                                sciFiShows,
                                navigateToDetail
                            )
                        }


                    }

                    item.equals("romance", ignoreCase = true) -> {

                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.romanceIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "romance",
                                    ""
                                )
                            }

                            StateScreen(title = "Romantic Shows", romanceShows, navigateToDetail)

                        }

                    }

                    item.equals("adventure", ignoreCase = true) -> {

                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.adventureIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "adventure",
                                    ""
                                )
                            }

                            StateScreen(title = "Adventure Shows", adventureShows, navigateToDetail)

                        }

                    }

                    item.equals("animation", ignoreCase = true) -> {

                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.animationIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "animation",
                                    ""
                                )
                            }

                            StateScreen(title = "Animation Shows", animationShows, navigateToDetail)

                        }

                    }

                    item.equals("comedy", ignoreCase = true) -> {

                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.comedyIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "comedy",
                                    ""
                                )
                            }

                            StateScreen(title = "Comedy Shows", comedyShows, navigateToDetail)

                        }
                    }

                    item.equals("crime", ignoreCase = true) -> {

                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.crimeIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "crime",
                                    ""
                                )
                            }

                            StateScreen(title = "Crime Shows", crimeShows, navigateToDetail)

                        }
                    }

                    item.equals("documentary", ignoreCase = true) -> {

                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.documentaryIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "documentary",
                                    ""
                                )
                            }

                            StateScreen(
                                title = "Documentary Shows",
                                documentaryShows,
                                navigateToDetail
                            )

                        }
                    }

                    item.equals("drama", ignoreCase = true) -> {

                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.dramaIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "drama",
                                    ""
                                )
                            }

                            StateScreen(title = "Drama Shows", dramaShows, navigateToDetail)

                        }
                    }

                    item.equals("fantasy", ignoreCase = true) -> {

                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.fantasyIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "fantasy",
                                    ""
                                )
                            }

                            StateScreen(title = "Fantasy Shows", fantasyShows, navigateToDetail)

                        }
                    }

                    item.equals("horror", ignoreCase = true) -> {

                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.horrorIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "horror",
                                    ""
                                )
                            }

                            StateScreen(title = "Horror Shows", horrorShows, navigateToDetail)

                        }
                    }

                    item.equals("thriller", ignoreCase = true) -> {

                        genreItems.add {
                            LaunchedEffect(Unit) {
                                viewModel.fetchGenreShows(
                                    viewModel._genreShowState[viewModel.thrillerIndex],
                                    "in",
                                    "",
                                    "",
                                    70,
                                    "thriller",
                                    ""
                                )
                            }

                            StateScreen(title = "Thriller Shows", thrillerShows, navigateToDetail)

                        }
                    }


                }
            }
        }
    }




    PullToRefreshBox(isRefreshing, state = refreshState, onRefresh = {

        coroutineScope.launch {
            isRefreshing = true
            viewModel._genreShowState[viewModel.topGenreShowsIndex].value =
                viewModel._genreShowState[viewModel.topGenreShowsIndex].value.copy(
                    list = emptyList(),
                    loading = true
                )

            viewModel.refreshData()

            viewModel.fetchGenreShows(
                viewModel._genreShowState[viewModel.topGenreShowsIndex],
                "in",
                "",
                "",
                70,
                viewModel.selectedGenres,
                ""
            )

            delay(3000L)

            isRefreshing = false

        }

    }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.Black),

            ) {

            when {
                !genreState.value.loading -> {
                    Text("${viewModel.selectedGenres}", color = Color.White)
                }
            }
            LaunchedEffect(Unit) {
                viewModel.fetchGenreShows(
                    viewModel._genreShowState[viewModel.topGenreShowsIndex],
                    "in",
                    "",
                    "",
                    70,
                    viewModel.selectedGenres,
                    ""
                )
            }
            when {
                imageSliderDataState.value.loading -> {
                    CircularProgressIndicator()
                }

                imageSliderDataState.value.error != null -> {
                    Text(imageSliderDataState.value.error.toString())
                }

                else -> {
                    ImageSlider(imageSliderDataState.value.list, navigateToDetail)
                }
            }
            for (item in genreItems) {
                item()
            }
            StateScreen(title = "Top Netflix Shows", netflixState, navigateToDetail)
            StateScreen(title = "Top Netflix Movies", netflixMovieState, navigateToDetail)
            StateScreen(title = "Top Apple Tv Shows", appleState, navigateToDetail)
            StateScreen(title = "Top Apple Tv Movies", appleMovieState, navigateToDetail)
            StateScreen(title = "Top Movies on Prime", primeMovieState, navigateToDetail)
        }
    }
}



@Composable
fun StateScreen(
    title: String,
    showState: MainViewModel.ShowState,
    navigateToDetail: (ShowDetails) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        when {

            showState.loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }

            showState.error != null -> {
                Text(showState.error, color = Color.White)
            }

            else -> {
                ShowsScreen(showState.list, title, navigateToDetail)
            }
        }
    }

}


@Composable
fun ShowsScreen(shows: List<ShowDetails>, title: String, navigateToDetail: (ShowDetails) -> Unit) {
    val context = LocalContext.current

    val logo = shows.get(0).streamingOptions?.`in`?.get(0)?.service?.imageSet?.darkThemeImage

    // Configure Coil with an ImageLoader that includes the SvgDecoder
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())  // Add SVG decoder explicitly
        }
        .build()
    Column(
        modifier = Modifier
            .wrapContentSize(unbounded = false)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize(unbounded = false)
                .padding(8.dp)
        ) {

            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(logo)
                    .size(Size.ORIGINAL)  // Adjust as needed
                    .build(),
                imageLoader = imageLoader
            )
//            Image(
//                painter = painter,
//                modifier = Modifier.size(width = 50.dp, height = 24.dp),
//                contentDescription = title,
//                contentScale = ContentScale.Crop
//
//            )

//            Spacer(Modifier.padding(4.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
        }


        LazyHorizontalGrid(
            modifier = Modifier
                .wrapContentSize(unbounded = false)
                .height(100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            rows = GridCells.Fixed(1)
        ) {
            items(shows) { item ->

                ShowItem(item, navigateToDetail)

            }
        }

    }


}


@Composable
fun ShowItem(item: ShowDetails, navigateToDetail: (ShowDetails) -> Unit) {


    val context = LocalContext.current


    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    Column(
        modifier = Modifier.clickable {
            try {

                navigateToDetail(item)
            } catch (e: Exception) {
                Log.d("nav", "ShowItem: " + e.message)
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val painter = rememberAsyncImagePainter(
            model = item.imageSet.horizontalPoster?.w360 // Adjust as needed
            ,
            imageLoader = imageLoader
        )
        Image(
            painter = painter,
            modifier = Modifier
                .size(width = 180.dp, height = 100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = item.title,
            contentScale = ContentScale.Crop

        )
    }


}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun VerticalImageSlider(data: List<ShowDetails>, navigateToDetail: (ShowDetails) -> Unit) {
    val viewModel: HotstarViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel()



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {

        val pagerState = rememberPagerState(0)
        LaunchedEffect(Unit) {
            while (true) {
                delay(4000)
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % data.size)
            }
        }

        com.google.accompanist.pager.HorizontalPager(
            state = pagerState,
            count = data.size,

            ) { page ->
            val showId = data[page].id
            val title = data[page].title
            val showType = data[page].showType
            val verticalImage = data[page].imageSet?.verticalPoster?.w480
            val horizontalImage = data[page].imageSet?.horizontalPoster?.w480
            val serviceMetadata = data[page].streamingOptions?.`in`

            val context = LocalContext.current as Activity

            val showData = SavedShowDetails(
                showId,
                title,
                showType,
                verticalImage,
                horizontalImage,
                serviceMetadata
            )

            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navigateToDetail(data[page])
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
//                        .fillMaxWidth()
                        .width(300.dp)
//                        .padding(8.dp)
                        .height(400.dp),
                    contentAlignment = Alignment.Center


                ) {
                    AsyncImage(
                        data[page].imageSet.verticalPoster?.w360,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape = RoundedCornerShape(8.dp))
                    )


                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            brush =
                            Brush
                                .verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(0.4f)
                                    ),
                                    startY = 0f, // Start at the top
                                    endY = 50f
                                )
                        )
//                        .align(Alignment.BottomCenter)
                        .zIndex(3f)
                ) {

                    Row(
                        modifier = Modifier.align(Alignment.Center),
                    ) {
//                            val genreDetailList = data[page].genres
//                            val genreList: MutableList<String> = mutableListOf()
//                            for (i in 0 until genreDetailList.size) {
//                                genreDetailList.get(i)?.name?.let { genreList.add(it) }
//                            }
//
//                            val genres = genreList.joinToString(" • ") { it }
//
//
//                            Text(
//                                text = genres,
//                                fontWeight = FontWeight.W500,
//                                color = Color.White,
//                                fontSize = 16.sp,
//                                modifier = Modifier
//                                    .padding(0.dp)
//                            )

                        var link = data[page].streamingOptions?.`in`?.get(0)?.link.toString()
                        for (i in 0 until data[page].streamingOptions?.`in`?.size!!) {
                            if (data[page].streamingOptions?.`in`?.get(i)?.service?.id.equals("hotstar")) {
                                link =
                                    data[page].streamingOptions?.`in`?.get(i)?.link.toString()
                            }
                        }
                        val launcher =
                            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {}
                        Button(
                            onClick = {
//
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(link)
                                )
                                launcher.launch(intent)

                            }, modifier = Modifier
                                .height(55.dp)
                                .padding(8.dp),
//                            .shadow(shape = RoundedCornerShape(4.dp), elevation = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = DeepGray
                            ), shape = RoundedCornerShape(8.dp)

                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "play"
                            )
                            Text(
                                text = "Watch Now",
                                color = Color.White,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
//
                                mainViewModel.uploadShowList(showData, context)

                            }, modifier = Modifier
                                .height(55.dp)
                                .padding(8.dp),
//                            .shadow(shape = RoundedCornerShape(4.dp), elevation = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = DeepGray
                            ), shape = RoundedCornerShape(8.dp)

                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "myList"
                            )
                            Text(
                                text = "My List",
                                color = Color.White,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }


                }
            }

        }


    }

}
