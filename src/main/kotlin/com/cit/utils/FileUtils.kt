package com.cit.utils

import java.io.File

fun getBytesAudio(audioName: String): ByteArray?{
    return getBytesFile(LocalPropertiesUtils.getLocalProperty("audio_path") + "/$audioName")
}

fun getBytesFile(pathFile: String): ByteArray?{
    val file = File(pathFile)
    return if(file.exists()) file.readBytes() else null
}