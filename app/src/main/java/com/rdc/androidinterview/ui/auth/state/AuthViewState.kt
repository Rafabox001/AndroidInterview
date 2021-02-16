package com.rdc.androidinterview.ui.auth.state

import com.rdc.androidinterview.models.AuthToken


data class AuthViewState(
    var loginFields: LoginFields? = LoginFields(),
    var authToken: AuthToken? = null
)

data class LoginFields(
    var login_username: String? = null,
    var login_password: String? = null
){
    class LoginError {

        companion object{

            fun mustFillAllFields(): String{
                return "You can't login without an email and password."
            }

            fun none():String{
                return "None"
            }

        }
    }
    fun isValidForLogin(): String{

        if(login_username.isNullOrEmpty()
            || login_password.isNullOrEmpty()){

            return LoginError.mustFillAllFields()
        }
        return LoginError.none()
    }

    override fun toString(): String {
        return "LoginState(email=$login_username, password=$login_password)"
    }
}


