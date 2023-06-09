package github.jun8848

import github.jun8848.config.Config
import github.jun8848.util.GlobalMethod.getRandomPic
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildForwardMessage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.io.File

object LocalPicture : KotlinPlugin(
    JvmPluginDescription(
        id = "github.jun8848.localpicture",
        name = "LocalPicture",
        version = "0.0.1",
    ) {
        author("mubai")
    }
) {
    override fun onEnable() {
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

            if (Config.enabledGroups.indexOf(group.id) != -1) {
                if (msg == "关闭setu" && sender.permission.isOperator()) {
                    Config.enabledGroups.remove(group.id)
                    subject.sendMessage("关闭setu成功")
                    return@subscribeAlways
                }
            }

            when {
                msg.startsWith(Config.keyWord) -> {
                    val m = msg.removePrefix(Config.keyWord).trim()
                    val regex = "\\d+$".toRegex()
                    val matchResult = regex.find(m)
                    val picList = if (matchResult != null) {
                        val index = matchResult.range.first
                        val head = m.substring(0, index).trim()
                        val tail = m.substring(index)
                        // 图片数量
                        val picNum = if (tail.toInt() >= Config.picMaxNum) Config.picMaxNum else tail.toInt()
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
                    return@subscribeAlways
                }

                msg.startsWith("设置全局路径") -> {
                    val picPath = msg.removePrefix("设置全局路径").trim()
                    Config.picturePath = picPath
                    subject.sendMessage("设置路径为:${picPath}成功")
                    return@subscribeAlways
                }

                msg.startsWith("设置触发关键词") -> {
                    val word = msg.removePrefix("设置触发关键词").trim()
                    Config.keyWord = word
                    subject.sendMessage("设置触发关键字为:${word}成功")
                    return@subscribeAlways
                }

                msg.startsWith("刷新本地setu") && sender.permission.isOperator() -> {
                    subject.sendMessage("刷新完成共有${Config.initGallery()}张图片")
                    return@subscribeAlways
                }
            }
        }
    }
}