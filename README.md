# Togethor
拼车社交应用

这个是本人在大三的时候参加一个创意比赛的作品，意图在于把社交和时下热门的拼车结合起来，但是并不是所有的功能的完成，所以这个目前只是一个半成品，而且
已经没有在维护了，但是里面也花了很多的时间，特此上传到github上供自己参考。


后台是在新浪的SAE上搭建的，基于PHP编写（很久没有维护，估计SAE会把这个空间给清掉。。。）。

Android端用了以下的库作为基本的框架：
- Volley
- ImageLoader
- jackson

在编写的过程中是以尽可能的不使用第三方库为原则，大部分用自己封装过得工具来写，比如：
- common/adapter ，这个包下的是对BaseAdapter的封装；
- annotation ， 这个包下是注解类，主要实现了控件初始化，即setContentView和findViewById。
