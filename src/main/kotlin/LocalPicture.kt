package github.jun8848

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

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
        logger.info { "Plugin loaded" }
    }
}