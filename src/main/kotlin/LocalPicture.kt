package github.jun8848

import github.jun8848.config.Config
import github.jun8848.util.FileUtil
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildForwardMessage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.info
import java.io.File
import kotlin.random.Random

object LocalPicture : KotlinPlugin(
    JvmPluginDescription(
        id = "github.jun8848.localpicture",
        name = "LocalPicture",
        version = "0.0.1",
    ) {
        author("mubai")
    }
) {
    private fun getRandomPic(num: Int, path: String = "null"): MutableList<String> {
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


    override fun onEnable() {
        logger.info { "Plugin loaded" }
        Config.reload()

        val eventChannel = globalEventChannel().parentScope(this)
        eventChannel.subscribeAlways<GroupMessageEvent> {
            val msg = message.contentToString()
            if (Config.enabledGroups.indexOf(group.id) == -1) {
                if (msg == "开启setu" && sender.permission.isOperator()) {
                    Config.enabledGroups.add(group.id)
                    subject.sendMessage("开启setu成功")
                }
                return@subscribeAlways
            }
            when {
                msg.startsWith("本地") -> {
                    val m = msg.removePrefix("本地").trim()
                    val regex = "\\d+$".toRegex()
                    val matchResult = regex.find(m)

                    val picList = if (matchResult != null) {
                        val index = matchResult.range.first
                        val head = m.substring(0, index).trim()
                        val tail = m.substring(index)
                        val picNum = if (tail.toInt()>=20) 20 else tail.toInt()
                        if (head == "setu")
                            getRandomPic(picNum)
                        else
                            getRandomPic(picNum, head)
                    } else {
                        if (m == "setu")
                            getRandomPic(1)
                        else
                            getRandomPic(1, m)
                    }

                    if (picList.isNotEmpty()) {
                        subject.sendMessage(
                            buildForwardMessage {
                                for (pic in picList) {
                                    val ex = File(pic).toExternalResource()
                                    val img = group.uploadImage(ex)
                                    ex.close()
                                    //                                        分割路径只取文件名
                                    sender.id named senderName says PlainText(pic.split("\\").last()) + "\n" + img
                                }
                            }
                        )
                    }

                }

                msg.startsWith("设置全局路径") -> {
                    val picPath = msg.removePrefix("设置全局路径").trim()
                    Config.picturePath = picPath
                    subject.sendMessage("设置路径为:${picPath}成功")
                }

                msg.startsWith("设置触发关键词") -> {
                    val trigger = msg.removePrefix("设置触发关键词").trim()
                    Config.trigger = trigger
                    subject.sendMessage("设置触发关键字为:${trigger}成功")
                }

                msg.startsWith("刷新本地setu") && sender.permission.isOperator() -> {
                    Config.picClass.clear()
                    Config.picClassify.clear()
                    // 拥有的分类文件夹
                    val filePaths = FileUtil.getAllFolders(Config.picturePath)

                    for (path in filePaths)
                        Config.picClass.add(path.removePrefix(Config.picturePath + "\\"))

                    var num = 0
                    for (pic in Config.picClass) {
                        Config.picClassify[pic] = FileUtil.getAllImages(Config.picturePath + "\\" + pic).toMutableList()
                        num += Config.picClassify[pic]!!.count()
                    }
                    subject.sendMessage("刷新完成共有${num}张图片")
                }
            }
        }
    }
}