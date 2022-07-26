package ui.component.lazyverticalgrid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ui.component.lazyverticalgrid.AnimeItemSpace.animeItemSpace

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UPlayerLazyVerticalGrid(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    dataList: List<Any>,
    adapter: LazyGridAdapter,
    key: ((index: Int, item: Any) -> Any)? = null
) {
    val listState = rememberLazyGridState()
    val spanIndexArray: MutableList<Int> = remember { mutableListOf() }
    LazyVerticalGrid(
        modifier = modifier,
        cells = GridCells.Fixed(MAX_SPAN_SIZE),
        state = listState,
        contentPadding = contentPadding
    ) {
        itemsIndexed(
            items = dataList,
            key = key,
            span = { index, item ->
                val spanIndex = maxLineSpan - maxCurrentLineSpan
                if (spanIndexArray.size > index) spanIndexArray[index] = spanIndex
                else spanIndexArray.add(spanIndex)
                GridItemSpan(uPlayerSpan(item))
            }
        ) { index, item ->
            adapter.Draw(
                modifier = Modifier.animeItemSpace(
                    item = item,
                    spanSize = uPlayerSpan(item),
                    spanIndex = spanIndexArray[index]
                ),
                index = index,
                data = item
            )
        }
    }
}