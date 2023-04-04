package com.cit.utils

import java.io.File

fun getBytesAudio(audioName: String): ByteArray?{
    return getBytesFile(LocalPropertiesUtils.getLocalProperty("audio_path") + "/$audioName")
}

fun getBytesFile(pathFile: String): ByteArray?{
    val file = File(pathFile)
    return if(file.exists()) file.readBytes() else null
}


fun removeImage(imageName: String): Boolean{
    val imagePath = LocalPropertiesUtils.getLocalProperty("images_path") + "/$imageName"
    return removeFile(imagePath)
}

fun removeFile(filePath: String): Boolean{
    return File(filePath).delete()
}

fun checkExistImage(imageName: String): Boolean{
    val imagePath = LocalPropertiesUtils.getLocalProperty("images_path") + "/$imageName"
    return checkExistFile(imagePath)
}

fun checkExistFile(filePath: String): Boolean{
    return File(filePath).exists()
}