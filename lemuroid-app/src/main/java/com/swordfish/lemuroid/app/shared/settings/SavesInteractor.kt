package com.swordfish.lemuroid.app.shared.settings

import android.content.Context
import android.net.Uri
import com.swordfish.lemuroid.lib.storage.DirectoriesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class SavesInteractor(
    private val context: Context,
    private val directoriesManager: DirectoriesManager,
) {
    suspend fun exportSaves(uri: Uri) =
        withContext(Dispatchers.IO) {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                ZipOutputStream(BufferedOutputStream(outputStream)).use { zipOutputStream ->
                    val savesDir = directoriesManager.getSavesDirectory()
                    val statesDir = directoriesManager.getStatesDirectory()

                    zipFolder(savesDir, "saves/", zipOutputStream)
                    zipFolder(statesDir, "states/", zipOutputStream)
                }
            }
        }

    private fun zipFolder(
        folder: File,
        parentPath: String,
        zos: ZipOutputStream,
    ) {
        folder.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                zipFolder(file, "$parentPath${file.name}/", zos)
            } else {
                zos.putNextEntry(ZipEntry("$parentPath${file.name}"))
                file.inputStream().use { it.copyTo(zos) }
                zos.closeEntry()
            }
        }
    }

    suspend fun importSaves(uri: Uri) =
        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                ZipInputStream(BufferedInputStream(inputStream)).use { zis ->
                    var entry = zis.nextEntry
                    val baseDir = context.getExternalFilesDir(null)
                    while (entry != null) {
                        val file = File(baseDir, entry.name)
                        if (entry.isDirectory) {
                            file.mkdirs()
                        } else {
                            file.parentFile?.mkdirs()
                            file.outputStream().use { zis.copyTo(it) }
                        }
                        zis.closeEntry()
                        entry = zis.nextEntry
                    }
                }
            }
        }
}
