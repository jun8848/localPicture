package github.jun8848.config

import github.jun8848.util.FileUtil
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {

    @ValueDescription("群开关")
    var enabledGroups: MutableList<Long> by value()

    @ValueDescription("全局路径")
    var picturePath: String by value()

    @ValueDescription("关键词")
    var keyWord: String by value("本地")

    @ValueDescription("图片分类")
    var picClass: MutableList<String> by value()

    @ValueDescription("不同分类的所有图片")
    var picClassify: MutableMap<String, MutableList<String>> by value()

    @ValueDescription("图片最大数量")
    var picMaxNum: Int by value(20)

    // 初始化图库
    fun initGallery(): Int {
        picClass.clear()
        picClassify.clear()
        // 拥有的分类文件夹
        val filePaths = FileUtil.getAllFolders(picturePath)
        for (path in filePaths)
            picClass.add(path.removePrefix(picturePath + "\\"))
        var num = 0
        for (pic in picClass) {
            picClassify[pic] = FileUtil.getAllImages(picturePath + "\\" + pic).toMutableList()
            num += picClassify[pic]!!.count()
        }
        return num
    }
}