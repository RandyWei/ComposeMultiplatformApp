package service

/**
 * 获取设备相关数据的service
 */
expect object DeviceService {
    /**
     * 获取底部安全区域的高度
     *
     * @return
     */
    fun getBottomSafeAreaHeight(): Double
}