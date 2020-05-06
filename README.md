<h1 align="center">TooltipPopupWord</h1></br>

<p align="center">
:loudspeaker: ToolTipopupWordTV is an Open Source Android library, that allows developers to easily open a popup like tooltips, fully customizable, with details by select a word from your text. :tada:
</p>
</br>
<p align="center">
  <a href="https://github.com/EusebiuCandrea/ToolTipPopupWordTV/blob/master/LICENSE"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=23"><img alt="API" src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat"/></a>
</p> <br>

<p align="center">
<img src="https://user-images.githubusercontent.com/33927023/81177746-d7281b00-8faf-11ea-888e-0821a41b29ca.png" width="32%"/>

## Including in your project
[![](https://jitpack.io/v/EusebiuCandrea/ToolTipPopupWordTV.svg)](https://jitpack.io/#EusebiuCandrea/ToolTipPopupWordTV)

### Gradle 
Add below codes to your **root** `build.gradle` file (not your module build.gradle file).
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
And add a dependency code to your **module**'s `build.gradle` file.
```gradle
dependencies {
	        implementation 'com.github.EusebiuCandrea:ToolTipPopupWordTV:1.00'
	}
```

## Features
- Customizable font include text size, typeface, color, backround and alignment.
- Customizable ToolPopupWindows and Arrow.
- Customizable layout.

### Custom attributes for `SelectableWordTextView`
|attribute name|attribute description|
|:-:|:-:|
|highlightBackgroundColor|The backround color of selected word.|
|highlightTextColor|The text color of selected word.|
|setUnderline|You can set a underline for selected word (true/false) |

## Usage

### Basic Example (Kotlin)
Firstly, you need to add this custom text view to the layout of the class, with custom attributes <br>

``` xml
<com.ecandrea.library.tooltipopwordtv.wordTextView.SelectableWordTextView
        android:id="@+id/word"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_margin="@dimen/space_30dp"
        android:textColor="@color/colorPrimaryDark"
        app:highlightBackgroundColor="@color/blue"
        app:highlightTextColor="@color/white"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:setUnderline="true" />
```


Here is a basic example of implementing ToolPopupWindows with a default layout using `ToolPopupWindows.ToolTipBuilder` class.<br>

```kotlin
 word.apply {
        text = "Select a word from this example."
        setToolTipListener(object : SelectableWordListeners {
            override fun onWordSelected(anchorView: TextView, wordSelected: String, lineNumber: Int, width: Int) {
                val toolPopupWindows = ToolPopupWindows.ToolTipBuilder(this@MainActivity)
                    .setToolTipListener { Toast.makeText(applicationContext, "dismissed", Toast.LENGTH_SHORT).show() }
                    .setTitleTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
                    .setTitleTextSize(20f)
                    .setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                    .setIsOutsideTouchable(false)
                    .setArrowCustomizer(...)
                    .build()

                word.showToolTipWindow(anchorView, wordSelected, lineNumber, width, toolPopupWindows)
            }
        })
        }
        word.setBackgroundWordColor(ContextCompat.getColor(this, R.color.colorAccent))
```
Also, the arrow can be customized using an `ArrowCustomizer.Builder` class<br>

```kotlin
 ArrowCustomizer.Builder(this@MainActivity)
    .setArrowColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
    .setArrowSize(20)
    .build()
```

### Customized layout
We can fully customize the ToolPopupWindows layout using below method.
```java
 .setCustomLayout(R.layout.custom_layout)
```

This is an example of implementing custom ToolPopupWindows.

Firstly create an xml layout file like `custom_layout`.
```kotlin
   val toolPopupWindows = ToolPopupWindows.ToolTipBuilder(this@MainActivity)
        .setToolTipListener { Toast.makeText(applicationContext, "dismissed", Toast.LENGTH_SHORT).show() }
        .setCustomLayout(R.layout.custom_layout)
        .setAutoDismissDuration(1500)
        .setIsOutsideTouchable(false)
        .setArrowCustomizer(ArrowCustomizer.Builder(this@MainActivity)
                .setArrowColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
                .setArrowSize(20)
                .build())
        .build()
```
And next we can get the inflated custom layout using `getCustomInflatedView` method.
```kotlin
val inflatedView = toolPopupWindows.getCustomInflatedView()
inflatedView?.let {
    it.newText.text = wordSelected
    it.newText.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
}
```
:warning: If you didn't added your custom layout this method can return `null`

### `ToolPopupWindows.ToolTipBuilder` methods:
```kotlin
.setWidthPercentsFromScreen(value: Double)
.setBackgroundColor(value: Int)
.setBackgroundDrawable(@DrawableRes value: Int)
setTextTitle(value: String)
.setToolTipDescription(value: String)
.setTitleTextColor(@ColorInt value: Int)
.setTitleTextColorResource(@ColorRes value: Int)
.setTitleTextTypeface(value: Int)
.setTitleTextSize(@Sp value: Float)
.setDescriptionTextColor(@ColorInt value: Int)
// same for description
.setCustomLayout(value: Int)
.setAutoDismissDuration(value: Long)
.setIsOutsideTouchable(value: Boolean)
.setToolTipListener(listener: ToolTipListeners)
.setArrowCustomizer(value: ArrowCustomizer)
.setToolTipListener(unit: () -> Unit)
.build()
```

### `ArrowCustomizer.Builder` methods:
```kotlin
.setArrowDrawable(value: Drawable?)
.setArrowDrawableResource(@DrawableRes value: Int)
.setArrowSize(@Px value: Int)
.setArrowColor(@ColorInt value: Int)
.setArrowColorResource(@ColorInt value: Int)
.setArrowVisibility(isVisible: Boolean)
.build()
```
## Find this library useful? :heart:
Be free to use it and enjoy. :star:

# License
```xml
 Copyright 2020

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
