# SwitchLiveAnimation
可直接用于项目的直播切换动画

V1.0.0
setBitmapsRes（）设置上下动画面板
setBottomPaneListener（）下面板开始滑动时请求下个直播间数据
setTopPaneListener（）上面板开始滑动时请求上个直播间数据
setAnimationEndListener（）面板滑动动画结束后更新UI或者跳转至对应的直播间

V1.0.1
将代码重构了下，改成了Ioc的模式
	public abstract void onTopPaneMoved();上面板开始滑动时请求上个直播间数据
	public abstract void onBottomPaneMoved();下面板开始滑动时请求下个直播间数据
	public abstract void onAnimationEnd();面板滑动动画结束后更新UI或者跳转至对应的直播间
