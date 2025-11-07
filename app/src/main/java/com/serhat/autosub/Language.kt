package com.serhat.autosub

data class Language(
    val code: String,
    val displayName: String,
    val nativeName: String
)

object Languages {
    val all = listOf(
        Language("af","Afrikaans","Afrikaans"),
        Language("sq","Albanian","Shqip"),
        Language("am","Amharic","አማርኛ"),
        Language("ar","Arabic","العربية"),
        Language("hy","Armenian","Հայերեն"),
        Language("az","Azerbaijani","Azərbaycan"),
        Language("bn","Bengali","বাংলা"),
        Language("en","English","English"),
        Language("hi","Hindi","हिन्दी"),
        Language("ta","Tamil","தமிழ்"),
        Language("te","Telugu","తెలుగు"),
        Language("kn","Kannada","ಕನ್ನಡ"),
        Language("ml","Malayalam","മലയാളം"),
        Language("mr","Marathi","मराठी"),
        Language("gu","Gujarati","ગુજરાતી"),
        Language("ur","Urdu","اردو"),
        Language("zh","Chinese (Simplified)","中文 (简体)"),
        Language("zh-TW","Chinese (Traditional)","中文 (繁體)"),
        Language("fr","French","Français"),
        Language("de","German","Deutsch"),
        Language("es","Spanish","Español"),
        Language("ru","Russian","Русский"),
        Language("ja","Japanese","日本語"),
        Language("ko","Korean","한국어")
    )

    val autoOnly = listOf(

        Language("en","English","English"),
        Language("hi","Hindi","हिन्दी")
    )
}
