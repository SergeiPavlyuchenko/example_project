package example.code.some_project.domain

class DataMissedException(message: String = "Some data missed") : RuntimeException(message)
class NoInternetException : Exception()
class NeedUpdateTokensException : Exception()
class TwentyFourHoursBlockedException : Exception()
class IncorrectNumberException : Exception()
class FiveMinutesBlockedException : Exception()
class NotEnoughMoneyException : Exception()
class PatchTokensException : Exception()
