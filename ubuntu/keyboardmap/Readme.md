# Keyboard map

Ubuntu 를 쓰다 보면 한/영 키를 사용하고 싶을 때, 그놈의 super 키가 사람을 
참 열받게 한다. xmodmap 으로 capslock key 를 ctrl 로 mapping 하는 것을 
진짜 어렵게 어렵게 했었는데, 그 짓을 또 하려니까 열이 너무 뻗쳐서, 그냥 
프로그램을 깔게 됐다. 

[이 곳](https://askubuntu.com/questions/105558/how-do-i-disable-the-super-key) 을 참고 하였다.

1. Install `compizconfig-settings-manager`

    ```bash
    sudo apt-get install compizconfig-settings-manager
    ```

2. Hit `Alt` + `F2`, type `about:config` and hit `Enter` to open the Unity configuration.
3. Change or disable the shortcut.

