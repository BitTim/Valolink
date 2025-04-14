/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bittim.valolink.R
import dev.bittim.valolink.content.data.repository.agent.AgentRepository
import dev.bittim.valolink.content.data.repository.contract.ContractRepository
import dev.bittim.valolink.core.domain.usecase.profile.GenerateAvatarUseCase
import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserAgentRepository
import dev.bittim.valolink.user.data.repository.data.UserContractRepository
import dev.bittim.valolink.user.domain.error.UsernameError
import dev.bittim.valolink.user.domain.usecase.validator.ValidateUsernameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val agentRepository: AgentRepository,
    private val userAgentRepository: UserAgentRepository,
    private val contractRepository: ContractRepository,
    private val userContractRepository: UserContractRepository,
    private val sessionRepository: SessionRepository,
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val generateAvatarUseCase: GenerateAvatarUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileSetupState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            sessionRepository.isLocal().collect {
                _state.update { it.copy(isLocal = it.isLocal) }
            }
        }

        selectAvatar("")
    }

    fun validateUsername(username: String): UiText? {
        val usernameResult = validateUsernameUseCase(username)
        val usernameError = when (usernameResult) {
            is Result.Ok -> null
            is Result.Err -> when (usernameResult.error) {
                UsernameError.EMPTY -> UiText.StringResource(R.string.error_empty)
                UsernameError.TOO_SHORT -> UiText.StringResource(R.string.error_username_tooShort)
            }
        }

        selectAvatar(username)
        _state.update { it.copy(usernameError = usernameError) }
        return usernameError
    }

    fun signOut() {
        viewModelScope.launch {
            sessionRepository.signOut()
        }
    }

    fun selectAvatar(
        username: String,
        context: Context? = null,
        uri: Uri? = null
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (uri == null || context == null) {
                    if (state.value.avatar != null && state.value.isAvatarCustom) return@withContext

                    _state.update {
                        it.copy(
                            isAvatarCustom = false,
                            avatarError = null,
                            avatar = generateAvatarUseCase(username)
                        )
                    }
                    return@withContext
                }

                val bitmap = try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    inputStream?.use {
                        BitmapFactory.decodeStream(it)
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(
                            avatarError = UiText.DynamicString(
                                e.localizedMessage ?: e.message ?: ""
                            )
                        )
                    }
                    return@withContext
                }

                _state.update {
                    it.copy(
                        isAvatarCustom = true,
                        avatarError = null,
                        avatar = bitmap
                    )
                }
            }
        }
    }

    fun setProfile(
        username: String,
        private: Boolean,
        avatar: Bitmap?,
        navRankSetup: () -> Unit
    ) {
        if (avatar == null) return

        viewModelScope.launch {
            val avatarOutputStream = ByteArrayOutputStream()
            avatar.compress(Bitmap.CompressFormat.JPEG, 90, avatarOutputStream)

            sessionRepository.setUsername(username)
            sessionRepository.setPrivate(private)
            sessionRepository.setAvatar(avatarOutputStream.toByteArray())
            //sessionRepository.setOnboardingStep(OnboardingScreen.ProfileSetup.step + 1)
            sessionRepository.setOnboardingStep(-1)

            val baseAgents = agentRepository.getAllBaseAgentUuids().first()
            for (agentUuid in baseAgents) {
                val agent = agentRepository.getByUuid(agentUuid).first()
                val uid = sessionRepository.getUid()
                if (agent != null && uid != null) userAgentRepository.set(
                    agent.toUserObj(uid)
                )
            }

            val contracts = contractRepository.getAll().first()
            for (contract in contracts) {
                val uid = sessionRepository.getUid()
                if (uid != null) userContractRepository.set(
                    contract.toUserObj(uid)
                )
            }

            navRankSetup()
        }
    }
}
