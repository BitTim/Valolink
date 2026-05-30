/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       AuthFlowRepo.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   30.05.26, 02:03
 */

package dev.bittim.valolink.feature.auth.domain.repo

class AuthFlowRepo {
    var email: String = ""
    var otp: String = ""

    fun clear() {
        email = ""
        otp = ""
    }
}