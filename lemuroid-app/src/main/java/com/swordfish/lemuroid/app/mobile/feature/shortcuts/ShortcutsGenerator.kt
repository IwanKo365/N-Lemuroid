package com.swordfish.lemuroid.app.mobile.feature.shortcuts

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.os.Build
import com.swordfish.lemuroid.app.shared.covers.CoverUtils
import com.swordfish.lemuroid.app.shared.deeplink.DeepLink
import com.swordfish.lemuroid.common.bitmap.cropToSquare
import com.swordfish.lemuroid.common.bitmap.toBitmap
import com.swordfish.lemuroid.lib.library.db.entity.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.InputStream

class ShortcutsGenerator(
    private val appContext: Context,
    retrofit: Retrofit,
) {
    private val thumbnailsApi = retrofit.create(ThumbnailsApi::class.java)

    suspend fun pinShortcutForGame(game: Game) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val shortcutManager = appContext.getSystemService(ShortcutManager::class.java)!!
        val originalBitmap = retrieveBitmap(game)
        val cartridgeBitmap = wrapInNothingCartridge(originalBitmap)

        val shortcutInfo =
            ShortcutInfo.Builder(appContext, "game_${game.id}")
                .setShortLabel(game.title)
                .setLongLabel(game.title)
                .setIntent(DeepLink.launchIntentForGame(appContext, game))
                .setIcon(Icon.createWithBitmap(cartridgeBitmap))
                .build()

        shortcutManager.requestPinShortcut(shortcutInfo, null)
    }

    private fun wrapInNothingCartridge(content: Bitmap): Bitmap {
        val size = getDesiredIconSize()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // 1. Draw Background (Black)
        paint.color = Color.BLACK
        canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), paint)

        // 2. Draw Cartridge Frame (White border)
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = size / 32f
        canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), paint)

        // 3. Draw inner padding area (dark grey)
        paint.style = Paint.Style.FILL
        paint.color = Color.argb(255, 20, 20, 20)
        val padding = size / 10f
        canvas.drawRect(padding, padding, size - padding, size - padding, paint)

        // 4. Draw content (cover or placeholder) inside the padded area
        val destRect = Rect(padding.toInt(), padding.toInt(), (size - padding).toInt(), (size - padding).toInt())
        canvas.drawBitmap(content, null, destRect, paint)
        
        // 5. Re-draw inner border for crispness
        paint.style = Paint.Style.STROKE
        paint.color = Color.WHITE
        paint.strokeWidth = 1f
        canvas.drawRect(destRect, paint)

        return bitmap
    }

    private suspend fun retrieveBitmap(game: Game): Bitmap =
        withContext(Dispatchers.IO) {
            val result =
                runCatching {
                    val response = thumbnailsApi.downloadThumbnail(game.coverFrontUrl!!)
                    BitmapFactory.decodeStream(response.body()).cropToSquare()
                }
            result.getOrElse { retrieveFallbackBitmap(game) }
        }

    private fun retrieveFallbackBitmap(game: Game): Bitmap {
        val desiredIconSize = getDesiredIconSize()
        return CoverUtils.getFallbackDrawable(appContext, game).toBitmap(desiredIconSize, desiredIconSize)
    }

    private fun getDesiredIconSize(): Int {
        val am = appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        return am?.launcherLargeIconSize ?: 256
    }

    fun supportShortcuts(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return false
        }

        val shortcutManager = appContext.getSystemService(ShortcutManager::class.java)!!
        return shortcutManager.isRequestPinShortcutSupported
    }

    interface ThumbnailsApi {
        @GET
        @Streaming
        suspend fun downloadThumbnail(
            @Url url: String,
        ): Response<InputStream>
    }
}
