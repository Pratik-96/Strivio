package com.example.imdbclone.Screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import coil.size.Size
import com.example.imdbclone.Activities.sarabunFont
import com.example.imdbclone.DataClasses.SavedShowDetails
import com.example.imdbclone.DataClasses.ServiceMetaData
import com.example.imdbclone.DataClasses.ShowDetails
import com.example.imdbclone.ViewModels.MainViewModel
import com.example.imdbclone.ui.theme.Gray




@Composable
fun DetailScreen(data: ShowDetails,navHostController: NavHostController) {

    val showId = data.id
    val title = data.title
    val showType = data.showType
    val verticalImage = data.imageSet.verticalPoster?.w480
    val horizontalImage = data.imageSet.horizontalPoster?.w480
    val serviceMetadata = data.streamingOptions?.`in`

    val mainViewModel:MainViewModel = viewModel()

    val showData = SavedShowDetails(showId,title,showType,verticalImage,horizontalImage,serviceMetadata)


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {


        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Box(
                    modifier = Modifier
                ) {
                    BackgroundPoster(data.imageSet.horizontalBackdrop?.w720 ?: "",navHostController)

                }
                Text(
                    text = data.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(8.dp)
                )
                LazyHorizontalGrid(
                    modifier = Modifier.fillMaxWidth().height(50.dp),  rows = GridCells.Fixed(1)
                ) {
                    item {
                       ShowMetadata(data)
                    }


                }

                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ImportantText("Streaming on: ")

                    for (i in 0 until (data.streamingOptions?.`in`?.size ?: 0)) {

                        if (data.streamingOptions?.`in`?.size == 0){
                            ImportantText("Unavailable")
                        }
                        if (data.streamingOptions?.`in`?.get(i)?.type.equals("subscription")) {
                            Logo(
                                data.streamingOptions?.`in`?.get(i)?.service?.imageSet?.darkThemeImage
                                    ?: ""
                            )
                        }



                    }

                }
                var isAvailable = remember { mutableStateOf(true) }
                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    ImportantText("Rent on: ")
                    for (i in 0 until (data.streamingOptions?.`in`?.size ?: 0)) {

                        if (data.streamingOptions?.`in`?.get(i)?.type.equals("rent")  ){
                            Logo(
                                data.streamingOptions?.`in`?.get(i)?.service?.imageSet?.darkThemeImage
                                    ?: ""
                            )
                        }else if (!data.streamingOptions?.`in`?.get(i)?.type.equals("buy") && !data.streamingOptions?.`in`?.get(i)?.type.equals("addon") &&!data.streamingOptions?.`in`?.get(i)?.type.equals("subscription")){
                            isAvailable.value = false
                        }

                    }
                    if (!isAvailable.value){
                        ImportantText("Unavailable")
                    }
                }
                var showDialog by remember { mutableStateOf(false) }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = {

                            showDialog = true


                        }, modifier = Modifier
                            .wrapContentSize()
                            .padding(8.dp)
                            .clip(
                                RoundedCornerShape(4.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = Color.White
                        )

                    ) {
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Play")
                        ButtonText("WATCH")
                        if (showDialog) {
                            ServiceDialog({ showDialog = false }, data)
                        }
                    }
                    val context = LocalContext.current as Activity
                    Button(
                        onClick = {

                            mainViewModel.uploadShowList(showData,context)

                        }, modifier = Modifier
                            .wrapContentSize()
                            .padding(8.dp)
                            .clip(
                                RoundedCornerShape(4.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color.DarkGray
                        )

                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                        Text(
                            text = "MY LIST",
                            color = Color.White,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 8.dp),
                            fontWeight = FontWeight.Bold
                        )                    }
                }
                NormalText(data.overview)

                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val cast: List<String> = data.cast
                    val castString = cast.joinToString(",") { it }
                    Text(
                        "Starring: ",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                    SmallText(castString)

                }

                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (data.showType.equals("movie")) {
                        val directors: List<String?> = data.directors
                        val directorString = directors.joinToString(",") { it.toString() }
                        Text(
                            "Directors: ",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                            fontWeight = FontWeight.Bold
                        )
                        SmallText(directorString)
                    } else {
                        val creators: List<String?> = data.creators
                        val createrString = creators.joinToString(",") { it.toString() }
                        Text(
                            "Creators: ",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                            fontWeight = FontWeight.Bold
                        )
                        SmallText(createrString)
                    }

                }
                if (!data.showType.equals("movie")) {
                    Text(
                        "No. of episodes: ${data.episodeCount}",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }


}

@Composable
fun ShowMetadata(data: ShowDetails) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {

        if (data.showType.equals("movie")) {
            NormalText(data.releaseYear.toString()) // modify
            NormalText("${data.runtime}m")
        } else {
            NormalText(data.firstAirYear.toString())
            NormalText("${data.seasonCount} Season")
        }
        NormalText(data.genres.get(0)?.name ?: "")
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, start = 8.dp)
        )
        Text(
            text = "${data.rating}%",
            color = Gray,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        if (!data.streamingOptions?.`in`?.get(0)?.quality.isNullOrEmpty()
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(1.dp, color = Gray, RoundedCornerShape(4.dp))
            ) {

                Text(
                    data.streamingOptions?.`in`?.get(0)?.quality.toString()
                        .toUpperCase(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(2.dp),
                    color = Gray,
                    fontSize = 12.sp
                )
            }

        }else{

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(1.dp, color = Gray, RoundedCornerShape(4.dp))
            ) {

                Text("UHD",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp),
                    color = Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}


@Composable
fun ServiceDialog(onDismiss: () -> Unit, data: ShowDetails) {

    Dialog(onDismissRequest = onDismiss) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            ImportantText("Stream on:")
            for (i in 0 until (data.streamingOptions?.`in`?.size ?: 0)) {
               data.streamingOptions?.`in`?.get(i)?.let {
                        DialogLogo(
                            it
                        )

                }

            }
//            ImportantText("Rent on:")
//            for (i in 0 until (data.streamingOptions?.`in`?.size ?: 0)) {
//                if (data.streamingOptions?.`in`?.get(i)?.type.equals("rent")) {
//                    data.streamingOptions?.`in`?.get(i)?.let {
//                        DialogLogo(
//                            it
//                        )
//                    }
//                }
//
//            }

        }
    }
}

@Composable
fun DialogLogo(data: ServiceMetaData) {
    // Redundant code make a fun
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {}
    // Configure Coil with an ImageLoader that includes the SvgDecoder
    val imageLoader = ImageLoader.Builder(context).components {
        add(SvgDecoder.Factory())  // Add SVG decoder explicitly
    }.build()
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(
            data.service?.imageSet?.darkThemeImage
                ?: ""
        ).size(Size.ORIGINAL)  // Adjust as needed
            .build(), imageLoader = imageLoader
    )
    Box(modifier = Modifier.wrapContentSize().padding(8.dp).clickable {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(data.link)
            )
            launcher.launch(intent)
        } catch (e: Exception) {
            Toast
                .makeText(context, e.message, Toast.LENGTH_SHORT)
                .show()
        }
    }){
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painter,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .size(width = 100.dp, height = 64.dp)
                    ,
                contentDescription = null,

                )

            ImportantText("(${data.type})")
        }
    }

}


@Composable
fun Logo(url: String) {
    val context = LocalContext.current
    // Redundant code make a fun
    // Configure Coil with an ImageLoader that includes the SvgDecoder
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())  // Add SVG decoder explicitly
        }
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder().directory(context.cacheDir.resolve("image_cache"))
                .maxSizeBytes(512 * 1024 * 1024).build()
        }
        .build()
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(url)
            .size(Size.ORIGINAL)  // Adjust as needed
            .build(),
        imageLoader = imageLoader
    )
    Image(
        painter = painter,
        modifier = Modifier
            .padding(top = 8.dp)
            .size(width = 50.dp, height = 24.dp),
        contentDescription = null
    )
}

@Composable
fun BackgroundPoster(url: String,navHostController: NavHostController) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    // Configure Coil with an ImageLoader that includes the SvgDecoder
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())  // Add SVG decoder explicitly
        }
        .build()
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(url)
            .build(),
        imageLoader = imageLoader
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .aspectRatio(16f / 9f),
            contentScale = ContentScale.Crop

        )
        val activityContext = LocalContext.current as Activity
        IconButton(
            onClick = {
                if(navHostController.currentBackStackEntry?.destination!=null) {
                    navHostController.popBackStack()
                }else{
                    activityContext.finish()
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopEnd),
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.1f))
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}



@Composable
fun ButtonText(text: String) {
    Text(
        text = text,
        color = Color.Black,
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        modifier = Modifier.padding(start = 8.dp),
        fontWeight = FontWeight.Bold
    )

}

@Composable
fun NormalText(text: String) {
    Text(text = text, color = Color.Gray,fontFamily = sarabunFont, fontSize = 16.sp, modifier = Modifier.padding(8.dp), maxLines = 5,overflow = TextOverflow.Ellipsis)
}
@Composable
fun LargeText(text: String) {
    Text(text = text, color = Color.White, fontSize = 24.sp, modifier = Modifier.padding(8.dp),fontFamily = sarabunFont, fontWeight = FontWeight.Bold)
}

@Composable
fun SmallText(text: String) {
    Text(text = text, color = Color.Gray,fontFamily = sarabunFont, fontSize = 14.sp, modifier = Modifier.padding(8.dp))
}

@Composable
fun ImportantText(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontFamily = sarabunFont,
        fontSize = 16.sp,
        modifier = Modifier.padding(8.dp),
        fontWeight = FontWeight.Bold
    )
}

