package net.fionix.minima.model

interface IModelFaculty {
    val facultyCode: String
    val facultyName: String
}

data class ModelFaculty(
        override val facultyCode: String,
        override val facultyName: String,
) : IModelFaculty