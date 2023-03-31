package github.jun8848.util

import java.io.File
import java.util.*

class FileUtil {
    companion object {
        /**
         * 遍历指定路径所有文件夹
         */
        fun getAllFolders(path: String): List<String> {
            val list = mutableListOf<String>()
            val dir = File(path)
            if (dir.exists() && dir.isDirectory) {
                val fileList = dir.listFiles()
                if (fileList != null && fileList.isNotEmpty()) {
                    for (file in fileList) {
                        if (file.isDirectory) {
                            list.add(file.path)
                            list.addAll(getAllFolders(file.path))
                        }
                    }
                }
            }
            return list
        }

        /**
         * 遍历指定文件夹里所有图片
         */
        fun getAllImages(path: String): List<String> {
            val list = mutableListOf<String>()
            val dir = File(path)
            if (dir.exists() && dir.isDirectory) {
                val fileList = dir.listFiles()
                if (fileList != null && fileList.isNotEmpty()) {
                    for (file in fileList) {
                        if (file.isDirectory) {
                            list.addAll(getAllImages(file.path))
                        } else if (isImageFile(file)) {
                            list.add(file.path.removePrefix(path+"\\"))
                        }
                    }
                }
            }
            return list
        }

        /**
         * 判断文件是否为图片文件
         */
        private fun isImageFile(file: File): Boolean {
            val name = file.name.lowercase(Locale.getDefault())
            return name.endsWith(".jpg") || name.endsWith(".jpeg")
                    || name.endsWith(".png") || name.endsWith(".bmp")
                    || name.endsWith(".gif")
        }
    }
}