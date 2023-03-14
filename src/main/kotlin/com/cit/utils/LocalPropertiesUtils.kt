package com.cit.utils

import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class LocalPropertiesUtils {
    companion object {
        private var PATH_LOCAL_PROPERTY = "local.properties"

        fun parseArgsLocalProperties(args: Array<String>){
            PATH_LOCAL_PROPERTY = if(args.isNotEmpty()) args[0] else PATH_LOCAL_PROPERTY
        }

        fun getLocalProperty(key: String): String {
            val properties = java.util.Properties()
            val localProperties = File(PATH_LOCAL_PROPERTY)
            if (localProperties.isFile) {
                InputStreamReader(FileInputStream(localProperties), StandardCharsets.UTF_8).use { reader ->
                    properties.load(reader)
                }
            } else error("File from not found")

            return properties.getProperty(key)
        }

    }
}