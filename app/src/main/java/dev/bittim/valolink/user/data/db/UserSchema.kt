/*
 Copyright (c) 2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       UserSchema.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   29.01.26, 15:30
 */

package dev.bittim.valolink.user.data.db

import com.powersync.db.schema.Column
import com.powersync.db.schema.Schema
import com.powersync.db.schema.Table

class UserSchema {
    companion object {
        val schema = Schema(
            Table(
                name = "users",
                columns = listOf(
                    Column.text("created_at"),
                    Column.text("username"),
                    Column.integer("is_private"),
                    Column.text("avatar"),
                    Column.integer("has_onboarded"),
                    Column.integer("has_rank")
                )
            ),
            Table(
                name = "agents",
                columns = listOf(
                    Column.text("created_at"),
                    Column.text("user"),
                    Column.text("agent")
                )
            ),
            Table(
                name = "contracts",
                columns = listOf(
                    Column.text("created_at"),
                    Column.text("user"),
                    Column.text("contract"),
                    Column.integer("free_only"),
                    Column.integer("xp_offset")
                )
            ),
            Table(
                name = "levels",
                columns = listOf(
                    Column.text("created_at"),
                    Column.text("contract"),
                    Column.text("level"),
                    Column.integer("is_purchased"),
                    Column.text("user")
                )
            ),
            Table(
                name = "matches",
                columns = listOf(
                    Column.text("created_at"),
                    Column.text("user"),
                    Column.text("details"),
                    Column.integer("xp"),
                    Column.integer("rr_offset"),
                    Column.integer("rr"),
                    Column.integer("is_team_b")
                )
            ),
            Table(
                name = "rel_match_contract",
                columns = listOf(
                    Column.text("contract"),
                    Column.text("user"),
                    Column.text("details")
                )
            ),
            Table(
                name = "friends",
                columns = listOf(
                    Column.text("user"),
                    Column.text("target"),
                    Column.text("created_at"),
                    Column.text("status")
                )
            ),
            Table(
                name = "match_details",
                columns = listOf(
                    Column.text("created_at"),
                    Column.integer("score_a"),
                    Column.integer("score_b"),
                    Column.integer("surrender_a"),
                    Column.integer("surrender_b"),
                    Column.text("map"),
                    Column.text("time"),
                    Column.text("mode")
                )
            )
        )
    }
}