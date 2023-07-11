package ui.components

import ComposeMultiplatformApp.shared.MR
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.compose.fontFamilyResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun PagerItem(content: String, from: String, date: String, weekday: String, imageUrl: String) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            if (date.isNotEmpty()) {
                val dates = date.split("-")
                if (dates.size == 3) {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)) {
                                append(dates.last())
                            }
                            append(" / ${dates.first()}.${dates[1]}")
                        }
                    )
                }
            }

            Text(weekday)
        }


        KamelImage(
            asyncPainterResource(imageUrl),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(220.dp),
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.TopCenter
        )

        Text(
            content,
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(horizontal = 8.dp, vertical = 36.dp),
            fontFamily = fontFamilyResource(MR.fonts.Qt.qt)
        )

        Text(
            "—— $from",
            fontFamily = fontFamilyResource(MR.fonts.Qt.qt),
            modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.End)
        )
    }
}