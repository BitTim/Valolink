package dev.bittim.valolink.feature.main.domain.model

data class UserData(
    val ownedAgents: List<String> = listOf(),
) {
    companion object {
        fun from(map: Map<String, Any>) = object {
            val agents: List<String> by map

            val data = UserData(agents)
        }.data
    }
}
