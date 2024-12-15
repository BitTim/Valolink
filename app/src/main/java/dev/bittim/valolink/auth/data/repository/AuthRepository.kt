/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       AuthRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.auth.data.repository

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String,
    ): Boolean

    suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): Boolean
}