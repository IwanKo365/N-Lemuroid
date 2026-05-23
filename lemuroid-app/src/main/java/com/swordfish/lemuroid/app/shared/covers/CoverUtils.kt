package com.swordfish.lemuroid.app.shared.covers

import android.content.Context
import android.widget.ImageView
import coil.ImageLoader
import coil.disk.DiskCache
import coil.imageLoader
import coil.load
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.swordfish.lemuroid.common.drawable.TextDrawable
import com.swordfish.lemuroid.lib.library.db.entity.Game
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient

/**
 * Nothing OS version — fallback covers use Nothing red (#FF3B30) always.
 * The random rainbow color is replaced with a fixed Nothing red.
 *
 * Replaces:
 * lemuroid-app/src/main/java/com/swordfish/lemuroid/app/shared/covers/CoverUtils.kt
 */
object CoverUtils {
    // Nothing red — consistent with the rest of the UI
    private const val NOTHING_RED = 0xFF0D0D0D.toInt()

    fun loadCover(
        game: Game,
        imageView: ImageView?,
    ) {
        if (imageView == null) return
        imageView.load(game.coverFrontUrl, imageView.context.imageLoader) {
            val fallbackDrawable = getFallbackDrawable(game)
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

    fun getFallbackDrawable(game: Game) = TextDrawable(computeTitle(game), NOTHING_RED)

    fun getFallbackRemoteUrl(game: Game): String {
        val color = "FF3B30" // Always Nothing red in remote placeholder too
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
            .capitalize()
    }
}
