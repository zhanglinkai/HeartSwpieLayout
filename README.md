# HeartSwipeLayout

***

### HeartSwipeLayout是一个仿IOS-侧滑删除效果的layout，写此的初衷是：再使用其他开源框架的时候，如果要达到二次删除即微信的确认删除功能，侧滑菜单再改变宽高或者隐藏显示状态时，会出现layout自动关闭的情况，因此再基于普通功能的情况下，优化此功能，从而达到确认删除的效果

> 查看功能是否是你想要的侧滑效果

  >> 1.可以上下左右添加侧滑菜单
  
  >> 2.动态改变侧滑菜单中的view
  
  >> 3.代码简单，就一个layout
  
# 使用

    allprojects {
        repositories {
          ...
          maven { url 'https://www.jitpack.io' }
        }
    }
    
    dependencies {
	        implementation 'com.github.zhanglinkai:HeartVideo:v1.0.1'
	  }
    
# 说明
          
          
   ## 在普通功能的基础上增加点击删除变为确认删除的样式，然后再点击即可删除，如果再使用的过程中出现bug或者更好的建议，请提出来，不胜感激
