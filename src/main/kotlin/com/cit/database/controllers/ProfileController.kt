package com.cit.database.controllers

import com.cit.coursesController
import com.cit.database.tables.*
import com.cit.models.ModelAnswer
import com.cit.models.ModelAnswer.Companion.asAnswer
import com.cit.models.ModelAnswer.Companion.asError
import com.cit.usersController
import com.cit.utils.DateTimeUtils
import com.cit.utils.LocalPropertiesUtils.Companion.getLocalProperty
import com.cit.utils.ValidationUtils.Companion.isValidEmail
import io.ktor.http.*
import java.io.File
import java.time.LocalDate

class ProfileController {
    suspend fun respondProfileCourses(idUser: Int): ModelAnswer<List<ModelCourseShort>> {
        val coursesUser = coursesController.getUserCourses(idUser).map { it.toModelCourseShort() }
        return coursesUser.asAnswer()
    }

    suspend fun respondProfileCourse(idCourse: Int, idUser: Int): ModelAnswer<ModelCourse> {
        val course = coursesController.getCourse(idCourse, idUser)
        if (course == null || !coursesController.checkBuyCourseUser(idUser, idCourse))
            return "Курс пользователя не найден".asError(HttpStatusCode.NotFound)
        return course.asAnswer()
    }

    suspend fun respondProfileTags(idUser: Int): ModelAnswer<List<Int>> {
        val tags = coursesController.getUserCourses(idUser).map { it.tags }.reduce { acc, ints -> acc + ints }
        return tags.asAnswer()
    }

    suspend fun respondProfile(idUser: Int): ModelAnswer<PersonData>{
        val user = usersController.getUser(idUser) ?: return "Пользователь не найден".asError(HttpStatusCode.NotFound)
        return user.toPersonData().asAnswer()
    }

    suspend fun uploadAvatarProfile(idUser: Int, format: String, binaryData: ByteArray): ModelAnswer<Boolean>{
        val imageDirectory = getLocalProperty("images_path")
        val filename = DateTimeUtils.getDateTimeFilename() + ".$format"
        val newFile = File("$imageDirectory/$filename")
        newFile.writeBytes(binaryData)
        val result = usersController.updateAvatar(filename, idUser) && newFile.exists()
        return if (result) true.asAnswer() else "Ошибка при сохранении, попробуйте позже".asError(HttpStatusCode.Conflict)
    }

    suspend fun respondPatchProfile(idUser: Int, pathBody: PatchUserBody): ModelAnswer<PersonData>{
        if (pathBody.email != null) {
            if (usersController.checkExistEmail(pathBody.email))
                return "Такая почта уже используется".asError()
            if (!pathBody.email.isValidEmail())
                return "Почта не корректна".asError()
        }
        if (pathBody.dateBirthDay != null && pathBody.dateBirthDay.isAfter(LocalDate.now())){
            return "День рождение не может быть в будущем".asError()
        }

        val user = usersController.patchUser(idUser, pathBody)
        return user?.toPersonData()?.asAnswer() ?: "Ошибка, попробуйте еще раз".asError()
    }
}