package example.code.some_project.domain.repo.device

@Deprecated ("view logic") // ToDo remove
interface DeviceManager {

    fun getScreenWidth(): Int

    fun getScreenHeight(): Int

    fun getAppBarHeight(): Int

    fun getStatusBarHeight(): Int

    fun getComplexBarsHeight(): Int
}
