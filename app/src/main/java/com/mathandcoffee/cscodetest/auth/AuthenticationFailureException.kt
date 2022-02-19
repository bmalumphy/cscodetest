package com.mathandcoffee.cscodetest.auth

import java.lang.Exception

class AuthenticationFailureException(val errorCode: Int): Exception()