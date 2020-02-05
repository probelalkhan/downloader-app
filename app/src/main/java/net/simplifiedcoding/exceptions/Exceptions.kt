package net.simplifiedcoding.exceptions

import java.io.IOException

class NoInternetException(_message: String) : IOException(_message)
class ApiException(_message: String) : IOException(_message)
