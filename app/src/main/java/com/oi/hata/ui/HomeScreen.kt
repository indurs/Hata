package com.oi.hata.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oi.hata.R
import kotlinx.coroutines.launch
import kotlin.math.max

enum class HataHomeScreens(val title: String) {
    Today("Today"),
    Tomorrow("Tomorrow"),
    Calendar("Calendar")
}

enum class HataHomeBottomMenus(val title:String){
    Task("Task"),
    Travel("Travel"),
    Diary("Diary")
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun HomeScreen(scaffoldState: ScaffoldState = rememberScaffoldState(), homeViewModel: HomeViewModel){

    val coroutineScope = rememberCoroutineScope()

    val todayScreen = TabContent(HataHomeScreens.Today){
        //Pass parameters to TodayScreen
        TodayScreen()
    }

    val tomorrowScreen = TabContent(HataHomeScreens.Tomorrow){
        //Pass parameters to TodayScreen
        TomorrowScreen()
    }

    val tabsContent = listOf(todayScreen,tomorrowScreen)
    //val (currentHomescreen, updateHomescreen) = remember { mutableStateOf(tabsContent.first().homescreen) }

   HomeScreen(
        tabsContent = tabsContent,
        tab = homeViewModel.currentTab,
        onTabChange = { homeViewModel.onSelectTab(it) },
        scaffoldState = scaffoldState,
        nocontent = true
    )

    //WithConstraintsComposable()

}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun HomeScreen(tabsContent: List<TabContent>,
                        tab: HataHomeScreens,
                        onTabChange: (HataHomeScreens) -> Unit,
                        scaffoldState: ScaffoldState,
                        nocontent: Boolean
) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {},
        topBar = {
            TopAppBar(
                title = { Text("Interests") },
                navigationIcon = {
                    IconButton(onClick = { coroutineScope.launch { scaffoldState.drawerState.open() } }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_jetnews_logo),
                            contentDescription = stringResource(
                                R.string.cd_open_navigation_drawer)
                        )
                    }
                }
            )
        },
        bottomBar = { },
        content= {


            Surface(modifier = Modifier.fillMaxSize(),color = Color.White) {
                HomeTabContent(
                    currentHomescreen = tab,
                    onTabChange,
                    tabsContent = tabsContent,
                    scaffoldState = scaffoldState,
                    nocontent = nocontent,

                    )
                Column(verticalArrangement = Arrangement.Bottom) {
                    BottomBar()
                }
            }

        }
    )

}


@ExperimentalMaterialApi
@Composable
private fun HomeTabContent(currentHomescreen: HataHomeScreens,
                           onTabChange: (HataHomeScreens) -> Unit,
                           tabsContent: List<TabContent>,
                           scaffoldState: ScaffoldState,
                           nocontent: Boolean,
                            ){
    val selectedTabIndex = tabsContent.indexOfFirst { it.homescreen == currentHomescreen }
    val coroutineScope = rememberCoroutineScope()
    Box {
        
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = {},
            divider = {},
            backgroundColor = Color.White
                        ) {
            tabsContent.forEachIndexed { index, tabContent ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick =  { onTabChange(tabContent.homescreen) },
                    //text = { tabContent.homescreen.title }
                ){
                    ChoiceChipContent(
                        text = tabContent.homescreen.title,
                        selected = index == selectedTabIndex,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


    }

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
private fun BottomBar(){
    var (selected,onSelected) = remember { mutableStateOf(false) }

    Surface(modifier = Modifier
        .fillMaxWidth(),
        color = MaterialTheme.colors.primary.copy(alpha = 0.08f),
        shape = RoundedCornerShape(topStart = 26.dp,topEnd = 26.dp),
    ) {
        Column() {
            AnimatedVisibility(visible = selected) {
                Row {
                    TaskSheet()
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment =  Alignment.CenterVertically) {

                ChipContent(text = "Task", selected,onSelected = {  onSelected(it) } )
                Spacer(modifier = Modifier.width(20.dp))
                ChipContent(text = "Diary", selected,onSelected = {  onSelected(it) } )
                Spacer(modifier = Modifier.width(20.dp))
                ChipContent(text = "Travel", selected,onSelected = {  onSelected(it) } )


            }

        }

    }
}



@Composable
private fun TodayScreen(){

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(modifier = Modifier.height(50.dp))
        Text("Today......")

    }

}

@Composable
private fun TomorrowScreen(){

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(modifier = Modifier.height(50.dp))
        Text(text = "Tomorrow")

    }

}

@Composable
private fun ChoiceChipContent(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary.copy(alpha = 0.08f)
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun ChipContent(
    text: String,
    selected: Boolean,
    onSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary.copy(alpha = 0.08f)
            else -> MaterialTheme.colors.onSecondary.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier.clickable {
            if(selected)
                onSelected(false)
            else
                onSelected(true)
                                      },
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}


@Composable
private fun TaskSheet(){
    Surface(modifier = Modifier
        .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 26.dp,topEnd = 26.dp),
        color = MaterialTheme.colors.surface.copy(alpha = 0.12f)
    ) {
        Column() {

        }
    }
}

@Composable
private fun Sample(){


    Box(
        Modifier
            .requiredSize(100.dp)
            .background(Color.Red), contentAlignment = Alignment.Center) {
        // The inner Box will be (30.dp x 50.dp).
        Box(
            Modifier
                .requiredWidth(30.dp)
                .fillMaxHeight(0.5f)
                .background(color = Color.Magenta)
        )
    }
}

@Composable
private fun SampleBox(){


    Surface(
        Modifier
            .fillMaxSize()
            .background(Color.Red),) {
        Column() {
            Box(
                Modifier
                    .size(100.dp)
                    .background(color = Color.Magenta))
        }

    }
}

@Composable
fun SampleBoxes(){
    Box {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Cyan))
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .size(150.dp, 150.dp)
                .background(Color.Blue)
        )
    }
}

@Composable
fun WithConstraintsComposable() {
    BoxWithConstraints {
        Log.d("htwt>>>>", "Max height "+ maxHeight + ", Maxwidth "+maxWidth)
        Surface(
            Modifier
                .fillMaxSize()
                .background(Color.Red),) {

        }
    }
}

@Composable
fun SampleColumn(){
    Surface() {
        Column() {
            Row(){
                Box(
                    Modifier
                        .size(40.dp)
                        .background(Color.Green))
            }
            Column(verticalArrangement = Arrangement.Center) {
                // The child with no weight will have the specified size.
                Box(
                    Modifier
                        .size(40.dp)
                        .background(Color.Magenta))
                // Has weight, the child will occupy half of the remaining height.
                Box(
                    Modifier
                        .width(40.dp)
                        .weight(1f)
                        .background(Color.Yellow))
                // Has weight and does not fill, the child will occupy at most half of the remaining height.
                // Therefore it will occupy 80.dp (its preferred height) if the assigned height is larger.
                Box(
                    Modifier
                        .size(40.dp, 80.dp)
                        .weight(1f, fill = true)
                        .background(Color.Red)
                )
            }
        }

    }

}



@Preview
@Composable
fun SamplePreview(){
    SampleColumn()
}


class TabContent(val homescreen: HataHomeScreens, val content: @Composable () -> Unit)
