package com.saddict.testkost.utils.utilscreens

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.saddict.testkost.R

@Composable
fun KostBottomNavigationBar(
    navigateToHome:() -> Unit,
    navigateToFavourites:() -> Unit,
    navigateToTrending:() -> Unit,
    navigateToAccount:() -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val menuTitle = listOf(
        R.string.home,
        R.string.favourite,
        R.string.trending,
        R.string.account
    )
    val menuIcons = listOf(
        R.drawable.home_circle,
        R.drawable.heart,
        R.drawable.trending_up,
        R.drawable.account_circle
    )
    NavigationBar(modifier = modifier) {
        menuIcons.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    when(index){
                        0 -> { navigateToHome() }
                        1 -> { navigateToFavourites() }
                        2 -> { navigateToTrending() }
                        3 -> { navigateToAccount() }
                    }
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(item),
                        contentDescription = null
                    )
                },
                label = if (selectedItem == index) {
                    { Text(text = stringResource(id = menuTitle[index])) }
                } else {
                    null
                }
            )
        }
    }
}