package github.jun8848.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Config:AutoSavePluginConfig ("config"){

    @ValueDescription("群开关")
    var enabledGroups:MutableList<Long> by value()

    @ValueDescription("全局路径")
    var picturePath:String by value()

    @ValueDescription("关键词")
    var keyWord : String by value("本地")

    @ValueDescription("图片分类")
    var picClass:MutableList<String> by value()

    @ValueDescription("不同分类的所有图片")
    var picClassify:MutableMap<String,MutableList<String>> by value()

}