package com.cit.database.controllers

import com.cit.coursesController
import com.cit.database.tables.*
import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.models.ModelAnswer.Companion.asError
import com.cit.usersController
import com.cit.utils.DateTimeUtils
import com.cit.utils.respond.uploadAudio
import com.cit.utils.respond.uploadImage
import io.ktor.client.request.forms.*
import io.ktor.http.*

class ProfileController {
    suspend fun respondProfileCourses(idUser: Int): ModelAnswer<List<ModelCourseShort>> {
        val coursesUser = coursesController.getUserCourses(idUser).map { it.toModelCourseShort() }
        return coursesUser.asAnswer()
    }

    suspend fun respondProfileCourse(idCourse: Int, idUser: Int): ModelAnswer<SoldModelCourse> {
        val course = coursesController.getSoldCourse(idCourse, idUser)
        if (course == null || !coursesController.checkBuyCourseUser(idUser, idCourse))
            return "Курс пользователя не найден".asError(HttpStatusCode.NotFound)
        return course.asAnswer()
    }

    suspend fun respondProfileTags(idUser: Int): ModelAnswer<List<Int>> {
        val tagsData = coursesController.getUserCourses(idUser).map { it.tags }
        if (tagsData.isEmpty())
            return emptyList<Int>().asAnswer()
        return tagsData.reduce { acc, ints -> acc + ints }.asAnswer()
    }

    suspend fun respondProfile(idUser: Int): ModelAnswer<PersonData>{
        val user = usersController.getUser(idUser) ?: return "Пользователь не найден".asError(HttpStatusCode.NotFound)
        return user.toPersonData().asAnswer()
    }

    suspend fun uploadAvatarProfile(idUser: Int, format: String, binaryData: ByteArray): ModelAnswer<String>{
        val filename = DateTimeUtils.getDateTimeFilename() + ".$format"
        val newFile = uploadImage(filename, binaryData)
        val result = usersController.updateAvatar(filename, idUser) && newFile.exists()
        return if (result) filename.asAnswer() else "Ошибка при сохранении, попробуйте позже".asError()
    }

    suspend fun respondPatchProfile(idUser: Int, body: ReceivePatchUserBody): ModelAnswer<PersonData>{
        if (body.email != null && usersController.checkExistEmail(body.email))
            return "Такая почта уже используется".asError()

        val avatar = if (body.avatar != null && (body.format != null || body.avatar.isBlank())){
            if (body.avatar.isBlank())
                ""
            else {
                val bytes = body.getByteArrayAvatar()!!
                val imageName = DateTimeUtils.getDateTimeFilename() + ".${body.format}"
                val newFile = uploadImage(imageName, bytes)
                if (!newFile.exists())
                    return "Аватарка не сохраненна".asError()
                imageName
            }
        }else { null }

        val user = usersController.patchUser(idUser, body.toPatchUserBody(avatar))
        return user?.toPersonData()?.asAnswer() ?: "Ошибка, попробуйте еще раз".asError()
    }
}