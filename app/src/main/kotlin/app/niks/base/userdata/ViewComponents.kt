package app.niks.base.userdata

sealed class ViewComponents(name: String) {

    // User Data

    // Login
    class Login {
        data class Login(val name: String = "Login") : ViewComponents(name)
        data class Forgot(val name: String = "Forgot") : ViewComponents(name)
    }
}
