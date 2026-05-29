package com.swordfish.lemuroid.app.shared.covers

import android.content.Context
import android.widget.ImageView
import coil.ImageLoader
import coil.disk.DiskCache
import coil.imageLoader
import coil.load
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.swordfish.lemuroid.R
import com.swordfish.lemuroid.common.drawable.TextDrawable
import com.swordfish.lemuroid.lib.library.db.entity.Game
import com.swordfish.lemuroid.lib.preferences.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import kotlin.math.absoluteValue

/**
 * Nothing OS version — fallback covers use Nothing style colors.
 * Synchronized with DotMatrixPlaceholder palette.
 */
object CoverUtils {
    private val PlaceholderColors = listOf(
        0xFFFF3B30.toInt(), // Nothing Red
        0xFFFFC700.toInt(), // Nothing Yellow
        0xFF007AFF.toInt(), // Blue
        0xFF34C759.toInt(), // Green
        0xFF5856D6.toInt(), // Indigo
        0xFFAF52DE.toInt(), // Purple
        0xFFFF9500.toInt()  // Orange
    )

    fun loadCover(
        game: Game,
        imageView: ImageView?,
    ) {
        if (imageView == null) return
        imageView.load(game.coverFrontUrl, imageView.context.imageLoader) {
            val fallbackDrawable = getFallbackDrawable(imageView.context, game)
            fallback(fallbackDrawable)
            error(fallbackDrawable)
        }
    }

    fun buildImageLoader(applicationContext: Context): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .diskCache(
                DiskCache.Builder()
                    .directory(applicationContext.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.20)
                    .build(),
            )
            .memoryCache {
                MemoryCache.Builder(applicationContext)
                    .maxSizePercent(0.20)
                    .build()
            }
            .okHttpClient {
                OkHttpClient.Builder()
                    .addNetworkInterceptor(ThrottleFailedThumbnailsInterceptor)
                    .build()
            }
            .crossfade(true)
            .interceptorDispatcher(Dispatchers.IO)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false)
            .build()
    }

    fun getFallbackDrawable(context: Context, game: Game): TextDrawable {
        val prefs = SharedPreferencesHelper.getSharedPreferences(context)
        val isMonochrome = prefs.getBoolean(context.getString(R.string.pref_key_monochrome_icons), true)
        
        val bgColor = if (isMonochrome) {
            0xFF000000.toInt()
        } else {
            val colorIndex = game.id.absoluteValue % PlaceholderColors.size
            PlaceholderColors[colorIndex]
        }
        
        return TextDrawable(computeTitle(game), bgColor, isMonochrome)
    }

    fun getFallbackRemoteUrl(game: Game): String {
        val color = "000000"
        val title = computeTitle(game)
        return "https://fakeimg.pl/512x512/$color/fff/?font=bebas&text=$title"
    }

    fun computeTitle(game: Game): String {
        val sanitizedName = game.title.replace(Regex("\\(.*\\)"), "")
        return sanitizedName.asSequence()
            .filter { it.isDigit() or it.isUpperCase() or (it == '&') }
            .take(3)
            .joinToString("")
            .ifBlank { game.title.first().toString() }
            .uppercase()
    }
}
