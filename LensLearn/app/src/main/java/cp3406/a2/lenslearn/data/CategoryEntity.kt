package cp3406.a2.lenslearn.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Table to hold category information
@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val filename: String,
    val definition: String,
    val detailedInformation: String
)

// Table to hold category images
@Entity(
    tableName = "image",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,
    val filename: String,
)

// Table to hold tasks for the photography do phase
@Entity(
    tableName = "task",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,
    val description: String,
    val filename: String
)

// Table to hold user photographs from do phase
@Entity(
    tableName = "user_image",
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskId: Int,
    val path: String
)

// Table to hold user progress
@Entity(tableName = "progress",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )])
data class UserProgress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,
    val hasShared: Boolean,
    val hasCompletedTask: Boolean,
    val progressPercentage: Int,
)

