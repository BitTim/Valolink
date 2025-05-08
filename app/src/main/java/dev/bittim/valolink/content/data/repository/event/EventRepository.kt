/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       EventRepository.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.data.repository.event

import dev.bittim.valolink.content.data.repository.ContentRepository
import dev.bittim.valolink.content.domain.model.Event

interface EventRepository : ContentRepository<Event>