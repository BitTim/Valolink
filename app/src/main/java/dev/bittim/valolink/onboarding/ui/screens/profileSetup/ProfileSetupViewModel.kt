/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupViewModel.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 20:51
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
import dev.bittim.valolink.core.domain.usecase.profile.GenerateAvatarUseCase
import dev.bittim.valolink.core.domain.util.Result
import dev.bittim.valolink.core.ui.util.UiText
import dev.bittim.valolink.onboarding.ui.screens.OnboardingScreen
import dev.bittim.valolink.user.data.repository.SessionRepository
import dev.bittim.valolink.user.data.repository.data.UserAgentRepository
import dev.bittim.valolink.user.data.repository.data.UserDataRepository
import dev.bittim.valolink.user.domain.error.UsernameError
import dev.bittim.valolink.user.domain.usecase.validator.ValidateUsernameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val agentRepository: AgentRepository,
    private val userAgentRepository: UserAgentRepository,
    private val sessionRepository: SessionRepository,
    private val userDataRepository: UserDataRepository,
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val generateAvatarUseCase: GenerateAvatarUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileSetupState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            sessionRepository.isAuthenticated().stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { isAuthenticated ->
                    _state.update { it.copy(isAuthenticated = isAuthenticated) }
                }
        }

        viewModelScope.launch {
            sessionRepository.isLocal().stateIn(viewModelScope, WhileSubscribed(5000), null)
                .collectLatest { isLocal ->
                    _state.update { it.copy(isLocal = isLocal) }
                }
        }

        viewModelScope.launch {
            val userData = userDataRepository.getWithCurrentUser().firstOrNull()
            if (userData == null) {
                selectAvatar("")
                return@launch
            }

            val avatarBytes = userDataRepository.downloadAvatarWithCurrentUser()
            val avatar = if (avatarBytes == null) generateAvatarUseCase(userData.username) else {
                BitmapFactory.decodeByteArray(avatarBytes, 0, avatarBytes.size)
            }

            _state.update {
                it.copy(
                    username = userData.username,
                    private = userData.isPrivate,
                    avatar = avatar
                )
            }
        }
    }

    fun onUsernameChanged(value: String) {
        _state.update { it.copy(username = value) }
        validateUsername(value)
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

    fun resetAvatar(
        username: String,
        context: Context? = null,
    ) {
        _state.update {
            it.copy(
                isAvatarCustom = false,
                avatarError = null,
            )
        }

        selectAvatar(username, context, null)
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
    ) {
        if (avatar == null) return

        viewModelScope.launch {
            val avatarOutputStream = ByteArrayOutputStream()
            avatar.compress(Bitmap.CompressFormat.JPEG, 90, avatarOutputStream)

            val baseAgents = agentRepository.getAllBaseAgentUuids().firstOrNull()
            if (baseAgents != null) {
                for (agentUuid in baseAgents) {
                    val agent = agentRepository.getByUuid(agentUuid).firstOrNull()
                    val uid = sessionRepository.getUid().firstOrNull()
                    if (agent != null && uid != null) userAgentRepository.set(
                        agent.toUserObj(uid)
                    )
                }
            }

            val userData = userDataRepository.getWithCurrentUser().firstOrNull()
            if (userData == null) return@launch

            val avatarLocation =
                userDataRepository.uploadAvatarWithCurrentUser(avatarOutputStream.toByteArray())
            userDataRepository.setWithCurrentUser(
                userData.copy(
                    username = username,
                    isPrivate = private,
                    onboardingStep = OnboardingScreen.ProfileSetup.step - OnboardingScreen.STEP_OFFSET + 1,
                    avatar = avatarLocation
                )
            )
        }
    }
}
