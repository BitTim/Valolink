/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       ComponentPreviewAnnotations.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.core.ui.util.annotations

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light Portrait English",
    group = "English Portrait",
    locale = "en",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Preview(
    name = "Dark Portrait English",
    group = "English Portrait",
    locale = "en",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Preview(
    name = "Light Landscape English",
    group = "English Landscape",
    locale = "en",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Preview(
    name = "Dark Landscape English",
    group = "English Landscape",
    locale = "en",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Preview(
    name = "Light Portrait German",
    group = "German Portrait",
    locale = "de",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Preview(
    name = "Dark Portrait German",
    group = "German Portrait",
    locale = "de",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=portrait"
)
@Preview(
    name = "Light Landscape German",
    group = "German Landscape",
    locale = "de",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Preview(
    name = "Dark Landscape German",
    group = "German Landscape",
    locale = "de",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
annotation class ComponentPreviewAnnotations