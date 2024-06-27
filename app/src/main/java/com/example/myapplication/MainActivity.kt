package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.regex.Pattern

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        test()
        // 显示通知
//        showNotification(this)
    }
}

fun filterSensitiveData(input: String, keysToFilter: List<String>): String {
    // 定义要替换的敏感字段列表
    val sensitiveKeys = listOf("description", "nickname", "operation_name", "text", "title", "sender_name", "mobile", "username", "content", "next_title", "help_title", "next_content", "help_content")

    // 构建正则表达式模式，匹配各种形式的键值对
    val jsonPattern = sensitiveKeys.joinToString("|") {
        "\"$it\"\\s*:\\s*\"((?:\\\\\"|[^\"])*)\"|\"$it\"\\s*:\\s*(\\d+|true|false|null)"
    }.toRegex()
    val keyValuePattern = sensitiveKeys.joinToString("|") {
        "$it\\s*=\\s*\"((?:\\\\\"|[^\"])*)\"|$it\\s*=\\s*([^,\\s]+)"
    }.toRegex()
    val escapedJsonPattern = sensitiveKeys.joinToString("|") {
        "\\\\\"$it\\\\\"\\s*:\\s*\\\\\"((?:\\\\\\\\\"|[^\"])*)\\\\\"|\\\\\"$it\\\\\"\\s*:\\s*(\\d+|true|false|null)"
    }.toRegex()

    // 替换敏感信息
    var result = input
    result = jsonPattern.replace(result) { matchResult ->
        val keyValue = matchResult.value
        val key = keyValue.substringBefore(":").trim()
        "$key: \"****\""
    }
    result = keyValuePattern.replace(result) { matchResult ->
        val keyValue = matchResult.value
        val key = keyValue.substringBefore("=").trim()
        "$key=****"
    }
    result = escapedJsonPattern.replace(result) { matchResult ->
        val keyValue = matchResult.value
        val key = keyValue.substringBefore(":").trim()
        "$key: \\\"****\\\""
    }

    return result
}


fun test(){
// val str = "{\"language\":\"chinese\",\"phoneNodeId\":\"1116e5c1bd\",\"main\":\"{\\\"mobile\\\":\\\"18069800000\\\",\\\"sub_users\\\":[{\\\"bind_status\\\":2,\\\"network_id\\\":\\\"60ecaffde770085b\\\",\\\"isLocalBind\\\":false,\\\"isLocalCompleted\\\":false,\\\"phd\\\":{\\\"co_brand\\\":\\\"default\\\",\\\"color\\\":0,\\\"name\\\":\\\"ME0452-11\\\",\\\"network_mode\\\":\\\"GPAN\\\",\\\"node_id\\\":\\\"a44f6f9394\\\",\\\"online\\\":true,\\\"sn\\\":\\\"E1100200174219000452\\\",\\\"type\\\":0,\\\"url\\\":\\\"https:\\/\\/etsme-product.oss-cn-hangzhou.aliyuncs.com\\/icons%2Fstandard_black_gpan.png?Expires\\\\u003d1719043585\\\\u0026OSSAccessKeyId\\\\u003dLTAI5tAdg8xrJVbj6aCetgTG\\\\u0026Signature\\\\u003dHuAaX1dMpAS2IimWvzJHVWWFHzw%3D\\\"},\\\"phd_master_network_id\\\":\\\"\\\",\\\"phd_master_nickname\\\":\\\"Me0000\\\",\\\"phd_master_sub_uid\\\":0,\\\"pin_init\\\":1,\\\"pin_open\\\":0,\\\"pin_question_set\\\":0,\\\"role_type\\\":1,\\\"sub_user_id\\\":17183,\\\"sub_user_id_str\\\":\\\"17183\\\"}],\\\"token\\\":\\\"MTI5Mzg6bW9iaWxlOjgxMTEwM2U3LTJjMzgtNDM3NC05MWRkLWQ5OGJmMGZjNDZkMw\\\\u003d\\\\u003d\\\",\\\"user_id\\\":12938}\",\"subUsr\":\"{\\\"env\\\":\\\"testing\\\",\\\"main_uid\\\":\\\"12938\\\",\\\"node_id\\\":\\\"a44f6f9394\\\",\\\"owner_global_net_id\\\":\\\"\\\",\\\"network_mode\\\":\\\"GPAN\\\",\\\"sn\\\":\\\"E1100200174219000452\\\",\\\"token\\\":\\\"dc07fe24-3fe8-403e-ac58-0a72f453e695\\\",\\\"user_info\\\":{\\\"avatar_key\\\":\\\"c4c80e0033a34b9e08a0d1e8fa85431e\\\",\\\"avatar_url\\\":\\\"\\/static\\/usermgmt\\/avatars\\/c4c80e0033a34b9e08a0d1e8fa85431e\\\",\\\"global_net_id\\\":\\\"60ecaffde770085b\\\",\\\"is_sync_to_cloud\\\":true,\\\"local_net_id\\\":\\\"50ecaffde70304c5\\\",\\\"main_uid\\\":\\\"12938\\\",\\\"mobile\\\":\\\"18069800000\\\",\\\"nickname\\\":\\\"浮生\\\",\\\"pin_init\\\":true,\\\"security_question_set\\\":false,\\\"pod_id\\\":\\\"e4671e1d443a1718852221367747132\\\",\\\"role\\\":1,\\\"uid\\\":\\\"17183\\\",\\\"username\\\":\\\"18069800000\\\"}}\"}."
// println(str.toSafeString())
    val inputString = """
        {
            "files": [{
                "file_id": "17d88f3f141f9bf23306609911873651",
                "name": "1693195132344602.png",
                "description": "An image of \"something\"",
                "mobile": "19800000725"
            }],
            "age": 30,
            "height": "180cm",
            "nickname": "john_doe",
            "operation_name": "upload",
            "text": "\"example\"",
            "mobile": "19800000725",
            "username": "johndoe",
            "content": "some content",
            "next_title": "next title",
            "help_title": "help title",
            "next_content": "next content",
            "help_content": "help content",
            mobile=19800000725,
            \"mobile\":\"19800000725\"
        }
    """.trimIndent()

    val keysToFilter = listOf("password", "apiKey", "address", "profile")

    val filteredString = filterSensitiveData(inputString, keysToFilter)
    println(filteredString)

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}