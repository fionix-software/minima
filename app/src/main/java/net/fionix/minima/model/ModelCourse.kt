package net.fionix.minima.model

interface IModelCourse : IModelFaculty {
    val courseCode: String
    val courseGroup: String
    val courseName: String
}

data class ModelCourse(
        override val courseCode: String,
        override val courseName: String,
        override val courseGroup: String,
        override val facultyCode: String,
        override val facultyName: String,
) : IModelCourse {}