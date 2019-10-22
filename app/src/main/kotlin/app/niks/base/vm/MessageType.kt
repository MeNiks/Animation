package app.niks.base.vm

sealed class MessageType(name: String) {

    class ShowToast(val message: String, val name: String = "ShowToast") : MessageType(name)

    // User Data
    class Login
}
