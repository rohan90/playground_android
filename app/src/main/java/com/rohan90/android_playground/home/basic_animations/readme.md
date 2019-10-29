Read
- [dev-docs google](https://developer.android.com/training/animation/overview)
- Fun Animations with code [link](https://www.raywenderlich.com/2785491-android-animation-tutorial-with-kotlin)
    * ValueAnimator
        - .OfFloat etc
        - props ie duration,interpolator,repeatmode etc
        - updateListener
        - start
    * ObjectAnimator (no need to call start here)
        -target, property (translationX,alpha,rotate etc)
    * AnimatorSet
- Animations with xml demonstrated in code base [BasicAnimationsActivity](./BasicAnimationsActivity.kt)
- ViewPropertyAnimator : It is kinda like a builder pattern to expose all animatable properties on view
  ```
  eg: 
  rocket.animate()
    .translationY(-screenHeight)
    .rotationBy(360f)
    .setDuration(DEFAULT_ANIMATION_DURATION)
    .start()
    ```
- AnimationListener 
    ```
    animator.addListener(object:Animator{
      override fun onAnimationStart(animation: Animator) {}
      override fun onAnimationEnd(animation: Animator) {}
      override fun onAnimationCancel(animation: Animator) {}
      override fun onAnimationRepeat(animation: Animator) {}
    })
    
    ``` 
    with `ViewPropertyAnimator` use rocket.animate().setListener({...})

- Activity Transition
    - enter (explode,slide,fade)
    - exit
        
    - shared
        - startActivity(options.toBundle())
            - options = ActivityOptions.makeSceneTransitionAnimation(this,target,"name")
    
- selectors
- animation-list
- vector
    - height,width,viewportX,viewporty
    - group
        - name
        - path
            - pathData M, L, C, Z
- animated-vector
    - list of vector targets
        - groupname
        - animator (rotation, pathMorph etc)
- ViewAnimationUtils (21 n above)
    - circular reveal
- physics animations
    - support-dynammic-animations lib or androidx.dynamicanims
        - fling
        - spring
- zoom
    [link gdoc](https://developer.android.com/training/animation/zoom)
- viewpager
    -pagerTransformer
        - zoomTransform
        - depthTransform [code](https://developer.android.com/training/animation/screen-slide)
- recycler view
    - customLayoutAnimator
    - onBind
    - LayoutTransitions
        - [link try](https://proandroiddev.com/enter-animation-using-recyclerview-and-layoutanimation-part-1-list-75a874a5d213)
    - drawables, layers, etc [link](https://en.proft.me/2017/06/15/shape-state-list-layer-list-drawable-android/)    