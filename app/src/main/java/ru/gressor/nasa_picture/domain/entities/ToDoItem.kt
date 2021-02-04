package ru.gressor.nasa_picture.domain.entities

data class ToDoItem(
    var isDone: Boolean = false,
    var taskText: String = ""
)