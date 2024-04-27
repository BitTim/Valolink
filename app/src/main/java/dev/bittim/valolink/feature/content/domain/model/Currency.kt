package dev.bittim.valolink.feature.content.domain.model

data class Currency(
    val uuid: String,
    val displayName: String,
    val displayNameSingular: String,
    val displayIcon: String,
    val largeIcon: String
) {
    companion object {
        const val doughUuid = "85ca954a-41f2-ce94-9b45-8ca3dd39a00d"
        const val vpUuid = "85ad13f7-3d1b-5128-9eb2-7cd8ee0b5741"
    }
}