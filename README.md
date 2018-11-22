# Txiaomo 
# （storage）获取存储权限
# （installApp）安装应用以及安装时获取安装未知来源应用权限插件
# （isPermission）检测是否有安装未知来源应用权限插件
# （exitApp）强退app 
## How to debug:

# callbackContext信息全抛在了error里，扎心。。。

# 1.获取存储权限。 下载内容的存储，在6.0以上适用，6.0以下不需要获取权限（静态注册的权限还是需要的） 
	cordova.plugins.achievejurisdiction.storage(result => {
	}, error => {
		if (error === 'success') {
			<!-- 已有存储权限 -->
		} else if (error === 'error') {
			<!-- 需要在设置中手动开启（这种情况属于软件申请权限时用户拒绝了） -->
		} else {
			<!-- 低版本系统，不需要申请 -->
		}
	});

# 2.安装应用插件，适用于所有android版本。 须传入app名字，默认为根目录文件。暂未加更改目录的功能。
#	如果不是8.0系统--->安装apk--->7.0以上（含7.0），使用FileProvider打开文件。7.0以下，直接用uri打开文件--->跳往安装
#	如果是8.0系统--->判断是否已获取权限（8.0才能使用的api，canRequestPackageInstalls()，8.0以下不能使用）--->没有权限--->跳往获取权限界面
#																									 --->有权限--->直接安装（与android7.0一样）
	cordova.plugins.achievejurisdiction.installApp("android.apk", result => {
	}, error => {
	})

# 3.检测是否有安装未知来源应用权限
	cordova.plugins.achievejurisdiction.isPermission("isPermission", result => {

	}, error => {
		console.log(error)
		if (error == "noPermission") {
			<!-- 没有权限 -->
		}else if(error == "havePermission"){
			<!-- 有权限 -->
		}
	})

# 4.退出app
#	关掉app进程，退出app。没有返回数据
	cordova.plugins.achievejurisdiction.exitApp("exitApp", result => {
	}, error => {
	})
