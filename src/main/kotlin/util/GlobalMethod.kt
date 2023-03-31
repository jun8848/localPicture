package github.jun8848.util

import github.jun8848.config.Config
import kotlin.random.Random

object GlobalMethod {
    fun getRandomPic(num: Int, path: String = "null"): MutableList<String> {
        // 返回的列表
        val returnList = mutableListOf<String>()
        // 图片分类列表
        val picPathList = Config.picClass
        // 图片列表
        val picList = Config.picClassify

        // 判断是否指定路径
        if (path == "null") {
            // 根据传入的值获取指定数量的图片
            for (i in 0 until num) {
                // 随机获取一个分类
                val picPathIndex = Random.nextInt(picPathList.count())
                // 随机获取该分类中的一张图片
                val picIndex = Random.nextInt(picList[picPathList[picPathIndex]]!!.count())
                // 将该图片的路径拼接  并加入返回列表中
                returnList.add(Config.picturePath + "\\" + picPathList[picPathIndex] + "\\" + picList[picPathList[picPathIndex]]!![picIndex])
            }
            return returnList
        } else {
            // 指定路径
            if (picList.keys.indexOf(path) != -1) {
                for (i in 0 until num) {
                    val picIndex = Random.nextInt(picList[path]!!.count())
                    returnList.add(Config.picturePath + "\\" + path + "\\" + picList[path]!![picIndex])
                }
                return returnList
            }
            return returnList
        }
    }
}