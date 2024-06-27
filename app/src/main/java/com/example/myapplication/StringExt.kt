package com.example.myapplication


fun Any.toSafeString(): String {
    runCatching {
//        if(BuildConfig.DEBUG){
//            return this.toString()
//        } else {
            return filterSensitiveData(this.toString())
//        }
    }.onFailure {
        return this.toString()
    }

    return this.toString()
}

fun filterSensitiveData(input: String): String {
    // 定义要替换的敏感字段列表
    val sensitiveKeys = listOf("nickname", "operation_name", "text", "report_text", "sender_name", "mobile", "username")

    // 构建正则表达式模式，匹配 "key":"value" 和 key=value 格式
    val jsonPattern = sensitiveKeys.joinToString("|") {"\"$it\"\\s*:\\s*(\"[^\"]*\"|\\d+|[^\"]*)?"}.toRegex()
    val keyValuePattern = sensitiveKeys.joinToString("|") { "$it\\s*=\\s*(\"[^\"]*\"|[0-9\\-]*)"}.toRegex()

    // 替换敏感信息
    var result = input.replace("\\\"", "\"")
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

    return result
}