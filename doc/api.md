# tattooju 接口文档

说明:

所有接口返回的格式都是:
<pre>
{
	code: xxx,
	data: xxx,
	msg: xxx
}
</pre>

比如:
<pre>
{
	code: 1000,
	data: {
		nickname: "raindrops",
		city: "beijing"
	},
	msg:"ok"
}
</pre>

>**注意：code非1000都是错误码 错误信息在msg 里**

## 授权

### 授权

<pre>
GET /account/auth
参数:
code* 授权码
iv* 
encryptedData*
响应:
nickName 昵称
openId 唯一标志
role 角色 1)普通用户 2)管理员
headimgurl 微信头像
sex 性别 1)男 2)女
token 登录凭证 后面的接口需要这个进行授权认证
</pre>

请在后续所有的http请求头(header)上加上:

<pre>
token: xxx
</pre>

## 上传

### 上传媒体对象(tattooju的图片或者视频等内容)

<pre>
POST /common/upload
参数
mediaType* 类型 1)非视频 2)视频
file* 上传的文件
响应:
data 图片路径
</pre>

## 活动和文章

### 添加活动或文章
<pre>
POST /article
参数:
content* 文章内容
coverImg* 封面图片路径
title* 标题
byte* 类型1)活动 2)文章

响应:
无
</pre>

### 更新活动或者文章
<pre>
PUT /article
参数:
id* 要更新的ID
content* 内容
coverImg* 封面图片
title* 标题

响应:
无
</pre>

### 获取文章或者活动信息
<pre>
GET /article
参数:
id* 获取的文章ID

响应:
id 文章
coverImg 封面路径
title 标题
type 类型 1)活动 2)文章
createTime 创建时间
updateTime 最后更新时间
content 内容
</pre>

### 删除文章或活动

<pre>
DELETE /article
参数 
id* 要删除的文章ID
响应
无
</pre>

### 获取文章列表

<pre>
GET /article/list
参数
pageNum 页码不填默认1
pageSize 页面大小默认5
type 1)活动 2)文章 不填都查

响应:
pageNum 当前页码
pageSize 当前页面大小
total 共有多少条数据
pages 共有多少页
list: [ 数据
	id 文章
	coverImg 封面路径
	title 标题
	type 类型 1)活动 2)文章
	createTime 创建时间
	updateTime 最后更新时间
	content 内容
	]
</pre>

### 获取评论

<pre>
GET /article/comment/list
参数
pageNum 页码不填默认1
pageSize 页面大小默认5
articleId* 文章或活动ID
响应：
pageNum 当前页码
pageSize 当前页面大小
total 共有多少条数据
pages 共有多少页
list: [ 数据
	id: 评论ID
	nickName 昵称
	headImgUrl 头像地址
	content 评论内容
	createTime 评论时间
]
</pre>

### 删除评论

<pre>
DELETE /article/comment
参数
id* 要删除的评论的ID

响应
无
</pre>

## 媒体(图片或视频)

### 添加媒体对象
<pre>
POST /media
参数
content* 内容
mediaPath* 图片或者视频地址
type* 类型  1)图片 2)视频
响应
无
</pre>

### 更新媒体对象
<pre>
PUT /media
参数
id* 要更新的ID
content* 内容
mediaPath* 图片或者视频地址
type* 类型  1)图片 2)视频
响应
无
</pre>

### 根据ID获取媒体对象
<pre>
GET /media
参数
id* 
响应
id 主键
content 内容
mediaPath 媒体对象链接
type  1)图片 2)视频
createTime 创建时间
tagContent 用","隔开的标签
</pre>

### 获取列表
<pre>
GET /media/list
参数
pageNum 默认1
pageSize 默认5
keyword 搜索关键字
tag 匹配的tag标签
响应
pageNum 当前页码
pageSize 当前页面大小
total 共有多少条数据
pages 共有多少页
list: [ 数据
	id 主键
	content 内容
	mediaPath 媒体对象链接
	type  1)图片 2)视频
	createTime 创建时间
	tagContent 用","隔开的标签
]
</pre>

### 删除媒体对象
<pre>
DELETE /media
参数
id* 要删除的ID
响应
无
</pre>

## 预约

### 添加预约

<pre>
POST /reserve
参数:
wxAccount* 微信账号
mobile* 手机号
body* 选择的部位,自行决定结构存取
content* 内容
reserveTime* yyy-MM-dd HH:mm 时间格式
响应:
无
</pre>

### 修改预约内容

<pre>
PUT /reserve
参数:
id* 要修改的预约ID
wxAccount* 微信账号
mobile* 手机号
body* 选择的部位,自行决定结构存取
content* 内容
reserveTime* yyy-MM-dd HH:mm 时间格式
响应:
无
</pre>

### 修改预约这状态
<pre>
PUT /reserve/status
参数:
status*  0)已取消 1)已预约 2)删除 3)已完成
id* 要修改的ID
响应:
无
</pre>

### 获取预约信息
<pre>
GET /reserve
参数:
id* 要获取的预约ID
响应:
id 预约ID
headImgUrl 头像
nickName 昵称
accountId 用户ID
wxAccount 微信账号
mobile 手机号
body 选择的部位
status 状态 0)已取消 1)已预约 3)已完成
reserveTime 预约时间
createTime 创建时间
updateTime 更新时间
content 内容
</pre>

### 获取预约信息列表
<pre>
GET /reserve/list
参数
pageNum 默认1
pageSize 默认5
date 可选日期yyyy-MM-dd 筛选条件
响应:
pageNum 当前页码
pageSize 当前页面大小
total 共有多少条数据
pages 共有多少页
list: [ 数据
	id 预约ID
	headImgUrl 头像
	nickName 昵称
	accountId 用户ID
	wxAccount 微信账号
	mobile 手机号
	body 选择的部位
	status 状态 0)已取消 1)已预约 3)已完成
	reserveTime 预约时间
	createTime 创建时间
	updateTime 更新时间
	content 内容
]
</pre>