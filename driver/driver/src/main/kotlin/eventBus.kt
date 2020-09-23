package com.eventbus

sealed class FcmEvent {
    object AlreadyAccepted: FcmEvent()
}