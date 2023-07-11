package service

import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

actual object DeviceService {
    /**
     * 获取底部安全区域的高度
     *
     * @return
     */
    actual fun getBottomSafeAreaHeight(): Double {
        val scene = UIApplication.sharedApplication.connectedScenes.first() as UIWindowScene
        val window = scene.windows.first() as UIWindow
        window.safeAreaInsets.useContents {
            return this.bottom
        }
    }
}