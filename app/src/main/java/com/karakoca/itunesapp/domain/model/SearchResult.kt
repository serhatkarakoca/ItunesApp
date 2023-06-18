package com.karakoca.itunesapp.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.karakoca.itunesapp.util.Constants.DEFAULT_DATE_FORMAT
import com.karakoca.itunesapp.util.Constants.RELEASE_DATE_FORMAT
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(tableName = "searchEntity")
@Parcelize
data class SearchResult(
    val trackId: Int?,
    val artistId: Int?,
    val artistName: String?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val currency: String?,
    val releaseDate: String?,
    val trackName: String?,
    val trackPrice: Double?,
    val trackTimeMillis: Int?,
): Parcelable {

    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun getFormattedDate(): String? {
        releaseDate ?: return null
        return try {
            val formatter = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
            val releaseFormatter = SimpleDateFormat(RELEASE_DATE_FORMAT, Locale.getDefault())
            val date = formatter.parse(releaseDate)
            date?.let { releaseFormatter.format(it) }

        } catch (e: Exception) {
            null
        }
    }
}
