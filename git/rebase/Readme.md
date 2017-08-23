# Rebase

rebase 를 할 때 `HEAD~2` 로 접근하지 말고 rebase 를 시작할 
커밋을 직접 고르자. 또한 선택되지 않은 커밋은 rebase 이후에 사라져 
버리니 조심해야 한다. 

```sh
$ git rebase -i fc0bb11

 pick bab4d32 [TIL] add indexable
 pick 216240b [TIL] EffectiveJava item 2
 pick 7f4ecb3 [TIL] EffectiveJava Item 3 added
 pick cd087d2 [TIL] Effective Java Item4
 
 # Rebase fc0bb11..cd087d2 onto fc0bb11 (4 command(s))
 #
 # Commands:
 # p, pick = use commit
 # r, reword = use commit, but edit the commit message
 # e, edit = use commit, but stop for amending
 # s, squash = use commit, but meld into previous commit
 # f, fixup = like "squash", but discard this commit's log message
 # x, exec = run command (the rest of the line) using shell
 # d, drop = remove commit
 #
 # These lines can be re-ordered; they are executed from top to bottom.
 #
 # If you remove a line here THAT COMMIT WILL BE LOST.
 #
 # However, if you remove everything, the rebase will be aborted.
 #
 # Note that empty commits are commented out

```

위의 상태에서 특정 커밋을 `squash` 혹은 `pick` 을 안하고 그냥 
지워버린다면, 그 커밋은 rebase 후에, 아예 사라져 버린다. 
합치고 싶은 커밋은 `squash` 그냥 남기고 싶은 커밋은 `pick` 을 하자.
