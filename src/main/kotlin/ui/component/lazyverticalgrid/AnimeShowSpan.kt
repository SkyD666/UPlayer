package ui.component.lazyverticalgrid

import bean.MusicCover1Bean
import com.skyd.db.History


const val MAX_SPAN_SIZE = 60

fun uPlayerSpan(
    data: Any
): Int = when (data) {
    is MusicCover1Bean,
    is History -> MAX_SPAN_SIZE
    else -> MAX_SPAN_SIZE
}
